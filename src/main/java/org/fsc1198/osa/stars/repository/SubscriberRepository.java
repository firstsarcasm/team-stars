package org.fsc1198.osa.stars.repository;

import org.fsc1198.osa.stars.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
	/**
	 * @return true if subscriptions was performed successfully
	 */
	default boolean subscribe(Long telegramChatId, Integer staffId) {
		if (hasAlreadySubscribed(telegramChatId)) {
			return false;
		}
		saveSubscriber(telegramChatId);
		return true;
	}

	private boolean hasAlreadySubscribed(Long telegramChatId) {
		return this.findById(telegramChatId).isPresent();
	}

	default void saveSubscriber(Long telegramChatId) {
		Subscriber subscriber = new Subscriber(telegramChatId);
		this.save(subscriber);
	}

	@Override
	List<Subscriber> findAll();
}
