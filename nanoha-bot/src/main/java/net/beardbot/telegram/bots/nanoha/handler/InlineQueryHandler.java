package net.beardbot.telegram.bots.nanoha.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.beardbot.telegram.bots.nanoha.api.Anime;
import net.beardbot.telegram.bots.nanoha.api.Manga;
import net.beardbot.telegram.bots.nanoha.api.MyAnimeList;
import net.beardbot.telegram.bots.nanoha.response.InlineMessageResponseCreator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InlineQueryHandler {
    @Value("${telegram.message.length.max}")
    private int maxMessageLength;
    @Value("${telegram.message.length.min}")
    private int minMessageLength;

    private final MyAnimeList myAnimeList;
    private final InlineMessageResponseCreator inlineMessageResponseCreator;

    public AnswerInlineQuery getAnswer(InlineQuery inlineQuery){
        String inlineQueryId = inlineQuery.getId();
        String text = inlineQuery.getQuery();

        log.debug("Received inline text from id {}: \"{}\"",inlineQueryId,text);

        RequestType type = RequestType.fromMessage(text);
        String query = type.extractQuery(text);

        if (StringUtils.isBlank(query)){
            log.debug("Ignored inline query from id {} due to empty text.",inlineQueryId);
            return null;
        }
        if (query.length() > maxMessageLength || query.length() < minMessageLength){
            log.debug("Ignored inline query from id {} due to message size limit.",inlineQueryId);
            return null;
        }

        List<Anime> animes = new ArrayList<>();
        List<Manga> mangas = new ArrayList<>();

        if (type == RequestType.ANIME || type == RequestType.BOTH || type == RequestType.NONE){
            animes = myAnimeList.searchForAnimes(query);
            log.debug("Found {} anime results for query \"{}\"",animes.size(),query);
        }
        if (type == RequestType.MANGA || type == RequestType.BOTH){
            mangas = myAnimeList.searchForMangas(query);
            log.debug("Found {} manga results for query \"{}\"",mangas.size(),query);
        }

        return inlineMessageResponseCreator.builder().withAnimes(animes).withMangas(mangas).build(inlineQueryId, Long.parseLong(inlineQueryId));
    }
}
