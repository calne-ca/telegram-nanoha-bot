FROM adoptopenjdk/openjdk11
COPY application/target/telegram-bots-application-1.0.0-exec.jar /usr/src/telegram-bots/telegram-bots.jar
WORKDIR /usr/src/telegram-bots
CMD ["java", "-jar", "telegram-bots.jar"]