package com.vgt.tournaments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreateTournamentRequestDto;
import com.vgt.tournaments.dto.UpdateTournamentDto;
import com.vgt.tournaments.repositories.PlayerRepository;
import com.vgt.tournaments.repositories.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TournamentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TournamentRepository tournamentRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @BeforeEach
  void setUp() {
    playerRepository.deleteAll();
    tournamentRepository.deleteAll();
  }

  @Test
  void testCreateTournament() throws Exception {
    CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
            .name("Gran Final")
            .gameTitle("League of Legends")
            .maxPlayers(8)
            .startDate(LocalDate.now().plusDays(5))
            .build();

    mockMvc.perform(post("/api/tournaments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Gran Final")))
            .andExpect(jsonPath("$.status", is("UPCOMING")));
  }

  @Test
  void testGetAllTournaments() throws Exception {
    Tournament t1 = Tournament.builder()
            .name("Torneo A")
            .gameTitle("Valorant")
            .maxPlayers(4)
            .startDate(LocalDate.now().plusDays(3).toEpochDay())
            .status(TournamentStatus.UPCOMING)
            .build();

    Tournament t2 = Tournament.builder()
            .name("Torneo B")
            .gameTitle("CS:GO")
            .maxPlayers(6)
            .startDate(LocalDate.now().plusDays(4).toEpochDay())
            .status(TournamentStatus.UPCOMING)
            .build();

    tournamentRepository.save(t1);
    tournamentRepository.save(t2);

    mockMvc.perform(get("/api/tournaments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void testGetTournamentById() throws Exception {
    Tournament tournament = Tournament.builder()
            .name("Torneo Individual")
            .gameTitle("Tekken")
            .maxPlayers(2)
            .startDate(LocalDate.now().plusDays(2).toEpochDay())
            .status(TournamentStatus.UPCOMING)
            .build();

    Tournament saved = tournamentRepository.save(tournament);

    mockMvc.perform(get("/api/tournaments/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Torneo Individual")));
  }

  @Test
  void testDeleteTournament() throws Exception {
    Tournament tournament = Tournament.builder()
            .name("Eliminar")
            .gameTitle("Street Fighter")
            .maxPlayers(4)
            .startDate(LocalDate.now().plusDays(7).toEpochDay())
            .status(TournamentStatus.UPCOMING)
            .build();

    Tournament saved = tournamentRepository.save(tournament);

    mockMvc.perform(delete("/api/tournaments/" + saved.getId()))
            .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/tournaments/" + saved.getId()))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateTournamentWithInvalidStartDate() throws Exception {
    CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
            .name("Fecha pasada")
            .gameTitle("Mario Kart")
            .maxPlayers(4)
            .startDate(LocalDate.now().minusDays(1))
            .build();

    mockMvc.perform(post("/api/tournaments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateTournamentWithInvalidMaxPlayers() throws Exception {
    CreateTournamentRequestDto dto = CreateTournamentRequestDto.builder()
            .name("Uno solo")
            .gameTitle("Chess")
            .maxPlayers(1)
            .startDate(LocalDate.now().plusDays(3))
            .build();

    mockMvc.perform(post("/api/tournaments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
  }


}

