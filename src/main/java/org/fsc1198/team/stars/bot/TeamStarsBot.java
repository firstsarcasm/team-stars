package org.fsc1198.team.stars.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.team.stars.dto.GivenStarDto;
import org.fsc1198.team.stars.dto.StarUserProjection;
import org.fsc1198.team.stars.logging.LogEntryAndExit;
import org.fsc1198.team.stars.service.RegistrationService;
import org.fsc1198.team.stars.service.StarService;
import org.fsc1198.team.stars.service.UserService;
import org.fsc1198.team.stars.util.TelegramUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.fsc1198.team.stars.bot.KeyboardMaker.GIVE_STAR_BUTTON_TITLE;
import static org.fsc1198.team.stars.bot.KeyboardMaker.HELP_BUTTON_TITLE;
import static org.fsc1198.team.stars.bot.KeyboardMaker.MY_STARS_BUTTON_TITLE;
import static org.fsc1198.team.stars.bot.KeyboardMaker.STATS_BUTTON_TITLE;
import static org.fsc1198.team.stars.bot.KeyboardMaker.SUPERSTAR_BUTTON_TITLE;
import static org.fsc1198.team.stars.bot.KeyboardMaker.TOXIC_BUTTON_TITLE;
import static org.springframework.util.ObjectUtils.isEmpty;

//todo дать токсичную звездочку
//todo самый токсичный должен назначить себя сам, по умолчанию Настя.
@Slf4j
@Service
@RequiredArgsConstructor
public class TeamStarsBot extends AbstractTeamStarsNotifierBot {

	private static final String EMPTY_MESSAGE = "EMPTY_MESSAGE";
	private static final String START_REGISTRATION_OPERATION = "/registration";
	private static final int MAX_USERNAME_LENGTH = 50;

	//todo move to separate class
	private static final String REGISTRATION_MESSAGE = "Регистрация не пройдена. \nЧтобы пройти регистрацию, отправь сообщение вида: \n/registration Иванов Иван\nПожалуйста, указывай имя корректно - его будут использовать для начисления звездочек.";
	private static final String GREETING_MESSAGE = "Привет '%s'. Ты успешно зарегистрирован. \nТеперь ты можешь получать звездочки от коллег.";
	private static final String NAME_IS_TOO_LONG_MESSAGE = "Слишком длинное имя.";
	private static final String NAME_IS_TOO_SHORT_MESSAGE = "Слишком короткое или пустое имя.";
	private static final String HELP_MESSAGE = "Регистрируешься типа, а потом, ну это самое, кнопки там нажимаешь, и звездочки такие начисляются всем. \nА себе нельзя начислять, единственное что. \nНо это и так никто делать не будет, понятно же. \nЕще там другие кнопки есть, ну они тоже что-то делают. Надеюсь понятно обьяснил. А там, еще вот если звездочек одинаково, то больше всех кого-то одного выбирает, а почему бы и нет? \nВот все.";
	private static final String STAR_GRANTED_MESSAGE = "Пользователь '%s' успешно получил звездочку";
	private static final String STAR_GRANTED_TO_YOU_MESSAGE = "Вы получили звездочку от пользователя '%s'";
	private static final String DONT_AUTOSTAR_MESSAGE = "Что-то пошло не так. Если ты хотел проставить звездочку сам себе, то так нельзя. Но можешь договорится с кем-то, и он поставит тебе хоть 100 штук :) . Да, мы могли бы убрать тебя из списка, но это не так весело.";
	private static final String ONCE_REG_MESSAGE = "Зарегистрироваться можно только один раз.";
	private static final String YOUR_STARS_AMOUNT_MESSAGE = "В спринте номер '%d' у тебя на данный момент '%d' звездочек.\nВсего звездочек за все спринты у тебя '%d'.";
	private static final String MAX_STARS_RIGHT_NOW = "Больше всех звездочек за текущий спринт сейчас у '%s'. Их целых: '%d' шт.\nЕсли у тебя их столько же, но суперзвезда не ты - значит ты получил их позже.";
	private static final String MAX_STARS_RIGHT_NOW_IS_ABSENT = "Пока звездочек никто не получил. Есть шанс стать первым :)";
	private static final String TOXIC_MESSAGE = "функционал в разработке";
	private static final String WHO_WE_ARE_MESSAGE = "Кто мы?";
	private static final String WE_ARE_BOT_MESSAGE = "Мы есть бот.";
	private static final int MIN_USERNAME_LENGTH = 5;
	private static final String ALL_STARS_MESSAGE = "Звездочки за все время:\n\n%s";
	private static final String CURRENT_SPRINT_STARS_MESSAGE = "Звездочки за текущий спринт:\n\n%s";
	private static final String UPDATE_MENU_FOR_OLD_USERS_COMMAND = "/update-menu-for-old-users";

	private final KeyboardMaker keyboardMaker;
	private final StarService starService;
	private final UserService userService;
	private final RegistrationService registrationService;

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
		var chatId = TelegramUtils.extractChatId(update);
		String messageText = extractMessageText(update);

		if (isNull(chatId)) {
			return;
		}

		boolean isUserRegistered = handleUserRegistration(update, chatId, messageText);
		if (!isUserRegistered) {
			return;
		}

