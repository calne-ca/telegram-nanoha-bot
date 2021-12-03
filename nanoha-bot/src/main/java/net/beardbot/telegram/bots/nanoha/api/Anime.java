package net.beardbot.telegram.bots.nanoha.api;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class Anime {
    private int id;
    private String title;
    private String englishTitle;
    private AnimeType type;
    private String synopsis;
    private int episodes;
    private AnimeStatus status;
    private double score;
    private Date startDate = Date.from(Instant.now());
    private Date endDate = Date.from(Instant.now());
    private String imageUrl;

    public String getMalUrl(){
        return String.format("https://myanimelist.net/anime/%d", id);
    }
}
