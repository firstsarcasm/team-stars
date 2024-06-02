package org.fsc1198.team.stars.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtils {

	public static LocalDate getToday() {
		var clock = Clock.system(ZoneId.of("Europe/Moscow"));
		return LocalDate.now(clock);
	}

	public static LocalDateTime getTodayDateTime() {
		var clock = Clock.system(ZoneId.of("Europe/Moscow"));
		return LocalDateTime.now(clock);
	}

	public static LocalDate getTomorrow() {
		return getToday().plusDays(1);
	}

	public static LocalDate getDayAfterTomorrow() {
		return getTomorrow().plusDays(1);
	}

}
