package com.cooladata.tracker.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.cooladata.common.messaging.kafka.KafkaConfig;

@Configuration
@ConfigurationProperties(prefix = "tracker.config")
public class KafkaTrackerConfiguration {

	private KafkaConfig kafkaConfig;
	
	
	private String egwUrl;
	
	
	private String topics;

	public KafkaConfig getKafkaConfig() {
		return kafkaConfig;
	}

	public void setKafkaConfig(KafkaConfig kafkaConfig) {
		this.kafkaConfig = kafkaConfig;
	}

	public String getEgwUrl() {
		return egwUrl;
	}

	public void setEgwUrl(String egwUrl) {
		this.egwUrl = egwUrl;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}
	
	
	
	
	
	
	
}
