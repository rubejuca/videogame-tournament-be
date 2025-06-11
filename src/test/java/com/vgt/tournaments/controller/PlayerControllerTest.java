package com.vgt.tournaments.controller;

import com.vgt.tournaments.domain.Player;
import com.vgt.tournaments.dto.CreatePlayerRequestDto;
import com.vgt.tournaments.dto.PlayerResponseDto;
import com.vgt.tournaments.dto.UpdatePlayerDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
class PlayerControllerTest {

    private PlayerController playerController;
    private PlayerService playerService;
    private PlayerRepository playerRepository;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Crear mock manualmente
        playerService = Mockito.mock(PlayerService.class);

        // Pasarlo manualmente al controlador (esto evita el NullPointerException)
        playerController = new PlayerController(playerService);


    }

    @Test
    void testCreatePlayer() {
        CreatePlayerRequestDto dto = CreatePlayerRequestDto.builder()
                .name("Cristiano Ronaldo")
                .nickName("CR7")
                .tournamentId(1L)
                .registrationDate(LocalDate.ofEpochDay(LocalDate.now().toEpochDay()))
                .build();

        Player player = Player.builder()
                .name("Cristiano Ronaldo")
                .nickName("CR7")
                .tournamentId(1L)
                .registrationDate(43202L)
                .build();

        Mockito.when(playerService.create(dto)).thenReturn(player);
        PlayerResponseDto response = playerController.create(dto);

        assertEquals("Cristiano Ronaldo", response.name());
        assertEquals("CR7", response.nickName());
        assertEquals(1L, response.tournamentId());
        assertEquals(LocalDate.ofEpochDay(43202L), response.registrationDate());
    }

    @Test
    void testReadPlayerById() {
        Long id = 1L;
        Player player = Player.builder()
                .name("Cristiano Ronaldo")
                .nickName("CR7")
                .tournamentId(1L)
                .registrationDate(43202L)
                .build();

        Mockito.when(playerService.readById(id)).thenReturn(player);
        PlayerResponseDto response = playerController.readById(id);

        assertEquals("Cristiano Ronaldo", response.name());
        assertEquals("CR7", response.nickName());
        assertEquals(1L, response.tournamentId());
        assertEquals(LocalDate.ofEpochDay(43202L), response.registrationDate());

    }

    @Test
    void  testReadAllPlayers() {
        Player player = Player.builder()
                .name("Cristiano Ronaldo")
                .nickName("CR7")
                .tournamentId(1L)
                .registrationDate(43202L)
                .build();
        Player player2 = Player.builder()
                .name("Lionel Messi")
                .nickName("Lionel Messi")
                .tournamentId(1L)
                .registrationDate(43202L)
                .build();

        Mockito.when(playerService.readAll()).thenReturn(List.of(player, player2));
        List<PlayerResponseDto> response = playerController.readAll();
        assertEquals(2, response.size());

    }

    @Test
    void testDeletePlayer() {
        Long id = 1L;
        playerController.deletePlayer(id);
        verify(playerService, Mockito.times(1)).delete(id);
    }



}