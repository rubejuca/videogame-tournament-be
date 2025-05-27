package com.vgt.tournaments;

import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.repositories.TournamentRepository;
import com.vgt.tournaments.services.TournamentService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TournamentServiceTest {

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
