package com.worldline.kafka.kafkamanager.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerOffsetDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;
import com.worldline.kafka.kafkamanager.dto.partition.PartitionResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicBrokerResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicResponseDto;

/**
 * Topic mapper.
 */
@Service
public class TopicMapper {

	/**
	 * Map topic {@link TopicDescription} to {@link TopicResponseDto}.
	 * 
	 * @param topic       {@link TopicDescription}
	 * @param brokerList  the broker list
	 * @param endOffsets  end offset by partition
	 * @param topicConfig the topic configuration
	 * @param isFull      {@code true} for a full mapping
	 * @return {@link TopicResponseDto}
	 */
	public TopicResponseDto mapping(TopicDescription topic, List<Integer> brokerList, Map<Integer, Long> endOffsets,
			Map<String, String> topicConfig, boolean isFull) {
		// Sort topic partition
		topic.partitions().sort(Comparator.comparingInt(TopicPartitionInfo::partition));

		// Create result
		TopicResponseDto result = new TopicResponseDto();
		result.setName(topic.name());
		result.setNbPartitions(topic.partitions().size());
		result.setNbReplications(topic.partitions().get(0).replicas().size());

		if (isFull) {
			result = mappingFull(topic, brokerList, endOffsets, result);
			result.setConfigurationMap(topicConfig);
		}
		return result;
	}

	/**
	 * Map {@link TopicPartition} to {@link TopicResponseDto}.
	 * 
	 * @param topicPartition {@link TopicPartition}
	 * @return {@link TopicResponseDto}
	 */
	public ConsumerGroupTopicPartitionDto mapFromKafkaNative(TopicPartition topicPartition) {
		return mapFromKafkaNative(topicPartition, null);
	}

	/**
	 * Map {@link TopicPartition} to {@link TopicResponseDto}.
	 * 
	 * @param topicPartition {@link TopicPartition}
	 * @param offsets        {@link ConsumerOffsetDto}
	 * @return {@link TopicResponseDto}
	 */
	public ConsumerGroupTopicPartitionDto mapFromKafkaNative(TopicPartition topicPartition, ConsumerOffsetDto offsets) {
		ConsumerGroupTopicPartitionDto result = new ConsumerGroupTopicPartitionDto();
		result.setName(topicPartition.topic());
		result.setPartition(topicPartition.partition());
		result.setOffsets(offsets);
		return result;
	}

	/**
	 * Map {@link ConsumerGroupTopicPartitionDto} to {@link TopicPartition}.
	 * 
	 * @param dto {@link ConsumerGroupTopicPartitionDto}
	 * @return {@link TopicPartition}
	 */
	public TopicPartition mapToKafkaNative(ConsumerGroupTopicPartitionDto dto) {
		return new TopicPartition(dto.getName(), dto.getPartition());
	}

	/**
	 * Full map topic {@link TopicDescription} to {@link TopicResponseDto}.
	 * 
	 * @param topic      {@link TopicDescription}
	 * @param brokerList the broker list
	 * @param endOffsets end offset by partition
	 * @param result     {@link TopicResponseDto}
	 * @return {@link TopicResponseDto}
	 */
	private TopicResponseDto mappingFull(TopicDescription topic, List<Integer> brokerList,
			Map<Integer, Long> endOffsets, TopicResponseDto result) {
		// Get partition data
		List<PartitionResponseDto> partitions = topic.partitions().stream().map(p -> {
			PartitionResponseDto partition = mappingPartition(topic, p);
			partition.setEndOffset(endOffsets.get(partition.getPartition()));
			return partition;
		}).collect(Collectors.toList());
		result.setPartitions(partitions);

		// Get broker data
		result = mappingBrokerData(result, brokerList);

		// Compute topic data
		int underReCount = 0;
		int preferredLeader = 0;
		for (PartitionResponseDto partition : partitions) {
			if (!partition.isPrefreLeader()) {
				preferredLeader++;
			}
			if (!partition.isUnderReplicate()) {
				underReCount++;
			}
		}
		result.setNbUnderReplication(100 * underReCount / topic.partitions().size());
		result.setPreferredReplicas(100 * preferredLeader / topic.partitions().size());
		result.setSumPartitionOffset(endOffsets.entrySet().stream().mapToLong(Map.Entry::getValue).sum());
		return result;
	}

