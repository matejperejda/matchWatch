package sk.upjs.ics.android.matchwatch.timeline;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import org.w3c.dom.Text;

import sk.upjs.ics.android.matchwatch.R;

public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    /*public TextView name;

    public TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.tx_name);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);

    }*/

    public TextView notif;
    public TextView team;
    public TextView type;
    public TextView time;
    public TextView player;
    public TextView assist1;
    public TextView assist2;
    public TextView duration;

    public TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        notif = (TextView) itemView.findViewById(R.id.notif_tl);
        team = (TextView) itemView.findViewById(R.id.team_tl);
        type = (TextView) itemView.findViewById(R.id.type_tl);
        time = (TextView) itemView.findViewById(R.id.time_tl);
        player = (TextView) itemView.findViewById(R.id.player_tl);
        assist1 = (TextView) itemView.findViewById(R.id.playerAssist1_tl);
        assist2 = (TextView) itemView.findViewById(R.id.playerAssist2_tl);
        duration = (TextView) itemView.findViewById(R.id.duration_tl);
    }

}