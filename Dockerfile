FROM eclipse-temurin:17-jre
COPY nanoha-bot/target/telegram-bot-nanoha-1.0.0.jar /usr/src/telegram-bots/nanoha-bot.jar
WORKDIR /usr/src/telegram-bots
CMD ["java", "-jar", "nanoha-bot.jar"]