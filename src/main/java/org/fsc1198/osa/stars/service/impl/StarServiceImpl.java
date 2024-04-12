package org.fsc1198.osa.stars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.osa.stars.entity.Sprint;
import org.fsc1198.osa.stars.entity.Star;
import org.fsc1198.osa.stars.entity.User;
import org.fsc1198.osa.stars.logging.LogEntryAndExit;
import org.fsc1198.osa.stars.repository.SprintRepository;
import org.fsc1198.osa.stars.repository.StarRepository;
import org.fsc1198.osa.stars.repository.UserRepository;
import org.fsc1198.osa.stars.service.StarService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class StarServiceImpl implements StarService {

	//todo use separate services ?
	private final UserRepository userRepository;
	private final StarRepository starRepository;
	private final SprintRepository sprintRepository;

	@LogEntryAndExit
	@Override
	//todo fix different sources for users?
	public void giveStar(String targetUserName, Long donorChatId) {
		Sprint currentSprint = getOrStartNewSprint();

		User targetUser = userRepository.findByName(targetUserName);
		User donorUser = userRepository.findByChatId(donorChatId);
		if(targetUser.getId().equals(donorUser.getId())) {
			throw new RuntimeException("user tried to give a star to himself");
		}
		addStarToTargetUser(targetUser, donorUser, currentSprint);
	}

	private Sprint getOrStartNewSprint() {
		Sprint currentSprint = sprintRepository.findFirstByOrderByIdDesc();
		if(isNull(currentSprint)) {
			throw new RuntimeException("Sprint not found");
		}

		LocalDate currentSprintEndDate = currentSprint.getEndDate();
		if (LocalDate.now().isBefore(currentSprintEndDate)) {
			return currentSprint;
		}

		Sprint newSprint = sprintRepository.save(Sprint.builder()
				.sprintNumber(currentSprint.getSprintNumber() + 1)
				.startDate(currentSprintEndDate)
				.endDate(currentSprintEndDate.plusDays(14))
				.build());
		log.info("new sprint has started");
		return newSprint;
	}

	private void addStarToTargetUser(User targetUser, User donorUser, Sprint currentSprint) {
		starRepository.save(Star.builder()
				.userId(targetUser)
				.donorUserId(donorUser.getId())
				.donationDate(LocalDateTime.now())
				.sprintId(currentSprint.getId())
				.build());
	}

}
