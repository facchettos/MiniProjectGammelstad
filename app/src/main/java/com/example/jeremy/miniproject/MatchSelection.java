package com.example.jeremy.miniproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MatchSelection extends AppCompatActivity implements RecyclerAdapter.onItemClickedListener{

    String mFieldName;
    Button mButton1;
    Button mButton2;
    Button mButtonReport;
    RecyclerAdapter mGameAdapter;
    ProgressBar pb;
    boolean team1Chose=false;
    boolean team2Chose=false;
    String team1Name;
    String team2Name;
    String group;
    String team1PlayersJson;
    String team2PlayersJson;
    String email;
    String passcode;
    String year ;
    String matchCode;
    String groupCode;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); //simulate the physical button not to recreate a brand new activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        if (requestCode == 1 && resultCode == RESULT_OK) {
            team1Chose = true;
            mButton1.setBackgroundColor(Color.GRAY);
            mButton1.setClickable(false);
            team1PlayersJson=data.getStringExtra("players");
//            Toast.makeText(getApplicationContext(),data.getStringExtra("players"),Toast.LENGTH_LONG).show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            team2Chose = true;
            mButton2.setBackgroundColor(Color.GRAY);
            mButton2.setClickable(false);
            team2PlayersJson=data.getStringExtra("players");

        }
        if (team1Chose && team2Chose) {
            mButtonReport.setClickable(true);
            mButtonReport.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_selection);

        mFieldName = getIntent().getStringExtra("field");
        mButton1 = findViewById(R.id.registerA);
        mButton2 = findViewById(R.id.registerB);
        mGameAdapter = new RecyclerAdapter( this);
        pb=findViewById(R.id.progressBar1);
        passcode = getIntent().getStringExtra("passcode");
        email = getIntent().getStringExtra("email");
        year = getIntent().getStringExtra("year");


        Log.d(TAG, "onCreate: create rv");
        RecyclerView gameRecycler = findViewById(R.id.gameRecycler);
        gameRecycler.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "onCreate: set adapter");
        gameRecycler.setAdapter(mGameAdapter);
        gameRecycler.setHasFixedSize(true);
        Log.d(TAG, "onCreate: doinbackground");
        new loadUrl().execute();

        mButton1.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TeamConfiguration.class);
            intent.putExtra("team","1");
            intent.putExtra("teamName",team1Name);
            intent.putExtra("group",group);
            intent.putExtra("year",year);
            intent.putExtra("passcode",passcode);
            intent.putExtra("birthmax",getIntent().getStringExtra("birthmax"));

            startActivityForResult(intent,1);
        });

        mButton2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TeamConfiguration.class);
            intent.putExtra("teamName",team2Name);
            intent.putExtra("group",group);
            intent.putExtra("year",year);
            intent.putExtra("passcode",passcode);
            intent.putExtra("team","2");
            intent.putExtra("birthmax",getIntent().getStringExtra("birthmax"));


            startActivityForResult(intent,2);
        });

        mButtonReport = findViewById(R.id.registerResults);

        mButtonReport.setBackgroundColor(Color.GRAY);
        mButtonReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MatchReport.class);
                intent.putExtra("team1Name",team1Name);
                intent.putExtra("team2Name",team2Name);
                intent.putExtra("year",year);
                intent.putExtra("team1Players",team1PlayersJson);
                intent.putExtra("team2Players",team2PlayersJson);
                intent.putExtra("email",email);
                intent.putExtra("matchCode",matchCode);

                startActivity(intent);
            }
        });
        mButtonReport.setClickable(false);

    }


    class loadUrl extends AsyncTask<URL, Void, ArrayList<GameDescription>> {

        @Override
        protected ArrayList<GameDescription> doInBackground(URL... urls) {
            Document doc = null;
            ArrayList<GameDescription> res = new ArrayList<>();
            GameDescription intermediateResult;
            try {
                Log.d(TAG, "doInBackground: making the request");
                doc = Jsoup.connect("http://teamplaycup.se/cup/?games&home=kurirenspelen/"+year+"&scope=all&arena="+mFieldName+"-manna%20(Gstad)&field=").get();
                Log.d(TAG, "doInBackground: request done");

                Elements games = doc.select(".table-striped tbody tr");
                for (Element e : games
                        ) {
                    intermediateResult = new GameDescription();
                    Elements id = e.select("i");
                    Log.d(TAG, "doInBackground: id " + id.get(0).ownText());
                    intermediateResult.gameid = id.get(0).ownText();
                    Element hours = e.getAllElements().get(5);
                    Log.d(TAG, "doInBackground: time " + hours.ownText());
                    intermediateResult.timeToHappen = hours.ownText();
                    Elements links = e.select("a");
                    Log.d(TAG, "doInBackground: group " + links.get(0).ownText());
                    intermediateResult.group = links.get(0).ownText();
                    Log.d(TAG, "doInBackground: team 1 " + links.get(1).ownText());
                    intermediateResult.team1 = links.get(1).ownText();
                    Log.d(TAG, "doInBackground: team 2 " + links.get(2).ownText());
                    intermediateResult.team2 = links.get(2).ownText();
                    res.add(intermediateResult);
                }
            } catch (
                    IOException e)
            {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<GameDescription> gameDescriptions) {
            pb.setVisibility(View.INVISIBLE);
            mGameAdapter.updateSet(gameDescriptions);
        }
    }

    @Override
    public void onItemClicked(String team1,String team2,String group,String id) {
        if (!(team1Chose || team2Chose)) {
            mButton1.setText("register players for " + team1);
            mButton2.setText("register players for " + team2);
            this.group=group;
            matchCode = id;
            team1Name=team1;
            team2Name=team2;
        }
    }
}

class GameDescription {
    String team1;
    String team2;
    String timeToHappen;
    String group;
    String gameid;
}