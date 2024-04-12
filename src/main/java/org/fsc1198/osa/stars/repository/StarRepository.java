package org.fsc1198.osa.stars.repository;

import org.fsc1198.osa.stars.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star, Long> {


	List<Star> findAllByUserId_ChatId(Long chatId);
	List<Star> findAllBySprintIdAndUserId_ChatId(Long sprintId, Long chatId);
}
