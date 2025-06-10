package com.vgt.tournaments.services;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreatePlayerDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    // TODO Danna - This must be final
    private final TournamentRepository tournamentRepository;

    public PlayerService(PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }



    public void delete(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The player does not exist"));

        playerRepository.delete(player);

    }

}

