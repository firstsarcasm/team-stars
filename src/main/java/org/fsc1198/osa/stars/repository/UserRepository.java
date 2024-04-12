package org.fsc1198.osa.stars.repository;

import org.fsc1198.osa.stars.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

	boolean existsByChatId(Long chatId);

	User findByName(String name);
	User findByChatId(Long chatId);

	User findTopByOrderByStarsDesc();

	@Override
	List<User> findAll();
}
