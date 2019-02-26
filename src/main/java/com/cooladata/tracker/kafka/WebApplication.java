package com.cooladata.tracker.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import com.cooladata.tracker.kafka.services.KafkaExecutors;




@Controller
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.cooladata")
public class WebApplication implements CommandLineRunner {

	
	@Autowired
	KafkaExecutors kafkaExecutors;
	
	 public static void main(String[] args) throws Exception {
	    	SpringApplication.run(WebApplication.class, args);
	    }

		public void run(String... arg0) throws Exception {
			System.out.println("start WebApplication");
			kafkaExecutors.start();
		}
}
