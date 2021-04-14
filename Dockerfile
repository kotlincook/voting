FROM maven:3.6.1-jdk-11
COPY . .
USER root
RUN mvn clean package -DskipTests
FROM openjdk:11-jre-slim
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ARG WEBAPP_FILE=src/main/webapp/frontend
COPY --from=0 ${JAR_FILE} app.jar
RUN mkdir -p ${WEBAPP_FILE}
COPY --from=0 ${WEBAPP_FILE}/* ${WEBAPP_FILE}/
ENTRYPOINT ["java","-jar","/app.jar"]