package org.fsc1198.osa.stars.service;

public interface RegistrationService {
	boolean isUserRegistered(Long chatId);

	void registerUser(Long chatId, String messageText);
}
