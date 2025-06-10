package com.vgt.tournaments.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder

public record CreateTournamentRequestDto(
    String name,
    String gameTitle,
    int maxPlayers,
    LocalDate startDate
) {}
