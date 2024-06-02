package org.fsc1198.team.stars.service;

public interface RegistrationService {
	boolean isUserRegistered(Long chatId);

	void registerUser(Long chatId, String messageText);
}
