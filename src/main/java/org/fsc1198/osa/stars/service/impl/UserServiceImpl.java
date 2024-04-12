package org.fsc1198.osa.stars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.osa.stars.entity.User;
import org.fsc1198.osa.stars.logging.LogEntryAndExit;
import org.fsc1198.osa.stars.repository.UserRepository;
import org.fsc1198.osa.stars.service.UserService;
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
}
