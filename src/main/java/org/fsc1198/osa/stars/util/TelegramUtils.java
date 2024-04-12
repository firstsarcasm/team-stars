package org.mascara.notifier.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TelegramUtils {

	public static Long extractChatId(Update update) {
		Message message = update.getMessage();
		if (hasTextMessage(update)) {
			return message.getChatId();
		} else {
			return Optional.of(update)
					.map(Update::getCallbackQuery)
					.map(CallbackQuery::getMessage)
					.map(Message::getChatId)
					.orElse(null);
		}
	}

	public static boolean hasTextMessage(Update update) {
		return update.hasMessage() && update.getMessage().hasText();
	}
}
