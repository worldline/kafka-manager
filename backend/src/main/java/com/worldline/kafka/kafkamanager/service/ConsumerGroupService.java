package com.worldline.kafka.kafkamanager.service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupOverviewDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;
import com.worldline.kafka.kafkamanager.dto.kafka.common.MemberDescriptionDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsOptions;
import org.apache.kafka.clients.admin.MemberToRemove;
import org.apache.kafka.clients.admin.RemoveMembersFromConsumerGroupOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescribeSearchDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescriptionDto;
import com.worldline.kafka.kafkamanager.exception.BadRequestException;
import com.worldline.kafka.kafkamanager.exception.KafkaException;
import com.worldline.kafka.kafkamanager.exception.ResourceNotFoundException;
import com.worldline.kafka.kafkamanager.kafka.KafkaOffsets;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.ConsumerGroupMapper;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;
import org.springframework.util.StringUtils;

/**
 * Consumer group service.
 */
@Service
public class ConsumerGroupService {

	private final ZooKaMixService zooKaMixService;
	private final PageableService pageableService;
	private final ConsumerGroupMapper consumerGroupMapper;
	private final ConsumerGroupOffsetService consumerGroupOffsetService;

	/**
	 * Constructor.
	 *
	 * @param zooKaMixService            the zookeeper kafka jmx service
	 * @param pageableService            the pageable service
	 * @param consumerGroupMapper        the consumer group mapper
	 * @param consumerGroupOffsetService the consumer offset service
	 */
	@Autowired
	public ConsumerGroupService(ZooKaMixService zooKaMixService, PageableService pageableService,
		ConsumerGroupMapper consumerGroupMapper, ConsumerGroupOffsetService consumerGroupOffsetService) {
		this.zooKaMixService = zooKaMixService;
		this.pageableService = pageableService;
		this.consumerGroupMapper = consumerGroupMapper;
		this.consumerGroupOffsetService = consumerGroupOffsetService;
	}

	/**
	 * List all consumer group associated with the provided clusterId. For each consumer grouo the
	 * following details specified:
	 * <ul>
	 *     <li>groupId</li>
	 *     <li>topic</li>
	 *     <li>state</li>
	 *     <li>lag</li>
	 * </ul>
	 *
	 * @param clusterId Cluster id to filter
	 * @param pageable  Pageable configuration
	 * @return Paged list of consumer groups
	 */
	public Page<ConsumerGroupOverviewDto> list(String clusterId, Pageable pageable) {

		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		Map<String, ConsumerGroupOverviewDto> results = new HashMap<>();

		try {
			// Load all groupIds 
			List<String> groupIds = zooKaMixAdmin.getAdminClient().listConsumerGroups().all().get()
				.stream()
				.filter(consumerGroup -> !consumerGroup.groupId()
					.equals(BatchConsumerOffsetService.OFFSET_CONSUMER_GROUP)
					&& !consumerGroup.groupId().startsWith("KM-"))
				.map(ConsumerGroupListing::groupId).collect(Collectors.toList());
			
			if (CollectionUtils.isEmpty(groupIds)) {
				return pageableService.createPage(new ArrayList<>(), pageable);
			}

			// Fetch all consumer groups
			ConsumerGroupDescribeSearchDto search = new ConsumerGroupDescribeSearchDto();
			search.setGroupIds(groupIds);
			List<ConsumerGroupDescriptionDto> consumerGroups = getConsumerGroups(clusterId, search);

			// Read the list of consumer groups
			consumerGroups.stream().forEach(cg -> {
				if (CollectionUtils.isNotEmpty(cg.getMembers())) {
					// Read the list of each member for the current consumer group. A member will
					// contain details about the consumer in the group the topis it's listening to.
					// It is technically possible to have a consumer group with consumer listening
					// to different topics.
					cg.getMembers().stream()
						.filter(member -> CollectionUtils.isNotEmpty(member.getTopicPartitions()))
						.map(MemberDescriptionDto::getTopicPartitions)
						.flatMap(Set::stream)
						.filter(consumerGroupTopicPartitionDto -> 
							Objects.nonNull(consumerGroupTopicPartitionDto) && 
								StringUtils.hasText(consumerGroupTopicPartitionDto.getName()))
						// Transform the result as map of topic:lag. The topic lag is the addition
						// of the lag of each partition.
						.collect(Collectors.toMap(ConsumerGroupTopicPartitionDto::getName,
							e -> e.getOffsets() != null ? e.getOffsets().getLag() : 0 ,
							(previousValue, newValue) -> previousValue + newValue))
						// Finally we can build the final object.
						.forEach((topic, lag) -> {
							addGroupConsumerOverviewIfNeeded(results, cg);
							results.get(cg.getGroupId()).addTopicDetails(topic, lag);
						});
				} else {
					// For whatever reason if we have no member for the consumer group, we add
					// the few data that we have. Topic name and lag will be empty.
					addGroupConsumerOverviewIfNeeded(results, cg);
				}
			});

			return pageableService.createPage(results.values().stream().collect(Collectors.toList()), pageable);
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka to retrieve consumer group detail", e);
		}
	}
	
