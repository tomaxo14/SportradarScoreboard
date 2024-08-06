package sportradar;

import java.util.List;

import lombok.RequiredArgsConstructor;
import sportradar.exception.IdNotFoundException;
import sportradar.exception.NegativeScoreException;
import sportradar.model.Match;

@RequiredArgsConstructor
public class ScoreboardController {

    private final ScoreboardService service;

    public Match startMatch(String homeTeamName, String awayTeamName) {
        return service.startMatch(homeTeamName, awayTeamName);
    }

    public Match updateScore(int matchId, int homeTeamScore, int awayTeamScore) throws NegativeScoreException, IdNotFoundException {
        return service.updateScore(matchId, homeTeamScore, awayTeamScore);
    }

    public List<Match> finishMatch(int matchId) throws IdNotFoundException {
        return service.finishMatch(matchId);
    }

    public List<Match> showOngoingMatches() {
        return service.showOngoingMatches();
    }
}
