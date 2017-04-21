package com.example.trung.footballschedule;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trung on 4/14/2017.
 */

class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        int positionId = getIntent().getIntExtra("POSITION_ID", -1);
        FixtureData fixture = (FixtureData) SplashActivity.dataList.get(positionId);

        TextView tv_date = (TextView) findViewById(R.id.date_detail);
        TextView tv_homeTeamName = (TextView) findViewById(R.id.home_team_name_detail);
        TextView tv_awayTeamName = (TextView) findViewById(R.id.away_team_name_detail);
        TextView tv_fixtureTime = (TextView) findViewById(R.id.fixture_time_detail);

        SimpleDateFormat typeOnDate = new SimpleDateFormat("EEE d MMM yyyy");
        Date fixtureTime = fixture.getDate();
        String date = typeOnDate.format(fixtureTime);

        tv_date.setText(date);
        tv_homeTeamName.setText(fixture.getHomeTeamName());
        tv_awayTeamName.setText(fixture.getAwayTeamName());
        tv_fixtureTime.setText(fixture.getFixtureTime());

        CountdownTimerFixture(fixtureTime);

    }

    private void CountdownTimerFixture(Date fixtureTime) {
        final TextView tv_days = (TextView) findViewById(R.id.days_detail);
        final TextView tv_time = (TextView) findViewById(R.id.time_detail);

        Date currentTime = new Date();
        long diff = fixtureTime.getTime() - currentTime.getTime();

        new CountDownTimer(diff, 1000) {
            long days = 0;
            long hours = 0;
            long minutes = 0;
            long seconds = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
                days = seconds / 86400;
                seconds = seconds % 86400;
                hours = seconds / 3600;
                seconds = seconds % 3600;
                minutes = seconds / 60;
                seconds = seconds % 60;

                tv_days.setText(days + " Days");
                tv_time.setText(String.format("%02d : %02d : %02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                tv_days.setText("Starting");
                tv_time.setText("");
            }
        }.start();
    }
}
