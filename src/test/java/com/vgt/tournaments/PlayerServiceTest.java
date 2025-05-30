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
    void testCreate() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        TournamentRepository tournamentRepository = mock(TournamentRepository.class);
        PlayerService playerService = new PlayerService(playerRepository, tournamentRepository);

        CreatePlayerDto dto = CreatePlayerDto.builder()
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
        when(playerRepository.findPlayersByTournamentId(1L)).thenReturn(Collections.emptyList());
        when(playerRepository.save(any())).thenReturn(player);

        Player createdPlayer = playerService.create(dto);

        assertNotNull(createdPlayer);
        assertEquals(player.getName(), createdPlayer.getName());
        assertEquals(player.getNickName(), createdPlayer.getNickName());
        assertEquals(player.getTournamentId(), createdPlayer.getTournamentId());
    }
}
