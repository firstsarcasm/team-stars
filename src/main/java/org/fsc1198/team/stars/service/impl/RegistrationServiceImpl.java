package org.fsc1198.team.stars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.team.stars.entity.User;
import org.fsc1198.team.stars.logging.LogEntryAndExit;
import org.fsc1198.team.stars.repository.UserRepository;
import org.fsc1198.team.stars.service.RegistrationService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

	private final UserRepository userRepository;

	@Override
	@LogEntryAndExit
	public boolean isUserRegistered(Long chatId) {
		return userRepository.existsByChatId(chatId);
	}

	@Override
	@LogEntryAndExit
	public void registerUser(Long chatId, String username) {
		User user = new User();
		user.setChatId(chatId);
		user.setName(username);
		userRepository.save(user);
	}
}
