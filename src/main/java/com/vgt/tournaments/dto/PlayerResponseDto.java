package com.vgt.tournaments.dto;

import com.vgt.tournaments.domain.Player;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PlayerResponseDto(

    Long id,
    String name,
    String nickName,
    Long tournamentId,
    LocalDate registrationDate

) {

  public static PlayerResponseDto from(Player player) {
    return PlayerResponseDto.builder()
        .id(player.getId())
        .name(player.getName())
        .nickName(player.getNickName())
        .tournamentId(player.getTournamentId())
        .registrationDate(LocalDate.ofEpochDay(player.getRegistrationDate()))
        .build();
  }

}