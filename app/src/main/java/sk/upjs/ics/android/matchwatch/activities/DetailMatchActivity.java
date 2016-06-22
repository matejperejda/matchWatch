package sk.upjs.ics.android.matchwatch.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.R;
import sk.upjs.ics.android.matchwatch.dbsEntity.GoalDatabase;
import sk.upjs.ics.android.matchwatch.dbsEntity.InterruptionDatabase;
import sk.upjs.ics.android.matchwatch.dbsEntity.MatchDatabase;
import sk.upjs.ics.android.matchwatch.dbsEntity.PenaltyDatabase;
import sk.upjs.ics.android.matchwatch.entity.Player;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;
import sk.upjs.ics.android.matchwatch.provider.Provider;
import sk.upjs.ics.android.matchwatch.timeline.TimeLineAdapter;
import sk.upjs.ics.android.matchwatch.timeline.TimeLineModel;

public class DetailMatchActivity extends AppCompatActivity {

    // finals
    private final int THOUSAND = 1000;
    private final int SIXTY = 60;
    private final int ALL_PERIODS = 4;
    private final long COUNT_DOWN_INTERVAL = 100; // 500

    private final int PERIOD_IN_MINUTES = 0; // ZADAT 20 MINUT !!!
    private final long PERIOD_IN_SECONDS = PERIOD_IN_MINUTES * SIXTY;
    private final long PERIOD_IN_MILLS = PERIOD_IN_SECONDS * THOUSAND;

    private Menu menu;

    // dialog views
    private View goalDialogView;
    private View penaltyDialogView;
    private View interruptDialogView;

    // progress bar
    private ProgressBar progressBar;

    // textviews
    private TextView minutesTime;
    private TextView secondsTime;
    private TextView period;

    private TextView homeTeamShortTextView;
    private TextView awayTeamShortTextView;
    private TextView homeTeamNameTextView;
    private TextView awayTeamNameTextView;
    private TextView homeTeamScoreTextView;
    private TextView awayTeamScoreTextView;

    // goal dialog widgets
    private Spinner teamGoalSpinner;
    private Spinner playerGoalSpinner;
    private Spinner assistOneGoalSpinner;
    private Spinner assistTwoGoalSpinner;
    private Spinner typeGoalSpinner;
    private NumberPicker numberMinuteGoalPicker;
    private NumberPicker numberSecondGoalPicker;

    // penalty dialog widgets
    private Spinner teamPenaltySpinner;
    private Spinner playerPenaltySpinner;
    private Spinner typePenaltySpinner;
    private Spinner durationPenaltySpinner;
    private NumberPicker numberMinutePenaltyPicker;
    private NumberPicker numberSecondPenaltyPicker;

    // interruption dialog widgets
    private Spinner teamInterruptSpinner;
    private Spinner typeInterruptSpinner;
    private NumberPicker numberMinuteInterruptPicker;
    private NumberPicker numberSecondInterruptPicker;

    // counter
    private CountDownTimer countDownTimer;
    private MyCount myCount;
    private MyCountOT myCountOT;

    private long secondsFromSpinner;
    private long minuteInMillsFromSpinner;

    private int COUNT_PERIODS = 1;

    private float secondsToFinish;

    private boolean isOT;
    private boolean isMatchOver;
    private boolean isPaused;

    // Timeline
    private RecyclerView timeLineRecyclerView;
    private TimeLineAdapter timeLineAdapter;
    private List<TimeLineModel> timeLineArrayList = new ArrayList<>();

    private String[] spinnerPenaltyNames;

    private String[] spinnerInterruptionNames;
    private String[] spinnerHomePlayerNames;
    private String[] spinnerAwayPlayerNames;

    private DatabaseOpenHelper dbHelper;

    private MatchDatabase selectedMatch;

    private List<GoalDatabase> goalsToSave;
    private List<InterruptionDatabase> interruptionsToSave;
    private List<PenaltyDatabase> penaltiesToSave;
    private List<Object> timeLineFromDatabase;

    public void getTimeLineFromDatabase() {
        timeLineFromDatabase = new ArrayList<>();

        dbHelper = new DatabaseOpenHelper(this);
        timeLineFromDatabase = dbHelper.getTimeLineInfoByMatchId(selectedMatch.getMatchId());

        for (Object object : timeLineFromDatabase) {
            // ak gol
            if (object.getClass().equals(GoalDatabase.class)) {
                GoalDatabase goal = (GoalDatabase) object;

                Player player = dbHelper.getPlayerById(goal.getPlayerId());
                Player assistant1 = dbHelper.getPlayerById(goal.getAssistIdSt());
                Player assistant2 = dbHelper.getPlayerById(goal.getAssistIdNd());

                String parsePlayer = "(" + player.getNumber() + ") " + player.getLastName().toUpperCase() + " " + player.getFirstName();
                String parseAssist1 = "(" + assistant1.getNumber() + ") " + assistant1.getLastName().toUpperCase() + " " + assistant1.getFirstName();
                String parseAssist2 = "(" + assistant2.getNumber() + ") " + assistant2.getLastName().toUpperCase() + " " + assistant2.getFirstName();

                String teamShort;
                if (selectedMatch.getHomeId() == goal.getTeamId())
                    teamShort = "(" + selectedMatch.getHomeShort() + ")";
                else
                    teamShort = "(" + selectedMatch.getAwayShort() + ")";

                setTimeLineItem(getResources().getString(R.string.goal) + ": ",
                        goal.getTime() + "(" + goal.getPeriod() + ")",
                        teamShort,
                        goal.getGoalType(),
                        parsePlayer,
                        parseAssist1,
                        parseAssist2,
                        null);

            }

            // ak prerusenie
            if (object.getClass().equals(InterruptionDatabase.class)) {
                InterruptionDatabase interruption = (InterruptionDatabase) object;

                String teamShort;
                if (selectedMatch.getHomeId() == interruption.getTeamId())
                    teamShort = "(" + selectedMatch.getHomeShort() + ")";
                else
                    teamShort = "(" + selectedMatch.getAwayShort() + ")";

                setTimeLineItem(typeInterruptSpinner.getItemAtPosition(interruption.getInterruptionId() - 1).toString() + ": ",
                        interruption.getTime() + "(" + interruption.getPeriod() + ")",
                        teamShort,
                        null,
                        null,
                        null,
                        null,
                        null);
            }

            // ak penalty
            if (object.getClass().equals(PenaltyDatabase.class)) {
                PenaltyDatabase penalty = (PenaltyDatabase) object;

                Player player = dbHelper.getPlayerById(penalty.getPlayerId());

                String parsePlayer = "(" + player.getNumber() + ") " + player.getLastName().toUpperCase() + " " + player.getFirstName();

                String teamShort;
                if (selectedMatch.getHomeId() == penalty.getTeamId())
                    teamShort = "(" + selectedMatch.getHomeShort() + ")";
                else
                    teamShort = "(" + selectedMatch.getAwayShort() + ")";

                setTimeLineItem(getResources().getString(R.string.penalty) + ": ",
                        penalty.getTime() + "(" + penalty.getPeriod() + ")",
                        teamShort,
                        typePenaltySpinner.getItemAtPosition(penalty.getPenaltyId() - 1).toString(),
                        parsePlayer,
                        null,
                        null,
                        penalty.getDuration());

            }
        }
    }

