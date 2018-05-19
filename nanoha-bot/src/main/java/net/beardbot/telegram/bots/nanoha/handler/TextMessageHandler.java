package net.beardbot.telegram.bots.nanoha.handler;

import lombok.extern.slf4j.Slf4j;
import net.beardbot.myanimelist.MALClient;
import net.beardbot.myanimelist.model.anime.Anime;
import net.beardbot.myanimelist.model.manga.Manga;
import net.beardbot.telegram.bots.nanoha.response.TextMessageResponseBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.util.List;

@Service
@Slf4j
public class TextMessageHandler {
    @Value("${telegram.message.length.max}")
    private int maxMessageLength;

    @Autowired
    private MALClient malClient;
    @Autowired
    private TextMessageResponseBuilder textMessageResponseBuilder;

    @Cacheable("nanoha-text-message")
    public SendMessage getAnswer(Message message){
        Long chatId = message.getChatId();
        String text = message.getText();

        log.debug("Received text message from id {}: \"{}\"",chatId,text);

        if (StringUtils.isBlank(text)){
            log.debug("Ignored text message from id {} due to empty text.",chatId);
            return null;
        }
        if (text.length() > maxMessageLength){
            log.debug("Ignored text message from id {} due to message size limit.",chatId);
            return null;
        }

        List<Anime> animes = malClient.searchForAnime(text);
        List<Manga> mangas = malClient.searchForManga(text);

        log.debug("Found {} results for query \"{}\"",animes.size() + mangas.size(),text);

        SendMessage sendMessage;

        if (!animes.isEmpty()){
            log.debug("Choosing first anime as result for id {}",chatId);
            sendMessage = textMessageResponseBuilder.buildResponse(animes.get(0), chatId);
        } else if (!mangas.isEmpty()){
            log.debug("Choosing first manga as result for id {}",chatId);
            sendMessage = textMessageResponseBuilder.buildResponse(mangas.get(0), chatId);
        } else {
            log.debug("Responding with nothing found message for id {}",chatId);
            sendMessage = textMessageResponseBuilder.buildNothingFoundResponse(chatId);
        }

        return sendMessage;
    }
}
