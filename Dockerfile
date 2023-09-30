FROM openjdk:17-ea-jdk-alpine
VOLUME /product-mgmt
EXPOSE 9090
ADD ./target/product-mgmt-1.0.0.jar product-mgmt.jar
ENTRYPOINT ["java","-jar","/product-mgmt.jar"]