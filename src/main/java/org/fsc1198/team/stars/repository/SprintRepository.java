package org.fsc1198.team.stars.repository;

import org.fsc1198.team.stars.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

	Sprint findFirstByOrderByIdDesc();

}
