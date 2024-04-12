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
	public static final String TOXIC_BUTTON_TITLE = "Токсичность";
	public static final String HELP_BUTTON_TITLE = "Помощь";

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
		var row4 = new KeyboardRow();

		KeyboardButton giveStarButton = new KeyboardButton(GIVE_STAR_BUTTON_TITLE);
		KeyboardButton myStarsButton = new KeyboardButton(MY_STARS_BUTTON_TITLE);
		KeyboardButton superstarButton = new KeyboardButton(SUPERSTAR_BUTTON_TITLE);
		KeyboardButton toxicButton = new KeyboardButton(TOXIC_BUTTON_TITLE);
		KeyboardButton helpButton = new KeyboardButton(HELP_BUTTON_TITLE);

		row.add(giveStarButton);
		row2.add(myStarsButton);
		row2.add(superstarButton);
		row3.add(toxicButton);
		row4.add(helpButton);

		List<KeyboardRow> keyboardRows = List.of(row, row2, row3, row4);
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
