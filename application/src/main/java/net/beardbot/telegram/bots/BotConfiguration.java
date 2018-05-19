package net.beardbot.telegram.bots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.LongPollingBot;
import org.telegram.telegrambots.generics.WebhookBot;

import java.util.Collections;
import java.util.List;

@Configuration
public class BotConfiguration {

    static class BotInitializer {
        @Autowired
        private TelegramBotsApi telegramBotsApi;
        @Autowired(required = false)
        private List<LongPollingBot> pollingBots = Collections.emptyList();
        @Autowired(required = false)
        private List<WebhookBot> webhookBots = Collections.emptyList();

        void init() {
            try {
                for (LongPollingBot bot : pollingBots) {
                    telegramBotsApi.registerBot(bot);
                }
                for (WebhookBot bot : webhookBots) {
                    telegramBotsApi.registerBot(bot);
                }
            } catch (TelegramApiRequestException e) {
                e.printStackTrace();
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotsApi.class)
    public TelegramBotsApi telegramBotsApi(){
        return new TelegramBotsApi();
    }

    @Bean
    public BotInitializer botInitializer(){
        return new BotInitializer();
    }
}
