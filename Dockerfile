FROM openjdk:17-jdk-slim
COPY target/retail-0.0.1.jar retail.jar
ENTRYPOINT ["java","-jar","/retail.jar"]