package org.fsc1198.osa.stars.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.osa.stars.logging.LogEntryAndExit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.UUID;

import static org.fsc1198.osa.stars.bot.KeyboardMaker.GIVE_STAR_BUTTON_TITLE;
import static org.mascara.notifier.util.TelegramUtils.extractChatId;
import static org.mascara.notifier.util.TelegramUtils.hasTextMessage;


@Slf4j
@Service
@RequiredArgsConstructor
public class OsaStarsBot extends AbstractOsaStarNotifierBot {

	private final KeyboardMaker keyboardMaker;

	@Value("${telegram.bot.name}")
	private String botName;

	@Value("${telegram.bot.token}")
	private String botToken;

	@Override
	public String getBotUsername() {
		return botName;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	@LogEntryAndExit
	public void onUpdateReceived(Update update) {
		var chatId = extractChatId(update);
		sendMainKeyboard(chatId);

		if (!hasTextMessage(update)) {
			if (update.hasCallbackQuery()) {
				String text = update.getCallbackQuery().getData();
				sendTextMessage(chatId, text);
				return;
			}
			return;
		}
		if (update.getMessage().getText().equals(GIVE_STAR_BUTTON_TITLE)) {
			List<String> nameList = List.of(
					UUID.randomUUID().toString(),
					UUID.randomUUID().toString(),
					UUID.randomUUID().toString()
			);
			tryExecute(keyboardMaker.getKeyboardMessage2(chatId, nameList));
		} else {
			sendTextMessage(chatId, update.getMessage().getText());
		}
	}

	private void sendMainKeyboard(Long chatId) {
		tryExecute(keyboardMaker.getKeyboardMessage(chatId));
	}

}
