package org.fsc1198.osa.stars.service;

import org.springframework.data.util.Pair;

public interface StarService {
	void giveStar(String targetUserName, Long donarChatId);

	int getAllStarsAmount(Long chatId);

	Pair<String, Integer> getSuperstarUsernameAndStars();

	Pair<Long, Integer> getStarsAmountForCurrentSprint(Long chatId);
}
