package com.vgt.tournaments.repositories;

import com.vgt.tournaments.domain.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

 @Query("SELECT COUNT(p) FROM Player p WHERE p.tournamentId = :tournamentId")
 int countPlayersByTournamentId(@Param("tournamentId") Long tournamentId);

 boolean existsByNickNameAndTournamentId(String nickName, Long tournamentId);

 List<Player> findByTournamentId(Long tournamentId);
}
