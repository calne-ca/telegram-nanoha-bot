FROM openjdk:8-jre-alpine
COPY telegram-bots.jar /usr/src/telegram-bots/
WORKDIR /usr/src/telegram-bots
CMD ["java", "-jar", "telegram-bots.jar"]