package sportradar.model;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(fluent = true)
@Getter
public class Match {

    private int id;
    private LocalDateTime startDateTime;
    private Team homeTeam;
    private Team awayTeam;
    private int homeTeamGoals;
    private int awayTeamGoals;

}
