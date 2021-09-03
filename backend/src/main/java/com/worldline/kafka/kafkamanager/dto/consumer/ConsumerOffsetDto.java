package com.worldline.kafka.kafkamanager.dto.consumer;

import java.io.Serializable;

import lombok.Data;

/**
 * Consumer offset dto.
 */
@Data
public class ConsumerOffsetDto implements Serializable {

	private static final long serialVersionUID = 4804448248896367265L;

	private Long current;

	private Long end;

	private Long lag = 0L;

	/**
	 * Set current offset.
	 * 
	 * @param current the current offset
	 */
	public void setCurrent(Long current) {
		this.current = current;
		computeLag();
	}

	/**
	 * Set end offset.
	 * 
	 * @param end the end offset
	 */
	public void setEnd(Long end) {
		this.end = end;
		computeLag();
	}

	/**
	 * Compute lag.
	 */
	private void computeLag() {
		if (this.end != null && this.current != null) {
			long lag = this.end - this.current;
			this.lag = lag > 0 ? lag : 0L;
		}
	}

}
