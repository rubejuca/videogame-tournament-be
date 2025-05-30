package com.vgt.tournaments.dto;

import com.vgt.tournaments.domain.enums.TournamentStatus;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record CreatePlayerDto (

    String name,
    String nickName,
    Long tournamentId,
    LocalDate registrationDate

) {

}