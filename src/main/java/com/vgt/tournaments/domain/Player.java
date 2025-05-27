package com.vgt.tournaments.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
public class Player {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id ;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "nick_name", length = 50)
  private String nickName;

  @Column(name = "tournament_id")
  private Long tournamentId;

  @Column(name = "registration_date")
  private Long registrationDate;


}
