package com.vgt.tournaments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreatePlayerRequestDto;
import com.vgt.tournaments.dto.UpdatePlayerDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import com.vgt.tournaments.services.PlayerService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PlayerServiceTest {

    @Test
    void testCreateSuccess() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Player player = Player.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now().toEpochDay())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.UPCOMING)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(0);
        when(playerRepository.existsByNickNameAndTournamentId("Sakura", 1L)).thenReturn(false);
        when(playerRepository.save(any())).thenReturn(player);

        Player createdPlayer = playerService.create(dto);

        assertNotNull(createdPlayer);
        assertEquals(player.getName(), createdPlayer.getName());
        assertEquals(player.getNickName(), createdPlayer.getNickName());
        assertEquals(player.getTournamentId(), createdPlayer.getTournamentId());
    }
    
    @Test
    void testCreateFailsValidateMaxPlayers() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Tournament tournament = Tournament.builder()
            .id(dto.tournamentId())
            .maxPlayers(5)
            .status(TournamentStatus.UPCOMING)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(dto.tournamentId())).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(dto.tournamentId())).thenReturn(5);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("The maximum number of players is 5", exception.getMessage());
    }
    @Test
    void testCreateFailsValidateTournamentStatusStarted() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.STARTED)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(3);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("The Tournament has started or finished", exception.getMessage());
    }
    @Test
    void testCreateFailsValidateTournamentStatusFinished() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.FINISHED)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(3);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("The Tournament has started or finished", exception.getMessage());
    }
    @Test
    void testCreateFailsWhenStartDateIsNull() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.UPCOMING)
            .startDate(null)
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(3);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("The start date is required", exception.getMessage());
    }



    @Test
    void testCreateFailsWhenStartDateIsInThePast() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.UPCOMING)
            .startDate(LocalDate.now().minusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(3);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("The start date can not be in the past", exception.getMessage());
    }
    @Test
    void testCreateFailsValidateRegistrationDate() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now().plusDays(2))
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.UPCOMING)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(3);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("Registration date cannot be after the tournament start date", exception.getMessage());
    }
    @Test
    void testCreateFailsWhenTournamentDoesNotExist() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("Tournament not found", exception.getMessage());
    }
    @Test
    void testCreateFailsValidateUniqueNickName() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .maxPlayers(10)
            .status(TournamentStatus.UPCOMING)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.countPlayersByTournamentId(1L)).thenReturn(3);
        when(playerRepository.existsByNickNameAndTournamentId("Sakura", 1L)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.create(dto)
        );

        assertEquals("A player with that nickname is already registered in this tournament", exception.getMessage());
    }



    @Test
    void testUpdateSuccess() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        UpdatePlayerDto dto = UpdatePlayerDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .registrationDate(LocalDate.now())
            .build();

        Player existingPlayer = Player.builder()
            .id(1L)
            .name("Old Name")
            .nickName("OldNick")
            .tournamentId(1L)
            .registrationDate(LocalDate.now().minusDays(1).toEpochDay())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        Player updatedPlayer = existingPlayer.toBuilder()
            .name(dto.name())
            .nickName(dto.nickName())
            .registrationDate(dto.registrationDate().toEpochDay())
            .build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.existsByNickNameAndTournamentId("Sakura", 1L)).thenReturn(false);
        when(playerRepository.save(any())).thenReturn(updatedPlayer);

        Player result = playerService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Danna", result.getName());
        assertEquals("Sakura", result.getNickName());
        assertEquals(dto.registrationDate().toEpochDay(), result.getRegistrationDate());
    }

    @Test
    void testUpdateFailsValidateUniqueNickName() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        UpdatePlayerDto dto = UpdatePlayerDto.builder()
            .name("Danna")
            .nickName("Sakura") // nuevo nick duplicado
            .registrationDate(LocalDate.now())
            .build();

        Player existingPlayer = Player.builder()
            .id(1L)
            .name("Old Name")
            .nickName("OldNick") // era distinto
            .tournamentId(1L)
            .registrationDate(LocalDate.now().minusDays(1).toEpochDay())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .startDate(LocalDate.now().plusDays(1).toEpochDay())
            .build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(playerRepository.existsByNickNameAndTournamentId("Sakura", 1L)).thenReturn(true); // nickname duplicado

        assertThrows(IllegalArgumentException.class, () -> {
            playerService.update(1L, dto);
        });
    }
    @Test
    void testUpdateFailsRegistrationDate() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        UpdatePlayerDto dto = UpdatePlayerDto.builder()
            .name("Danna")
            .nickName("Sakura")
            .registrationDate(LocalDate.now().plusDays(5))
            .build();

        Player existingPlayer = Player.builder()
            .id(1L)
            .name("Old Name")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now().toEpochDay())
            .build();

        Tournament tournament = Tournament.builder()
            .id(1L)
            .startDate(LocalDate.now().plusDays(2).toEpochDay())
            .build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        assertThrows(IllegalArgumentException.class, () -> {
            playerService.update(1L, dto);
        });
    }





    @Test
    void testReadAll() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, null);

        Player player1 = Player.builder()
                .name("Camilo")
                .nickName("Sakura")
                .tournamentId(1L)
                .registrationDate(LocalDate.now().toEpochDay())
                .build();
        Player player2 = Player.builder()
                .name("Camilo")
                .nickName("Sakura")
                .tournamentId(1L)
                .registrationDate(LocalDate.now().toEpochDay())
                .build();
        Player player3 = Player.builder()
                .name("Camilo")
                .nickName("Sakura")
                .tournamentId(1L)
                .registrationDate(LocalDate.now().toEpochDay())
                .build();

        when(playerRepository.findAll()).thenReturn(List.of(player1, player2, player3));

        List<Player> players = playerService.readAll();

        assertNotNull(players);
        assertEquals(3, players.size());
    }

    @Test
    void testReadById() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, null);

        Player player = Player.builder()
                .name("Camilo")
                .nickName("Sakura")
                .tournamentId(1L)
                .registrationDate(LocalDate.now().toEpochDay())
                .build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Player result = playerService.readById(1L);

        assertNotNull(result);
        assertEquals("Camilo", result.getName());
        assertEquals("Sakura", result.getNickName());
        assertEquals(1L, result.getTournamentId());
        assertEquals(LocalDate.now().toEpochDay(), result.getRegistrationDate());
    }

    @Test
    void testDelete() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        Player player = Player.builder()
            .name("Danna")
            .nickName("Sakura")
            .tournamentId(1L)
            .registrationDate(LocalDate.now().toEpochDay())
            .build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.delete(1L);

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).delete(any());
    }
}
