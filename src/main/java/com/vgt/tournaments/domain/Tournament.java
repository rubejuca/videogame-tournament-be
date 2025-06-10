package com.vgt.tournaments.domain;

import com.vgt.tournaments.domain.enums.TournamentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tournament")
public class Tournament {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id ;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "game_title", length = 500)
  private String gameTitle;

  @Column(name = "max_players")
  private int maxPlayers;

  @Column(name = "start_date")
  private Long startDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TournamentStatus status;

  public boolean isStarted() {
    return status == TournamentStatus.STARTED;
  }

  public boolean isFinished() {
    return status == TournamentStatus.FINISHED;
  }

  public boolean canAddPlayers() {
    return isStarted() && !isFinished();
  }

}
