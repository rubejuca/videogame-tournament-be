package com.vgt.tournaments.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdatePlayerDto(
    String name,
    String nickName,
    Long tournamentId,
    LocalDate registrationDate
) {}