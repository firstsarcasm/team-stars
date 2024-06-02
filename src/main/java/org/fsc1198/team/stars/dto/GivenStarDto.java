package org.fsc1198.team.stars.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fsc1198.team.stars.entity.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GivenStarDto {
	private User donorUser;
	private User targetUser;
}
