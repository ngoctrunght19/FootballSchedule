package com.example.trung.footballschedule;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trung on 4/10/2017.
 */

public class FixtureData {
    private String awayTeamName;
    private String homeTeamName;
    private Date date;
    private double oddsHomeWin;
    private double oddsAwayWin;
    private double oddsDraw;

    public FixtureData(String awayTeamName, String homeTeamName, Date date, double oddsHomeWin, double oddsAwayWin, double oddsDraw) {
        this.awayTeamName = awayTeamName;
        this.homeTeamName = homeTeamName;
        this.date = date;
        this.oddsHomeWin = oddsHomeWin;
        this.oddsAwayWin = oddsAwayWin;
        this.oddsDraw = oddsDraw;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getFixtureTime() {
        String fixtureTime = null;
        SimpleDateFormat typeOnFixtureTime = new SimpleDateFormat("HH:mm");
        fixtureTime = typeOnFixtureTime.format(date);
        return fixtureTime;
    }

    public Date getDate() {
        return date;
    }

    public double getOddsHomeWin() {
        return oddsHomeWin;
    }

    public double getOddsAwayWin() {
        return oddsAwayWin;
    }

    public double getOddsDraw() {
        return oddsDraw;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOddsHomeWin(double oddsHomeWin) {
        this.oddsHomeWin = oddsHomeWin;
    }

    public void setOddsAwayWin(double oddsAwayWin) {
        this.oddsAwayWin = oddsAwayWin;
    }

    public void setOddsDraw(double oddsDraw) {
        this.oddsDraw = oddsDraw;
    }
}
