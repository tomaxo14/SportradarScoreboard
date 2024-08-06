package sportradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sportradar.exception.IdNotFoundException;
import sportradar.exception.NegativeScoreException;
import sportradar.model.Match;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreboardServiceTest {

    ScoreboardService service;
    @BeforeEach
    void setUp() {
        service = new ScoreboardService();
    }

    @Test
    void startMatch_ReturnsZeroToZeroScore() {
        Match startedMatch = service.startMatch("Poland", "France");

        assertEquals(0, startedMatch.homeTeamGoals());
        assertEquals(0, startedMatch.awayTeamGoals());
    }

    @Test
    void startMatch_ReturnsTeamsWithCorrectNames() {
        Match startedMatch = service.startMatch("Poland", "France");

        assertEquals("Poland", startedMatch.homeTeam().name());
        assertEquals("France", startedMatch.awayTeam().name());
    }

    @Test
    void startMatch_MatchWasAddedToOngoingMatches() {
        Match startedMatch = service.startMatch("Poland", "France");
        List<Match> ongoingMatches = service.showOngoingMatches();

        assertTrue(ongoingMatches.contains(startedMatch));
    }

    @Test
    void updateScore_ReturnsCorrectScore() throws NegativeScoreException, IdNotFoundException {
        Match startedMatch = service.startMatch("Poland", "France");
        Match updatedMatch = service.updateScore(startedMatch.id(), 1, 2);

        assertEquals(1, updatedMatch.homeTeamGoals());
        assertEquals(2, updatedMatch.awayTeamGoals());
    }

    @Test
    void updateScore_MatchWasUpdatedInOngoingMatches() throws NegativeScoreException, IdNotFoundException {
        Match startedMatch = service.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();

        service.updateScore(startedMatchId, 1, 2);
        List<Match> matchesById = service.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(1, matchesById.size());
        assertEquals(1, matchesById.get(0).homeTeamGoals());
        assertEquals(2, matchesById.get(0).awayTeamGoals());
    }

    @Test
    void updateScore_ReturnsErrorWhenNegativeScore() {
        Match startedMatch = service.startMatch("Poland", "France");

        Exception exception = assertThrows(NegativeScoreException.class, () -> service.updateScore(startedMatch.id(), -1, 2));
        assertEquals("You cannot pass negative numbers as a score", exception.getMessage());
    }

    @Test
    void updateScore_DoesNotChangeOngoingMatchWhenNegativeScore() throws NegativeScoreException, IdNotFoundException {
        Match startedMatch = service.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();

        service.updateScore(startedMatchId, 1, 1);

        assertThrows(NegativeScoreException.class, () -> service.updateScore(startedMatchId, -1, 2));

        List<Match> matchesById = service.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(1, matchesById.size());
        assertEquals(1, matchesById.get(0).homeTeamGoals());
        assertEquals(1, matchesById.get(0).awayTeamGoals());
    }

    @Test
    void updateScore_ReturnErrorWhenWrongId() {
        Match startedMatch = service.startMatch("Poland", "France");

        Exception exception = assertThrows(IdNotFoundException.class, () -> service.updateScore(startedMatch.id() + 1, 1, 2));
        assertEquals("Match with given ID not found", exception.getMessage());
    }

    @Test
    void finishMatch_MatchWasDeletedFromOngoingMatches() throws IdNotFoundException {
        Match startedMatch = service.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();
        service.finishMatch(startedMatchId);

        List<Match> matchesById = service.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(0, matchesById.size());
    }

    @Test
    void finishMatch_AlreadyUpdatedMatchWasDeletedFromOngoingMatches() throws NegativeScoreException, IdNotFoundException {
        Match startedMatch = service.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();
        service.updateScore(startedMatchId, 1, 2);
        service.finishMatch(startedMatchId);
        List<Match> matchesById = service.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(0, matchesById.size());
    }

    @Test
    void finishMatch_ReturnErrorWhenWrongId() {
        Match startedMatch = service.startMatch("Poland", "France");

        Exception exception = assertThrows(IdNotFoundException.class, () -> service.finishMatch(startedMatch.id() + 1));
        assertEquals("Match with given ID not found", exception.getMessage());
    }

    @Test
    void showOngoingMatches_ReturnsMatchesOrderedByTotalScoreAndStartTime() throws NegativeScoreException, IdNotFoundException {
        Match firstStartedMatch = service.startMatch("Poland", "France");
        Match secondStartedMatch = service.startMatch("Spain", "Brazil");
        Match thirdStartedMatch = service.startMatch("Argentina", "England");

        int firstId = firstStartedMatch.id();
        int secondId = secondStartedMatch.id();
        int thirdId = thirdStartedMatch.id();

        service.updateScore(firstId, 1, 2);
        service.updateScore(secondId, 4, 0);
        service.updateScore(thirdId, 0, 3);

        List<Match> ongoingMatches = service.showOngoingMatches();

        assertEquals(secondId, ongoingMatches.get(0).id());
        assertEquals(thirdId, ongoingMatches.get(1).id());
        assertEquals(firstId, ongoingMatches.get(2).id());
    }

    @Test
    void showOngoingMatches_ReturnsEmptyListWhenAllMatchesAreFinished() throws IdNotFoundException {
        Match firstStartedMatch = service.startMatch("Poland", "France");
        Match secondStartedMatch = service.startMatch("Spain", "Brazil");
        Match thirdStartedMatch = service.startMatch("Argentina", "England");

        service.finishMatch(firstStartedMatch.id());
        service.finishMatch(secondStartedMatch.id());
        service.finishMatch(thirdStartedMatch.id());

        List<Match> ongoingMatches = service.showOngoingMatches();

        assertEquals(0, ongoingMatches.size());
    }
}
