package sportradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sportradar.exception.IdNotFoundException;
import sportradar.exception.NegativeScoreException;
import sportradar.model.Match;
import sportradar.model.Team;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoreboardControllerTest {

    private ScoreboardController controller;
    private ScoreboardService service;

    @BeforeEach
    void setUp() {
        service = mock(ScoreboardService.class);
        controller = new ScoreboardController(service);
    }

    @Test
    void startMatch_PassesResultFromService() {
        Match matchToReturn = Match.builder()
                .id(1)
                .homeTeam(new Team("Poland"))
                .awayTeam(new Team("France"))
                .homeTeamGoals(0)
                .awayTeamGoals(0)
                .startDateTime(LocalDateTime.of(2024, 8, 6, 22, 30))
                .build();

        when(service.startMatch(anyString(), anyString())).thenReturn(matchToReturn);
        Match resultFromController = controller.startMatch("Poland", "France");

        assertEquals(matchToReturn, resultFromController);
    }

    @Test
    void updateScore_PassesResultFromService() throws NegativeScoreException, IdNotFoundException {
        Match matchToReturn = Match.builder()
                .id(1)
                .homeTeam(new Team("Poland"))
                .awayTeam(new Team("France"))
                .homeTeamGoals(1)
                .awayTeamGoals(2)
                .startDateTime(LocalDateTime.of(2024, 8, 6, 22, 30))
                .build();

        when(service.updateScore(anyInt(), anyInt(), anyInt())).thenReturn(matchToReturn);
        Match resultFromController = controller.updateScore(1, 1, 2);

        assertEquals(matchToReturn, resultFromController);
    }


    @Test
    void finishMatch_PassesResultFromService() throws IdNotFoundException {
        Match matchToReturn1 = Match.builder()
                .id(1)
                .homeTeam(new Team("Poland"))
                .awayTeam(new Team("France"))
                .homeTeamGoals(1)
                .awayTeamGoals(2)
                .startDateTime(LocalDateTime.of(2024, 8, 6, 22, 30))
                .build();

        Match matchToReturn2 = Match.builder()
                .id(2)
                .homeTeam(new Team("Argentina"))
                .awayTeam(new Team("Spain"))
                .homeTeamGoals(3)
                .awayTeamGoals(0)
                .startDateTime(LocalDateTime.of(2024, 8, 6, 22, 31))
                .build();

        List<Match> matchesToReturn = List.of(matchToReturn1, matchToReturn2);

        when(service.finishMatch(anyInt())).thenReturn(matchesToReturn);
        List<Match> resultFromController = controller.finishMatch(3);

        assertEquals(matchesToReturn, resultFromController);
    }

    @Test
    void showOngoingMatches_PassesResultFromService() {
        Match matchToReturn1 = Match.builder()
                .id(1)
                .homeTeam(new Team("Poland"))
                .awayTeam(new Team("France"))
                .homeTeamGoals(1)
                .awayTeamGoals(2)
                .startDateTime(LocalDateTime.of(2024, 8, 6, 22, 30))
                .build();

        Match matchToReturn2 = Match.builder()
                .id(2)
                .homeTeam(new Team("Argentina"))
                .awayTeam(new Team("Spain"))
                .homeTeamGoals(3)
                .awayTeamGoals(0)
                .startDateTime(LocalDateTime.of(2024, 8, 6, 22, 31))
                .build();

        List<Match> matchesToReturn = List.of(matchToReturn1, matchToReturn2);

        when(service.showOngoingMatches()).thenReturn(matchesToReturn);
        List<Match> resultFromController = controller.showOngoingMatches();

        assertEquals(matchesToReturn, resultFromController);
    }


}
