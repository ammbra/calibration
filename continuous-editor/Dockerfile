# Example of custom Java runtime using jlink in a multi-stage container build

FROM container-registry.oracle.com/java/openjdk:21-oraclelinux8 as jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
	--add-modules java.base,java.logging,jdk.httpserver,jdk.management \
	--no-man-pages \
	--no-header-files \
	--compress=2 \
	--output /javaruntime

# Define your base image
FROM oraclelinux:8-slim

ENV JAVA_HOME /usr/java/openjdk-21
ENV PATH $JAVA_HOME/bin:$PATH

COPY --from=jre-build /javaruntime $JAVA_HOME

# Continue with your application deployment
ADD ./target/libs/ ./libs/
COPY ./target/continuous-editor.jar /app.jar

RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN touch /file.txt && chown appuser /file.txt && chmod -R 755 /file.txt && chmod +x /file.txt
USER appuser
EXPOSE 8001

ENV JDK_JAVA_OPTIONS "--enable-preview"

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]
