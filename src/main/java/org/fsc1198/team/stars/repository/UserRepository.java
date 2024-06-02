package org.fsc1198.team.stars.repository;

import org.fsc1198.team.stars.dto.StarUserProjection;
import org.fsc1198.team.stars.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

	boolean existsByChatId(Long chatId);

	User findByName(String name);

	User findByChatId(Long chatId);

	@Query(
			nativeQuery = true,
			value = "SELECT usr.name as name, count(star.id) as starsAmount\n" +
					"FROM users usr\n" +
					"         inner join star on star.user_id_id = usr.id\n" +
					"GROUP BY usr.name\n" +
					"ORDER BY count(star.id) DESC"
	)
	List<StarUserProjection> findAllStarsStatistics();

	@Query(
			nativeQuery = true,
			value = "SELECT usr.*,count(star.id) as starsAmount\n" +
					"FROM users usr\n" +
					"         inner join star on star.user_id_id = usr.id\n" +
					"WHERE star.sprint_id = (SELECT max(id) FROM sprint)\n" +
					"GROUP BY usr.name\n" +
					"ORDER BY count(star.id) DESC"
	)
	List<StarUserProjection> findCurrentSprintStarsStatistics();

	@Query(
			nativeQuery = true,
			value = "\n" +
					"SELECT usr.name, count(star.id) as starsAmount\n" +
					"FROM users usr\n" +
					"         inner join star star on star.user_id_id = usr.id\n" +
					"WHERE star.sprint_id = (SELECT max(id) FROM sprint)\n" +
					"GROUP BY usr.name\n" +
					"ORDER BY count(star.id) DESC\n" +
					"LIMIT 1"
	)
	StarUserProjection findSuperstarForCurrentSprint();

	@Query(
			nativeQuery = true,
			value = "\n" +
					"SELECT usr.*\n" +
					"FROM users usr\n" +
					"         inner join star star on star.user_id_id = usr.id\n" +
					"GROUP BY usr.name\n" +
					"ORDER BY count(star.id) DESC\n" +
					"LIMIT 1"
	)
	User findSuperstarOfTheWholeTime();

	@Override
	List<User> findAll();
}
