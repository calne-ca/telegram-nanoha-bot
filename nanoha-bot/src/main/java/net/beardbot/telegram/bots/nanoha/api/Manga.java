package net.beardbot.telegram.bots.nanoha.api;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class Manga {
    private int id;
    private String title;
    private String englishTitle;
    private MangaType type;
    private String synopsis;
    private Integer volumes;
    private Integer chapters;
    private MangaStatus status;
    private Double score;
    private Date startDate = Date.from(Instant.now());
    private Date endDate = Date.from(Instant.now());
    private String imageUrl;

    public String getMalUrl() {
        return String.format("https://myanimelist.net/manga/%d", id);
    }
}
