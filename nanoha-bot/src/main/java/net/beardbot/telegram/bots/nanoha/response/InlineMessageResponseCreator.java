package net.beardbot.telegram.bots.nanoha.response;

import net.beardbot.telegram.bots.nanoha.api.Anime;
import net.beardbot.telegram.bots.nanoha.api.Manga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InlineMessageResponseCreator
{
	@Value("${telegram.inline.results.description.length}")
	private int descriptionLength;
	@Value("${telegram.inline.results.max}")
	private int maxResults;

	@Autowired
    private TextMessageResponseBuilder textMessageResponseBuilder;

	public InlineMessageResponseBuilder builder(){
		return new InlineMessageResponseBuilder();
	}

	public class InlineMessageResponseBuilder {
		private final List<Anime> animes = new ArrayList<>();
		private final List<Manga> mangas = new ArrayList<>();

		private InlineMessageResponseBuilder() {
		}

		public InlineMessageResponseBuilder withAnimes(List<Anime> animes) {
			this.animes.addAll(animes);
			return this;
		}

		public InlineMessageResponseBuilder withMangas(List<Manga> mangas) {
			this.mangas.addAll(mangas);
			return this;
		}

		public AnswerInlineQuery build(String inlineId, long chatId) {
			var response = new AnswerInlineQuery();
			response.setInlineQueryId(inlineId);
			var results = buildResultList(chatId);
			response.setResults(results);
			return response;
		}

		private List<InlineQueryResult> buildResultList(long chatId) {
			var results = new ArrayList<InlineQueryResult>();

			for (Anime anime : this.animes) {
				if (results.size() < maxResults) {
					results.add(createResult(anime, chatId));
				}
			}
			for (Manga manga : this.mangas) {
				if (results.size() < maxResults) {
					results.add(createResult(manga, chatId));
				}
			}
			return results;
		}

		private InlineQueryResultArticle createResult(Anime anime, long chatId) {
			var result = new InlineQueryResultArticle();
			result.setId(UUID.randomUUID().toString());
			result.setInputMessageContent(getInputContent(anime, chatId));
			result.setDescription(shortenString(anime.getSynopsis()));
			result.setTitle(anime.getTitle());
			result.setThumbnailUrl(anime.getImageUrl());
			return result;
		}

		private InlineQueryResultArticle createResult(Manga manga, long chatId) {
			var result = new InlineQueryResultArticle();
			result.setId(UUID.randomUUID().toString());
			result.setInputMessageContent(getInputContent(manga, chatId));
			result.setDescription(shortenString(manga.getSynopsis()));
			result.setTitle(manga.getTitle());
			result.setThumbnailUrl(manga.getImageUrl());
			return result;
		}

		private InputTextMessageContent getInputContent(Anime anime, long chatId) {
			var content = new InputTextMessageContent();
			var text = textMessageResponseBuilder.buildResponse(anime, chatId).getText();
			content.setMessageText(text);
			content.setParseMode(ParseMode.HTML);
			return content;
		}

		private InputTextMessageContent getInputContent(Manga manga, long chatId) {
			var content = new InputTextMessageContent();
			var text = textMessageResponseBuilder.buildResponse(manga, chatId).getText();
			content.setMessageText(text);
			content.setParseMode(ParseMode.HTML);
			return content;
		}

		private String shortenString(String text) {
			if (text == null) {
				return null;
			}

			if (text.length() <= descriptionLength)
				return text;

			return text.substring(0, descriptionLength);
		}
	}
}
