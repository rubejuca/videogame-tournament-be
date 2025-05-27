package com.vgt.tournaments.services;

import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreateTournamentDto;
import com.vgt.tournaments.repositories.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class TournamentService {

  private final TournamentRepository tournamentRepository;

  public TournamentService(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
  }

  public Tournament create(CreateTournamentDto dto) {

    log.info("Creating: {}", dto);

    validateStartDate(dto.startDate());

    validateMaxPlayers(dto.maxPlayers());

    Tournament tournament = Tournament.builder()
        .gameTitle(dto.gameTitle())
        .name(dto.name())
        .maxPlayers(dto.maxPlayers())
        .startDate(dto.startDate().toEpochDay())
        .status(TournamentStatus.UPCOMING)
        .build();

    // TODO Investigate if the tournament can repeat the name

    return tournamentRepository.save(tournament);
  }

  private static void validateMaxPlayers(int maxPlayers) {
    if (maxPlayers <= 1) {
      throw new IllegalArgumentException("The minimum number of players is 2");
    }
  }

  private static void validateStartDate(LocalDate startDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("The start date is required");
    }
    if (LocalDate.now().isAfter(startDate)) {
      throw new IllegalArgumentException("The start date can not be in the past");
    }
  }
}
