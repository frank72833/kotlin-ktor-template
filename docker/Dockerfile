FROM amazoncorretto:21.0.3-alpine3.19
EXPOSE 8080:8080
RUN mkdir /app
COPY ./kotlin-ktor-template-deployment/build/libs/*-all.jar /app/application.jar
ENTRYPOINT ["java","-jar","/app/application.jar"]