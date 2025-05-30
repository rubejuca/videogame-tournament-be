package com.vgt.tournaments.controller;


import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.dto.CreatePlayerDto;
import com.vgt.tournaments.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    @PostMapping("/api/players")
    @ResponseStatus(HttpStatus.CREATED)
    public Player create(@RequestBody CreatePlayerDto dto) {
        return playerService.create(dto);
    }
}
