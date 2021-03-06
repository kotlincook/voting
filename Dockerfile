FROM openjdk:11-jre-slim
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ARG WEBAPP_FILE=src/main/webapp/frontend
COPY ${JAR_FILE} app.jar
RUN mkdir -p ${WEBAPP_FILE}
COPY ${WEBAPP_FILE}/* ${WEBAPP_FILE}/
ENTRYPOINT ["java","-jar","/app.jar"]