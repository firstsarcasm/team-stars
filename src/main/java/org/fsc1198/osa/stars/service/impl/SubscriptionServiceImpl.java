package org.fsc1198.osa.stars.service.impl;

import lombok.RequiredArgsConstructor;
import org.fsc1198.osa.stars.bot.OsaStarsBot;
import org.fsc1198.osa.stars.logging.LogEntryAndExit;
import org.fsc1198.osa.stars.repository.SubscriberRepository;
import org.fsc1198.osa.stars.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

	private final SubscriberRepository subscribersRepository;

	@Override
	@LogEntryAndExit
	public boolean subscribe(Long telegramChatId, Integer staffId, OsaStarsBot bot) {
		return subscribersRepository.subscribe(telegramChatId, staffId);
	}

	@Override
	@LogEntryAndExit
	public void unsubscribe(Long telegramChatId) {
		subscribersRepository.deleteById(telegramChatId);
	}
}
