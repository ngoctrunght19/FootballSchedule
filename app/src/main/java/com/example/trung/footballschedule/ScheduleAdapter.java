package com.example.trung.footballschedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by trung on 4/10/2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int DATE = 0;
    private static final int FIXTURE = 1;
    private ArrayList<Object> dataList;
    private Context context;

    public ScheduleAdapter(ArrayList<Object> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ViewHolderDate extends RecyclerView.ViewHolder {
        public TextView date;

        public ViewHolderDate(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }

    public class ViewHolderFixture extends RecyclerView.ViewHolder {
        public TextView awayTeamName, homeTeamName, fixtureTime;

        public ViewHolderFixture(View itemView) {
            super(itemView);
            awayTeamName = (TextView) itemView.findViewById(R.id.away_team_name);
            homeTeamName = (TextView) itemView.findViewById(R.id.home_team_name);
            fixtureTime = (TextView) itemView.findViewById(R.id.fixture_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        switch (viewType) {
            case DATE:
                final View viewDate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
                return new ViewHolderDate(viewDate);
            case FIXTURE:
                final View viewFixture = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false);
                final ViewHolderFixture holder = new ViewHolderFixture(viewFixture);
                viewFixture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int itemPosition = holder.getAdapterPosition();
                        Intent i = new Intent(context, DetailActivity.class);
                        i.putExtra("POSITION_ID", itemPosition);
                        context.startActivity(i);
                    }
                });
                return holder;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DATE:
                ViewHolderDate holderDate = (ViewHolderDate) holder;
                DateData dateData = (DateData) dataList.get(position);
                holderDate.date.setText(dateData.getDate());
                break;
            case FIXTURE:
                ViewHolderFixture holderFixture = (ViewHolderFixture) holder;
                FixtureData fixtureData = (FixtureData) dataList.get(position);
                holderFixture.awayTeamName.setText(fixtureData.getAwayTeamName());
                holderFixture.homeTeamName.setText(fixtureData.getHomeTeamName());
                holderFixture.fixtureTime.setText(fixtureData.getFixtureTime());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof DateData) {
            return DATE;
        }
        else if (dataList.get(position) instanceof FixtureData) {
            return FIXTURE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