    public void savePenaltyToContentProvider() {
        ContentValues contentValues = new ContentValues();

        for (PenaltyDatabase penalty : penaltiesToSave) {
            int matchId = penalty.getMatchId();
            int penaltyId = penalty.getPenaltyId() + 1; // pridane +1
            int playerId = penalty.getPlayerId();
            String duration = penalty.getDuration();
            String time = penalty.getTime();
            int teamId = penalty.getTeamId();
            String period = penalty.getPeriod();

            contentValues.put(Provider.MatchPenalty.MATCH_ID, matchId);
            contentValues.put(Provider.MatchPenalty.PENALTY_ID, penaltyId);
            contentValues.put(Provider.MatchPenalty.PLAYER_ID, playerId);
            contentValues.put(Provider.MatchPenalty.DURATION, duration);
            contentValues.put(Provider.MatchPenalty.TIME, time);
            contentValues.put(Provider.MatchPenalty.TEAM_ID, teamId);
            contentValues.put(Provider.MatchPenalty.PERIOD, period);

            getContentResolver().insert(Provider.MatchPenalty.CONTENT_URI, contentValues);
        }
    }

    public void saveGoalToContentProvider() {
        ContentValues contentValues = new ContentValues();

        for (GoalDatabase goal : goalsToSave) {
            int matchId = goal.getMatchId();
            int playerId = goal.getPlayerId();
            String time = goal.getTime();
            String goalType = goal.getGoalType();
            int assist1 = goal.getAssistIdSt();
            int assist2 = goal.getAssistIdNd();
            int teamId = goal.getTeamId();
            String period = goal.getPeriod();

            contentValues.put(Provider.MatchGoal.MATCH_ID, matchId);
            contentValues.put(Provider.MatchGoal.PLAYER_ID, playerId);
            contentValues.put(Provider.MatchGoal.TIME, time);
            contentValues.put(Provider.MatchGoal.GOAL_TYPE, goalType);
            contentValues.put(Provider.MatchGoal.ASSIST_ID_1, assist1);
            contentValues.put(Provider.MatchGoal.ASSIST_ID_2, assist2);
            contentValues.put(Provider.MatchGoal.TEAM_ID, teamId);
            contentValues.put(Provider.MatchGoal.PERIOD, period);

            getContentResolver().insert(Provider.MatchGoal.CONTENT_URI, contentValues);
        }
    }

    public void saveInterruptionToContentProvider() {
        ContentValues contentValues = new ContentValues();

        for (InterruptionDatabase interruption : interruptionsToSave) {
            int matchId = interruption.getMatchId();
            int interId = interruption.getInterruptionId() + 1; // pridane + 1
            String time = interruption.getTime();
            int teamId = interruption.getTeamId();
            String period = interruption.getPeriod();

            contentValues.put(Provider.MatchInterruption.MATCH_ID, matchId);
            contentValues.put(Provider.MatchInterruption.INTERRUPTION_ID, interId);
            contentValues.put(Provider.MatchInterruption.TIME, time);
            contentValues.put(Provider.MatchInterruption.TEAM_ID, teamId);
            contentValues.put(Provider.MatchInterruption.PERIOD, period);

            getContentResolver().insert(Provider.MatchInterruption.CONTENT_URI, contentValues);
        }
    }

    public void saveGoalsPensInterToDatabase() {

        if (!interruptionsToSave.isEmpty()) {
            saveInterruptionToContentProvider();
        }

        if (!goalsToSave.isEmpty()) {
            saveGoalToContentProvider();
        }

        if (!penaltiesToSave.isEmpty()) {
            savePenaltyToContentProvider();
        }
    }

    public void saveMatchToDatabase() {

        selectedMatch.setIsFinished(1);
        saveMatchToContentProvider();
    }

