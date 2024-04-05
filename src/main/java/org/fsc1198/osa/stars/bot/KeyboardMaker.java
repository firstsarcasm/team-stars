package org.fsc1198.osa.stars.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeyboardMaker {

	public static final String GIVE_STAR_BUTTON_TITLE = "Дать звездочку";
	public static final String MY_STARS_BUTTON_TITLE = "Мои звездочки";
	public static final String SUPERSTAR_BUTTON_TITLE = "Лидер по звездочкам в спринте";
	public static final String TOXIC_BUTTON_TITLE = "Токичность";

	private static final String HEADER_TEXT = "-----------";

	private final SendMessage.SendMessageBuilder keyboardMessage = SendMessage.builder()
			.replyMarkup(createKeyboard())
			.text(HEADER_TEXT);

	public SendMessage getKeyboardMessage(Long chatId) {
		return keyboardMessage
				.chatId(chatId.toString())
				.build();
	}

	public SendMessage getKeyboardMessage2(Long chatId, List<String> nameList) {
		SendMessage.SendMessageBuilder keyboardMessage2 = SendMessage.builder()
				.replyMarkup(createInlineKeyboard(nameList))
				.text("Кому даем звездочку?");
		return keyboardMessage2
				.chatId(chatId.toString())
				.build();
	}

	private ReplyKeyboardMarkup createKeyboard() {
		var row = new KeyboardRow();
		var row2 = new KeyboardRow();
		var row3 = new KeyboardRow();

		KeyboardButton scheduleButton = new KeyboardButton(GIVE_STAR_BUTTON_TITLE);
		KeyboardButton subscribeButton = new KeyboardButton(MY_STARS_BUTTON_TITLE);
		KeyboardButton unsubscribeButton = new KeyboardButton(SUPERSTAR_BUTTON_TITLE);
		KeyboardButton workingDaysButton = new KeyboardButton(TOXIC_BUTTON_TITLE);

		row.add(scheduleButton);
		row2.add(subscribeButton);
		row2.add(unsubscribeButton);
		row3.add(workingDaysButton);

		List<KeyboardRow> keyboardRows = List.of(row, row2, row3);
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
		keyboardMarkup.setResizeKeyboard(true);

		return keyboardMarkup;
	}

	private InlineKeyboardMarkup createInlineKeyboard(List<String> nameList) {
		var buttons = nameList.stream()
				.map(name -> {
					InlineKeyboardButton button = new InlineKeyboardButton(name);
					button.setCallbackData(name);
					return button;
				})
				.map(List::of)
				.collect(Collectors.toList());

		return new InlineKeyboardMarkup(buttons);
	}

}