	/**
	 * Map partition data {@link TopicPartitionInfo} to
	 * {@link PartitionResponseDto}.
	 * 
	 * @param topic     {@link TopicDescription}
	 * @param partition {@link TopicPartitionInfo}
	 * @return {@link PartitionResponseDto}
	 */
	private PartitionResponseDto mappingPartition(TopicDescription topic, TopicPartitionInfo partition) {
		// Get data
		int leader = partition.leader().id();
		List<String> replicaList = partition.replicas().stream().map(Node::idString).collect(Collectors.toList());
		List<String> isrList = partition.isr().stream().map(Node::idString).collect(Collectors.toList());

		PartitionResponseDto result = new PartitionResponseDto();
		result.setLeader(leader);
		result.setPartition(partition.partition());
		result.setReplicas(replicaList);
		result.setIsr(isrList);
		result.setPrefreLeader(Integer.parseInt(replicaList.get(0)) == leader);
		result.setUnderReplicate(isrList.size() == replicaList.size());
		return result;
	}

	/**
	 * Mapping broker data.
	 * 
	 * @param result     {@link TopicResponseDto}
	 * @param brokerList the broker list
	 * @return {@link TopicResponseDto}
	 */
	private TopicResponseDto mappingBrokerData(TopicResponseDto result, List<Integer> brokerList) {
		// Get partition by broker
		final Map<Integer, Integer> leaderMap = new HashMap<>();
		final Map<String, Set<Integer>> partitionByBroker = new HashMap<>();
		result.getPartitions().stream().forEach(partition -> {
			int partitionLeader = partition.getLeader();
			partition.getReplicas().stream().forEach(replica -> {
				if (!partitionByBroker.containsKey(replica)) {
					partitionByBroker.put(replica, new HashSet<>());
				}
				partitionByBroker.get(replica).add(partition.getPartition());
			});
			if (brokerList.contains(partitionLeader)) {
				int leader = leaderMap.containsKey(partitionLeader) ? leaderMap.get(partitionLeader) + 1 : 1;
				leaderMap.put(partitionLeader, leader);
			}
		});

		// Map broker data
		List<TopicBrokerResponseDto> brokerData = partitionByBroker.entrySet().stream().map(e -> {
			int leader = (leaderMap.get(Integer.valueOf(e.getKey())) != null) ? leaderMap.get(Integer.valueOf(e.getKey())) : 0;
			return mappingBrokerData(e.getKey(), e.getValue(), leader, result,
					brokerList);
		}).collect(Collectors.toList());
		result.setBrokers(brokerData);

		// Map topic data related to broker data
		result.setNbBrokerForTopic(partitionByBroker.keySet().size());
		result.setBrokersSpread(100 * partitionByBroker.size() / brokerList.size());
		result.setTotalNumberOfBrokers(brokerList.size());
		result.setBrokersSkewed(
				100 * brokerData.stream().filter(TopicBrokerResponseDto::isSkewed).count() / brokerList.size());
		result.setBrokersLeaderSkewed(
				100 * brokerData.stream().filter(TopicBrokerResponseDto::isLeaderSkewed).count() / brokerList.size());
		return result;
	}

	/**
	 * Map broker data to {@link TopicBrokerResponseDto}.
	 * 
	 * @param replica    the replica
	 * @param partitions the partition list related to the replica
	 * @param leader     the leader
	 * @param topic      the topic data
	 * @param brokerList the broker list
	 * @return {@link TopicBrokerResponseDto}
	 */
	private TopicBrokerResponseDto mappingBrokerData(String replica, Set<Integer> partitions, int leader,
			TopicResponseDto topic, List<Integer> brokerList) {
		TopicBrokerResponseDto result = new TopicBrokerResponseDto();
		result.setId(replica);
		result.setNbLeader(leader);
		result.setNbPartitions(partitions.size());
		result.setPartitions(new ArrayList<>(partitions));
		double skewed = Math.ceil((double) topic.getNbPartitions() * topic.getPartitions().get(0).getReplicas().size()
				/ brokerList.size());
		result.setSkewed(skewed < partitions.size());
		result.setLeaderSkewed(skewed < leader);
		return result;
	}
}