    public void setActionButtonsEnabled(boolean value, boolean isPlay, boolean isOT) {
        menu.findItem(R.id.action_goal).setEnabled(value);
        menu.findItem(R.id.action_interruption).setEnabled(value);
        menu.findItem(R.id.action_penalty).setEnabled(value);

        if (isPlay) {
            menu.findItem(R.id.action_play).setEnabled(value);

            if (selectedMatch.getIsFinished() == 1) {
                if (isOT) {
                    period.setText(getResources().getString(R.string.period_ot));
                } else {
                    period.setText(getResources().getString(R.string.period_match_over));
                }
            }
        }
    }

    public void saveMatchToContentProvider() {
        ContentValues contentValues = new ContentValues();

        int matchId = selectedMatch.getMatchId();
        String gameDate = selectedMatch.getGameDate();
        int homeId = selectedMatch.getHomeId();
        int awayId = selectedMatch.getAwayId();
        int scoreHome = selectedMatch.getHomeScore();
        int scoreAway = selectedMatch.getAwayScore();
        int isFinished = selectedMatch.getIsFinished();
        // byte[] image = match.getMatchImage().getBytes();

        //contentValues.put(Provider.Match._ID, matchId);
        contentValues.put(Provider.Match.GAME_DATE, gameDate);
        contentValues.put(Provider.Match.HOME_TEAM_ID, homeId);
        contentValues.put(Provider.Match.AWAY_TEAM_ID, awayId);
        contentValues.put(Provider.Match.SCORE_HOME, scoreHome);
        contentValues.put(Provider.Match.SCORE_AWAY, scoreAway);
        contentValues.put(Provider.Match.IS_FINISHED, isFinished);
        // contentValues.put(Provider.Match.MATCH_IMAGE, image);

        long id = (long) matchId;

        getContentResolver().update(Provider.Match.CONTENT_URI, contentValues, Provider.Match._ID + "=" + id, Defaults.NO_SELECTION_ARGS);
    }

    public void refreshScore() {
        homeTeamScoreTextView.setText(String.valueOf(selectedMatch.getHomeScore()));
        awayTeamScoreTextView.setText(String.valueOf(selectedMatch.getAwayScore()));
    }

    public void setDialogViews() {

        // goal dialog init
        goalDialogView = getCustomDialogView(R.layout.goal_dialog_layout);
        teamGoalSpinner = (Spinner) goalDialogView.findViewById(R.id.teamGoalSpinner);
        playerGoalSpinner = (Spinner) goalDialogView.findViewById(R.id.playerGoalSpinner);
        assistOneGoalSpinner = (Spinner) goalDialogView.findViewById(R.id.assistOneGoalSpinner);
        assistTwoGoalSpinner = (Spinner) goalDialogView.findViewById(R.id.assistTwoGoalSpinner);
        typeGoalSpinner = (Spinner) goalDialogView.findViewById(R.id.typeGoalSpinner);
        numberMinuteGoalPicker = (NumberPicker) goalDialogView.findViewById(R.id.numberMinuteGoalPicker);
        numberSecondGoalPicker = (NumberPicker) goalDialogView.findViewById(R.id.numberSecondGoalPicker);

        // penalty dialog init
        penaltyDialogView = getCustomDialogView(R.layout.penalty_dialog_layout);
        teamPenaltySpinner = (Spinner) penaltyDialogView.findViewById(R.id.teamPenaltySpinner);
        playerPenaltySpinner = (Spinner) penaltyDialogView.findViewById(R.id.playerPenaltySpinner);
        typePenaltySpinner = (Spinner) penaltyDialogView.findViewById(R.id.typePenaltySpinner);
        durationPenaltySpinner = (Spinner) penaltyDialogView.findViewById(R.id.durationPenaltySpinner);
        numberMinutePenaltyPicker = (NumberPicker) penaltyDialogView.findViewById(R.id.numberMinutePenaltyPicker);
        numberSecondPenaltyPicker = (NumberPicker) penaltyDialogView.findViewById(R.id.numberSecondPenaltyPicker);

        // interruption dialog init
        interruptDialogView = getCustomDialogView(R.layout.interruption_dialog_layout);
        teamInterruptSpinner = (Spinner) interruptDialogView.findViewById(R.id.teamInterruptSpinner);
        typeInterruptSpinner = (Spinner) interruptDialogView.findViewById(R.id.typeInterruptSpinner);
        numberMinuteInterruptPicker = (NumberPicker) interruptDialogView.findViewById(R.id.numberMinuteInterruptPicker);
        numberSecondInterruptPicker = (NumberPicker) interruptDialogView.findViewById(R.id.numberSecondInterruptPicker);

        // pridane kvoli tahaniu timelinu z dbs
        setCustomSpinner(typeInterruptSpinner, spinnerInterruptionNames);
        setCustomSpinner(playerGoalSpinner, spinnerHomePlayerNames);
        setCustomSpinner(assistOneGoalSpinner, spinnerHomePlayerNames);
        setCustomSpinner(assistTwoGoalSpinner, spinnerHomePlayerNames);
        setCustomSpinner(typeGoalSpinner, new String[]{"N/A", "Short-handed", "Shootout"});
        setCustomSpinner(typePenaltySpinner, spinnerPenaltyNames);

    }

    public void setTextViewsData() {
        homeTeamShortTextView = (TextView) findViewById(R.id.homeTeamShortTextView);
        awayTeamShortTextView = (TextView) findViewById(R.id.awayTeamShortTextView);
        homeTeamNameTextView = (TextView) findViewById(R.id.homeTeamNameTextView);
        awayTeamNameTextView = (TextView) findViewById(R.id.awayTeamNameTextView);
        homeTeamScoreTextView = (TextView) findViewById(R.id.homeTeamScoreTextView);
        awayTeamScoreTextView = (TextView) findViewById(R.id.awayTeamScoreTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        minutesTime = (TextView) findViewById(R.id.minutes);
        secondsTime = (TextView) findViewById(R.id.seconds);
        period = (TextView) findViewById(R.id.period);

        homeTeamShortTextView.setText("(" + selectedMatch.getHomeShort() + ")");
        awayTeamShortTextView.setText("(" + selectedMatch.getAwayShort() + ")");
        homeTeamNameTextView.setText(selectedMatch.getHomeName());
        awayTeamNameTextView.setText(selectedMatch.getAwayName());
        homeTeamScoreTextView.setText(String.valueOf(selectedMatch.getHomeScore()));
        awayTeamScoreTextView.setText(String.valueOf(selectedMatch.getAwayScore()));
    }

    public void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        selectedMatch = (MatchDatabase) extras.get("SELECTED_MATCH");
    }

    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setActionButtonsEnabled(true, true, false); // pridane

