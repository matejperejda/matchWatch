package sk.upjs.ics.android.matchwatch.activities;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.R;
import sk.upjs.ics.android.matchwatch.provider.Provider;


public class PlayerListActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;

    private Spinner teamSpinnerPlayerList;
    private TableLayout listPlayerTableLayout;

    private TableRow rowHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        this.setTitle("Player Roosters");

        teamSpinnerPlayerList = (Spinner) findViewById(R.id.teamSpinnerPlayerList);
        listPlayerTableLayout = (TableLayout) findViewById(R.id.listPlayerTableLayout);

        teamSpinnerPlayerList.setAdapter(adapterInit());

        teamSpinnerPlayerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String team = (String) parent.getSelectedItem();

                //teamSpinnerPlayerList.getAdapter().
                // tahat tabulku z dbs
                buildTable(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing
            }
        });
    }


    private SimpleCursorAdapter adapterInit() {
        String from[] = new String[]{Provider.Team.FULL_NAME};
        int to[] = new int[]{android.R.id.text1};

        Cursor cursor = getContentResolver().query(Provider.Team.CONTENT_URI, null, null, null, null);

        return new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, cursor, from, to, Defaults.NO_FLAGS);
    }

    private void initTable() {
        rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"No.", "Last name", "First name", "Birth Date", "P", "S", "Height", "Weight", "Club"};
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        listPlayerTableLayout.addView(rowHeader);
    }

    private int[] isMyPlayer(int position) {
        Cursor c = getContentResolver().query(Provider.TeamPlayer.CONTENT_URI, null, Provider.TeamPlayer._ID + "=" + position, null, null);

        c.moveToFirst();
        int[] idx = new int[c.getCount()];
        int i = 0;

        while (c.moveToNext()) {
            int mPosition = c.getInt(c.getColumnIndex(Provider.TeamPlayer._ID));

            if (mPosition == position) {
                idx[i] = c.getInt(c.getColumnIndex(Provider.TeamPlayer.PLAYER_ID));
                i++;
            }
        }
        c.close();
        return idx;
    }

    private void buildTable(int teamIndex) {
        initTable();
        Cursor cursor = getContentResolver().query(Provider.Player.CONTENT_URI, null, null, null, null);

        /*int rows = cursor.getCount();
        int columns = cursor.getColumnCount();

        cursor.moveToFirst();

        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < columns; j++) {
                TextView txtView = new TextView(this);
                txtView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                txtView.setGravity(Gravity.CENTER);
                txtView.setTextSize(18);
                txtView.setPadding(1, 1, 1, 1);

                txtView.setText(cursor.getString(j));

                tableRow.addView(txtView);

            }

            cursor.moveToNext();
            listPlayerTableLayout.addView(tableRow);
        }*/

        cursor.moveToFirst();

        int[] goodIdx = isMyPlayer(teamIndex);

        if (cursor.getCount() > 0) {
            //for (int i = 0; i < goodIdx.length; i++) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(Provider.Player._ID));

                // if (id == goodIdx[i]) {
                int number = cursor.getInt(cursor.getColumnIndex(Provider.Player.NUMBER));
                String firstName = cursor.getString(cursor.getColumnIndex(Provider.Player.FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(Provider.Player.LAST_NAME));
                String birth = cursor.getString(cursor.getColumnIndex(Provider.Player.BIRTH_DATE));
                String position = cursor.getString(cursor.getColumnIndex(Provider.Player.POSITION));
                String shoots = cursor.getString(cursor.getColumnIndex(Provider.Player.SHOOTS));
                int height = (int) cursor.getFloat(cursor.getColumnIndex(Provider.Player.HEIGHT));
                int weight = cursor.getInt(cursor.getColumnIndex(Provider.Player.WEIGHT));
                String club = cursor.getString(cursor.getColumnIndex(Provider.Player.CLUB));

                TableRow row = new TableRow(this);
                String[] columnText = {String.valueOf(number), lastName, firstName, birth, position, shoots, String.valueOf(height), String.valueOf(weight), club};

                for (String text : columnText) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    row.addView(tv);
                }

                listPlayerTableLayout.addView(row);
                //  }
            }
        }
    }
}