		if (!TelegramUtils.hasTextMessage(update)) {
			if (update.hasCallbackQuery()) {
				String targetUsername = update.getCallbackQuery().getData();
				try {
					GivenStarDto givenStarDto = starService.giveStar(targetUsername, chatId);
					Long targetUserChatId = givenStarDto.getTargetUser().getChatId();
					String donorUser = givenStarDto.getDonorUser().getName();
					sendTextMessage(targetUserChatId, String.format(STAR_GRANTED_TO_YOU_MESSAGE, donorUser));
				} catch (Exception e) {
					sendTextMessage(chatId, DONT_AUTOSTAR_MESSAGE);
					return;
				}
				sendTextMessage(chatId, String.format(STAR_GRANTED_MESSAGE, targetUsername));
				return;
			}
			return;
		}

		if(UPDATE_MENU_FOR_OLD_USERS_COMMAND.equals(messageText)) {
//			sendMainKeyboard(chatId);
			userService.getAllUserChatIds().forEach(this::sendMainKeyboard);
			return;
		}

		//todo move to separate class
		switch (messageText) {
			case GIVE_STAR_BUTTON_TITLE:
				List<String> users = userService.getAllUserNames();
				tryExecute(keyboardMaker.getKeyboardMessage2(chatId, users));
				return;
			case MY_STARS_BUTTON_TITLE:
				int starsAmount = starService.getAllStarsAmount(chatId);
				Pair<Long, Integer> currentSprintToStars = starService.getStarsAmountForCurrentSprint(chatId);
				sendTextMessage(chatId, String.format(YOUR_STARS_AMOUNT_MESSAGE, currentSprintToStars.getFirst(), currentSprintToStars.getSecond(), starsAmount));
				return;
			case SUPERSTAR_BUTTON_TITLE:
				StarUserProjection superstar = starService.getSuperstarForCurrentSprintUsernameAndStars();
				if (isNull(superstar)) {
					sendTextMessage(chatId, MAX_STARS_RIGHT_NOW_IS_ABSENT);
				} else {
					sendTextMessage(chatId, String.format(MAX_STARS_RIGHT_NOW, superstar.getName(), superstar.getStarsAmount()));
				}
				return;
			case STATS_BUTTON_TITLE:
				List<StarUserProjection> currentSprintStarsStats = starService.getCurrentSprintStarsStats();
				String currentSprintStarsStatsResult = toStatsTable(currentSprintStarsStats);
				sendTextMessage(chatId, String.format(CURRENT_SPRINT_STARS_MESSAGE, currentSprintStarsStatsResult));

				List<StarUserProjection> stats = starService.getAllStarsStats();
				String statsResult = toStatsTable(stats);
				sendTextMessage(chatId, String.format(ALL_STARS_MESSAGE, statsResult));
				return;
			case TOXIC_BUTTON_TITLE:
				sendTextMessage(chatId, TOXIC_MESSAGE);
				return;
			case HELP_BUTTON_TITLE:
				sendTextMessage(chatId, HELP_MESSAGE);
				return;
			case WHO_WE_ARE_MESSAGE:
				sendTextMessage(chatId, WE_ARE_BOT_MESSAGE);
				return;
		}

	}

	private boolean handleUserRegistration(Update update, Long chatId, String messageText) {
		if (registrationService.isUserRegistered(chatId)) {
			if (isRegOperation(update, messageText)) {
				sendTextMessage(chatId, ONCE_REG_MESSAGE);
			}
			return true;
		}
		if (isRegOperation(update, messageText)) {
			String username = messageText.replace(START_REGISTRATION_OPERATION, "").trim();
			if (username.length() > MAX_USERNAME_LENGTH) {
				sendTextMessage(chatId, NAME_IS_TOO_LONG_MESSAGE);
				return false;
			}
			if (isEmpty(username) || StringUtils.trimAllWhitespace(username).isEmpty() || username.length() < MIN_USERNAME_LENGTH) {
				sendTextMessage(chatId, NAME_IS_TOO_SHORT_MESSAGE);
				return false;
			}
			registrationService.registerUser(chatId, username);
			sendTextMessage(chatId, String.format(GREETING_MESSAGE, username));
			sendMainKeyboard(chatId);
			return false;
		} else {
			sendTextMessage(chatId, REGISTRATION_MESSAGE);
			return false;
		}
	}

	private static boolean isRegOperation(Update update, String messageText) {
		return TelegramUtils.hasTextMessage(update) && messageText.startsWith(START_REGISTRATION_OPERATION);
	}

	private static String toStatsTable(List<StarUserProjection> currentSprintStarsStats) {
		return currentSprintStarsStats.stream()
				.map(stat -> stat.getName() + ": " + stat.getStarsAmount())
				.collect(Collectors.joining("\n"));
	}

	private static String extractMessageText(Update update) {
		return Optional.ofNullable(update)
				.map(Update::getMessage)
				.map(Message::getText)
				.orElse(EMPTY_MESSAGE);
	}

	private void sendMainKeyboard(Long chatId) {
		tryExecute(keyboardMaker.getKeyboardMessage(chatId));
	}
}
