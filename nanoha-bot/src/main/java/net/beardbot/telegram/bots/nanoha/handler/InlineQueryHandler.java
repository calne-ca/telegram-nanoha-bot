package net.beardbot.telegram.bots.nanoha.handler;

import lombok.extern.slf4j.Slf4j;
import net.beardbot.myanimelist.MALClient;
import net.beardbot.myanimelist.model.anime.Anime;
import net.beardbot.myanimelist.model.manga.Manga;
import net.beardbot.telegram.bots.nanoha.response.InlineMessageResponseCreator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;

import java.util.List;

@Service
@Slf4j
public class InlineQueryHandler {
    @Autowired
    private MALClient malClient;
    @Autowired
    private InlineMessageResponseCreator inlineMessageResponseCreator;

    @Cacheable("nanoha-inline-query")
    public AnswerInlineQuery getAnswer(InlineQuery inlineQuery){
        String inlineQueryId = inlineQuery.getId();
        String query = inlineQuery.getQuery();

        log.debug("Received inline query from id {}: \"{}\"",inlineQueryId,query);

        if (StringUtils.isBlank(query)){
            log.debug("Ignored inline query from id {} due to empty query",inlineQueryId);
            return null;
        }

        List<Anime> animes = malClient.searchForAnime(query);
        List<Manga> mangas = malClient.searchForManga(query);

        log.debug("Found {} results for query \"{}\"",animes.size() + mangas.size(),query);

        return inlineMessageResponseCreator.builder().withAnimes(animes).withMangas(mangas).build(inlineQueryId, Long.parseLong(inlineQueryId));
    }
}
