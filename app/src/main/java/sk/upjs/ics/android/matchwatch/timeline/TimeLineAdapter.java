package sk.upjs.ics.android.matchwatch.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.List;

import sk.upjs.ics.android.matchwatch.R;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;

    public TimeLineAdapter(List<TimeLineModel> feedList) {
        mFeedList = feedList;
    }

    public void swapAdapter(List<TimeLineModel> data){
        mFeedList.clear();
        mFeedList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        TimeLineModel timeLineModel = mFeedList.get(position);
        //holder.name.setText("name：" + timeLineModel.getName() + "    age：" + timeLineModel.getAge());

        holder.notif.setText(timeLineModel.getNotif());
        holder.time.setText(timeLineModel.getTime());
        holder.team.setText(timeLineModel.getTeam());
        holder.type.setText(timeLineModel.getType());
        holder.player.setText(timeLineModel.getPlayer());
        holder.assist1.setText(timeLineModel.getAssist1());
        holder.assist2.setText(timeLineModel.getAssist2());
        holder.duration.setText(timeLineModel.getDuration());
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

}