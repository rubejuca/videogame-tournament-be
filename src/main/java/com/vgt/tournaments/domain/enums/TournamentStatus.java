package com.vgt.tournaments.domain.enums;

public enum TournamentStatus {
  UPCOMING, STARTED, FINISHED;

    public boolean isStarted() {
        return this == STARTED;
    }
}
