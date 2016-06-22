package sk.upjs.ics.android.matchwatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.matchwatch.R;
import sk.upjs.ics.android.matchwatch.dbsEntity.MatchDatabase;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;

public class MainActivityArrayAdapter extends ArrayAdapter<MatchDatabase> {

    public static class MatchHolder {
        private ImageView thumbImageView;
        private TextView dateTextView;
        private TextView homeTeamTextView;
        private TextView homeScoreTextView;
        private TextView awayTeamTextView;
        private TextView awayScoreTextView;
        private TextView finishedTextView;
    }

    private Context context;
    private int layoutResourceId;
    private List<MatchDatabase> list;


    private DatabaseOpenHelper dbHelper;

    public MainActivityArrayAdapter(Context context, int resource, List<MatchDatabase> list) {
        super(context, resource, list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;
    }

    public MainActivityArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.layoutResourceId = resource;
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MatchHolder matchHolder = null;
        MatchDatabase matchDatabase = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            matchHolder = new MatchHolder();
            matchHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateAndTime);
            matchHolder.homeTeamTextView = (TextView) convertView.findViewById(R.id.homeTeam_text);
            matchHolder.awayTeamTextView = (TextView) convertView.findViewById(R.id.awayTeam_text);
            matchHolder.homeScoreTextView = (TextView) convertView.findViewById(R.id.homeScore_text);
            matchHolder.awayScoreTextView = (TextView) convertView.findViewById(R.id.awayScore_text);
            matchHolder.finishedTextView = (TextView) convertView.findViewById(R.id.isFinished_text);

            convertView.setTag(matchHolder);
        } else {
            matchHolder = (MatchHolder) convertView.getTag();
        }

        matchHolder.dateTextView.setText(matchDatabase.getGameDate());
        matchHolder.homeTeamTextView.setText(matchDatabase.getHomeName());
        matchHolder.homeScoreTextView.setText(String.valueOf(matchDatabase.getHomeScore()));
        matchHolder.awayTeamTextView.setText(matchDatabase.getAwayName());
        matchHolder.awayScoreTextView.setText(String.valueOf(matchDatabase.getAwayScore()));

        if(matchDatabase.getIsFinished() == 0) {
            matchHolder.finishedTextView.setText(context.getResources().getString(R.string.game_is_not_over));
        } else {
            matchHolder.finishedTextView.setText(context.getResources().getString(R.string.game_is_over));
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /*private List<MatchDatabase> getData() {
        dbHelper = new DatabaseOpenHelper(context);
        return dbHelper.getMatchesJoin();
    }*/
}
