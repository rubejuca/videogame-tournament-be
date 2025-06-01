package com.vgt.tournaments.dto;

import com.vgt.tournaments.domain.enums.TournamentStatus;

public record DeleteTournamentDto(
    Long id,
    String name,
    String gameTitle,
    int maxPlayers,
    Long startDate,
    TournamentStatus status
) {
}
