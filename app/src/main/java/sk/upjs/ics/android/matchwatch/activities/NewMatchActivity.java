package sk.upjs.ics.android.matchwatch.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.R;
import sk.upjs.ics.android.matchwatch.entity.Match;
import sk.upjs.ics.android.matchwatch.entity.Person;
import sk.upjs.ics.android.matchwatch.entity.Team;
import sk.upjs.ics.android.matchwatch.parser.RosterParser;

public class NewMatchActivity extends AppCompatActivity {

    private static final int FILE_CODE = 1;

    private ProgressDialog progressDialog;

    private EditText homeTeamEditText;
    private EditText awayTeamEditText;

    private EditText shortHomeTeamEditText;
    private EditText shortAwayTeamEditText;

    private EditText homeSourceEditText;
    private EditText awaySourceEditText;

    private EditText refereeOneEditText;
    private EditText refereeTwoEditText;
    private EditText linesmanOneEditText;
    private EditText linesmanTwoEditText;

    private boolean isHomeSource;
    private boolean isAwaySource;

    private RosterParser homeRosterParser;
    private RosterParser awayRosterParser;

    private List<Person> referees;
    private List<Person> linesmen;

    private Team homeTeam;
    private Team awayTeam;

    private Match match;

    /**
     * Vnorena trieda, ktora zabezpecuje nacitanie rosterov hracov na pozadi,
     * zatial co do popredia dava ProgressDialog.
     * Vynimky su osetrene a v nutnom pripade vyhadyuju AlertDialogy.
     */
    public class BackgroundRun extends AsyncTask<Void, Void, Void> {

        private boolean isAllRight;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getRosters();
                isAllRight = true;
            } catch (Exception e) {
                progressDialog.cancel();
                isAllRight = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isAllRight) {
                progressDialog.cancel();

                new AlertDialog.Builder(NewMatchActivity.this)
                        .setTitle(NewMatchActivity.this.getResources().getString(R.string.done))
                        .setCancelable(false)
                        .setMessage(NewMatchActivity.this.getResources().getString(R.string.rosters_loaded_success))
                        .setPositiveButton(NewMatchActivity.this.getResources().getString(R.string.cont), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue

                                // informacie o timoch
                                String homeTeamName = homeTeamEditText.getText().toString().trim();
                                String homeShortName = shortHomeTeamEditText.getText().toString().trim();
                                homeTeam = new Team(homeTeamName, homeShortName, homeRosterParser.getPlayers());

                                String awayTeamName = awayTeamEditText.getText().toString().trim();
                                String awayShortName = shortAwayTeamEditText.getText().toString().trim();
                                awayTeam = new Team(awayTeamName, awayShortName, awayRosterParser.getPlayers());

                                getLinesmenRefereeToList();

                                // informacie o zapase
                                Date actualDate = Calendar.getInstance().getTime();
                                match = new Match(actualDate, homeTeam, awayTeam, 0, 0, referees, linesmen);

                                // ulozime vysledky
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("MATCH_NEW", match);
                                //resultIntent.putExtra("HOME_TEAM", homeTeam);
                                //resultIntent.putExtra("AWAY_TEAM", awayTeam);
                                setResult(Activity.RESULT_OK, resultIntent);

                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            } else {
                new AlertDialog.Builder(NewMatchActivity.this)
                        .setTitle(NewMatchActivity.this.getResources().getString(R.string.wrong_input))
                        .setMessage(NewMatchActivity.this.getResources().getString(R.string.support_format))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        getAllIds();

        homeRosterParser = new RosterParser();
        awayRosterParser = new RosterParser();

        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this.getResources().getString(R.string.loading_team_rosters));
    }

    @Override
    protected void onResume() {
        super.onResume();

        isHomeSource = false;
        isAwaySource = false;
    }

    public void getAllIds() {
        // home/away team names
        homeTeamEditText = (EditText) findViewById(R.id.homeTeamEditText);
        awayTeamEditText = (EditText) findViewById(R.id.awayTeamEditText);

        // home/away shorts
        shortHomeTeamEditText = (EditText) findViewById(R.id.shortHomeTeamEditText);
        shortAwayTeamEditText = (EditText) findViewById(R.id.shortAwayTeamEditText);

        // home/away roster source files
        homeSourceEditText = (EditText) findViewById(R.id.homeSourceEditText);
        awaySourceEditText = (EditText) findViewById(R.id.awaySourceEditText);

        // referee names
        refereeOneEditText = (EditText) findViewById(R.id.refereeOneEditText);
        refereeTwoEditText = (EditText) findViewById(R.id.refereeTwoEditText);
        linesmanOneEditText = (EditText) findViewById(R.id.linesmanOneEditText);
        linesmanTwoEditText = (EditText) findViewById(R.id.linesmanTwoEditText);
    }

    public void setFilePicker(Intent intent) {
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(intent, FILE_CODE);
    }

