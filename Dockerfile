FROM gradle:7.6-jdk11 AS build
WORKDIR /app
COPY . .
RUN gradle war --no-daemon

FROM tomcat:9.0-jdk11
COPY --from=build /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war
RUN rm -rf /usr/local/tomcat/webapps/ROOT
EXPOSE 8080
CMD ["catalina.sh", "run"]
