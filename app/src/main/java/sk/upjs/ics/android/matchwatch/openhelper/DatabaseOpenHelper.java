package sk.upjs.ics.android.matchwatch.openhelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.dbsEntity.GoalDatabase;
import sk.upjs.ics.android.matchwatch.dbsEntity.InterruptionDatabase;
import sk.upjs.ics.android.matchwatch.dbsEntity.MatchDatabase;
import sk.upjs.ics.android.matchwatch.dbsEntity.PenaltyDatabase;
import sk.upjs.ics.android.matchwatch.entity.Player;
import sk.upjs.ics.android.matchwatch.provider.Provider;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context) {
        super(context, Defaults.DATABASE_NAME, null, Defaults.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tabulky hlavnych entit, ktore neobsahuju cudzie kluce
        db.execSQL(createMatchTableSql());
        db.execSQL(createTeamTableSql());
        db.execSQL(createPlayerTableSql());
        db.execSQL(createPersonTableSql());
        db.execSQL(createInterruptionInfoTableSql());
        db.execSQL(createPenaltyInfoTableSql());

        // tabulky obsahujuce cudzie kluce
        db.execSQL(createMatchRefereeTableSql());
        db.execSQL(createMatchLinesmenTableSql());
        db.execSQL(createMatchInterruptionTableSql());
        db.execSQL(createMatchGoalTableSql());
        db.execSQL(createMatchPenaltyTableSql());
        db.execSQL(createTeamPlayerTableSql());
        db.execSQL(createTeamPersonTableSql());

        // vkladanie sample dat
        insertIntoPenaltyInfo(db);
        insertIntoInterruptionInfo(db);
    }

    private String createTeamTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "UNIQUE(%s, %s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.Team.TABLE_NAME,
                Provider.Team._ID,
                Provider.Team.FULL_NAME,
                Provider.Team.SHORT_NAME,
                Provider.Team.FULL_NAME,
                Provider.Team.SHORT_NAME
        );
    }

    private String createPlayerTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s REAL,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + "UNIQUE(%s, %s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.Player.TABLE_NAME,
                Provider.Player._ID,
                Provider.Player.FIRST_NAME,
                Provider.Player.LAST_NAME,
                Provider.Player.BIRTH_DATE,
                Provider.Player.NUMBER,
                Provider.Player.POSITION,
                Provider.Player.SHOOTS,
                Provider.Player.HEIGHT,
                Provider.Player.WEIGHT,
                Provider.Player.CLUB,
                Provider.Player.FIRST_NAME,
                Provider.Player.LAST_NAME
        );
    }

    private String createPersonTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "UNIQUE(%s, %s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.Person.TABLE_NAME,
                Provider.Person._ID,
                Provider.Person.FIRST_NAME,
                Provider.Person.LAST_NAME,
                Provider.Person.BIRTH_DATE,
                Provider.Person.NATIONALITY,
                Provider.Person.FUNCTION,
                Provider.Person.FIRST_NAME,
                Provider.Person.LAST_NAME
        );
    }

    private String createTeamPersonTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.TeamPerson.TABLE_NAME,
                Provider.TeamPerson._ID,
                Provider.TeamPerson.TEAM_ID,
                Provider.TeamPerson.PERSON_ID,
                Provider.TeamPerson.TEAM_ID,
                Provider.Team.TABLE_NAME,
                Provider.Team._ID,
                Provider.TeamPerson.PERSON_ID,
                Provider.Person.TABLE_NAME,
                Provider.Person._ID
        );
    }

    private String createTeamPlayerTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "UNIQUE(%s, %s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.TeamPlayer.TABLE_NAME,
                Provider.TeamPlayer._ID,
                Provider.TeamPlayer.TEAM_ID,
                Provider.TeamPlayer.PLAYER_ID,
                Provider.TeamPlayer.TEAM_ID,
                Provider.TeamPlayer.PLAYER_ID,
                Provider.TeamPlayer.TEAM_ID,
                Provider.Team.TABLE_NAME,
                Provider.Team._ID,
                Provider.TeamPlayer.PLAYER_ID,
                Provider.Player.TABLE_NAME,
                Provider.Player._ID
        );
    }

    private String createPenaltyInfoTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT"
                + ")";

        return String.format(sqlTemplate,
                Provider.PenaltyInfo.TABLE_NAME,
                Provider.PenaltyInfo._ID,
                Provider.PenaltyInfo.NAME
        );
    }

    private String createMatchPenaltyTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.MatchPenalty.TABLE_NAME,
                Provider.MatchPenalty._ID,
                Provider.MatchPenalty.MATCH_ID,
                Provider.MatchPenalty.PENALTY_ID,
                Provider.MatchPenalty.PLAYER_ID,
                Provider.MatchPenalty.DURATION,
                Provider.MatchPenalty.TIME,
                Provider.MatchPenalty.TEAM_ID,
                Provider.MatchPenalty.PERIOD,
                Provider.MatchPenalty.MATCH_ID,
                Provider.Match.TABLE_NAME,
                Provider.Match._ID,
                Provider.MatchPenalty.PENALTY_ID,
                Provider.PenaltyInfo.TABLE_NAME,
                Provider.PenaltyInfo._ID
        );
    }

    private String createMatchGoalTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.MatchGoal.TABLE_NAME,
                Provider.MatchGoal._ID,
                Provider.MatchGoal.MATCH_ID,
                Provider.MatchGoal.PLAYER_ID,
                Provider.MatchGoal.TIME,
                Provider.MatchGoal.GOAL_TYPE,
                Provider.MatchGoal.ASSIST_ID_1,
                Provider.MatchGoal.ASSIST_ID_2,
                Provider.MatchGoal.TEAM_ID,
                Provider.MatchGoal.PERIOD,
                Provider.MatchGoal.MATCH_ID,
                Provider.Match.TABLE_NAME,
                Provider.Match._ID,
                Provider.MatchGoal.PLAYER_ID,
                Provider.Player.TABLE_NAME,
                Provider.Player._ID
        );
    }

    private String createInterruptionInfoTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT"
                + ")";

        return String.format(sqlTemplate,
                Provider.InterruptionInfo.TABLE_NAME,
                Provider.InterruptionInfo._ID,
                Provider.InterruptionInfo.NAME
        );
    }

    private String createMatchInterruptionTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s TEXT,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.MatchInterruption.TABLE_NAME,
                Provider.MatchInterruption._ID,
                Provider.MatchInterruption.MATCH_ID,
                Provider.MatchInterruption.INTERRUPTION_ID,
                Provider.MatchInterruption.TIME,
                Provider.MatchInterruption.TEAM_ID,
                Provider.MatchInterruption.PERIOD,
                Provider.MatchInterruption.MATCH_ID,
                Provider.Match.TABLE_NAME,
                Provider.Match._ID,
                Provider.MatchInterruption.INTERRUPTION_ID,
                Provider.InterruptionInfo.TABLE_NAME,
                Provider.InterruptionInfo._ID
        );
    }

    private String createMatchLinesmenTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.MatchLinesmen.TABLE_NAME,
                Provider.MatchLinesmen._ID,
                Provider.MatchLinesmen.MATCH_ID,
                Provider.MatchLinesmen.LINESMEN_ID,
                Provider.MatchLinesmen.MATCH_ID,
                Provider.Match.TABLE_NAME,
                Provider.Match._ID,
                Provider.MatchLinesmen.LINESMEN_ID,
                Provider.Person.TABLE_NAME,
                Provider.Person._ID
        );
    }

    private String createMatchRefereeTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.MatchReferee.TABLE_NAME,
                Provider.MatchReferee._ID,
                Provider.MatchReferee.MATCH_ID,
                Provider.MatchReferee.REFEREE_ID,
                Provider.MatchReferee.MATCH_ID,
                Provider.Match.TABLE_NAME,
                Provider.Match._ID,
                Provider.MatchReferee.REFEREE_ID,
                Provider.Person.TABLE_NAME,
                Provider.Person._ID
        );
    }

    private String createMatchTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s BLOB,"
                + "%s INTEGER,"
                + " FOREIGN KEY (%s) REFERENCES %s(%s),"
                + " FOREIGN KEY (%s) REFERENCES %s(%s)"
                + ")";

        return String.format(sqlTemplate,
                Provider.Match.TABLE_NAME,
                Provider.Match._ID,
                Provider.Match.GAME_DATE,
                Provider.Match.HOME_TEAM_ID,
                Provider.Match.AWAY_TEAM_ID,
                Provider.Match.SCORE_HOME,
                Provider.Match.SCORE_AWAY,
                Provider.Match.MATCH_IMAGE,
                Provider.Match.IS_FINISHED,
                Provider.Match.HOME_TEAM_ID,
                Provider.Team.TABLE_NAME,
                Provider.Team._ID,
                Provider.Match.AWAY_TEAM_ID,
                Provider.Team.TABLE_NAME,
                Provider.Team._ID
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + Provider.Match.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.Team.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.Player.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.Person.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.InterruptionInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.PenaltyInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.MatchReferee.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.MatchLinesmen.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.MatchInterruption.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.MatchGoal.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.MatchPenalty.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.TeamPlayer.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.TeamPerson.TABLE_NAME);

        // create new tables
        onCreate(db);
    }

    public String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return simpleDateFormat.format(date);
    }

    private void insertPenaltyInfoSampleEntry(SQLiteDatabase db, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.PenaltyInfo.NAME, name);

        db.insert(Provider.PenaltyInfo.TABLE_NAME, null, contentValues);
    }

    private void insertIntoPenaltyInfo(SQLiteDatabase db) {
        insertPenaltyInfoSampleEntry(db, "Charging");
        insertPenaltyInfoSampleEntry(db, "Clipping");
        insertPenaltyInfoSampleEntry(db, "Closing hand on puck");
        insertPenaltyInfoSampleEntry(db, "Cross-checking");
        insertPenaltyInfoSampleEntry(db, "Diving");
        insertPenaltyInfoSampleEntry(db, "Delay of game");
        insertPenaltyInfoSampleEntry(db, "Elbowing");
        insertPenaltyInfoSampleEntry(db, "Goalkeeper interference");
        insertPenaltyInfoSampleEntry(db, "High-sticking");
        insertPenaltyInfoSampleEntry(db, "Holding");
        insertPenaltyInfoSampleEntry(db, "Holding the stick");
        insertPenaltyInfoSampleEntry(db, "Hooking");
        insertPenaltyInfoSampleEntry(db, "Illegal equipment");
        insertPenaltyInfoSampleEntry(db, "Illegal stick");
        insertPenaltyInfoSampleEntry(db, "Instigator");
        insertPenaltyInfoSampleEntry(db, "Interference");
        insertPenaltyInfoSampleEntry(db, "Kneeing");
        insertPenaltyInfoSampleEntry(db, "Leaving penalty bench too early");
        insertPenaltyInfoSampleEntry(db, "Leaving the crease (goalkeeper)");
        insertPenaltyInfoSampleEntry(db, "Participating in the play beyond the centre red line (goalkeeper)");
        insertPenaltyInfoSampleEntry(db, "Roughing");
        insertPenaltyInfoSampleEntry(db, "Slashing");
        insertPenaltyInfoSampleEntry(db, "Throwing puck towards opponent’s goal (goalkeeper)");
        insertPenaltyInfoSampleEntry(db, "Throwing stick");
        insertPenaltyInfoSampleEntry(db, "Tripping");
        insertPenaltyInfoSampleEntry(db, "Unsportsmanlike conduct");
    }

    public List<String> getAllPenaltyInfoNames() {
        List<String> labels = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Provider.PenaltyInfo.TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(cursor.getColumnIndex(Provider.PenaltyInfo.NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }

    private void insertInterruptionInfoSampleEntry(SQLiteDatabase db, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.InterruptionInfo.NAME, name);

        db.insert(Provider.InterruptionInfo.TABLE_NAME, null, contentValues);
    }

    private void insertIntoInterruptionInfo(SQLiteDatabase db) {
        insertInterruptionInfoSampleEntry(db, "Commercial break");
        insertInterruptionInfoSampleEntry(db, "Face-off");
        insertInterruptionInfoSampleEntry(db, "Icing");
        insertInterruptionInfoSampleEntry(db, "Offside");
        insertInterruptionInfoSampleEntry(db, "Timeout");
        insertInterruptionInfoSampleEntry(db, "Unspecified");
    }

    public List<String> getAllInterruptionInfoNames() {
        List<String> labels = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Provider.InterruptionInfo.TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(cursor.getColumnIndex(Provider.InterruptionInfo.NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }

    public List<String> getPlayersByTeamName(String teamName) {
        List<String> players = new ArrayList<>();

        String selectQuery = "SELECT p." + Provider.Player.NUMBER + ",p." +
                Provider.Player.FIRST_NAME + ",p." +
                Provider.Player.LAST_NAME + " FROM " +
                Provider.Team.TABLE_NAME + " JOIN " +
                Provider.TeamPlayer.TABLE_NAME + " ON " +
                Provider.Team.TABLE_NAME + "." +
                Provider.Team._ID + "=" +
                Provider.TeamPlayer.TABLE_NAME + "." +
                Provider.TeamPlayer.TEAM_ID + " JOIN " +
                Provider.Player.TABLE_NAME + " p ON " +
                Provider.TeamPlayer.TABLE_NAME + "." +
                Provider.TeamPlayer.PLAYER_ID + "=" +
                "p." + Provider.Player._ID + " WHERE " +
                Provider.Team.TABLE_NAME + "." + Provider.Team.FULL_NAME + "=\"" + teamName + "\"";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int number = cursor.getInt(cursor.getColumnIndex(Provider.Player.NUMBER));
                String firstName = cursor.getString(cursor.getColumnIndex(Provider.Player.FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(Provider.Player.LAST_NAME));

                String player = "(" + String.valueOf(number) + ") " + lastName.toUpperCase() + " " + firstName;
                players.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    public Player getPlayerById(int id) {

        String selectQuery = "SELECT * FROM " + Provider.Player.TABLE_NAME;
        Player player = new Player();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int playerId = cursor.getInt(cursor.getColumnIndex(Provider.Player._ID));
                if (playerId == id) {
                    player.setNumber(cursor.getInt(cursor.getColumnIndex(Provider.Player.NUMBER)));
                    player.setFirstName(cursor.getString(cursor.getColumnIndex(Provider.Player.FIRST_NAME)));
                    player.setLastName(cursor.getString(cursor.getColumnIndex(Provider.Player.LAST_NAME)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return player;
    }

    public Cursor getMatchesCursor() {
        String selectQuery = "SELECT " +
                "match." + Provider.Match._ID + ", " +
                "match." + Provider.Match.GAME_DATE + ", " +
                "t1.fullName AS " + Defaults.HOME_TEAM + ", " +
                "t1.shortName AS " + Defaults.HOME_SHORT + ", " +
                "match." + Provider.Match.SCORE_HOME + ", " +
                "t2.fullName AS " + Defaults.AWAY_TEAM + ", " +
                "t2.shortName AS " + Defaults.AWAY_SHORT + ", " +
                "match." + Provider.Match.SCORE_AWAY + ", " +
                "match." + Provider.Match.IS_FINISHED + ", " +
                "match." + Provider.Match.MATCH_IMAGE + " " +
                "FROM " + Provider.Match.TABLE_NAME + " " +
                "JOIN " + Provider.Team.TABLE_NAME + " t1 " +
                "ON match." + Provider.Match.HOME_TEAM_ID + "=t1._id " +
                "JOIN " + Provider.Team.TABLE_NAME + " t2 " +
                "ON match." + Provider.Match.AWAY_TEAM_ID + "=t2._id";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public List<MatchDatabase> getMatchesJoin() {
        List<MatchDatabase> matches = new ArrayList<>();

        String selectQuery = "SELECT " +
                "match." + Provider.Match._ID + ", " +
                "match." + Provider.Match.GAME_DATE + ", " +
                "match." + Provider.Match.HOME_TEAM_ID + ", " +
                "match." + Provider.Match.AWAY_TEAM_ID + ", " +
                "t1.fullName AS " + Defaults.HOME_TEAM + ", " +
                "t1.shortName AS " + Defaults.HOME_SHORT + ", " +
                "match." + Provider.Match.SCORE_HOME + ", " +
                "t2.fullName AS " + Defaults.AWAY_TEAM + ", " +
                "t2.shortName AS " + Defaults.AWAY_SHORT + ", " +
                "match." + Provider.Match.SCORE_AWAY + ", " +
                "match." + Provider.Match.IS_FINISHED + ", " +
                "match." + Provider.Match.MATCH_IMAGE + " " +
                "FROM " + Provider.Match.TABLE_NAME + " " +
                "JOIN " + Provider.Team.TABLE_NAME + " t1 " +
                "ON match." + Provider.Match.HOME_TEAM_ID + "=t1._id " +
                "JOIN " + Provider.Team.TABLE_NAME + " t2 " +
                "ON match." + Provider.Match.AWAY_TEAM_ID + "=t2._id";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int matchId = cursor.getInt(cursor.getColumnIndex(Provider.Match._ID));
                String gameDate = cursor.getString(cursor.getColumnIndex(Provider.Match.GAME_DATE));
                int homeId = cursor.getInt(cursor.getColumnIndex(Provider.Match.HOME_TEAM_ID));
                int awayId = cursor.getInt(cursor.getColumnIndex(Provider.Match.AWAY_TEAM_ID));
                int homeScore = cursor.getInt(cursor.getColumnIndex(Provider.Match.SCORE_HOME));
                int awayScore = cursor.getInt(cursor.getColumnIndex(Provider.Match.SCORE_AWAY));
                byte[] matchImage = cursor.getBlob(cursor.getColumnIndex(Provider.Match.MATCH_IMAGE));
                int isFinished = cursor.getInt(cursor.getColumnIndex(Provider.Match.IS_FINISHED));
                String homeName = cursor.getString(cursor.getColumnIndex(Defaults.HOME_TEAM));
                String awayName = cursor.getString(cursor.getColumnIndex(Defaults.AWAY_TEAM));
                String homeShort = cursor.getString(cursor.getColumnIndex(Defaults.HOME_SHORT));
                String awayShort = cursor.getString(cursor.getColumnIndex(Defaults.AWAY_SHORT));

                MatchDatabase match = new MatchDatabase(matchId, gameDate, homeId, homeName, homeShort, homeScore, awayId, awayName, awayShort, awayScore, isFinished, matchImage);

                matches.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return matches;
    }

    public int getIdByName(String firstName, String lastName) {
        String selectQuery = "SELECT * FROM " + Provider.Player.TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String fN = cursor.getString(cursor.getColumnIndex(Provider.Player.FIRST_NAME));
                String lN = cursor.getString(cursor.getColumnIndex(Provider.Player.LAST_NAME));

                if (firstName.equals(fN) && lastName.equals(lN)) {
                    return cursor.getInt(cursor.getColumnIndex(Provider.Player._ID));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return -1;
    }

    public List<Object> getTimeLineInfoByMatchId(int matchId) {
        List<Object> list = new ArrayList<>();

        String selectQuery =
                "SELECT " + "mg." + Provider.MatchGoal.MATCH_ID + " AS " + Provider.MatchGoal.MATCH_ID + ", " +
                        "mg." + Provider.MatchGoal.TEAM_ID + " AS " + Provider.MatchGoal.TEAM_ID + ", " +
                        "mg." + Provider.MatchGoal.PLAYER_ID + " AS " + Provider.MatchGoal.PLAYER_ID + ", " +
                        "mg." + Provider.MatchGoal.TIME + " AS " + Provider.MatchGoal.TIME + ", " +
                        "mg." + Provider.MatchGoal.PERIOD + " AS " + Provider.MatchGoal.PERIOD + ", " +
                        "mg." + Provider.MatchGoal.GOAL_TYPE + " AS " + Provider.MatchGoal.GOAL_TYPE + ", " +
                        "mg." + Provider.MatchGoal.ASSIST_ID_1 + " AS " + Provider.MatchGoal.ASSIST_ID_1 + ", " +
                        "mg." + Provider.MatchGoal.ASSIST_ID_2 + " AS " + Provider.MatchGoal.ASSIST_ID_2 + "," +
                        "null " + " AS " + Provider.MatchInterruption.INTERRUPTION_ID + ", " +
                        "null " + " AS " + Provider.MatchPenalty.PENALTY_ID + ", " +
                        "null " + " AS " + "\"" + Provider.InterruptionInfo.TABLE_NAME + "\"" + ", " +
                        "null " + " AS " + "\"" + Provider.PenaltyInfo.TABLE_NAME + "\"" + ", " +
                        "null " + " AS " + Provider.MatchPenalty.DURATION + " " +
                        "FROM " + Provider.MatchGoal.TABLE_NAME + " AS mg " +

                        "UNION " +

                        "SELECT " + "mp." + Provider.MatchPenalty.MATCH_ID + ", " +
                        "mp." + Provider.MatchPenalty.TEAM_ID + ", " +
                        "mp." + Provider.MatchPenalty.PLAYER_ID + ", " +
                        "mp." + Provider.MatchPenalty.TIME + ", " +
                        "mp." + Provider.MatchPenalty.PERIOD + ", " +
                        "null, " +
                        "null, " +
                        "null, " +
                        "null, " +
                        "mp. " + Provider.MatchPenalty.PENALTY_ID + ", " +
                        "null, " +
                        "p1." + Provider.PenaltyInfo.NAME + ", " +
                        "mp." + Provider.MatchPenalty.DURATION + " " +
                        "FROM " + Provider.MatchPenalty.TABLE_NAME + " AS mp " +
                        "LEFT OUTER JOIN " + Provider.PenaltyInfo.TABLE_NAME + " p1 " + "ON " +
                        "mp." + Provider.MatchPenalty.PENALTY_ID + "=" + "p1." + Provider.PenaltyInfo._ID + " " +

                        "UNION " +

                        "SELECT " + "mi." + Provider.MatchInterruption.MATCH_ID + ", " +
                        "mi." + Provider.MatchInterruption.TEAM_ID + ", " +
                        "null, " +
                        "mi." + Provider.MatchInterruption.TIME + ", " +
                        "mi." + Provider.MatchInterruption.PERIOD + ", " +
                        "null, " +
                        "null, " +
                        "null, " +
                        "mi." + Provider.MatchInterruption.INTERRUPTION_ID + ", " +
                        "null, " +
                        "i1." + Provider.InterruptionInfo.NAME + ", " +
                        "null" + ", " +
                        "null " +
                        "FROM " + Provider.MatchInterruption.TABLE_NAME + " AS mi " +
                        "LEFT OUTER JOIN " + Provider.InterruptionInfo.TABLE_NAME + " i1 " + "ON " +
                        "mi." + Provider.MatchInterruption.INTERRUPTION_ID + "=" + "i1." + Provider.InterruptionInfo._ID + " " +

                        "ORDER BY " + "mg." + Provider.MatchGoal.PERIOD + " ASC, "+ "mg." + Provider.MatchGoal.TIME;


        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int mtchId = cursor.getInt(cursor.getColumnIndex(Provider.MatchGoal.MATCH_ID));

                if (matchId == mtchId) {
                    int teamId = cursor.getInt(cursor.getColumnIndex(Provider.MatchGoal.TEAM_ID));
                    int playerId = cursor.getInt(cursor.getColumnIndex(Provider.MatchGoal.PLAYER_ID));
                    String time = cursor.getString(cursor.getColumnIndex(Provider.MatchGoal.TIME));
                    String period = cursor.getString(cursor.getColumnIndex(Provider.MatchGoal.PERIOD));
                    String goalType = cursor.getString(cursor.getColumnIndex(Provider.MatchGoal.GOAL_TYPE));
                    int assist1 = cursor.getInt(cursor.getColumnIndex(Provider.MatchGoal.ASSIST_ID_1));
                    int assist2 = cursor.getInt(cursor.getColumnIndex(Provider.MatchGoal.ASSIST_ID_2));
                    int interruptionId = cursor.getInt(cursor.getColumnIndex(Provider.MatchInterruption.INTERRUPTION_ID));
                    int penaltyId = cursor.getInt(cursor.getColumnIndex(Provider.MatchPenalty.PENALTY_ID));
                    String duration = cursor.getString(cursor.getColumnIndex(Provider.MatchPenalty.DURATION));

                    Log.i("databaza", "teamId: " + String.valueOf(teamId)
                            + " playerId: " + String.valueOf(playerId)
                            + " time: " + time
                            + " period: " + period
                            + " goalType: " + goalType
                            + " assist1: " + String.valueOf(assist1)
                            + " asssist2: " + String.valueOf(assist2)
                            + " interrId: " + String.valueOf(interruptionId)
                            + " penaltyId: " + String.valueOf(penaltyId));

                    // goal
                    if (interruptionId == 0 && penaltyId == 0) {
                        GoalDatabase goal = new GoalDatabase(mtchId, playerId, time, goalType, assist1, assist2, teamId, period);
                        list.add(goal);
                    }

                    // interruption
                    if (goalType == null && assist1 == 0 && assist2 == 0 && penaltyId == 0) {
                        InterruptionDatabase interruption = new InterruptionDatabase(mtchId, interruptionId, time, teamId, period);
                        list.add(interruption);
                    }

                    // penalty
                    if (goalType == null && assist1 == 0 && assist2 == 0 && interruptionId == 0) {
                        PenaltyDatabase penalty = new PenaltyDatabase(mtchId, penaltyId, playerId, duration, time, teamId, period);
                        list.add(penalty);
                    }

                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
