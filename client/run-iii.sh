#!/bin/bash

echo $PWD

sleep 1s

CP="/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jaccess.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/java-atk-wrapper.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/nashorn.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunec.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar:/home/lumos/dev/SpringWebSocket/target/classes:/home/lumos/.m2/repository/org/springframework/boot/spring-boot-starter/1.5.4.RELEASE/spring-boot-starter-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/boot/spring-boot/1.5.4.RELEASE/spring-boot-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-context/4.3.9.RELEASE/spring-context-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-aop/4.3.9.RELEASE/spring-aop-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-expression/4.3.9.RELEASE/spring-expression-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/1.5.4.RELEASE/spring-boot-autoconfigure-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/boot/spring-boot-starter-logging/1.5.4.RELEASE/spring-boot-starter-logging-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/ch/qos/logback/logback-classic/1.1.11/logback-classic-1.1.11.jar:/home/lumos/.m2/repository/ch/qos/logback/logback-core/1.1.11/logback-core-1.1.11.jar:/home/lumos/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.25/jcl-over-slf4j-1.7.25.jar:/home/lumos/.m2/repository/org/slf4j/jul-to-slf4j/1.7.25/jul-to-slf4j-1.7.25.jar:/home/lumos/.m2/repository/org/slf4j/log4j-over-slf4j/1.7.25/log4j-over-slf4j-1.7.25.jar:/home/lumos/.m2/repository/org/springframework/spring-core/4.3.9.RELEASE/spring-core-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/yaml/snakeyaml/1.17/snakeyaml-1.17.jar:/home/lumos/.m2/repository/org/springframework/boot/spring-boot-starter-websocket/1.5.4.RELEASE/spring-boot-starter-websocket-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/boot/spring-boot-starter-web/1.5.4.RELEASE/spring-boot-starter-web-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/1.5.4.RELEASE/spring-boot-starter-tomcat-1.5.4.RELEASE.jar:/home/lumos/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/8.5.15/tomcat-embed-core-8.5.15.jar:/home/lumos/.m2/repository/org/apache/tomcat/embed/tomcat-embed-el/8.5.15/tomcat-embed-el-8.5.15.jar:/home/lumos/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/8.5.15/tomcat-embed-websocket-8.5.15.jar:/home/lumos/.m2/repository/org/hibernate/hibernate-validator/5.3.5.Final/hibernate-validator-5.3.5.Final.jar:/home/lumos/.m2/repository/javax/validation/validation-api/1.1.0.Final/validation-api-1.1.0.Final.jar:/home/lumos/.m2/repository/org/jboss/logging/jboss-logging/3.3.1.Final/jboss-logging-3.3.1.Final.jar:/home/lumos/.m2/repository/com/fasterxml/classmate/1.3.3/classmate-1.3.3.jar:/home/lumos/.m2/repository/org/springframework/spring-web/4.3.9.RELEASE/spring-web-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-webmvc/4.3.9.RELEASE/spring-webmvc-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-messaging/4.3.9.RELEASE/spring-messaging-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-beans/4.3.9.RELEASE/spring-beans-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/springframework/spring-websocket/4.3.9.RELEASE/spring-websocket-4.3.9.RELEASE.jar:/home/lumos/.m2/repository/org/webjars/webjars-locator/0.32-1/webjars-locator-0.32-1.jar:/home/lumos/.m2/repository/org/webjars/webjars-locator-core/0.32/webjars-locator-core-0.32.jar:/home/lumos/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:/home/lumos/.m2/repository/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:/home/lumos/.m2/repository/org/apache/commons/commons-compress/1.9/commons-compress-1.9.jar:/home/lumos/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.8/jackson-core-2.8.8.jar:/home/lumos/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.8/jackson-databind-2.8.8.jar:/home/lumos/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.0/jackson-annotations-2.8.0.jar:/home/lumos/.m2/repository/org/webjars/sockjs-client/1.0.2/sockjs-client-1.0.2.jar:/home/lumos/.m2/repository/org/webjars/stomp-websocket/2.3.3/stomp-websocket-2.3.3.jar:/home/lumos/.m2/repository/org/webjars/bootstrap/3.3.7/bootstrap-3.3.7.jar:/home/lumos/.m2/repository/org/webjars/jquery/3.1.0/jquery-3.1.0.jar:/home/lumos/.m2/repository/com/google/code/gson/gson/2.8.5/gson-2.8.5.jar:/home/lumos/.m2/repository/org/java-websocket/Java-WebSocket/1.3.9/Java-WebSocket-1.3.9.jar"


cd /home/lumos/dev/SpringWebSocket/target/classes
java -cp ${CP} com.kukinet.client.ConsoleClient login,iii,1234
#java -cp ${CP} com.kukinet.pkr.ConsoleClient login,iii,1234,testGame

