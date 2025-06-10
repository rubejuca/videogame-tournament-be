package com.vgt.tournaments.dto;

import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record TournamentResponseDto (
    Long id,
    String name,
    String gameTitle,
    int maxPlayers,
    LocalDate startDate,
    TournamentStatus status
) {

  public static TournamentResponseDto from(Tournament tournament) {
    return TournamentResponseDto.builder()
        .id(tournament.getId())
        .name(tournament.getName())
        .gameTitle(tournament.getGameTitle())
        .maxPlayers(tournament.getMaxPlayers())
        .startDate(LocalDate.ofEpochDay(tournament.getStartDate()))
        .status(tournament.getStatus())
        .build();
  }


}
