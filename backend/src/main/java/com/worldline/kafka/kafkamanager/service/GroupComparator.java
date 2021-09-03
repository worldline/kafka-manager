package com.worldline.kafka.kafkamanager.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Group comparator.
 */
public class GroupComparator<T extends Serializable> implements Comparator<T>, Serializable {

	private static final long serialVersionUID = 6114460464261165375L;

	private ArrayList<Comparator<T>> comparators = new ArrayList<>();

	public void addComparator(Comparator<T> comparator) {
		comparators.add(comparator);
	}

	@Override
	public int compare(T object1, T object2) {
		for (Comparator<T> comparator : comparators) {
			int returnValue = comparator.compare(object1, object2);
			if (returnValue != 0) {
				return returnValue;
			}
		}
		return 0;
	}

}
