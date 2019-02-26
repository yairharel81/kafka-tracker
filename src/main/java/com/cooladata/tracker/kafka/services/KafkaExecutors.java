package com.cooladata.tracker.kafka.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cooladata.common.messaging.kafka.BaseKafkaExecutors;
import com.cooladata.common.messaging.kafka.KafkaConnector;
import com.cooladata.common.messaging.kafka.KafkaWorker;
import com.cooladata.tracker.kafka.config.KafkaTrackerConfiguration;

@Service
public class KafkaExecutors extends BaseKafkaExecutors {

	private static final Logger logger = LoggerFactory.getLogger(KafkaExecutors.class);
	
	
	@Value("${tracker.kafka.closeTimout:1000}")
	private long kafkaCloseTimeout;
	
	@Autowired
	private KafkaTrackerConfiguration configuration;
	@Autowired
	private RestTemplate restTemplate;
	
	
	protected Map<String, Integer> topicsNames;
	protected String hostName;
	
	@PostConstruct
	public void init() {
		String[] topicArr = configuration.getTopics().split(",");
		topicsNames = new HashMap<>();
		for (String topic: topicArr) {
			topicsNames.put(topic, configuration.getKafkaConfig().getStreamsPerTopic());
		}
		try {
			this.hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hostName = "localhost";
		}
	}
	
	@Override
	protected void acceptMoreEvents(boolean arg0) {
		
	}

	@Override
	public KafkaConnector createKafkaConnector(String topicName, Integer streamPerTopic) {
		String connectrName = String.format("EventsProcessor_%s", this.hostName);
		KafkaConnector kafkaConnector = new KafkaConnector(configuration.getKafkaConfig(), topicName, connectrName,
				streamPerTopic);

		kafkaConnector.start(false);

		return kafkaConnector;
	}

	@Override
	protected KafkaWorker createWorker() {
		return new KafkaWorker() {

			@Override
			public void handleMessage(ConsumerRecord<String, String> record) {
				try {
				handleMessageInternal(record);
				} catch(Exception ex) {
					logger.error("unexpected error", ex);
				}
			}

			

			@Override
			protected boolean getShutdownSequence() {
				return false;
			}

			@Override
			protected boolean acceptMoreEvents() {
				return true;
			}
		};
	}

	
	private void handleMessageInternal(ConsumerRecord<String, String> record) {
		byte[] message = record.value().getBytes();
		String messageAsString = new String(message, Charset.forName("UTF-8"));
	
		ResponseEntity<String> response = restTemplate.postForEntity(configuration.getEgwUrl(), messageAsString, String.class);
		
		if(logger.isDebugEnabled())
			logger.debug(response.getBody());
	
	}
	
	@Override
	protected long getKafkaCloseTimeout() {
		return kafkaCloseTimeout;
	}

	@Override
	protected Map<String, Integer> getTopicNames() {
		return Collections.unmodifiableMap(this.topicsNames);
	}

}
