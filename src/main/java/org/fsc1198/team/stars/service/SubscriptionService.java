package org.fsc1198.team.stars.service;


import org.fsc1198.team.stars.bot.TeamStarsBot;

public interface SubscriptionService {
	boolean subscribe(Long telegramChatId, Integer staffId, TeamStarsBot bot);

	void unsubscribe(Long telegramChatId);
}
