package sk.upjs.ics.android.matchwatch.provider;

import android.net.Uri;

import static android.content.ContentResolver.SCHEME_CONTENT;

import android.provider.BaseColumns;

import sk.upjs.ics.android.matchwatch.entity.Person;

public interface Provider {

    // Match table
    public interface Match extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.MatchesContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(Match.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "match";

        public static final String GAME_DATE = "gameDate";

        public static final String HOME_TEAM_ID = "homeTeamId";

        public static final String AWAY_TEAM_ID = "awayTeamId";

        public static final String SCORE_HOME = "scoreHome";

        public static final String SCORE_AWAY = "scoreAway";

        public static final String MATCH_IMAGE = "matchImage";

        public static final String IS_FINISHED = "isFinished";

    }

    // MatchReferee table
    public interface MatchReferee extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.MatchesRefereesContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(MatchReferee.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "match_referee";

        public static final String MATCH_ID = "matchId";

        public static final String REFEREE_ID = "refereeId";
    }

    // MathLinesmen table
    public interface MatchLinesmen extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.MatchesLinesmenContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(MatchLinesmen.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "match_linesmen";

        public static final String MATCH_ID = "matchId";

        public static final String LINESMEN_ID = "linesmenId";
    }

    // MatchInterruption table
    public interface MatchInterruption extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.MatchesInterruptionsContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(MatchInterruption.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "match_interruption";

        public static final String MATCH_ID = "matchId";

        public static final String INTERRUPTION_ID = "interruptionId";

        public static final String TIME = "time";

        public static final String TEAM_ID = "teamId";

        public static final String PERIOD = "period";
    }

    // InterruptionInfo table
    public interface InterruptionInfo extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.InterruptionInfoContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(InterruptionInfo.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "interruption_info";

        public static final String NAME = "name";
    }

    // MatchGoal table
    public interface MatchGoal extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.MatchesGoalsContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(MatchGoal.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "match_goal";

        public static final String MATCH_ID = "matchId";

        public static final String PLAYER_ID = "playerId";

        public static final String TIME = "time";

        public static final String GOAL_TYPE = "goalType";

        public static final String ASSIST_ID_1 = "assistId_1";

        public static final String ASSIST_ID_2 = "assistId_2";

        public static final String TEAM_ID = "teamId";

        public static final String PERIOD = "period";
    }

    // MatchPenalty table
    public interface MatchPenalty extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.MatchesPenaltiesContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(MatchPenalty.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "match_penalty";

        public static final String MATCH_ID = "matchId";

        public static final String PENALTY_ID = "penaltyId";

        public static final String PLAYER_ID = "playerId";

        public static final String DURATION = "duration";

        public static final String TIME = "time";

        public static final String TEAM_ID = "teamId";

        public static final String PERIOD = "period";
    }

    // PenaltyInfo table
    public interface PenaltyInfo extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.PenaltyInfoContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(PenaltyInfo.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "penalty_info";

        public static final String NAME = "name";
    }

    // Team table
    public interface Team extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.TeamsContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(Team.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "team";

        public static final String FULL_NAME = "fullName";

        public static final String SHORT_NAME = "shortName";
    }

    // TeamPlayer table
    public interface TeamPlayer extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.TeamsPlayersContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(TeamPlayer.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "team_player";

        public static final String TEAM_ID = "teamId";

        public static final String PLAYER_ID = "playerId";
    }

    // TeamPerson table
    public interface TeamPerson extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.TeamsPersonContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(TeamPerson.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "team_person";

        public static final String TEAM_ID = "teamId";

        public static final String PERSON_ID = "personId";
    }

    // Person table
    public interface Person extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.PersonContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(Person.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "person";

        public static final String FIRST_NAME = "firstName";

        public static final String LAST_NAME = "lastName";

        public static final String BIRTH_DATE = "birthDate";

        public static final String NATIONALITY = "nationality";

        public static final String FUNCTION = "function";
    }

    // Player table
    public interface Player extends BaseColumns {

        public static final String AUTHORITY = "sk.upjs.ics.android.matchwatch.provider.PlayersContentProvider";

        public static final Uri CONTENT_URI = new Uri.Builder()
                .scheme(SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(Player.TABLE_NAME)
                .build();

        public static final String TABLE_NAME = "player";

        public static final String FIRST_NAME = "firstName";

        public static final String LAST_NAME = "lastName";

        public static final String BIRTH_DATE = "birthDate";

        public static final String NUMBER = "number";

        public static final String POSITION = "position";

        public static final String SHOOTS = "shoots";

        public static final String HEIGHT = "height";

        public static final String WEIGHT = "weight";

        public static final String CLUB = "club";
    }

}
