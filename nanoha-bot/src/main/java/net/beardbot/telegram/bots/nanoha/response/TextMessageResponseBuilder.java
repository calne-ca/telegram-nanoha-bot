package net.beardbot.telegram.bots.nanoha.response;

import net.beardbot.telegram.bots.nanoha.api.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TextMessageResponseBuilder
{
    private static final String NOTHING_FOUND = "Sorry, I couldn't find anything with that title :/";
    private static final String DATE_FROMAT = "yyyy";
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
		String header = generateHeaderString(anime);
		String synopsis = "<i>" + anime.getSynopsis() + "</i>";
		String rating = generateRatingString(anime.getScore());
		String text = header + "\n\n" + synopsis + "\n\n" + rating;
		return text;
    }

	private static String generateMessageText(Manga manga) {
		String header = generateHeaderString(manga);
		String synopsis = "<i>" + manga.getSynopsis() + "</i>";
		String rating = generateRatingString(manga.getScore());
		String text = header + "\n\n" + synopsis + "\n\n" + rating;
		return text;
	}

    private static String generateHeaderString(Anime anime) {
		String header = "<a href=\"" + anime.getImageUrl() + "\">" + ICON_IMAGE_URL + "</a> ";
		header += "Anime, " + (anime.getType() == null ? "Unknown" : anime.getType().getValue());
		header += generateTitle(anime);
		header += generateDateString(anime);
		header += generateEpisodes(anime);
		return header;
    }

	private static String generateHeaderString(Manga manga) {
		String header = "<a href=\"" + manga.getImageUrl() + "\">" + ICON_IMAGE_URL + "</a> ";
		header += (manga.getType() == null ? "Manga, Unknown" : manga.getType().getValue());
		header += generateTitle(manga);
		header += generateDateString(manga);
		header += generateEpisodes(manga);
		return header;
	}

    private static String generateTitle(Anime anime) {
		String title = "\n<a href=\"" + anime.getMalUrl() + "\">" + ICON_TITLE + " " + anime.getTitle();
		if (!StringUtils.isBlank(anime.getEnglishTitle())) {
			title += "\n" + ICON_ENGLISH + " " + anime.getEnglishTitle();
		}
		title += "</a>";
		return title;
    }

	private static String generateTitle(Manga manga) {
		String title = "\n<a href=\"" + manga.getMalUrl() + "\">" + ICON_TITLE + " " + manga.getTitle();
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
		String episodes = anime.getEpisodes() == 0 ? "?" : String.valueOf(anime.getEpisodes());
		return String.format("\n\n%s Episodes: %s",ICON_EPISODES,episodes);
    }

	private static String generateEpisodes(Manga manga) {
		String chapters = manga.getChapters() == 0 ? "?" : String.valueOf(manga.getChapters());
		String volumes = manga.getVolumes() == 0 ? "?" : String.valueOf(manga.getVolumes());
		return String.format("\n\n%s Volumes: %s  Chapters: %s",ICON_VOLUMES,volumes,chapters);
	}



    private static String generateRatingString(double f) {
		String str = "";
		int whole = (int)f;
		double fraction = f - whole;
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
		String str = "\n" + ICON_DATE + " ";
		Date start = anime.getStartDate();
		Date end = anime.getEndDate();
		String startString = formatDate(start);
		String endString = formatDate(end);
		if (startString.equals(endString) || end.getTime() < start.getTime())
			endString = "";
		else
			endString = " - " + endString;
			AnimeStatus status = anime.getStatus();
			if (status != AnimeStatus.NOT_YET_AIRED) {
			str += startString + endString;
		}
		if (status != AnimeStatus.FINISHED_AIRING) {
			str += " (" + status.getValue() + ")";
		}
		return str;
    }

	private static String generateDateString(Manga manga) {
		String str = "\n" + ICON_DATE + " ";
		Date start = manga.getStartDate();
		Date end = manga.getEndDate();
		String startString = formatDate(start);
		String endString = formatDate(end);
		if (startString.equals(endString) || end.getTime() < start.getTime())
			endString = "";
		else
			endString = " - " + endString;
		MangaStatus status = manga.getStatus();
		if (!status.equals(MangaStatus.NOT_YET_PUBLISHED)) {
			str += startString + endString;
		}
		if (!status.equals(MangaStatus.FINISHED)) {
			str += " (" + status.getValue() + ")";
		}
		return str;
	}

	private static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FROMAT);
		return formatter.format(date);
	}
}
