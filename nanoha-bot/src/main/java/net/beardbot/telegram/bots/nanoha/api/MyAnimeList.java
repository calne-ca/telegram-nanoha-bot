package net.beardbot.telegram.bots.nanoha.api;

import com.github.Doomsdayrs.Jikan4java.connection.Anime.AnimeConnection;
import com.github.Doomsdayrs.Jikan4java.connection.Manga.MangaConnection;
import com.github.Doomsdayrs.Jikan4java.types.Main.Anime.AnimePage.AnimePage;
import com.github.Doomsdayrs.Jikan4java.types.Main.Anime.AnimePage.AnimePageAnime;
import com.github.Doomsdayrs.Jikan4java.types.Main.Manga.MangaPage.MangaPage;
import com.github.Doomsdayrs.Jikan4java.types.Main.Manga.MangaPage.MangaPageManga;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MyAnimeList {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Cacheable("anime-search")
    public List<Anime> searchForAnimes(String query){
        List<Anime> animes = new ArrayList<>();
        try {
            log.debug("Fetching animes from Jikan API for query '{}'",query);
            AnimePage animePage = new AnimeConnection().searchPage(query, 1);
            animePage.getAnimes().forEach(a->animes.add(convert(a)));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return animes;
    }

    @Cacheable("manga-search")
    public List<Manga> searchForMangas(String query){
        List<Manga> mangas = new ArrayList<>();
        try {
            log.debug("Fetching mangas from Jikan API for query '{}'",query);
            MangaPage mangaPage = new MangaConnection().searchPage(query, 1);
            mangaPage.getMangas().forEach(m->mangas.add(convert(m)));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return mangas;
    }

    private Manga convert(MangaPageManga mangaPageManga){
        Manga manga = new Manga();

        manga.setId(mangaPageManga.getMal_id());
        manga.setTitle(mangaPageManga.getTitle());
        manga.setSynopsis(mangaPageManga.getSynopsis());
        manga.setType(MangaType.of(mangaPageManga.getType()));
        manga.setImageUrl(determineImageUrl(mangaPageManga));
        manga.setVolumes(mangaPageManga.getVolumes());
        manga.setChapters(mangaPageManga.getChapters());
        manga.setStatus(determineStatus(mangaPageManga));
        manga.setScore(mangaPageManga.getScore());
        manga.setStartDate(convertDate(mangaPageManga.getStart_date()));
        manga.setEndDate(convertDate(mangaPageManga.getEnd_date()));

        return manga;
    }

    private Anime convert(AnimePageAnime animePageAnime){
        Anime anime = new Anime();

        anime.setId(animePageAnime.getMal_id());
        anime.setTitle(animePageAnime.getTitle());
        anime.setSynopsis(animePageAnime.getSynopsis());
        anime.setType(AnimeType.of(animePageAnime.getType()));
        anime.setImageUrl(determineImageUrl(animePageAnime));
        anime.setEpisodes(animePageAnime.getEpisodes());
        anime.setStatus(determineStatus(animePageAnime));
        anime.setScore(animePageAnime.getScore());
        anime.setStartDate(convertDate(animePageAnime.getStart_date()));
        anime.setEndDate(convertDate(animePageAnime.getEnd_date()));

        return anime;
    }

    private String determineImageUrl(AnimePageAnime animePageAnime){
        return animePageAnime.getIconURL().replace(".jpg","l.jpg");
    }

    private String determineImageUrl(MangaPageManga mangaPageManga){
        return mangaPageManga.getIconURL().replace(".jpg","l.jpg");
    }

    private AnimeStatus determineStatus(AnimePageAnime animePageAnime){
        if (animePageAnime.isAiring()){
            return AnimeStatus.CURRENTLY_AIRING;
        }

        return StringUtils.isBlank(animePageAnime.getEnd_date()) ? AnimeStatus.NOT_YET_AIRED : AnimeStatus.FINISHED_AIRING;
    }

    private MangaStatus determineStatus(MangaPageManga mangaPageManga){
        if (mangaPageManga.isPublishing()){
            return MangaStatus.PUBLISHING;
        }

        return StringUtils.isBlank(mangaPageManga.getEnd_date()) ? MangaStatus.NOT_YET_PUBLISHED : MangaStatus.FINISHED;
    }

    private Date convertDate(String dateString){
        if (StringUtils.isBlank(dateString)){
            return Date.from(Instant.ofEpochMilli(0));
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        try {
            return dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
