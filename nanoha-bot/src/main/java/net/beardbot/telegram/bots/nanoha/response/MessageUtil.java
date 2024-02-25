package net.beardbot.telegram.bots.nanoha.response;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageUtil {
	public static SendMessage createBasicSendMessage(long chatId, String text) {
		var message = new SendMessage();
		message.enableHtml(true);
		message.setText(text);
		message.setChatId(chatId);
		return message;
	}
}
