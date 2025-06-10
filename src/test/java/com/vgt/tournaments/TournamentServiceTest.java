package com.vgt.tournaments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreateTournamentRequestDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import com.vgt.tournaments.services.TournamentService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TournamentServiceTest {


  @Test
  void testCreateSuccess() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);



    CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
          .name("juan")
          .gameTitle("first tournament")
          .maxPlayers(5)
          .startDate(LocalDate.now())
          .build();

      Tournament tournament = Tournament.builder()
          .name("juan")
          .gameTitle("first tournament")
          .maxPlayers(5)
          .startDate(LocalDate.now().toEpochDay())
          .status(TournamentStatus.UPCOMING)
          .build();

      when(tournamentRepository
          .save(any()))
          .thenReturn(tournament);

      Tournament createdTournament = tournamentService.create(dto);
      assertNotNull(createdTournament);
      assertEquals(createdTournament, tournament);

    }

    @Test
    void testCreateFailButMaxPlayers() throws Exception {


      PlayerRepository playerRepository = mock(PlayerRepository.class);
      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


      CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
          .name("juan")
          .gameTitle("first tournament")
          .maxPlayers(1)
          .startDate(LocalDate.now())
          .build();

      when(tournamentRepository
          .save(any()))
          .thenReturn(null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> {tournamentService.create(dto);
      });
      assertEquals("The minimum number of players is 2", exception.getMessage());

    }

    @Test
    void testCreateFailButNameDuplicate() throws Exception {


      PlayerRepository playerRepository = mock(PlayerRepository.class);
      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);

      CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
          .name("juan")
          .gameTitle("first tournament")
          .maxPlayers(5)
          .startDate(LocalDate.now())
          .build();

      when(tournamentRepository.findByName(dto.name()))
          .thenReturn(new Tournament());

      IllegalStateException exception = assertThrows(IllegalStateException.class,
          () -> {tournamentService.create(dto);
      });
      assertEquals("The tournament name already exists", exception.getMessage());


    }

    @Test
    void testCreateFailButStartDate() throws Exception {


      PlayerRepository playerRepository = mock(PlayerRepository.class);
      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


      CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
          .name("juan")
          .gameTitle("first tournament")
          .maxPlayers(5)
          .startDate(LocalDate.now().minusDays(1))
          .build();

      when(tournamentRepository
          .save(any()))
          .thenReturn(null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> {tournamentService.create(dto);
      });
      assertEquals("The start date can not be in the past", exception.getMessage());

    }

    @Test
    void testCreateFailButStartDateNull() throws Exception {


      PlayerRepository playerRepository = mock(PlayerRepository.class);
      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


      CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
          .name("juan")
          .gameTitle("first tournament")
          .maxPlayers(5)
          .startDate(null)
          .build();

      when(tournamentRepository
          .save(any()))
          .thenReturn(null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> {tournamentService.create(dto);
      });
      assertEquals("The start date is required", exception.getMessage());

    }



  @Test
  void testFindAllSuccess() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


    when(tournamentRepository
        .findAll())
        .thenReturn(List.of(Tournament.builder()
            .id(1L)
            .name("Camilo Rubio")
            .gameTitle("second tournament")
            .maxPlayers(5)
            .startDate(212L)
            .status(TournamentStatus.UPCOMING)
            .build()));

    List<Tournament> foundTournaments = tournamentService.findAll();
    assertNotNull(foundTournaments);
    assertEquals(foundTournaments.size(), 1);
  }

  @Test
  void testFindByIdSuccess() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


    Tournament tournament = Tournament.builder()
        .id(1L)
        .name("Camilo Rubio")
        .gameTitle("second tournament")
        .maxPlayers(5)
        .startDate(212L)
        .status(TournamentStatus.UPCOMING)
        .build();

    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.of(tournament));

    Tournament foundTournament = tournamentService.findById(1L);
    assertNotNull(foundTournament);
    assertEquals(foundTournament, tournament);
  }

  @Test
  void testFindByIdFail() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class,
        () -> tournamentService.findById(1L));
  }


  @Test
  void testDeleteSuccess() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


    Tournament tournament = Tournament.builder()
        .id(1L)
        .name("Aaron Peña")
        .gameTitle("second tournament")
        .maxPlayers(5)
        .startDate(212L)
        .status(TournamentStatus.UPCOMING)
        .build();

    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.of(tournament));

    tournamentService.delete(1L);

    verify(tournamentRepository, times(1))
        .findById(1L);

    verify(tournamentRepository, times(1))
        .delete(any());

  }
  @Test
  void testDeleteFail() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


    Tournament tournament = Tournament.builder()
        .id(1L)
        .name("Aaron Peña")
        .gameTitle("second tournament")
        .maxPlayers(5)
        .startDate(212L)
        .status(TournamentStatus.STARTED)
        .build();

    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.of(tournament));

    assertThrows(IllegalArgumentException.class, () -> {
      tournamentService.delete(1L);
    });

    verify(tournamentRepository, times(1))
        .findById(1L);

    verifyNoMoreInteractions(tournamentRepository);

  }

  @Test
  void testUpdateSuccess() {
    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);

    UpdateTournamentDto dto = UpdateTournamentDto.builder()
        .name("juan")
        .gameTitle("first tournament")
        .maxPlayers(5)
        .startDate(LocalDate.now())
        .status(TournamentStatus.UPCOMING)
        .build();

    Tournament tournament = Tournament.builder()
        .id(1L)
        .name("juan camilo")
        .gameTitle("second tournament")
        .maxPlayers(5)
        .startDate(212L)
        .status(TournamentStatus.UPCOMING)
        .build();

    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.of(tournament));

    when(tournamentRepository.save(any()))
        .thenReturn(tournament);

    Tournament tournamentUpdate = tournamentService.update(1L,dto);

    assertNotNull(tournamentUpdate);

    verify(tournamentRepository, times(1))
        .findById(1L);

    verify(tournamentRepository, times(1))
        .save(any());

    verifyNoMoreInteractions(tournamentRepository);

  }

  @Test
  void testUpdateFail() throws Exception {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);


    UpdateTournamentDto dto = UpdateTournamentDto.builder()
        .name("juan")
        .gameTitle("first tournament")
        .maxPlayers(5)
        .startDate(LocalDate.now())
        .status(TournamentStatus.UPCOMING)
        .build();

    Tournament tournament = Tournament.builder()
        .id(1L)
        .name("juan camilo")
        .gameTitle("second tournament")
        .maxPlayers(5)
        .startDate(212L)
        .status(TournamentStatus.STARTED)
        .build();

    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.of(tournament));

    when(tournamentRepository.save(any()))
        .thenReturn(tournament);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> tournamentService.update(1L,dto));


    assertEquals("The Tournament has started", exception.getMessage());

    verify(tournamentRepository, times(1))
        .findById(1L);

    verifyNoMoreInteractions(tournamentRepository);
  }

  @Test
  void testGetPlayersTournamentSuccess() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    
    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);

    Tournament tournament =
        Tournament.builder()
            .id(1L)
            .name("juan camilo")
            .gameTitle("second tournament")
            .maxPlayers(5)
            .startDate(212L)
            .status(TournamentStatus.UPCOMING)
            .build();

    when(tournamentRepository.findById(1L))
        .thenReturn(Optional.of(tournament));

    List<Player> players = tournamentService.getTournamentPlayers(1L);

    assertNotNull(players);
    assertEquals(players.size(), 0);
  }

  @Test
  void testGetPlayersTournamentFail() {

    PlayerRepository playerRepository = mock(PlayerRepository.class);
    TournamentRepository tournamentRepository = mock(TournamentRepository.class);

    TournamentService tournamentService = new TournamentService(tournamentRepository, playerRepository);

    when(tournamentRepository.findById(1L))
        .thenReturn(Optional.empty());

    EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
      tournamentService.getTournamentPlayers(1L);

    });

    assertEquals("The tournament does not exist", entityNotFoundException.getMessage());

    verify(tournamentRepository, times(1))
        .findById(1L);
  }

}