    public void setRosterSourceEditText(String source) {
        if (isHomeSource) {
            homeSourceEditText.setText(source);
            Toast.makeText(this, "Home roster:\n" + source, Toast.LENGTH_LONG).show();
            Log.i("Path home roster", source);
        } else if (isAwaySource) {
            awaySourceEditText.setText(source);
            Toast.makeText(this, "Away roster:\n" + source, Toast.LENGTH_LONG).show();
            Log.i("Path away roster", source);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {

                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();

                            // Do something with the URI
                            setRosterSourceEditText(uri.getPath());
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);

                            // Do something with the URI
                            setRosterSourceEditText(uri.getPath());
                        }
                    }
                }

            } else {
                Uri uri = data.getData();

                // Do something with the URI
                setRosterSourceEditText(uri.getPath());
            }
        }
    }

    public void onClickHomeRosterButton(View view) {
        // otvorenie a najdenie cesty pre roster
        isHomeSource = true;
        Intent intent = new Intent(this, FilePickerActivity.class);
        setFilePicker(intent);
    }

    public void onClickAwayRosterButton(View view) {
        // otvorenie a najdenie cesty pre roster
        isAwaySource = true;
        Intent intent = new Intent(this, FilePickerActivity.class);
        setFilePicker(intent);
    }

    public boolean isFilled() {
        String strHome = homeTeamEditText.getText().toString().trim();
        String strAway = awayTeamEditText.getText().toString().trim();

        String strHomeShort = shortHomeTeamEditText.getText().toString().trim();
        String strAwayShort = shortAwayTeamEditText.getText().toString().trim();

        String strHomeSrc = homeSourceEditText.getText().toString();
        String strAwaySrc = awaySourceEditText.getText().toString();

        String strRefOne = refereeOneEditText.getText().toString().trim();
        String strRefTwo = refereeTwoEditText.getText().toString().trim();
        String strLineOne = linesmanOneEditText.getText().toString().trim();
        String strLineTwo = linesmanTwoEditText.getText().toString().trim();

        if (strHome.equals("") || strAway.equals("") || strHomeShort.equals("") || strAwayShort.equals("") ||
                strHomeSrc.equals("") || strAwaySrc.equals("") || strRefOne.equals("") ||
                strRefTwo.equals("") || strLineOne.equals("") || strLineTwo.equals("")) {
            return false;
        }

        return true;
    }

    public boolean isFilledAtLeastOne() {
        String strHome = homeTeamEditText.getText().toString().trim();
        String strAway = awayTeamEditText.getText().toString().trim();

        String strHomeShort = shortHomeTeamEditText.getText().toString().trim();
        String strAwayShort = shortAwayTeamEditText.getText().toString().trim();

        String strHomeSrc = homeSourceEditText.getText().toString();
        String strAwaySrc = awaySourceEditText.getText().toString();

        String strRefOne = refereeOneEditText.getText().toString().trim();
        String strRefTwo = refereeTwoEditText.getText().toString().trim();
        String strLineOne = linesmanOneEditText.getText().toString().trim();
        String strLineTwo = linesmanTwoEditText.getText().toString().trim();

        if (!(strHome.equals("")) || !(strAway.equals("")) || !(strHomeShort.equals("")) || !(strAwayShort.equals("")) ||
                !(strHomeSrc.equals("")) || !(strAwaySrc.equals("")) || !(strRefOne.equals("")) ||
                !(strRefTwo.equals("")) || !(strLineOne.equals("")) || !(strLineTwo.equals(""))) {
            return true;
        }

        return false;
    }

    public void getRosters() {
        homeRosterParser.loadTeamRosterFromFile(homeSourceEditText.getText().toString());
        awayRosterParser.loadTeamRosterFromFile(awaySourceEditText.getText().toString());
    }

    public void getLinesmenRefereeToList() {
        referees = new ArrayList<>();
        linesmen = new ArrayList<>();

        Person ref1 = new Person();
        String[] ref1Split = refereeOneEditText.getText().toString().trim().split(" ");
        ref1.setFirstName(ref1Split[0]);
        ref1.setLastName(ref1Split[1]);
        referees.add(ref1);

        Person ref2 = new Person();
        String[] ref2Split = refereeTwoEditText.getText().toString().trim().split(" ");
        ref2.setFirstName(ref1Split[0]);
        ref2.setLastName(ref1Split[1]);
        referees.add(ref2);

        Person line1 = new Person();
        String[] line1Split = linesmanOneEditText.getText().toString().trim().split(" ");
        line1.setFirstName(line1Split[0]);
        line1.setLastName(line1Split[1]);
        linesmen.add(line1);

        Person line2 = new Person();
        String[] line2Split = linesmanTwoEditText.getText().toString().trim().split(" ");
        line2.setFirstName(line2Split[0]);
        line2.setLastName(line2Split[1]);
        linesmen.add(line2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_accept) {
            // ak su EditTextViews vyplnene
            if (isFilled()) {
                progressDialog.show();
                // nacitanie oboch rosters zo suborov a oparsovanie
                new BackgroundRun().execute();
            } else {
                Toast.makeText(this, this.getResources().getString(R.string.fill_empty_rows), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // ak je aspon jedno pole vyplnene
        if (isFilledAtLeastOne()) {
            // vybehne oznamenie ze pozor, vyplnene udaje
            View parentLayout = findViewById(R.id.newMatchLayoutID);

            Snackbar snackbar = Snackbar.make(parentLayout, this.getResources().getString(R.string.filled_text_erased), Snackbar.LENGTH_LONG)
                    .setAction(this.getResources().getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });

            // ak chceme cerveny snackbar
            //View snackbarView = snackbar.getView();
            //snackbarView.setBackgroundColor(Color.parseColor("#FF5252"));
            snackbar.show();
        } else {
            super.onBackPressed();
        }
    }
}
