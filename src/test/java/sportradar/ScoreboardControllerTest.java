package sportradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sportradar.model.Match;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardControllerTest {

    ScoreboardController controller;
    @BeforeEach
    void setUp() {
        controller = new ScoreboardController();
    }

    @Test
    void startMatch_ReturnsZeroToZeroScore() {
        Match startedMatch = controller.startMatch("Poland", "France");

        assertEquals(0, startedMatch.homeTeamGoals());
        assertEquals(0, startedMatch.awayTeamGoals());
    }

    @Test
    void startMatch_ReturnsTeamsWithCorrectNames() {
        Match startedMatch = controller.startMatch("Poland", "France");

        assertEquals("Poland", startedMatch.homeTeam().name());
        assertEquals("France", startedMatch.awayTeam().name());
    }

    @Test
    void startMatch_MatchWasAddedToOngoingMatches() {
        Match startedMatch = controller.startMatch("Poland", "France");
        List<Match> ongoingMatches = controller.showOngoingMatches();

        assertTrue(ongoingMatches.contains(startedMatch));
    }

    @Test
    void updateScore_ReturnsCorrectScore() {
        Match startedMatch = controller.startMatch("Poland", "France");
        Match updatedMatch = controller.updateScore(startedMatch.id(), 1, 2);

        assertEquals(1, updatedMatch.homeTeamGoals());
        assertEquals(2, updatedMatch.awayTeamGoals());
    }

    @Test
    void updateScore_MatchWasUpdatedInOngoingMatches() {
        Match startedMatch = controller.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();

        controller.updateScore(startedMatchId, 1, 2);
        List<Match> matchesById = controller.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(1, matchesById.size());
        assertEquals(1, matchesById.get(0).homeTeamGoals());
        assertEquals(2, matchesById.get(0).awayTeamGoals());
    }

    @Test
    void updateScore_ReturnsErrorWhenNegativeScore() {
        Match startedMatch = controller.startMatch("Poland", "France");

        Exception exception = assertThrows(Exception.class, () -> controller.updateScore(startedMatch.id(), -1, 2));
        assertEquals("You cannot pass negative numbers as a score", exception.getMessage());
    }

    @Test
    void updateScore_DoesNotChangeOngoingMatchWhenNegativeScore() {
        Match startedMatch = controller.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();

        controller.updateScore(startedMatchId, 1, 1);
        controller.updateScore(startedMatchId, -1, 2);

        List<Match> matchesById = controller.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(1, matchesById.size());
        assertEquals(1, matchesById.get(0).homeTeamGoals());
        assertEquals(1, matchesById.get(0).awayTeamGoals());
    }

    @Test
    void updateScore_ReturnErrorWhenWrongId() {
        Match startedMatch = controller.startMatch("Poland", "France");

        Exception exception = assertThrows(Exception.class, () -> controller.updateScore(startedMatch.id() + 1, 1, 2));
        assertEquals("Match with given ID not found", exception.getMessage());
    }

    @Test
    void finishMatch_MatchWasDeletedFromOngoingMatches() {
        Match startedMatch = controller.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();
        controller.finishMatch(startedMatchId);

        List<Match> matchesById = controller.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(0, matchesById.size());
    }

    @Test
    void finishMatch_AlreadyUpdatedMatchWasDeletedFromOngoingMatches() {
        Match startedMatch = controller.startMatch("Poland", "France");
        int startedMatchId = startedMatch.id();
        controller.updateScore(startedMatchId, 1, 2);
        controller.finishMatch(startedMatchId);
        List<Match> matchesById = controller.showOngoingMatches().stream()
                .filter(match -> match.id() == startedMatchId).toList();

        assertEquals(0, matchesById.size());
    }

    @Test
    void finishMatch_ReturnErrorWhenWrongId() {
        Match startedMatch = controller.startMatch("Poland", "France");

        Exception exception = assertThrows(Exception.class, () -> controller.finishMatch(startedMatch.id() + 1));
        assertEquals("Match with given ID not found", exception.getMessage());
    }

    @Test
    void showOngoingMatches_ReturnsMatchesOrderedByTotalScoreAndStartTime() {
        Match firstStartedMatch = controller.startMatch("Poland", "France");
        Match secondStartedMatch = controller.startMatch("Spain", "Brazil");
        Match thirdStartedMatch = controller.startMatch("Argentina", "England");

        int firstId = firstStartedMatch.id();
        int secondId = secondStartedMatch.id();
        int thirdId = thirdStartedMatch.id();

        controller.updateScore(firstId, 1, 2);
        controller.updateScore(secondId, 4, 0);
        controller.updateScore(thirdId, 0, 3);

        List<Match> ongoingMatches = controller.showOngoingMatches();

        assertEquals(secondId, ongoingMatches.get(0).id());
        assertEquals(thirdId, ongoingMatches.get(1).id());
        assertEquals(firstId, ongoingMatches.get(2).id());
    }

    @Test
    void showOngoingMatches_ReturnsEmptyListWhenAllMatchesAreFinished() {
        Match firstStartedMatch = controller.startMatch("Poland", "France");
        Match secondStartedMatch = controller.startMatch("Spain", "Brazil");
        Match thirdStartedMatch = controller.startMatch("Argentina", "England");

        controller.finishMatch(firstStartedMatch.id());
        controller.finishMatch(secondStartedMatch.id());
        controller.finishMatch(thirdStartedMatch.id());

        List<Match> ongoingMatches = controller.showOngoingMatches();

        assertEquals(0, ongoingMatches.size());
    }
}
