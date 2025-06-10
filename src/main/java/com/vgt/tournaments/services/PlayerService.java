package com.vgt.tournaments.services;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreatePlayerRequestDto;
import com.vgt.tournaments.dto.UpdatePlayerDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TournamentRepository tournamentRepository;

    public PlayerService(PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    public Player create(CreatePlayerRequestDto dto) {

        log.info("Creating: {}", dto);

        int currentPlayers = playerRepository.countPlayersByTournamentId(dto.tournamentId());
        Tournament tournament = tournamentRepository.findById(dto.tournamentId())
            .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        int maxPlayers = tournament.getMaxPlayers();
        validateMaxPlayers(currentPlayers, maxPlayers);

        TournamentStatus status = tournament.getStatus();
        validateTournamentStatus(status);

        Long initialStartDate = tournament.getStartDate();
        if (initialStartDate == null) {
            throw new IllegalArgumentException("The start date is required");
        }

        LocalDate startDate = LocalDate.ofEpochDay(initialStartDate);
        validateStartDate(startDate);
        validateRegistrationDate(dto.registrationDate(), startDate);


        validateUniqueNickName(dto.nickName(), dto.tournamentId());


        Player player = Player.builder()
            .name(dto.name())
            .nickName(dto.nickName())
            .tournamentId(dto.tournamentId())
            .registrationDate(dto.registrationDate().toEpochDay())
            .build();
        return playerRepository.save(player);

    }
    public Player update(Long id, UpdatePlayerDto dto) {

        Player currentPlayer = playerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("The player does not exist"));

        if (!dto.nickName().equals(currentPlayer.getNickName())) {
            validateUniqueNickName(dto.nickName(), currentPlayer.getTournamentId());
        }
        LocalDate registrationDate = dto.registrationDate();
        LocalDate startDate = getTournamentStartDate(currentPlayer.getTournamentId());
        validateRegistrationDate(registrationDate, startDate);


        Player player = currentPlayer.toBuilder()
            .name(dto.name())
            .nickName(dto.nickName())
            .registrationDate(dto.registrationDate().toEpochDay())
            .build();
        return playerRepository.save(player);
    }

    public List<Player> readAll() {
        return playerRepository.findAll();
    }


    public Player readById(Long id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("The player does not exist"));
    }


    public void delete(Long id) {
        Player player = playerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("The player does not exist"));

        playerRepository.delete(player);

    }



    private static void validateMaxPlayers(int currentPlayers, int maxPlayers) {
        if (currentPlayers == maxPlayers) {
            throw new IllegalArgumentException("The maximum number of players is " + maxPlayers);
        }
    }

    private static void validateTournamentStatus(TournamentStatus status) {
        if (status == TournamentStatus.STARTED || status == TournamentStatus.FINISHED) {
            throw new IllegalArgumentException("The Tournament has started or finished");
        }

    }

    private static void validateStartDate(LocalDate startDate) {

        if (LocalDate.now().isAfter(startDate)) {
            throw new IllegalArgumentException("The start date can not be in the past");
        }
    }

    private static void validateRegistrationDate(LocalDate registrationDate, LocalDate startDate) {
        if (registrationDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Registration date cannot be after the tournament start date");
        }
    }
    private void validateUniqueNickName(String nickName, Long tournamentId) {
        if (playerRepository.existsByNickNameAndTournamentId(nickName, tournamentId)) {
            throw new IllegalArgumentException("A player with that nickname is already registered in this tournament");
        }
    }

    private LocalDate getTournamentStartDate(Long tournamentId) {
        return LocalDate.ofEpochDay(
            tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"))
                .getStartDate()
        );
    }




}