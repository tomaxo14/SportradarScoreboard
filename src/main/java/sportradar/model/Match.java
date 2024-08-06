package sportradar.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(fluent = true)
@Getter
@Setter
@Builder
public class Match {

    private int id;
    private LocalDateTime startDateTime;
    private Team homeTeam;
    private Team awayTeam;
    private int homeTeamGoals;
    private int awayTeamGoals;


    public int getSumOfGoals() {

        return homeTeamGoals + awayTeamGoals;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
}
