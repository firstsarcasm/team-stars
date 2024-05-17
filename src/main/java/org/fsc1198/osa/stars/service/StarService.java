package org.fsc1198.osa.stars.service;

import org.fsc1198.osa.stars.dto.GivenStarDto;
import org.fsc1198.osa.stars.dto.StarUserProjection;
import org.springframework.data.util.Pair;

import java.util.List;

public interface StarService {
	GivenStarDto giveStar(String targetUserName, Long donarChatId);

	int getAllStarsAmount(Long chatId);

	StarUserProjection getSuperstarForCurrentSprintUsernameAndStars();

	List<StarUserProjection> getAllStarsStats();

	List<StarUserProjection> getCurrentSprintStarsStats();

	Pair<Long, Integer> getStarsAmountForCurrentSprint(Long chatId);
}
