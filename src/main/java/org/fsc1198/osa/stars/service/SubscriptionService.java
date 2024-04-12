package org.fsc1198.osa.stars.service;


import org.fsc1198.osa.stars.bot.OsaStarsBot;

public interface SubscriptionService {
	boolean subscribe(Long telegramChatId, Integer staffId, OsaStarsBot bot);

	void unsubscribe(Long telegramChatId);
}
