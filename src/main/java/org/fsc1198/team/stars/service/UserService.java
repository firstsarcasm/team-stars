package org.fsc1198.team.stars.service;

import org.fsc1198.team.stars.logging.LogEntryAndExit;

import java.util.List;

public interface UserService {
	List<String> getAllUserNames();

	@LogEntryAndExit
	List<Long> getAllUserChatIds();
}
