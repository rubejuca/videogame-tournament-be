package com.vgt.tournaments.dto;

import java.time.LocalDate;

public record CreateTournamentDto(
    String name,
    String gameTitle,
    int maxPlayers,
    LocalDate startDate
) {}
