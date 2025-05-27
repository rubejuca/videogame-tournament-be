package com.vgt.tournaments.controller;

import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.dto.CreateTournamentDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.services.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/api/tournaments/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Tournament update(@PathVariable("id") Long id, @RequestBody UpdateTournamentDto dto) {
    return tournamentService.update(id, dto);
  }

}
