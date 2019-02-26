#!/bin/bash

java -Dspring.profiles.active=PROD  -Dlogging.config=logback.xml  -Dspring.config.location=appliaction.yml -jar tracker.kafka-0.0.1-SNAPSHOT.jar 
