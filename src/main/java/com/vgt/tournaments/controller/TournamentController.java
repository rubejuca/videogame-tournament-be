package com.vgt.tournaments.controller;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.dto.CreateTournamentRequestDto;
import com.vgt.tournaments.dto.PlayerResponseDto;
import com.vgt.tournaments.dto.TournamentResponseDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.services.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TournamentController {

  private final TournamentService tournamentService;

  public TournamentController(TournamentService tournamentService) {
    this.tournamentService = tournamentService;
  }

  @PostMapping("/api/tournaments")
  @ResponseStatus(HttpStatus.CREATED)
  public TournamentResponseDto create(@RequestBody CreateTournamentRequestDto dto) {
    Tournament tournament = tournamentService.create(dto);
    return TournamentResponseDto.from(tournament);
  }

  @PutMapping("/api/tournaments/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Tournament update(@PathVariable("id") Long id, @RequestBody UpdateTournamentDto dto) {
    return tournamentService.update(id, dto);
  }

  @GetMapping("/api/tournaments")
  public List<TournamentResponseDto> readAll() {
    List<Tournament> tournaments = tournamentService.findAll();

    return tournaments.stream()
        .map(TournamentResponseDto::from)
        .collect(Collectors.toList());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/api/tournaments/{id}")
  public TournamentResponseDto readById(@PathVariable Long id) {
    Tournament tournament = tournamentService.findById(id);
    return TournamentResponseDto.from(tournament);
  }

  @GetMapping("/api/tournaments/{tournamentId}/players")
  public List<PlayerResponseDto> readAllPlayersByTournamentId(@PathVariable Long tournamentId) {

    List<Player> players = tournamentService.getTournamentPlayers(tournamentId);

    return players.stream()
        .map(PlayerResponseDto::from)
        .collect(Collectors.toList());
  }

  @DeleteMapping("/api/tournaments/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    tournamentService.delete(id);
  }
}
