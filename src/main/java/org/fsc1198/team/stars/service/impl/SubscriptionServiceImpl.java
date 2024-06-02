package org.fsc1198.team.stars.service.impl;

import lombok.RequiredArgsConstructor;
import org.fsc1198.team.stars.bot.TeamStarsBot;
import org.fsc1198.team.stars.logging.LogEntryAndExit;
import org.fsc1198.team.stars.repository.SubscriberRepository;
import org.fsc1198.team.stars.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

	private final SubscriberRepository subscribersRepository;

	@Override
	@LogEntryAndExit
	public boolean subscribe(Long telegramChatId, Integer staffId, TeamStarsBot bot) {
		return subscribersRepository.subscribe(telegramChatId, staffId);
	}

	@Override
	@LogEntryAndExit
	public void unsubscribe(Long telegramChatId) {
		subscribersRepository.deleteById(telegramChatId);
	}
}
