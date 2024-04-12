package org.fsc1198.osa.stars.repository;

import org.fsc1198.osa.stars.entity.Sprint;
import org.fsc1198.osa.stars.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

	Sprint findFirstByOrderByIdDesc();

}
