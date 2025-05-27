package com.vgt.tournaments.dto;

import com.vgt.tournaments.domain.enums.TournamentStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder

public record UpdateTournamentDto (

  String name,
  String gameTitle,
  int maxPlayers,
  LocalDate startDate,
  TournamentStatus status
){


}
