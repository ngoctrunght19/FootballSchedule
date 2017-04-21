package com.example.trung.footballschedule;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by trung on 4/12/2017.
 */

public class SplashActivity extends AppCompatActivity {
    public static ArrayList<Object> dataList = new ArrayList<>();
    public static final String API_KEY = "69960dc9e5604c52afcb3c99fd6f9532";
    private ProgressBar progressBar;
    private boolean isInternetPresent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    getData("http://api.football-data.org/v1/competitions/426/fixtures");
                } finally {
                    if (isInternetPresent) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        SplashActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showNoInternetDialog(SplashActivity.this);
                            }
                        });
                    }
                    //finish();
                }
            }
        };
        welcomeThread.start();

    }

    private void showNoInternetDialog(Context context) {
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
        builer.setTitle("No Internet");
        builer.setMessage("Please check your Internet connection !")
            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        builer.create().show();
    }

    private void getData(String urlString) {
        String jsonString = null;
        try {
            URL url= new URL(urlString);
            progressBar.setProgress(10);
            jsonString = downloadUrl(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            isInternetPresent = false;
        }
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray fixture = jsonObject.getJSONArray("fixtures");
                String tempDate = null;
                progressBar.setProgress(80);
                for (int i=0; i<fixture.length(); i++) {
                    JSONObject f = fixture.getJSONObject(i);
                    String status = f.getString("status");
                    if (status.equals("TIMED")) {
                        String dateString = f.getString("date");
                        SimpleDateFormat typeOnJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        typeOnJSON.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date vietNameTime = typeOnJSON.parse(dateString);
                        SimpleDateFormat typeOnDate = new SimpleDateFormat("EEE d MMM yyyy");
                        String date = typeOnDate.format(vietNameTime);
                        if (!date.equals(tempDate)) {
                            DateData dateData = new DateData(date);
                            dataList.add(dateData);
                            tempDate = date;
                        }

                        String awayTeamName = f.getString("awayTeamName");
                        awayTeamName = shortTeamName(awayTeamName);
                        String homeTeamName = f.getString("homeTeamName");
                        homeTeamName = shortTeamName(homeTeamName);
                        JSONObject odds = f.getJSONObject("odds");
                        double oddsHomeWin = odds.getDouble("homeWin");
                        double oddsAwayWin = odds.getDouble("awayWin");
                        double oddsDraw = odds.getDouble("draw");
                        FixtureData fixtureData = new FixtureData(awayTeamName, homeTeamName, vietNameTime, oddsHomeWin, oddsAwayWin, oddsDraw);
                        dataList.add(fixtureData);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        progressBar.setProgress(100);
    }

    private String shortTeamName(String awayTeamName) {
        int index = awayTeamName.indexOf(" FC");
        if (index != -1) {
            awayTeamName = awayTeamName.substring(0, index);
        }
        return awayTeamName;
    }

    private String downloadUrl(URL url) throws IOException {

        String result = null;
        HttpURLConnection connect = null;
        InputStream is = null;
        try {
            connect = (HttpURLConnection) url.openConnection();
            progressBar.setProgress(20);
            connect.setRequestProperty("X-Auth-Token", API_KEY);
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(3000);
            connect.setRequestMethod("GET");
            connect.setDoInput(true);
            connect.connect();
            int responseCode = connect.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            is = connect.getInputStream();
            progressBar.setProgress(50);
            if (is != null) {
                result = readInputStream(is);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (connect != null) {
                connect.disconnect();
            }
        }
        progressBar.setProgress(70);
        return result;
    }

    private String readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = is.read(buffer);
        while (length != -1) {
            result.write(buffer, 0, length);
            length = is.read(buffer);
        }
        return result.toString("UTF-8");
    }

}
