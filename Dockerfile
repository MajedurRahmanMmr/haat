FROM openjdk
ARG JAR_FILE=target/*.jar
ENV TZ="Asia/Dhaka"

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
