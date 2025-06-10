package com.vgt.tournaments.services;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreateTournamentRequestDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TournamentService {

  private final TournamentRepository tournamentRepository;
  private final PlayerRepository playerRepository;

  public TournamentService(TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
    this.tournamentRepository = tournamentRepository;
    this.playerRepository = playerRepository;
  }

  public Tournament create(CreateTournamentRequestDto dto) {

    log.info("Creating: {}", dto);

    validateDuplicateTournamentName(dto.name());

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

  public List<Tournament> findAll() {
      return tournamentRepository.findAll();
  }

  public Tournament findById(Long id) {
    return tournamentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("The tournament does not exist"));
  }


  public Tournament update(Long id, UpdateTournamentDto dto) {

    Tournament currentTournament = tournamentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("The tournament does not exist"));

    validateTournamentUpdate(currentTournament.getStatus());

    Tournament tournament = currentTournament.toBuilder()
        .name(dto.name())
        .gameTitle(dto.gameTitle())
        .maxPlayers(dto.maxPlayers())
        .startDate(dto.startDate().toEpochDay())
        .status(dto.status())
        .build();

    return tournamentRepository.save(tournament);
  }

  public  List<Player> getTournamentPlayers(Long tournamentId) {

    tournamentRepository.findById(tournamentId)
        .orElseThrow(() -> new IllegalArgumentException("The tournament does not exist"));

    List<Player> players = playerRepository.findByTournamentId(tournamentId);

    return players;
  }


  private static void validateTournamentUpdate(TournamentStatus status) {

      if (status == TournamentStatus.STARTED) {
          throw new IllegalArgumentException("The Tournament has started");
      }
  }
  public void delete(Long id) {
    Tournament deletedTournament = tournamentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + id));

    validateTournamentStatusForDeletion(deletedTournament.getStatus());
    tournamentRepository.delete(deletedTournament);
  }

  public void validateDuplicateTournamentName(String name) {

    if (tournamentRepository.findByName(name) != null) {
      throw new IllegalStateException("The tournament name already exists");
    }
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

  private static void validateTournamentStatusForDeletion(TournamentStatus tournament) {
    if (tournament.isStarted()) {
      throw new IllegalArgumentException("The tournament is already started");
    }
  }

}
