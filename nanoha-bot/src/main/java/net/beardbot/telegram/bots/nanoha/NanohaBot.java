package net.beardbot.telegram.bots.nanoha;

import lombok.extern.slf4j.Slf4j;
import net.beardbot.telegram.bots.nanoha.handler.InlineQueryHandler;
import net.beardbot.telegram.bots.nanoha.handler.TextMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
@Slf4j
public class NanohaBot extends TelegramLongPollingBot {
    @Value("${bot.nanoha.username}")
    private String username;
    @Value("${bot.nanoha.token}")
    private String token;

    @Autowired
    private InlineQueryHandler inlineQueryHandler;
    @Autowired
    private TextMessageHandler textMessageHandler;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    public void onUpdateReceived(Update update) {
        BotApiMethod<?> answer = null;

        try {
            if (update.hasInlineQuery()){
                answer = inlineQueryHandler.getAnswer(update.getInlineQuery());
            } else if (update.hasMessage()){
                answer = textMessageHandler.getAnswer(update.getMessage());
            }

            if (answer != null){
                sendApiMethod(answer);
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send response to Telegram due to: {}",e.getMessage());
        }
    }
}
