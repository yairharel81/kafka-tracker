package com.cooladata.tracker.kafka.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableAutoConfiguration
public class KafkaTrackerBeanConfiguration {

	
	@Value("${rest.readTimeout:60000}")
	private int readTimeout;

	@Value("${rest.connectTimeout:10000}")
	private int connectTimeout;
	
	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() {

		RestTemplate rest = new RestTemplate();
		rest.setErrorHandler(new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				int rawStatusCode = response.getRawStatusCode();
				return rawStatusCode >= HttpStatus.BAD_REQUEST.value();
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				IOException ex = new IOException(response.getStatusText());
				throw ex;
			}
		});
		rest.setRequestFactory(clientHttpRequestFactory());
		return rest;
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(readTimeout);
		factory.setConnectTimeout(connectTimeout);
		return factory;
	}
}
