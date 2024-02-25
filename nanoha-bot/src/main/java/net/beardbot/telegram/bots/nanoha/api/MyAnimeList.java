package net.beardbot.telegram.bots.nanoha.api;

import lombok.extern.slf4j.Slf4j;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MyAnimeList {
    private final Jikan jikan = new Jikan();

    @Cacheable("anime-search")
    public List<Anime> searchForAnimes(String query) {
        var animes = new ArrayList<Anime>();

        try {
            log.debug("Fetching animes from Jikan API for query '{}'", query);

            jikan.query().anime().search()
                    .query(query)
                    .limit(20)
                    .execute()
                    .collectList()
                    .block().forEach(a -> animes.add(convert(a)));
        } catch (JikanQueryException e) {
            e.printStackTrace();
        }
        return animes;
    }

    @Cacheable("manga-search")
    public List<Manga> searchForMangas(String query) {
        var mangas = new ArrayList<Manga>();

        try {
            log.debug("Fetching mangas from Jikan API for query '{}'", query);

            jikan.query().manga().search()
                    .query(query)
                    .limit(20)
                    .execute()
                    .collectList()
                    .block().forEach(m -> mangas.add(convert(m)));
        } catch (JikanQueryException e) {
            e.printStackTrace();
        }

        return mangas;
    }

    private Manga convert(net.sandrohc.jikan.model.manga.Manga m) {
        var manga = new Manga();

        manga.setId(m.getMalId());
        manga.setTitle(m.getTitle());
        manga.setSynopsis(m.getSynopsis());
        manga.setType(convert(m.getType()));
        manga.setImageUrl(m.getImages().getPreferredImageUrl());
        manga.setVolumes(m.getVolumes());
        manga.setChapters(m.getChapters());
        manga.setStatus(determineStatus(m));
        manga.setScore(m.getScore());
        manga.setStartDate(convert(m.getPublished().getFrom()));
        manga.setEndDate(convert(m.getPublished().getTo()));

        return manga;
    }

    private Anime convert(net.sandrohc.jikan.model.anime.Anime a) {
        var anime = new Anime();

        anime.setId(a.getMalId());
        anime.setTitle(a.getTitle());
        anime.setSynopsis(a.getSynopsis());
        anime.setType(convert(a.getType()));
        anime.setImageUrl(a.getImages().getPreferredImageUrl());
        anime.setEpisodes(a.getEpisodes());
        anime.setStatus(determineStatus(a));
        anime.setScore(a.getScore());
        anime.setStartDate(convert(a.getAired().getFrom()));
        anime.setEndDate(convert(a.getAired().getTo()));

        return anime;
    }

    private AnimeType convert(net.sandrohc.jikan.model.anime.AnimeType type) {
        if (type == null) {
            return AnimeType.UNKNOWN;
        }

        return AnimeType.of(type.getSearch());
    }

    private MangaType convert(net.sandrohc.jikan.model.manga.MangaType type) {
        if (type == null) {
            return MangaType.UNKNOWN;
        }

        return MangaType.of(type.getSearch());
    }

    private Date convert(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        return Date.from(offsetDateTime.toInstant());
    }

    private AnimeStatus determineStatus(net.sandrohc.jikan.model.anime.Anime animePageAnime) {
        if (animePageAnime.isAiring()) {
            return AnimeStatus.CURRENTLY_AIRING;
        }

        return animePageAnime.getAired().getTo() == null ? AnimeStatus.NOT_YET_AIRED : AnimeStatus.FINISHED_AIRING;
    }

    private MangaStatus determineStatus(net.sandrohc.jikan.model.manga.Manga m) {
        if (m.isPublishing()) {
            return MangaStatus.PUBLISHING;
        }

        return m.getPublished().getTo() == null ? MangaStatus.NOT_YET_PUBLISHED : MangaStatus.FINISHED;
    }
}
