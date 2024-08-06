# SportradarScoreboard

The repository contains a set of methods to perform given functionalities of a live football scoreboard.

The appplication was implemented in **Java 17** and all tests in application **are passed**. 

According to the task description, any interface (web, graphic or command line) **was not prepared**. Methods to be called by some future interface are located in the **ScoreboardController** class. Methods in the controller were prepared in a way to be easily transformed to REST API endpoints.

Some information in terms of business logic of the application:
- Matches have their own IDs to be able to identify which match should be updated on the backend site. It is assumed that ID would not be visible for the user. The counter for setting IDs is zeroed when all ongoing matches are finished.
- ID counter was also used for setting the start time of the match. It is added as a number of seconds to actual time. Thanks to that, one can be sure that two matches won't have exactly the same start time.
- During updating a match, there is no mechanism to check if the new score is bigger than the old score. It is made on purpose to allow to set the previous score on the dashboard for example after the VAR call.