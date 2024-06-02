package org.fsc1198.team.stars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.team.stars.entity.User;
import org.fsc1198.team.stars.logging.LogEntryAndExit;
import org.fsc1198.team.stars.repository.UserRepository;
import org.fsc1198.team.stars.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	@LogEntryAndExit
	public List<String> getAllUserNames() {
		return userRepository.findAll()
				.stream()
				.map(User::getName)
				.collect(Collectors.toList());
	}

	@Override
	@LogEntryAndExit
	public List<Long> getAllUserChatIds() {
		return userRepository.findAll()
				.stream()
				.map(User::getChatId)
				.collect(Collectors.toList());
	}
}
