package org.fsc1198.team.stars.bot;

import lombok.extern.slf4j.Slf4j;
import org.fsc1198.team.stars.logging.LogEntryAndExit;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public abstract class AbstractTeamStarsNotifierBot extends TelegramLongPollingBot implements MessageSender {

	protected void tryExecute(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@LogEntryAndExit
	protected void sendTextMessage(Long chatId, String text) {
		var message = SendMessage.builder()
				.text(text)
				.chatId(chatId.toString())
				.build();
		tryExecute(message);
	}

}
