package com.vgt.tournaments;

import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreateTournamentDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.repositories.TournamentRepository;
import com.vgt.tournaments.services.TournamentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TournamentServiceTest {


  @Test
  void testCreateSuccess() {

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);


      CreateTournamentDto dto = CreateTournamentDto.builder()
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


      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository);

      CreateTournamentDto dto = CreateTournamentDto.builder()
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
    void testCreateFailButStartDate() throws Exception {


      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository);

      CreateTournamentDto dto = CreateTournamentDto.builder()
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


      TournamentRepository tournamentRepository = mock(TournamentRepository.class);
      TournamentService tournamentService = new TournamentService(tournamentRepository);

      CreateTournamentDto dto = CreateTournamentDto.builder()
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

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

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

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

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

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

    when(tournamentRepository
        .findById(1L))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class,
        () -> tournamentService.findById(1L));
  }


  @Test
  void testDeleteSuccess() {

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

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

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

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

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

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

    TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    TournamentService tournamentService = new TournamentService(tournamentRepository);

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
}
