package sk.upjs.ics.android.matchwatch.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.upjs.ics.android.matchwatch.R;
import sk.upjs.ics.android.matchwatch.adapters.MainActivityArrayAdapter;
import sk.upjs.ics.android.matchwatch.loaders.MatchDatabaseLoader;
import sk.upjs.ics.android.matchwatch.entity.Match;
import sk.upjs.ics.android.matchwatch.dbsEntity.MatchDatabase;
import sk.upjs.ics.android.matchwatch.entity.Player;
import sk.upjs.ics.android.matchwatch.entity.Team;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;
import sk.upjs.ics.android.matchwatch.provider.Provider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MatchDatabase>> {

    private static final int MATCH_VALUE = 1;

    private ListView matchesListView;

    private List<MatchDatabase> matches;

    private DatabaseOpenHelper dbHelper;

    private MainActivityArrayAdapter mainActivityArrayAdapter;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        matchesListView = (ListView) findViewById(R.id.matchesListView);

        matches = new ArrayList<>();
        dbHelper = new DatabaseOpenHelper(this);
        matches = dbHelper.getMatchesJoin();

        mainActivityArrayAdapter = new MainActivityArrayAdapter(this, R.layout.list_row, matches);
        matchesListView.setAdapter(mainActivityArrayAdapter);
        getLoaderManager().initLoader(0, Bundle.EMPTY, this);

        //DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(this);
        //mainActivityCursorAdapter = new MainActivityCursorAdapter(this, dbHelper.getMatchesCursor(), Defaults.NO_FLAGS);
        //matchesListView.setAdapter(mainActivityCursorAdapter);

        // ak je databaza prazdna
        if (matchesListView.getAdapter().getCount() == 0) {
            matchesListView.setEmptyView(findViewById(R.id.noResultTextView));
        }

        // ak klikneme na polozku zoznamu
        matchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchDatabase match = matches.get(position);

                // vykona sa novy intent a preposle sa id vybraneho zapasu
                Intent intent = new Intent(MainActivity.this, DetailMatchActivity.class);
                intent.putExtra("SELECTED_MATCH", match);
                startActivity(intent);
            }
        });

        // pri dlhsom podrzani sa ZATIAL vymaze zo zoznamu
        matchesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String homeTeam = matches.get(position).getHomeName();
                final String awayTeam = matches.get(position).getAwayName();

                // aktivovanie multivyberu poloziek zoznamu
                //matchesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onDeleteComplete(int token, Object cookie, int result) {
                        Toast.makeText(MainActivity.this, homeTeam + " vs " + awayTeam + " - removed", Toast.LENGTH_SHORT).show();
                    }
                };

                long matchId = matches.get(position).getMatchId();
                Uri uri = ContentUris.withAppendedId(Provider.Match.CONTENT_URI, matchId);
                handler.startDelete(0, null, uri, null, null);

                matches.remove(position);
                mainActivityArrayAdapter.notifyDataSetChanged();

                return true;
            }
        });

        // floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ked sa klikneme na FloatingButton, prepneme sa do NewMatchActivity
                Intent intent = new Intent(MainActivity.this, NewMatchActivity.class);
                startActivityForResult(intent, MATCH_VALUE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (MATCH_VALUE): {
                if (resultCode == Activity.RESULT_OK) {
                    // vytiahneme data z NewMatchActivity
                    Match match = (Match) data.getSerializableExtra("MATCH_NEW");
                    //Team homeTeam = (Team) data.getSerializableExtra("HOME_TEAM");
                    //Team awayTeam = (Team) data.getSerializableExtra("AWAY_TEAM");
                    Team homeTeam = match.getHomeTeam();
                    Team awayTeam = match.getAwayTeam();

                    // ulozenie dat do dbs !!!
                    // HomeTeam
                    long homeTeamId = saveTeamToTeamsContentProvider(homeTeam);
                    // ukladanie do tabulky Player
                    for (Player p : homeTeam.getPlayers()) {
                        long playerId = savePlayerToPlayersContentProvider(p);
                        saveToTeamsPlayersContentProvider(homeTeamId, playerId);
                    }

                    // AwayTeam
                    long awayTeamId = saveTeamToTeamsContentProvider(awayTeam);
                    for (Player p : awayTeam.getPlayers()) {
                        long playerId = savePlayerToPlayersContentProvider(p);
                        saveToTeamsPlayersContentProvider(awayTeamId, playerId);
                    }

                    // Match
                    long matchId = saveMatchToMatchesContentProvider(match, homeTeamId, awayTeamId);
                }
                break;
            }
        }
    }

    // methods implemented by buttons in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // tlacidlo vymazat
        if (item.getItemId() == R.id.action_delete) {
            Toast.makeText(this, "You have to choose at least one item", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.action_list) {
            Intent intent = new Intent(MainActivity.this, PlayerListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveToTeamsPlayersContentProvider(long teamId, long playerId) {
        ContentValues contentValues = new ContentValues();

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                // nic
            }
        };

        contentValues.put(Provider.TeamPlayer.TEAM_ID, teamId);
        contentValues.put(Provider.TeamPlayer.PLAYER_ID, playerId);

        handler.startInsert(0, null, Provider.TeamPlayer.CONTENT_URI, contentValues);
    }

    public long saveTeamToTeamsContentProvider(Team team) {
        ContentValues contentValues = new ContentValues();

        String fullName = team.getFullName();
        String shortName = team.getShortName();

        contentValues.put(Provider.Team.FULL_NAME, fullName);
        contentValues.put(Provider.Team.SHORT_NAME, shortName);

        /*AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(MainActivity.this, "Match saved!", Toast.LENGTH_SHORT).show();
            }
        };

        handler.startInsert(0, null, Provider.Team.CONTENT_URI, contentValues);*/

        Uri result = getContentResolver().insert(Provider.Team.CONTENT_URI, contentValues);
        long id = Long.parseLong(result.getLastPathSegment());

        return id;
    }

    public long savePlayerToPlayersContentProvider(Player p) {
        ContentValues contentValues = new ContentValues();

        String firstName = p.getFirstName();
        String lastName = p.getLastName();
        String birthDate = p.getBirthDate()[0] + ", " + p.getBirthDate()[1] + ", " + p.getBirthDate()[2];
        int number = p.getNumber();
        String position = p.getPosition();
        String shoots = String.valueOf(p.getShoots());
        double height = p.getHeight();
        int weight = p.getWeight();
        String club = p.getClub();

        contentValues.put(Provider.Player.FIRST_NAME, firstName);
        contentValues.put(Provider.Player.LAST_NAME, lastName);
        contentValues.put(Provider.Player.BIRTH_DATE, birthDate);
        contentValues.put(Provider.Player.NUMBER, number);
        contentValues.put(Provider.Player.POSITION, position);
        contentValues.put(Provider.Player.SHOOTS, shoots);
        contentValues.put(Provider.Player.HEIGHT, height);
        contentValues.put(Provider.Player.WEIGHT, weight);
        contentValues.put(Provider.Player.CLUB, club);

        Uri result = getContentResolver().insert(Provider.Player.CONTENT_URI, contentValues);
        long id = Long.parseLong(result.getLastPathSegment());

        return id;
    }

    public long saveMatchToMatchesContentProvider(Match match, long homeTeamId, long awayTeamId) {
        ContentValues contentValues = new ContentValues();

        String gameDate = dateToString(match.getGameDate());
        int scoreHome = match.getScoreHome();
        int scoreAway = match.getScoreAway();
        int isFinished = 0;
        // byte[] image = match.getMatchImage().getBytes();

        contentValues.put(Provider.Match.GAME_DATE, gameDate);
        contentValues.put(Provider.Match.HOME_TEAM_ID, homeTeamId);
        contentValues.put(Provider.Match.AWAY_TEAM_ID, awayTeamId);
        contentValues.put(Provider.Match.SCORE_HOME, scoreHome);
        contentValues.put(Provider.Match.SCORE_AWAY, scoreAway);
        contentValues.put(Provider.Match.IS_FINISHED, isFinished);
        // contentValues.put(Provider.Match.MATCH_IMAGE, image);

        Uri result = getContentResolver().insert(Provider.Match.CONTENT_URI, contentValues);
        long id = Long.parseLong(result.getLastPathSegment());

        return id;
    }

    public String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return simpleDateFormat.format(date);
    }

    // methods implemented by LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<List<MatchDatabase>> onCreateLoader(int id, Bundle args) {
        MatchDatabaseLoader loader = new MatchDatabaseLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<MatchDatabase>> loader, List<MatchDatabase> data) {
        mainActivityArrayAdapter.clear();
        mainActivityArrayAdapter.addAll(data);
        mainActivityArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<MatchDatabase>> loader) {
        mainActivityArrayAdapter.clear();
        mainActivityArrayAdapter.notifyDataSetChanged();
    }

      /*@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this);
        int cursorId = cursorLoader.getId();

        if (cursorId == 0) {
            cursorLoader.setUri(Provider.Match.CONTENT_URI);
        }
        if (cursorId == 1) {
            cursorLoader.setUri(Provider.Team.CONTENT_URI);
        }

        return cursorLoader;
    }*/
}
