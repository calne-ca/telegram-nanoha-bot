package net.beardbot.telegram.bots.nanoha.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.beardbot.telegram.bots.nanoha.api.Anime;
import net.beardbot.telegram.bots.nanoha.api.Manga;
import net.beardbot.telegram.bots.nanoha.api.MyAnimeList;
import net.beardbot.telegram.bots.nanoha.response.TextMessageResponseBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextMessageHandler {
    @Value("${telegram.message.length.max}")
    private int maxMessageLength;
    @Value("${telegram.message.length.min}")
    private int minMessageLength;

    private final MyAnimeList myAnimeList;
    private final TextMessageResponseBuilder textMessageResponseBuilder;

    public SendMessage getAnswer(Message message){
        Long chatId = message.getChatId();
        String text = message.getText();

        log.debug("Received text message from id {}: \"{}\"",chatId,text);

        RequestType type = RequestType.fromMessage(text);
        String query = type.extractQuery(text);

        if (StringUtils.isBlank(query)){
            log.debug("Ignored query from id {} due to empty text.",chatId);
            return null;
        }
        if (query.length() > maxMessageLength || query.length() < minMessageLength){
            log.debug("Ignored query from id {} due to message size limit.",chatId);
            return null;
        }

        if (type != RequestType.MANGA){
            List<Anime> animes = myAnimeList.searchForAnimes(query);
            log.debug("Found {} anime results for query \"{}\"",animes.size(),query);

            if (!animes.isEmpty()){
                log.debug("Choosing first anime as result for id {}",chatId);
                return textMessageResponseBuilder.buildResponse(animes.get(0), chatId);
            }
        }
        if (type != RequestType.ANIME){
            List<Manga> mangas = myAnimeList.searchForMangas(query);
            log.debug("Found {} manga results for query \"{}\"",mangas.size(),query);

            if (!mangas.isEmpty()){
                log.debug("Choosing first manga as result for id {}",chatId);
                return textMessageResponseBuilder.buildResponse(mangas.get(0), chatId);
            }
        }

        log.debug("Responding with nothing found message for id {}",chatId);
        return textMessageResponseBuilder.buildNothingFoundResponse(chatId);
    }
}