            secondsToFinish = millisUntilFinished / THOUSAND;

            int minutes = (int) secondsToFinish / SIXTY;
            int seconds = (int) secondsToFinish % SIXTY;

            Log.i("minutes", String.valueOf(minutes));
            minutesTime.setText(getSingleValueOfTime(minutes));

            Log.i("seconds", String.valueOf(seconds));
            secondsTime.setText(getSingleValueOfTime(seconds));

            progressBar.setProgress(Math.abs((int) (millisUntilFinished - PERIOD_IN_MILLS)));
        }


        @Override
        public void onFinish() {
            // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
            menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailMatchActivity.this, R.drawable.ic_play_arrow_black_24dp));
            progressBar.setProgress((int) PERIOD_IN_MILLS);
            COUNT_PERIODS++;
            period.setText(period.getText() + " - " + getResources().getString(R.string.finish));

            setActionButtonsEnabled(false, false, false); // pridane
            if (COUNT_PERIODS == 4) {
                saveMatchToDatabase(); // pridane
                saveGoalsPensInterToDatabase(); // pridane
            }
        }
    }

    public class MyCountOT extends CountDownTimer {

        public MyCountOT(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            secondsToFinish = millisUntilFinished / THOUSAND;

            int minutes = (int) secondsToFinish / SIXTY;
            int seconds = (int) secondsToFinish % SIXTY;

            Log.i("minutes", String.valueOf(minutes));
            minutesTime.setText(getSingleValueOfTime(minutes));

            Log.i("seconds", String.valueOf(seconds));
            secondsTime.setText(getSingleValueOfTime(seconds));

            progressBar.setProgress(Math.abs((int) (millisUntilFinished - minuteInMillsFromSpinner)));
        }


        @Override
        public void onFinish() {
            // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_block_black_24dp));
            menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailMatchActivity.this, R.drawable.ic_block_black_24dp));
            menu.getItem(0).setTitle(getResources().getString(R.string.game_is_over));
            progressBar.setProgress((int) minuteInMillsFromSpinner);
            isMatchOver = true;
            period.setText(period.getText() + " - " + getResources().getString(R.string.finish));

            setActionButtonsEnabled(false, true, false); // pridane
            saveMatchToDatabase(); // pridane
            saveGoalsPensInterToDatabase(); // pridane
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_match);

        // polia do ktorych sa ukladaju priebezne udaje z timeline-u na ulozenie
        goalsToSave = new ArrayList<>();
        interruptionsToSave = new ArrayList<>();
        penaltiesToSave = new ArrayList<>();

        // prijatie vybraneho zapasu z MainActivity
        getDataFromIntent();

        getDatabaseDialogLabels();

        setTextViewsData();

        // nastavenie vychodzieho casu
        minutesTime.setText(getSingleValueOfTime(PERIOD_IN_MINUTES));
        secondsTime.setText(getSingleValueOfTime(0));

        initTimeLine();

        setDialogViews();

        if (selectedMatch.getIsFinished() == 1) {
            getTimeLineFromDatabase();
        }
    }

    @Override
    public void onBackPressed() {
        View parentLayout = findViewById(R.id.detailMatchLayoutID);

        // ak sa zapas este neskoncil, data sa neulozia
        if (selectedMatch.getIsFinished() == 0) {
            Snackbar snackbar = Snackbar.make(parentLayout, this.getResources().getString(R.string.match_aborted), Snackbar.LENGTH_LONG)
                    .setAction(this.getResources().getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
            snackbar.show();
        } else {
            // inak po skonceni su data uz ulozene v dbs a mozeme sa vratit spat
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_match, menu);
        this.menu = menu; // vytiahneme si menu do instancnej premennej, aby sme s nou mohli neskor pracovat

        // ak zapas skoncil, buttony sa zmenia na neaktivne
        if (selectedMatch.getIsFinished() == 1) {
            setActionButtonsEnabled(false, true, false);
        } else {
            setActionButtonsEnabled(false, false, false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // play/pause
        if (item.getItemId() == R.id.action_play) {
            //if (item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp).getConstantState())) {
            // nefunguje na API21
            if (item.getIcon().getConstantState().equals(ContextCompat.getDrawable(DetailMatchActivity.this, R.drawable.ic_play_arrow_black_24dp).getConstantState())) {

                item.setTitle(getResources().getString(R.string.play));

                // ak je "4" tretina, nastavime predlzenie
                if (COUNT_PERIODS == ALL_PERIODS) {
                    isOT = true;
                }

                // ikona play sa zmeni na pause
                item.setIcon(R.drawable.ic_pause_black_24dp);
                item.setTitle(getResources().getString(R.string.pause));

                // ak je casomiera zmrazena, opatovne ju nahodime
                if (!isPaused) {
                    // ak je predlzenie
                    if (isOT) {
                        final Spinner spinner = getSpinner(new String[]{"01:00 (demonštračné)", "05:00", "10:00", "20:00"});

                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedTime = (String) spinner.getSelectedItem();
                                String[] splitTime = selectedTime.split(":");

                                int minute = Integer.parseInt(splitTime[0]);
                                Log.i("minute", String.valueOf(minute));

                                // ZREDUKOVAT TENTO KOD A VYUZIT new MyCountOT() !!!
                                minuteInMillsFromSpinner = minute * SIXTY * THOUSAND;
                                secondsFromSpinner = minute * SIXTY;

                                setPeriod();
                                // aktivujeme novy countDownTimer s novou hodnotou casu
                                progressBar.setMax((int) minuteInMillsFromSpinner);
                                countDownTimer = new CountDownTimer(minuteInMillsFromSpinner, COUNT_DOWN_INTERVAL) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        setActionButtonsEnabled(true, true, true); // pridane

                                        secondsToFinish = (millisUntilFinished / THOUSAND);

                                        int minutes = (int) secondsToFinish / SIXTY;
                                        int seconds = (int) secondsToFinish % SIXTY;

                                        Log.i("minutes", String.valueOf(minutes));
                                        minutesTime.setText(getSingleValueOfTime(minutes));

                                        Log.i("seconds", String.valueOf(seconds));
                                        secondsTime.setText(getSingleValueOfTime(seconds));

                                        progressBar.setProgress(Math.abs((int) (millisUntilFinished - minuteInMillsFromSpinner)));
                                    }

                                    @Override
                                    public void onFinish() {
                                        // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_block_black_24dp));
                                        menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailMatchActivity.this, R.drawable.ic_block_black_24dp));
                                        menu.getItem(0).setTitle(getResources().getString(R.string.game_is_over));
                                        progressBar.setProgress((int) minuteInMillsFromSpinner);
                                        isMatchOver = true;
                                        period.setText(period.getText() + " - " + getResources().getString(R.string.finish));

                                        setActionButtonsEnabled(false, true, false); // pridane
                                        saveMatchToDatabase();
                                        saveGoalsPensInterToDatabase(); // pridane
                                    }
                                }.start();
                            }
                        };

                        new android.app.AlertDialog.Builder(this)
                                .setTitle(getResources().getString(R.string.ot_settings))
                                .setPositiveButton(getResources().getString(R.string.ok), listener)
                                .setCancelable(false)
                                .setView(spinner)
                                .show();

                    } else {
                        // ak nie je predlzenie
                        // odpocitavanie sa zacne normalne s casomierou 20 minut
                        startTimer();

                        // nastavi sa spravna perioda
                        setPeriod();
                    }
                } else {
                    // ak je casomiera pauznuta
                    if (!isOT) {
                        myCount = new MyCount((long) secondsToFinish * THOUSAND, COUNT_DOWN_INTERVAL);
                        myCount.start();
                    } else {
                        myCountOT = new MyCountOT((long) secondsToFinish * THOUSAND, COUNT_DOWN_INTERVAL);
                        myCountOT.start();
                    }
                    item.setIcon(R.drawable.ic_pause_black_24dp);
                    item.setTitle(getResources().getString(R.string.pause));
                    isPaused = false;
                }
                return true;
            } else {
                // ak este zapas neskoncil, znamena to ze klikame na pause a chceme prerusit zapas
                if (!isMatchOver) {
                    countDownTimer.cancel();

                    if (myCountOT != null) {
                        myCountOT.cancel();
                    }
                    if (myCount != null) {
                        myCount.cancel();
                    }
                    isPaused = true;
                    // ikona pause sa zmeni na play
                    item.setIcon(R.drawable.ic_play_arrow_black_24dp);
                    item.setTitle(getResources().getString(R.string.play));
                } else {
                    // ak zapas uz skoncil
                    Toast.makeText(this, getResources().getString(R.string.game_is_over), Toast.LENGTH_SHORT).show();
                }
            }
        }

        // goal action bar button
        if (item.getItemId() == R.id.action_goal) {
            //pauseTime();
            setGoalDialogWidgets();
        }

        // penalty action bar button
        if (item.getItemId() == R.id.action_penalty) {
            //pauseTime();
            setPenaltyDialogWidgets();
        }

        // interruption action bar button
        if (item.getItemId() == R.id.action_interruption) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //pauseTime();
                    setInterruptDialogWidgets();
                }
            };

            new android.app.AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.add_interuption))
                    .setMessage(getResources().getString(R.string.really_pause_time))
                    .setPositiveButton(getResources().getString(R.string.yes), listener)
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // cancel
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDatabaseDialogLabels() {
        dbHelper = new DatabaseOpenHelper(this);

        // tahanie penalt z dbs
        spinnerPenaltyNames = new String[dbHelper.getAllPenaltyInfoNames().size()];
        spinnerPenaltyNames = dbHelper.getAllPenaltyInfoNames().toArray(spinnerPenaltyNames);

        // tahanie nazvov preruseni z dbs
        spinnerInterruptionNames = new String[dbHelper.getAllInterruptionInfoNames().size()];
        spinnerInterruptionNames = dbHelper.getAllInterruptionInfoNames().toArray(spinnerInterruptionNames);

        // tahanie aktualnych rosterov hracov z dbs pre home a away team
        spinnerHomePlayerNames = new String[dbHelper.getPlayersByTeamName(selectedMatch.getHomeName()).size()];
        spinnerHomePlayerNames = dbHelper.getPlayersByTeamName(selectedMatch.getHomeName()).toArray(spinnerHomePlayerNames);

        spinnerAwayPlayerNames = new String[dbHelper.getPlayersByTeamName(selectedMatch.getAwayName()).size()];
        spinnerAwayPlayerNames = dbHelper.getPlayersByTeamName(selectedMatch.getAwayName()).toArray(spinnerAwayPlayerNames);
    }

    private void setInterruptDialogWidgets() {
        setCustomSpinner(teamInterruptSpinner, new String[]{selectedMatch.getHomeShort(), selectedMatch.getAwayShort()});
        setCustomSpinner(typeInterruptSpinner, spinnerInterruptionNames);
        if (!isOT) {
            setDialogNumberPicker(numberMinuteInterruptPicker, numberSecondInterruptPicker, PERIOD_IN_MINUTES);
        } else {
            setDialogNumberPicker(numberMinuteInterruptPicker, numberSecondInterruptPicker, (int) (secondsFromSpinner / SIXTY));
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Vlozenie dat do timeline-u
                String team = teamInterruptSpinner.getSelectedItem().toString();
                String type = typeInterruptSpinner.getSelectedItem().toString();
                // String time = getSingleValueOfTime(numberMinuteInterruptPicker.getValue()) + " : " + getSingleValueOfTime(numberSecondInterruptPicker.getValue());
                int[] timeArray = timeBackConverse(numberMinuteInterruptPicker.getValue(), numberSecondInterruptPicker.getValue());
                String time = getSingleValueOfTime(timeArray[0]) + ":" + getSingleValueOfTime(timeArray[1]);
                setTimeLineItem(type + ":", time + "(" + period.getText().toString() + ")", team, null, null, null, null, null);

                int teamId = -1;
                if (teamInterruptSpinner.getSelectedItemPosition() == 0) {
                    teamId = selectedMatch.getHomeId();
                } else {
                    teamId = selectedMatch.getAwayId();
                }
                InterruptionDatabase interruptionDatabase = new InterruptionDatabase(selectedMatch.getMatchId(),
                        typeInterruptSpinner.getSelectedItemPosition(),
                        time,
                        teamId,
                        period.getText().toString());
                interruptionsToSave.add(interruptionDatabase);

                // potrebne vzdy na konci pre uzatvorenie layout-u !!!
                closeDialogView(interruptDialogView);
            }
        };

        new android.app.AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.add_interuption))
                .setPositiveButton(getResources().getString(R.string.ok), listener)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    // ak stlaceny back button
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        closeDialogView(interruptDialogView);
                    }
                })
                .setView(interruptDialogView)
                .show();
    }

    private void setPenaltyDialogWidgets() {
        setCustomSpinner(teamPenaltySpinner, new String[]{selectedMatch.getHomeShort(), selectedMatch.getAwayShort()});
        teamPenaltySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                        case 0:
                            setCustomSpinner(playerPenaltySpinner, spinnerHomePlayerNames);
                            break;
                        case 1:
                            setCustomSpinner(playerPenaltySpinner, spinnerAwayPlayerNames);
                            break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        setCustomSpinner(typePenaltySpinner, spinnerPenaltyNames);
        setCustomSpinner(durationPenaltySpinner, new String[]{"2:00", "5:00", "10:00"});
        if (!isOT) {
            setDialogNumberPicker(numberMinutePenaltyPicker, numberSecondPenaltyPicker, PERIOD_IN_MINUTES);
        } else {
            setDialogNumberPicker(numberMinutePenaltyPicker, numberSecondPenaltyPicker, (int) (secondsFromSpinner / SIXTY));
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // hodi vsetky udaje do timeline-u
                String team = teamPenaltySpinner.getSelectedItem().toString();
                String[] durationArray = (durationPenaltySpinner.getSelectedItem().toString()).split(":");
                String durationMinute = getSingleValueOfTime(Integer.parseInt(durationArray[0]));
                String durationSecond = getSingleValueOfTime(Integer.parseInt(durationArray[1]));
                String duration = durationMinute + ":" + durationSecond;
                String type = typePenaltySpinner.getSelectedItem().toString();
                String player = playerPenaltySpinner.getSelectedItem().toString();
                //String time = getSingleValueOfTime(numberMinutePenaltyPicker.getValue()) + ":" + getSingleValueOfTime(numberSecondPenaltyPicker.getValue());
                int[] timeArray = timeBackConverse(numberMinutePenaltyPicker.getValue(), numberSecondPenaltyPicker.getValue());
                String time = getSingleValueOfTime(timeArray[0]) + ":" + getSingleValueOfTime(timeArray[1]);
                setTimeLineItem(getResources().getString(R.string.penalty) + ": ", time + "(" + period.getText().toString() + ")", team, type, player, null, null, duration);

                String[] playerInfo = player.split(" ");
                int teamId = -1;
                if (teamPenaltySpinner.getSelectedItemPosition() == 0) {
                    teamId = selectedMatch.getHomeId();
                } else {
                    teamId = selectedMatch.getAwayId();
                }
                PenaltyDatabase penaltyDatabase = new PenaltyDatabase(selectedMatch.getMatchId(),
                        typePenaltySpinner.getSelectedItemPosition(),
                        dbHelper.getIdByName(playerInfo[2], playerInfo[1]),
                        duration,
                        time,
                        teamId,
                        period.getText().toString());
                penaltiesToSave.add(penaltyDatabase);

                // potrebne vzdy na konci pre uzatvorenie layout-u !!!
                closeDialogView(penaltyDialogView);
            }
        };

        new android.app.AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.add_penalty))
                .setPositiveButton(getResources().getString(R.string.ok), listener)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    // ak stlaceny back button
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        closeDialogView(penaltyDialogView);
                    }
                })
                .setView(penaltyDialogView)
                .show();
    }

    private void setGoalDialogWidgets() {
        setCustomSpinner(teamGoalSpinner, new String[]{selectedMatch.getHomeShort(), selectedMatch.getAwayShort()});

        teamGoalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        setCustomSpinner(playerGoalSpinner, spinnerHomePlayerNames);
                        setCustomSpinner(assistOneGoalSpinner, spinnerHomePlayerNames);
                        setCustomSpinner(assistTwoGoalSpinner, spinnerHomePlayerNames);
                        if (typeGoalSpinner.getSelectedItemPosition() == 2) {
                            setCustomSpinner(assistOneGoalSpinner, new String[]{""});
                            assistOneGoalSpinner.setEnabled(false);
                            setCustomSpinner(assistTwoGoalSpinner, new String[]{""});
                            assistTwoGoalSpinner.setEnabled(false);
                        }
                        break;
                    case 1:
                        setCustomSpinner(playerGoalSpinner, spinnerAwayPlayerNames);
                        setCustomSpinner(assistOneGoalSpinner, spinnerAwayPlayerNames);
                        setCustomSpinner(assistTwoGoalSpinner, spinnerAwayPlayerNames);
                        if (typeGoalSpinner.getSelectedItemPosition() == 2) {
                            setCustomSpinner(assistOneGoalSpinner, new String[]{""});
                            assistOneGoalSpinner.setEnabled(false);
                            setCustomSpinner(assistTwoGoalSpinner, new String[]{""});
                            assistTwoGoalSpinner.setEnabled(false);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        setCustomSpinner(typeGoalSpinner, new String[]{"N/A", "Short-handed", "Shootout"});
        typeGoalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 2:
                        setCustomSpinner(assistOneGoalSpinner, new String[]{""});
                        assistOneGoalSpinner.setEnabled(false);
                        setCustomSpinner(assistTwoGoalSpinner, new String[]{""});
                        assistTwoGoalSpinner.setEnabled(false);
                        break;

                    default:
                        assistOneGoalSpinner.setEnabled(true);
                        assistTwoGoalSpinner.setEnabled(true);
                        if (teamGoalSpinner.getSelectedItemPosition() == 0) {
                            setCustomSpinner(assistOneGoalSpinner, spinnerHomePlayerNames);
                            setCustomSpinner(assistTwoGoalSpinner, spinnerHomePlayerNames);
                        } else {
                            setCustomSpinner(assistOneGoalSpinner, spinnerAwayPlayerNames);
                            setCustomSpinner(assistTwoGoalSpinner, spinnerAwayPlayerNames);
                        }

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        if (!isOT) {
            setDialogNumberPicker(numberMinuteGoalPicker, numberSecondGoalPicker, PERIOD_IN_MINUTES);

        } else {
            setDialogNumberPicker(numberMinuteGoalPicker, numberSecondGoalPicker, (int) (secondsFromSpinner / SIXTY));
        }


        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // hodi vsetky udaje do timeline-u
                // String time = getSingleValueOfTime(numberMinuteGoalPicker.getValue()) + ":" + getSingleValueOfTime(numberSecondGoalPicker.getValue());
                int[] timeArray = timeBackConverse(numberMinuteGoalPicker.getValue(), numberSecondGoalPicker.getValue());
                String time = getSingleValueOfTime(timeArray[0]) + ":" + getSingleValueOfTime(timeArray[1]);
                String team = teamGoalSpinner.getSelectedItem().toString();
                String type = typeGoalSpinner.getSelectedItem().toString();
                String player = playerGoalSpinner.getSelectedItem().toString();
                String assist1 = assistOneGoalSpinner.getSelectedItem().toString();
                String assist2 = assistTwoGoalSpinner.getSelectedItem().toString();

                // vypis na timeline
                setTimeLineItem(getResources().getString(R.string.goal) + ": ", time + "(" + period.getText().toString() + ")", team, type, player, assist1, assist2, null);

                // ak je vybraty home team
                if (teamGoalSpinner.getSelectedItemPosition() == 0) {
                    selectedMatch.setHomeScore(selectedMatch.getHomeScore() + 1);
                } else {
                    selectedMatch.setAwayScore(selectedMatch.getAwayScore() + 1);
                }
                refreshScore();

                String[] playerInfo = player.split(" ");
                String[] assist1Info = assist1.split(" ");
                String[] assist2Info = assist2.split(" ");
                int teamId = -1;
                if (teamGoalSpinner.getSelectedItemPosition() == 0) {
                    teamId = selectedMatch.getHomeId();
                } else {
                    teamId = selectedMatch.getAwayId();
                }

                GoalDatabase goalDatabase;
                if (!assistOneGoalSpinner.isEnabled() && !assistTwoGoalSpinner.isEnabled()) {
                    goalDatabase = new GoalDatabase(selectedMatch.getMatchId(),
                            dbHelper.getIdByName(playerInfo[2], playerInfo[1]),
                            time,
                            type,
                            -1,
                            -1, teamId,
                            period.getText().toString());
                } else {
                    // ulozenie golov do docasneho listu
                    goalDatabase = new GoalDatabase(selectedMatch.getMatchId(),
                            dbHelper.getIdByName(playerInfo[2], playerInfo[1]),
                            time,
                            type,
                            dbHelper.getIdByName(assist1Info[2], assist1Info[1]),
                            dbHelper.getIdByName(assist2Info[2], assist2Info[1]),
                            teamId,
                            period.getText().toString());
                }

                goalsToSave.add(goalDatabase);

                // potrebne vzdy na konci pre uzatvorenie layout-u !!!
                closeDialogView(goalDialogView);
            }
        };

        new android.app.AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.add_goal))
                .setPositiveButton(getResources().getString(R.string.ok), listener)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    // ak stlaceny back button
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        closeDialogView(goalDialogView);
                    }
                })
                .setView(goalDialogView)
                .show();
    }

    private void setDialogNumberPicker(NumberPicker numberPickerMinutes, NumberPicker numberPickerSeconds, int maxMinute) {
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(maxMinute - 1);
        numberPickerMinutes.setValue(Integer.parseInt(minutesTime.getText().toString()));
        numberPickerMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        // hack, ktory zabezpeci, ze rendering 00 bude fungovat
        try {
            Method method = numberPickerMinutes.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(numberPickerMinutes, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);
        numberPickerSeconds.setValue(Integer.parseInt(secondsTime.getText().toString()));
        numberPickerSeconds.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
    }

    private void pauseTime() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (myCount != null) {
            myCount.cancel();
        }
        if (myCountOT != null) {
            myCountOT.cancel();
        }
    }

    /**
     * Uzatvara dialog view.
     *
     * @param view
     */
    private void closeDialogView(View view) {
        // potrebne na uzatvorenie layout-u !!!
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public View getCustomDialogView(int layout) {
        LayoutInflater inflater = this.getLayoutInflater();
        return inflater.inflate(layout, null);
    }

    private Spinner getSpinner(String[] values) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(DetailMatchActivity.this, android.R.layout.simple_spinner_dropdown_item, values);
        Spinner spinner = new Spinner(DetailMatchActivity.this);

        spinner.setLayoutParams(new LinearLayout
                .LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        return spinner;
    }

    private void setCustomSpinner(Spinner spinner, String[] values) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, values);

        spinner.setLayoutParams(new LinearLayout
                .LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

        spinner.setAdapter(adapter);
    }

    private void startTimer() {
        progressBar.setMax((int) PERIOD_IN_MILLS);

        countDownTimer = new CountDownTimer(PERIOD_IN_MILLS, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                setActionButtonsEnabled(true, true, false); // pridane

                secondsToFinish = (millisUntilFinished / THOUSAND);

                int minutes = (int) secondsToFinish / SIXTY;
                int seconds = (int) secondsToFinish % SIXTY;

                Log.i("minutes", String.valueOf(minutes));
                minutesTime.setText(getSingleValueOfTime(minutes));

                Log.i("seconds", String.valueOf(seconds));
                secondsTime.setText(getSingleValueOfTime(seconds));

                progressBar.setProgress(Math.abs((int) (millisUntilFinished - PERIOD_IN_MILLS)));
            }

            @Override
            public void onFinish() {
                // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailMatchActivity.this, R.drawable.ic_play_arrow_black_24dp));
                menu.getItem(0).setTitle(getResources().getString(R.string.play));

                progressBar.setProgress((int) PERIOD_IN_MILLS);
                COUNT_PERIODS++;
                period.setText(period.getText() + " - " + getResources().getString(R.string.finish));

                setActionButtonsEnabled(false, false, false); // pridane
                if (COUNT_PERIODS == 4) {
                    saveMatchToDatabase(); // pridane
                    saveGoalsPensInterToDatabase(); // pridane
                }

            }
        }.start();
    }

    private void setTimeLineItem(String notif, String time, String team, String type, String player, String assist1, String assist2, String duration) {
        timeLineRecyclerView = (RecyclerView) findViewById(R.id.timeLineRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailMatchActivity.this);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);

        TimeLineModel model = new TimeLineModel();
        model.setNotif(notif);
        model.setTime(time);
        model.setTeam(team);
        model.setType(type);
        model.setPlayer(player);
        model.setAssist1(assist1);
        model.setAssist2(assist2);
        model.setDuration(duration);
        timeLineArrayList.add(model);

        //TODO: doriesit zmenu farby markerov podla aktualnej notifikacie
        timeLineAdapter = new TimeLineAdapter(timeLineArrayList);
        timeLineRecyclerView.setAdapter(timeLineAdapter);
    }

    private void initTimeLine() {
        timeLineRecyclerView = (RecyclerView) findViewById(R.id.timeLineRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);

        timeLineAdapter = new TimeLineAdapter(timeLineArrayList);
        timeLineRecyclerView.setAdapter(timeLineAdapter);
    }

    /**
     * Nastravi spravne TextView s prislusnou periodou.
     */
    private void setPeriod() {
        switch (COUNT_PERIODS) {
            case 1:
                period.setText(getResources().getString(R.string.period_1));
                break;
            case 2:
                period.setText(getResources().getString(R.string.period_2));
                break;
            case 3:
                period.setText(getResources().getString(R.string.period_3));
                break;
            case 4:
                period.setText(getResources().getString(R.string.period_ot));
                break;
        }
    }

    /**
     * Z jednocifernej int hodnoty spravi String hodnotu s nulou pred pre krajsi vypis casomiery.
     *
     * @param value
     * @return
     */
    private String getSingleValueOfTime(int value) {

        switch (value) {
            case 9: {
                return "09";
            }
            case 8: {
                return "08";
            }
            case 7: {
                return "07";
            }
            case 6: {
                return "06";
            }
            case 5: {
                return "05";
            }
            case 4: {
                return "04";
            }
            case 3: {
                return "03";
            }
            case 2: {
                return "02";
            }
            case 1: {
                return "01";
            }
            case 0: {
                return "00";
            }
        }

        return String.valueOf(value);
    }

    private int[] timeBackConverse(int minutes, int seconds) {
        int[] array = new int[2];

        long secs = (minutes * SIXTY) + seconds;
        if (!isOT) {
            array[0] = (int) (((PERIOD_IN_MINUTES * SIXTY) - secs) / SIXTY);
            array[1] = (int) (((PERIOD_IN_MINUTES * SIXTY) - secs) % SIXTY);
        } else {
            array[0] = (int) ((secondsFromSpinner - secs) / SIXTY);
            array[1] = (int) ((secondsFromSpinner - secs) % SIXTY);
        }
        return array;
    }
}
