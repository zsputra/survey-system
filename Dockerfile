FROM openjdk:8-jdk-alpine
ADD /target/*.jar survey-service.jar
ADD survey-service.sh survey-service.sh
RUN ["chmod","+x","survey-service.sh"]
EXPOSE 9090:9090
ENTRYPOINT ["/survey-service.sh"]

