package com.vgt.tournaments.controller;

import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.dto.CreateTournamentDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.services.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TournamentController {

  private final TournamentService tournamentService;

  public TournamentController(TournamentService tournamentService) {
    this.tournamentService = tournamentService;
  }

  @PostMapping("/api/tournaments")
  @ResponseStatus(HttpStatus.CREATED)
  public Tournament create(@RequestBody CreateTournamentDto dto) {
    return tournamentService.create(dto);
  }

  @PutMapping("/api/tournaments/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Tournament update(@PathVariable("id") Long id, @RequestBody UpdateTournamentDto dto) {
    return tournamentService.update(id, dto);
  }

  @GetMapping("/api/tournaments")
  public List<Tournament> findAll() {
    return tournamentService.findAll();
  }

  @GetMapping("/api/tournaments/{id}")
  public Tournament findById(@PathVariable Long id) {
    return tournamentService.findById(id);
  }

  @DeleteMapping("/api/tournaments/{id}")
  ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
    try {
      tournamentService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
