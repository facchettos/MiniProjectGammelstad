package com.example.jeremy.miniproject;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class TeamConfiguration extends AppCompatActivity implements TeamConfigAdapter.getInfosAboutSelection {

    RecyclerView mRecycler;
    TeamConfigAdapter mAdapter;
    Button adminButton;
    ProgressBar pb;
    String realAdminCode;
    EditText mCode;
    boolean isAdmin=false;
    Integer playersSelected=0;
    Integer overagedPlayers=0;
    Integer requestCode;
    String teamName;
    int nbOfPlayers;
    static Toast mToast;
    Button confirmButton;
    LinearLayout ll;
    int llcolor;
    Bitmap bitmap;
    View view;
    Path path2;
    Paint paint;
    Canvas canvas;
    int backColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nbOfPlayers=0;
        setContentView(R.layout.team_configuration);
        mToast=Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        ll = findViewById(R.id.linearlayoutteamconfig);
        pb=findViewById(R.id.progressBar2);
        llcolor=ll.getSolidColor();
        Toast.makeText(getApplicationContext(),getIntent().getStringExtra("teamName"),Toast.LENGTH_LONG).show();
        mCode = findViewById(R.id.passAdmin);
        mRecycler=findViewById(R.id.recyclerViewPlayers);
        mAdapter= new TeamConfigAdapter(this,Integer.parseInt(getIntent().getStringExtra("birthmax")));
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view = new TeamConfiguration.SketchSheetView(this, displayMetrics.widthPixels,1000);
        int height = displayMetrics.heightPixels;

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.height=height/4;

        view.setLayoutParams(p);
        ll.addView(view);

        paint = new Paint();

        path2 = new Path();

        paint.setDither(true);

        paint.setColor(Color.parseColor("#000000"));

        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStrokeWidth(2);







        setResult(RESULT_CANCELED);

        confirmButton = new Button(getApplicationContext());
        confirmButton.setText("Confirm");
        confirmButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("players",mAdapter.getSelectedPlayersAsJson());
            setResult(RESULT_OK,intent);

            try {
                File f = new File(Environment.getExternalStorageDirectory()
                        +"/gammelstad_cup_results/signatureCoach"
                        +getIntent().getStringExtra("team")+".png");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG,95,fos);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mAdapter.getSelectedPlayersAsJson();
            finish();
        });

        ll.addView(confirmButton);
        backColor=ll.getSolidColor();






        teamName=getIntent().getStringExtra("teamName");
        realAdminCode = getIntent().getStringExtra("passcode");



        adminButton=findViewById(R.id.adminaccess);
        adminButton.setOnClickListener(view -> {
            if (mCode.getText().toString().equals(realAdminCode)){
                Toast.makeText(getApplicationContext(),"Good code",Toast.LENGTH_LONG).show();
                isAdmin=true;
                ll.setBackgroundColor(Color.GREEN);
            }else {
                Toast.makeText(getApplicationContext(),"Wrong code",Toast.LENGTH_LONG).show();
                isAdmin=false;
                ll.setBackgroundColor(llcolor);
            }
        });

        new loadUrl().execute();
        Log.d(TAG, "onCreate: ");

    }



    class SketchSheetView extends View {

        public SketchSheetView(Context context, int width, int height) {

            super(context);

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

            canvas = new Canvas(bitmap);

            this.setBackgroundColor(Color.WHITE);

        }

        private ArrayList<TeamConfiguration.DrawingClass> DrawingClassArrayList = new ArrayList<TeamConfiguration.DrawingClass>();

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            TeamConfiguration.DrawingClass pathWithPaint = new TeamConfiguration.DrawingClass();

            canvas.drawPath(path2, paint);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                path2.moveTo(event.getX(), event.getY());

                path2.lineTo(event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                path2.lineTo(event.getX(), event.getY());

                pathWithPaint.setPath(path2);

                pathWithPaint.setPaint(paint);

                DrawingClassArrayList.add(pathWithPaint);
            }

            invalidate();
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (DrawingClassArrayList.size() > 0) {

                canvas.drawPath(
                        DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPath(),

                        DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPaint());
            }
        }
    }

    public class DrawingClass {

        Path DrawingClassPath;
        Paint DrawingClassPaint;

        public Path getPath() {
            return DrawingClassPath;
        }

        public void setPath(Path path) {
            this.DrawingClassPath = path;
        }


        public Paint getPaint() {
            return DrawingClassPaint;
        }

        public void setPaint(Paint paint) {
            this.DrawingClassPaint = paint;
        }
    }


    @Override
    public void getNumberOfPlayers(int nb) {
//        if (mToast!=null){
//            mToast.cancel();
//        }
//        nbOfPlayers=nb;
//        if (overagedPlayers>2 && !isAdmin){
//            mToast.makeText(this,"go and see the admin",Toast.LENGTH_LONG).show();
//            this.ll.setBackground(new ColorDrawable(Color.RED));
//            this.confirmButton.setClickable(false);
//        }else{
//            this.confirmButton.setClickable(true);
//        }
//        mToast.show();
    }

    @Override
    public void getNumberOfOveragedPlayers(int nb) {
        overagedPlayers=nb;
        nbOfPlayers=nb;
        if (overagedPlayers>2 && !isAdmin){
            Toast.makeText(this,"go and see the admin",Toast.LENGTH_LONG).show();
            this.ll.setBackground(new ColorDrawable(Color.RED));
            this.confirmButton.setClickable(false);
        }else{
            if (!isAdmin){
                this.ll.setBackgroundColor(backColor);
            }
            this.confirmButton.setClickable(true);
        }
    }


    class loadUrl extends AsyncTask<URL, Void, ArrayList<Player>> {

        @Override
        protected ArrayList<Player> doInBackground(URL... urls) {
            Document doc = null;
            ArrayList<Player> res = new ArrayList<>();
            try {
                Log.d(TAG, "doInBackground: making the request");
                doc = Jsoup.connect("http://teamplaycup.se/cup/?team&home=kurirenspelen/"
                        +getIntent().getStringExtra("year")+"&scope="
                        +getIntent().getStringExtra("group")+"&name="+teamName).get();
                Log.d(TAG, "doInBackground: request done");

                Elements tables = doc.select("table");


//                Elements players = doc.select(".table-striped tbody tr td");
                Element playerTable;
                try {
                     playerTable = tables.get(1);
                }catch (Exception e){
                     playerTable = tables.get(0);
                }
                Player p=new Player();
                Elements players = playerTable.select("tbody tr td");
                for (int i = 0; i < players.size(); i++) {
                    if (i%2==0){
                        p=new Player();
                        p.playerName=players.get(i).ownText();
                    }else{
                        p.yearOfBirth=players.get(i).ownText();
                        res.add(p);
                    }
                }
            } catch (
                    IOException e)

            {
                e.printStackTrace();
            }

            for (Player p:res
                 ) {
                Log.d(TAG, "doInBackground: name: "+p.playerName);
                Log.d(TAG, "doInBackground: birth: "+p.yearOfBirth);
            }

            return res;
        }

        @Override
        protected void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Player> players) {
            pb.setVisibility(View.INVISIBLE);
            mAdapter.updateSet(players);
            
        }


    }

}
