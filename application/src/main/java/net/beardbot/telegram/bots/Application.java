package net.beardbot.telegram.bots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
// @EnableTelegramBots <- Doesn't seem to work
@EnableCaching
public class Application {

    public static void main(String... args) {
        ApiContextInitializer.init();
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        BotConfiguration.BotInitializer botInitializer = context.getBean(BotConfiguration.BotInitializer.class);
        botInitializer.init();
    }
}
