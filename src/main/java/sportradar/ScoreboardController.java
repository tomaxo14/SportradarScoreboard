package sportradar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import sportradar.model.Match;
import sportradar.model.Team;

public class ScoreboardController {

    private int idIterator = 0;
    private List<Match> ongoingMatches = new ArrayList<>();

    public Match startMatch(String homeTeamName, String awayTeamName) {

        idIterator += 1;
        Match newMatch = Match.builder()
                .id(idIterator)
                .homeTeam(new Team(homeTeamName))
                .awayTeam(new Team(awayTeamName))
                .homeTeamGoals(0)
                .awayTeamGoals(0)
                .startDateTime(LocalDateTime.now().plusSeconds(idIterator))
                .build();
        ongoingMatches.add(newMatch);

        return newMatch;
    }

    public Match updateScore(int matchId, int homeTeamScore, int awayTeamScore) throws Exception {

        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new Exception("You cannot pass negative numbers as a score");
        }

        Optional<Match> optionalMatchToUpdate = ongoingMatches.stream().filter(match -> match.id() == matchId).findFirst();
        Match matchToUpdate;
        if (optionalMatchToUpdate.isPresent()) {
            matchToUpdate = optionalMatchToUpdate.get();
            ongoingMatches.remove(matchToUpdate);
        } else {
            throw new Exception("Match with given ID not found");
        }
        matchToUpdate.homeTeamGoals(homeTeamScore);
        matchToUpdate.awayTeamGoals(awayTeamScore);
        ongoingMatches.add(matchToUpdate);

        return matchToUpdate;
    }

    public List<Match> finishMatch(int matchId) throws Exception {
        Optional<Match> optionalMatchToDelete = ongoingMatches.stream().filter(match -> match.id() == matchId).findFirst();

        if (optionalMatchToDelete.isPresent()) {
            ongoingMatches.remove(optionalMatchToDelete.get());
        } else {
            throw new Exception("Match with given ID not found");
        }

        if (ongoingMatches.isEmpty()) {
            idIterator = 0;
        }

        return ongoingMatches;
    }

    public List<Match> showOngoingMatches() {

        Comparator<Match> startDateTimeComparator = Comparator
                .comparing(Match::getStartDateTime)
                .reversed();

        Comparator<Match> goalsComparator = Comparator
                .comparingInt(Match::getSumOfGoals)
                .reversed();

        ongoingMatches.sort(startDateTimeComparator);
        ongoingMatches.sort(goalsComparator);

        return ongoingMatches;
    }
}
