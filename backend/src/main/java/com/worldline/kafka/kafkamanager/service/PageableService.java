package com.worldline.kafka.kafkamanager.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Pageable service.
 */
@Service
public class PageableService {

	/**
	 * Apply {@link Pageable} on {@link List}.
	 * 
	 * @param list     to transform
	 * @param pageable pageable
	 * @param <T>      generic object
	 * @return sub list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Serializable> List<T> createList(List<T> list, Pageable pageable) {
		if (list == null) {
			return new ArrayList<T>();
		}
		if (null != pageable.getSort()) {
			GroupComparator<T> comparators = new GroupComparator<T>();
			pageable.getSort().forEach(sort -> {
				if (Direction.ASC.equals(sort.getDirection())) {
					comparators.addComparator(new BeanComparator(sort.getProperty()));
				} else {
					comparators.addComparator(new BeanComparator(sort.getProperty()).reversed());
				}
			});
			Collections.sort(list, comparators);
		}
		List<T> subList = list.stream().skip(pageable.getOffset()).limit(pageable.getPageSize())
				.collect(Collectors.toList());
		return subList;
	}

	/**
	 * Create page.
	 * 
	 * @param list     the list
	 * @param pageable the pageable data
	 * @return the page
	 */
	public <T extends Serializable> Page<T> createPage(List<T> list, Pageable pageable) {
		List<T> result = createList(list, pageable);
		return new PageImpl<T>(result, pageable, list.size());
	}

}
