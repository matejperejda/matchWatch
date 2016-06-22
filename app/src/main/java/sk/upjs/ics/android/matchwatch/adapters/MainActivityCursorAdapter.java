package sk.upjs.ics.android.matchwatch.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.R;
import sk.upjs.ics.android.matchwatch.provider.Provider;

public class MainActivityCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    private ImageView thumbImageView;
    private TextView dateTextView;
    private TextView homeTeamTextView;
    private TextView homeScoreTextView;
    private TextView awayTeamTextView;
    private TextView awayScoreTextView;
    private TextView finishedTextView;


    public MainActivityCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_row, parent, false);
        bindView(view, context, cursor);
        return view;
    }

    public void initViews(View view) {
        dateTextView = (TextView) view.findViewById(R.id.dateAndTime);
        homeTeamTextView = (TextView) view.findViewById(R.id.homeTeam_text);
        awayTeamTextView = (TextView) view.findViewById(R.id.awayTeam_text);
        homeScoreTextView = (TextView) view.findViewById(R.id.homeScore_text);
        awayScoreTextView = (TextView) view.findViewById(R.id.awayScore_text);
        finishedTextView = (TextView) view.findViewById(R.id.isFinished_text);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        initViews(view);

        String[] names = cursor.getColumnNames();
        Log.e("names", Arrays.toString(names));
        Log.e("position", String.valueOf(cursor.getPosition()));
        Log.e("homeTeamTextView index", String.valueOf(cursor.getColumnIndex(Defaults.HOME_TEAM)));
        Log.e("homeScoreTextView index", String.valueOf(cursor.getColumnIndex(Provider.Match.SCORE_HOME)));

        //TODO: vyriesit preco pocase stlpce miznu
        //if (cursor.getColumnIndex(Defaults.HOME_TEAM) != -1) {
        String gameDate = cursor.getString(cursor.getColumnIndex(Provider.Match.GAME_DATE));

        //String homeTeamFullName = cursor.getString(cursor.getColumnIndex(Defaults.HOME_TEAM));
        //String homeTeamShortName = cursor.getString(cursor.getColumnIndex(Defaults.HOME_SHORT));
        int scoreHome = cursor.getInt(cursor.getColumnIndex(Provider.Match.SCORE_HOME));

        //String awayTeamFullName = cursor.getString(cursor.getColumnIndex(Defaults.AWAY_TEAM));
        //String awayTeamShortName = cursor.getString(cursor.getColumnIndex(Defaults.AWAY_SHORT));
        int scoreAway = cursor.getInt(cursor.getColumnIndex(Provider.Match.SCORE_AWAY));
        int isFinished = cursor.getInt(cursor.getColumnIndex(Provider.Match.IS_FINISHED));

        //TODO: doimplementovat tahanie obrazkov do cursor adaptera
        // byte[] matchImage
        // thumbImageView = (ImageView) view.findViewById(R.id.list_image);

        //homeTeamTextView.setText(homeTeamFullName);
        //awayTeamTextView.setText(awayTeamFullName);
        homeScoreTextView.setText(String.valueOf(scoreHome));
        awayScoreTextView.setText(String.valueOf(scoreAway));
        dateTextView.setText(gameDate);
        finishedTextView.setText(String.valueOf(isFinished));
        //}
    }
}
