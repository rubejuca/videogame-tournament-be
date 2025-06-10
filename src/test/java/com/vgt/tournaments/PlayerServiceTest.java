package com.vgt.tournaments;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreatePlayerDto;
import com.vgt.tournaments.dto.CreateTournamentDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import com.vgt.tournaments.services.PlayerService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

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
