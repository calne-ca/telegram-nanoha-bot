package net.beardbot.telegram.bots.nanoha.response;

import net.beardbot.telegram.bots.nanoha.api.Anime;
import net.beardbot.telegram.bots.nanoha.api.Manga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.ArrayList;
import java.util.List;

@Service
public class InlineMessageResponseCreator
{
	@Value("${telegram.inline.results.description.length}")
	private int descriptionLength = 50;
	@Value("${telegram.inline.results.max}")
	private int maxResults = 5;

	@Autowired
    private TextMessageResponseBuilder textMessageResponseBuilder;

	public InlineMessageResponseBuilder builder(){
		return new InlineMessageResponseBuilder();
	}

    public class InlineMessageResponseBuilder {
		private List<Anime> animes = new ArrayList<>();
		private List<Manga> mangas = new ArrayList<>();

		private InlineMessageResponseBuilder(){}

		public InlineMessageResponseBuilder withAnimes(List<Anime> animes){
			this.animes.addAll(animes);
			return this;
		}

		public InlineMessageResponseBuilder withMangas(List<Manga> mangas){
			this.mangas.addAll(mangas);
			return this;
		}

		public AnswerInlineQuery build(String inlineId, long chatId) {
			AnswerInlineQuery response = new AnswerInlineQuery();
			response.setInlineQueryId(inlineId);
			List<InlineQueryResult> results = buildResultList(chatId);
			response.setResults(results);
			return response;
		}

		private List<InlineQueryResult> buildResultList(long chatId) {
			List<InlineQueryResult> results = new ArrayList<>();
			for (Anime anime : this.animes) {
				if (results.size() < maxResults){
					results.add(createResult(anime,chatId));
				}
			}
			for (Manga manga : this.mangas) {
				if (results.size() < maxResults){
					results.add(createResult(manga,chatId));
				}
			}
			return results;
		}

		private InlineQueryResultArticle createResult(Anime anime, long chatId){
			InlineQueryResultArticle result = new InlineQueryResultArticle();
			result.setId("" + anime.getId());
			result.setInputMessageContent(getInputContent(anime, chatId));
			result.setDescription(shortenString(anime.getSynopsis()));
			result.setTitle(anime.getTitle());
			result.setThumbUrl(anime.getImageUrl());
			return result;
		}

		private InlineQueryResultArticle createResult(Manga manga, long chatId){
			InlineQueryResultArticle result = new InlineQueryResultArticle();
			result.setId("" + manga.getId());
			result.setInputMessageContent(getInputContent(manga, chatId));
			result.setDescription(shortenString(manga.getSynopsis()));
			result.setTitle(manga.getTitle());
			result.setThumbUrl(manga.getImageUrl());
			return result;
		}

		private InputTextMessageContent getInputContent(Anime anime, long chatId) {
			InputTextMessageContent content = new InputTextMessageContent();
			String text = textMessageResponseBuilder.buildResponse(anime, chatId).getText();
			content.enableHtml(true);
			content.setMessageText(text);
			return content;
		}

		private InputTextMessageContent getInputContent(Manga manga, long chatId) {
			InputTextMessageContent content = new InputTextMessageContent();
			String text = textMessageResponseBuilder.buildResponse(manga, chatId).getText();
			content.enableHtml(true);
			content.setMessageText(text);
			return content;
		}

		private String shortenString(String text) {
			if (text.length() <= descriptionLength)
				return text;
			return text.substring(0, descriptionLength);
		}
	}
}