	/**
	 * Return details for each consumer group from the list in the provided
	 * clusterId.
	 *
	 * @param clusterId Cluster id to filter.
	 * @param request   List of group ids.
	 * @param pageable  the pageable data
	 * @return Contains group details.
	 */
	public Page<ConsumerGroupDescriptionDto> describesGroups(String clusterId, ConsumerGroupDescribeSearchDto request,
		Pageable pageable) {
		return pageableService.createPage(getConsumerGroups(clusterId, request), pageable);
	}

	/**
	 * Return details for each consumer group from the list in the provided
	 * clusterId.
	 *
	 * @param clusterId Cluster id to filter.
	 * @param request   List of group ids.
	 * @return Contains group details.
	 */
	public List<ConsumerGroupDescriptionDto> getConsumerGroups(String clusterId,
		ConsumerGroupDescribeSearchDto request) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		try {
			// Search consumer groups
			Map<String, ConsumerGroupDescription> consumerGroups = describeGroupsSearch(zooKaMixAdmin, request);
			if (consumerGroups == null | consumerGroups.size() < 1) {
				return new ArrayList<>();
			}

			KafkaOffsets offsets = request.isOffsets()
				? consumerGroupOffsetService.getOffsets(zooKaMixAdmin, consumerGroups)
				: new KafkaOffsets();

			return consumerGroups.values().stream().map(v -> consumerGroupMapper.map(v, offsets))
				.collect(Collectors.toList());

		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka to retrieve consumer group detail", e);
		}
	}

	/**
	 * Describe group seach.
	 *
	 * @param zooKaMixAdmin the zookeeper kafka admin connection
	 * @param request       the search request
	 * @return the describe group request
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private Map<String, ConsumerGroupDescription> describeGroupsSearch(ZooKaMixAdmin zooKaMixAdmin,
		ConsumerGroupDescribeSearchDto request) throws InterruptedException, ExecutionException {
		if (CollectionUtils.isNotEmpty(request.getGroupIds())) {
			return zooKaMixAdmin.getAdminClient().describeConsumerGroups(request.getGroupIds()).all().get();
		} else if (CollectionUtils.isNotEmpty(request.getTopicNames())) {
			Collection<ConsumerGroupListing> groups = zooKaMixAdmin.getAdminClient().listConsumerGroups().all().get();
			List<String> groupIds = groups.stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList());
			Map<String, ConsumerGroupDescription> result = new HashMap<>();
			if (CollectionUtils.isNotEmpty(groupIds)) {
				Map<String, ConsumerGroupDescription> data = zooKaMixAdmin.getAdminClient()
					.describeConsumerGroups(groupIds).all().get();
				data.entrySet().stream().forEach(e -> {
					if (e.getValue().members().stream().anyMatch(consumer -> {
						return consumer.assignment().topicPartitions().stream().anyMatch(partition -> {
							return request.getTopicNames().contains(partition.topic());
						});
					})) {
						result.put(e.getKey(), e.getValue());
					}
				});
			}
			return result;
		}
		// throw new BadRequestException("Cannot find consumer group without arguments");
		return new HashMap<>();
	}

	/**
	 * Get consumer group detail.
	 *
	 * @param clusterId       the cluster ID
	 * @param consumerGroupId the consumer group ID
	 * @return the consumer group detail
	 */
	public ConsumerGroupDescriptionDto get(String clusterId, String consumerGroupId) {
		ConsumerGroupDescribeSearchDto search = new ConsumerGroupDescribeSearchDto();
		search.setGroupIds(Arrays.asList(consumerGroupId));
		search.setOffsets(true);
		List<ConsumerGroupDescriptionDto> result = getConsumerGroups(clusterId, search);
		if (CollectionUtils.isEmpty(result) || !consumerGroupId.equals(result.get(0).getGroupId())) {
			throw new ResourceNotFoundException("Cannot find consumer group " + consumerGroupId);
		}
		return result.get(0);
	}

	/**
	 * Delete each group ids for the clusterid.
	 *
	 * @param clusterId the cluster ID
	 * @param groupIds  group ID list
	 */
	public void delete(String clusterId, List<String> groupIds) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Todo: handle futures
		try {
			DeleteConsumerGroupsOptions options = new DeleteConsumerGroupsOptions();
			options.timeoutMs(5000);
			zooKaMixAdmin.getAdminClient().deleteConsumerGroups(groupIds, options).all().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Remove members from group.
	 *
	 * @param clusterId   the cluster ID
	 * @param groupId     the group Id
	 * @param consumerIds the consumer ID list
	 */
	public void removeMembersFromGroup(String clusterId, String groupId, List<String> consumerIds) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Remove members
		try {
			RemoveMembersFromConsumerGroupOptions options = new RemoveMembersFromConsumerGroupOptions(
				consumerIds.stream().map(MemberToRemove::new).collect(Collectors.toList()));
			options.timeoutMs(5000);
			zooKaMixAdmin.getAdminClient().removeMembersFromConsumerGroup(groupId, options).all().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Add an element in the map if it doesn't exist. The key is the groupId.
	 */
	private void addGroupConsumerOverviewIfNeeded(Map<String, ConsumerGroupOverviewDto> results,  ConsumerGroupDescriptionDto consumerGroupDescription) {
		if (!results.containsKey(consumerGroupDescription.getGroupId())) {
			results.put(consumerGroupDescription.getGroupId(),
				new ConsumerGroupOverviewDto(consumerGroupDescription.getGroupId(), consumerGroupDescription.getState()));
		}
	}

}
