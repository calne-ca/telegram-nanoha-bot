package net.beardbot.telegram.bots.nanoha.response;

import org.telegram.telegrambots.api.methods.send.SendMessage;

public class MessageUtil
{
    public static SendMessage createBasicSendMessage(long chatId, String text){
		SendMessage message = new SendMessage();
		message.enableHtml(true);
		message.setText(text);
		message.setChatId(chatId);
		return message;
    }
}
