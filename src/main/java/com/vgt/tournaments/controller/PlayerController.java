package com.vgt.tournaments.controller;


import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.dto.CreatePlayerRequestDto;
import com.vgt.tournaments.dto.PlayerResponseDto;
import com.vgt.tournaments.dto.UpdatePlayerDto;
import com.vgt.tournaments.services.PlayerService;
import com.vgt.tournaments.services.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    @PostMapping("/api/players")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponseDto create(@RequestBody CreatePlayerRequestDto dto) {
        Player player = playerService.create(dto);
        return PlayerResponseDto.from(player);
    }

    @PutMapping("/api/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Player update(@PathVariable("id") Long id, @RequestBody UpdatePlayerDto dto) {
        return playerService.update(id, dto);
    }

    @GetMapping("/api/players/{id}")
    public PlayerResponseDto readById(@PathVariable Long id) {
        Player player = playerService.readById(id);
        return PlayerResponseDto.from(player);
    }

    @GetMapping("/api/players")
    public List<PlayerResponseDto> readAll() {
        List<Player> players = playerService.readAll();

        return players.stream()
            .map(PlayerResponseDto::from)
            .collect(Collectors.toList());
    }

    @DeleteMapping("/api/players/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerService.delete(id);
    }

}
