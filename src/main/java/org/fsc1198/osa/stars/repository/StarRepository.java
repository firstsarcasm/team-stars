package org.fsc1198.osa.stars.repository;

import org.fsc1198.osa.stars.entity.Star;
import org.fsc1198.osa.stars.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {

}
