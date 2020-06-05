package com.suhael.spring.aws.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(IncomingProcessor.class)
public class IncomingConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(IncomingProcessor.class);

	@StreamListener(IncomingProcessor.INCOMMING_STREAM)
	public void incoming(String message) {

		LOG.info("Read from incoming: {}", message);
	}

}
