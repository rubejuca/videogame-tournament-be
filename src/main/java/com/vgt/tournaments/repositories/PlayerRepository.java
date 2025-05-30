package com.vgt.tournaments.repositories;

import com.vgt.tournaments.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
 public List<Player> findPlayersByTournamentId(Long tournamentId);
}
