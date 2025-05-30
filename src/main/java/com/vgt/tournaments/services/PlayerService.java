package com.vgt.tournaments.services;

import com.vgt.tournaments.domain.Player;
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
    private TournamentRepository tournamentRepository;

    public PlayerService(PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    public Player create(CreatePlayerDto dto) {

        log.info("Creating: {}", dto);

        int currentPlayers = playerRepository.findPlayersByTournamentId(dto.tournamentId()).size();
        int maxPlayers = tournamentRepository.findById(dto.tournamentId()).get().getMaxPlayers();
        validateMaxPlayers(currentPlayers, maxPlayers);

        TournamentStatus status = tournamentRepository.findById(dto.tournamentId()).get().getStatus();
        validateTournamentStatus(status);
        Long startDate = tournamentRepository.findById(dto.tournamentId()).get().getStartDate();
        validateStartDate(LocalDate.ofEpochDay(startDate));


        Player player = Player.builder()
                .name(dto.name())
                .nickName(dto.nickName())
                .tournamentId(dto.tournamentId())
                .registrationDate(dto.registrationDate().toEpochDay())
                .build();
        return playerRepository.save(player);

    }

    private static void validateMaxPlayers(int currentPlayers, int maxPlayers) {
        if (currentPlayers > maxPlayers) {
            throw new IllegalArgumentException("The maximum number of players is " + maxPlayers);
        }
    }

    private static void validateTournamentStatus(TournamentStatus status) {
        if (status == TournamentStatus.STARTED || status == TournamentStatus.FINISHED) {
            throw new IllegalArgumentException("The Tournament has started or finished");
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

