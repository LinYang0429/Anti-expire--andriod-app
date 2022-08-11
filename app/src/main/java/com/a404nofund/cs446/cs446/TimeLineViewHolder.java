package com.a404nofund.cs446.cs446;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    ImageView mImage;
    TextView mDate;
    CardView cv;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.timeline_cardview);
        mImage = (ImageView) cv.findViewById(R.id.image_timeline);
        mDate = (TextView) cv.findViewById(R.id.text_timeline_date);
    }
}
