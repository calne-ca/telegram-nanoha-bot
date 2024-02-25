package net.beardbot.telegram.bots.nanoha.response;

import net.beardbot.telegram.bots.nanoha.api.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TextMessageResponseBuilder
{
	private static final String NOTHING_FOUND = "Sorry, I couldn't find anything with that title :/";
	private static final String DATE_FORMAT = "yyyy";
	private static final String ICON_IMAGE_URL = "🍙";
    private static final String ICON_TITLE = "🇯🇵";
    private static final String ICON_ENGLISH = "🇬🇧";
    private static final String ICON_DATE = "📆";
    private static final String ICON_EPISODES = "📺";
    private static final String ICON_VOLUMES = "📚";
    private static final String RATING_FULL = "🌕";
    private static final String RATING_EMPTY = "🌑";
    private static final String[] RATING_FRACTION = {"🌘","🌗","🌖"};

    public SendMessage buildResponse(Anime anime, long chatId) {
		return MessageUtil.createBasicSendMessage(chatId,generateMessageText(anime));
    }
	public SendMessage buildResponse(Manga manga, long chatId) {
		return MessageUtil.createBasicSendMessage(chatId,generateMessageText(manga));
	}

    public SendMessage buildNothingFoundResponse(long chatId) {
		return MessageUtil.createBasicSendMessage(chatId,NOTHING_FOUND);
    }

    private static String generateMessageText(Anime anime) {
		var header = generateHeaderString(anime);
		var synopsis = "<i>" + anime.getSynopsis() + "</i>";
		var rating = generateRatingString(anime.getScore());
		return header + "\n\n" + synopsis + "\n\n" + rating;
	}

	private static String generateMessageText(Manga manga) {
		var header = generateHeaderString(manga);
		var synopsis = "<i>" + manga.getSynopsis() + "</i>";
		var rating = generateRatingString(manga.getScore());
		return header + "\n\n" + synopsis + "\n\n" + rating;
	}

    private static String generateHeaderString(Anime anime) {
		var header = "<a href=\"" + anime.getImageUrl() + "\">" + ICON_IMAGE_URL + "</a> ";
		header += "Anime, " + (anime.getType() == null ? "Unknown" : anime.getType().getValue());
		header += generateTitle(anime);
		header += generateDateString(anime);
		header += generateEpisodes(anime);
		return header;
    }

	private static String generateHeaderString(Manga manga) {
		var header = "<a href=\"" + manga.getImageUrl() + "\">" + ICON_IMAGE_URL + "</a> ";
		header += (manga.getType() == null ? "Manga, Unknown" : manga.getType().getValue());
		header += generateTitle(manga);
		header += generateDateString(manga);
		header += generateEpisodes(manga);
		return header;
	}

    private static String generateTitle(Anime anime) {
		var title = "\n<a href=\"" + anime.getMalUrl() + "\">" + ICON_TITLE + " " + anime.getTitle();

		if (!StringUtils.isBlank(anime.getEnglishTitle())) {
			title += "\n" + ICON_ENGLISH + " " + anime.getEnglishTitle();
		}

		title += "</a>";

		return title;
	}

	private static String generateTitle(Manga manga) {
		var title = "\n<a href=\"" + manga.getMalUrl() + "\">" + ICON_TITLE + " " + manga.getTitle();

		if (!StringUtils.isBlank(manga.getEnglishTitle())) {
			title += "\n" + ICON_ENGLISH + " " + manga.getEnglishTitle();
		}

		title += "</a>";
		return title;
	}

    private static String generateEpisodes(Anime anime) {
		if (anime.getType() == AnimeType.MOVIE) {
			return "";
		}

		var episodes = anime.getEpisodes() == null ? "?" : String.valueOf(anime.getEpisodes());
		return String.format("\n\n%s Episodes: %s", ICON_EPISODES, episodes);
	}

	private static String generateEpisodes(Manga manga) {
		var chapters = manga.getChapters() == null ? "?" : String.valueOf(manga.getChapters());
		var volumes = manga.getVolumes() == null ? "?" : String.valueOf(manga.getVolumes());
		return String.format("\n\n%s Volumes: %s  Chapters: %s", ICON_VOLUMES, volumes, chapters);
	}



    private static String generateRatingString(double f) {
		var str = "";
		var whole = (int) f;
		var fraction = f - whole;

		for (int i = 0; i < whole; i++) {
			str += RATING_FULL;
		}
		if (fraction >= 0.01 && fraction <= 0.33)
			str += RATING_FRACTION[0];
		if (fraction >= 0.34 && fraction <= 0.66)
			str += RATING_FRACTION[1];
		if (fraction >= 0.67 && fraction <= 0.99)
			str += RATING_FRACTION[2];
		if (fraction == 0.00)
			str += RATING_EMPTY;
		for (int i = 0; i < 9 - whole; i++) {
			str += RATING_EMPTY;
		}
		str += " (" + f + "/10)";
		return str;
    }

    private static String generateDateString(Anime anime) {
		var str = "\n" + ICON_DATE + " ";
		var start = anime.getStartDate();
		var end = anime.getEndDate();
		var startString = start == null ? "Not yet aired" : formatDate(start);
		var endString = end == null ? "" : formatDate(end);

		if (startString.equals(endString) || endString.isBlank())
			endString = "";
		else
			endString = " - " + endString;

		var status = anime.getStatus();

		if (status != AnimeStatus.NOT_YET_AIRED) {
			str += startString + endString;
		}
		if (status != AnimeStatus.FINISHED_AIRING) {
			str += " (" + status.getValue() + ")";
		}
		return str;
	}

	private static String generateDateString(Manga manga) {
		var str = "\n" + ICON_DATE + " ";
		var start = manga.getStartDate();
		var end = manga.getEndDate();
		var startString = start == null ? "Not yet published" : formatDate(start);
		var endString = end == null ? "" : formatDate(end);

		if (startString.equals(endString) || endString.isBlank())
			endString = "";
		else
			endString = " - " + endString;

		var status = manga.getStatus();

		if (!status.equals(MangaStatus.NOT_YET_PUBLISHED)) {
			str += startString + endString;
		}
		if (!status.equals(MangaStatus.FINISHED)) {
			str += " (" + status.getValue() + ")";
		}
		return str;
	}

	private static String formatDate(Date date) {
		var formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(date);
	}
}
