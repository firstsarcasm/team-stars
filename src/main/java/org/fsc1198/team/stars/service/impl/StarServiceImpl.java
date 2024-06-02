package org.fsc1198.team.stars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsc1198.team.stars.dto.GivenStarDto;
import org.fsc1198.team.stars.dto.StarUserProjection;
import org.fsc1198.team.stars.entity.Sprint;
import org.fsc1198.team.stars.entity.Star;
import org.fsc1198.team.stars.entity.User;
import org.fsc1198.team.stars.logging.LogEntryAndExit;
import org.fsc1198.team.stars.repository.SprintRepository;
import org.fsc1198.team.stars.repository.StarRepository;
import org.fsc1198.team.stars.repository.UserRepository;
import org.fsc1198.team.stars.service.StarService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
	public GivenStarDto giveStar(String targetUserName, Long donorChatId) {
		Sprint currentSprint = getOrStartNewSprint();

		User targetUser = userRepository.findByName(targetUserName);
		User donorUser = userRepository.findByChatId(donorChatId);
		if(targetUser.getId().equals(donorUser.getId())) {
			throw new RuntimeException("user tried to give a star to himself");
		}
		addStarToTargetUser(targetUser, donorUser, currentSprint);
		return GivenStarDto.builder()
				.donorUser(donorUser)
				.targetUser(targetUser)
				.build();
	}

	@Override
	public int getAllStarsAmount(Long chatId) {
		return starRepository.findAllByUserId_ChatId(chatId).size();
	}

	@Override
	public StarUserProjection getSuperstarForCurrentSprintUsernameAndStars() {
		return userRepository.findSuperstarForCurrentSprint();
	}

	@Override
	public List<StarUserProjection> getAllStarsStats() {
		return userRepository.findAllStarsStatistics();
	}

	@Override
	public List<StarUserProjection> getCurrentSprintStarsStats() {
		return userRepository.findCurrentSprintStarsStatistics();
	}

	@Override
	public Pair<Long, Integer> getStarsAmountForCurrentSprint(Long chatId) {
		Sprint currentSprint = getOrStartNewSprint();
		List<Star> starsForCurrentSprint = starRepository.findAllBySprintIdAndUserId_ChatId(currentSprint.getId(), chatId);
		return Pair.of(currentSprint.getSprintNumber(), starsForCurrentSprint.size());
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
