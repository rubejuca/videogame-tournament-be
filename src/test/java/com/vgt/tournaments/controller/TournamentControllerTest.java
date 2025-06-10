package com.vgt.tournaments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vgt.tournaments.domain.Tournament;
import com.vgt.tournaments.domain.enums.TournamentStatus;
import com.vgt.tournaments.dto.CreateTournamentRequestDto;
import com.vgt.tournaments.services.TournamentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  TournamentService tournamentService;

  private ObjectMapper objectMapper = JsonMapper
      .builder()
      .addModule(new Jdk8Module())
      .addModule(new JavaTimeModule())
      .build();



}
