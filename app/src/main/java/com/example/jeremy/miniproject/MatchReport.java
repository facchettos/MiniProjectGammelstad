package com.example.jeremy.miniproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class MatchReport extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    Button mFinalButton;
    Button mFairPlay;
    Button mResult;
    Bitmap bitmap;
    View view;
    Path path2;
    Paint paint;
    Canvas canvas;
    boolean done1=false;
    boolean done2=false;



    public void createPdf() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(Environment.getExternalStorageDirectory()
                    + "/gammelstad_cup_results/"
                    +getIntent().getStringExtra("matchCode")
                    +"_"+getIntent().getStringExtra("year")
                    +".pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph chunk = new Paragraph("Report for game "+getIntent().getStringExtra("matchCode")+"\n ", font);




        Image image = null;
        Image image2 = null;
        try {
            image = Image.getInstance(Environment.getExternalStorageDirectory()+"/gammelstad_cup_results/fairplay.png");
            image2 = Image.getInstance(Environment.getExternalStorageDirectory()+"/gammelstad_cup_results/result.png");
            image.scaleAbsolute(112 ,200);
            image2.scaleAbsolute(112 ,200);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            document.add(chunk);
            document.add(new Paragraph(getIntent().getStringExtra("team1Name")));
            document.add(new Paragraph(extractNamesFromJson(getIntent().getStringExtra("team1Players"))));
            Image sign1=Image.getInstance(Environment.getExternalStorageDirectory()+"/gammelstad_cup_results/signatureCoach1.png");
            sign1.scaleAbsolute(100,100);
            document.add(sign1);

            document.add(new Paragraph(getIntent().getStringExtra("team2Name")));
            document.add(new Paragraph(extractNamesFromJson(getIntent().getStringExtra("team2Players"))));
            Image sign2=Image.getInstance(Environment.getExternalStorageDirectory()+"/gammelstad_cup_results/signatureCoach2.png");
            sign2.scaleAbsolute(100,100);
            document.add(sign2);


            document.add(Chunk.NEXTPAGE);
            document.add(image);
            document.add(image2);
            Image signRef = Image.getInstance(Environment.getExternalStorageDirectory()+"/gammelstad_cup_results/signatureRef.png");
            signRef.scaleAbsolute(100,100);
            document.add(signRef);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }

    public String extractNamesFromJson(String json){
        Gson gson = new Gson();
        StringBuilder builder= new StringBuilder();
        builder.append("");
        Type listType = new TypeToken<ArrayList<Player>>(){}.getType();
        ArrayList<Player> yourClassList = new Gson().fromJson(json, listType);
        for (Player p:yourClassList
             ) {
            builder.append(p.playerName);
            builder.append("\n");
        }

        return builder.toString();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK){
            done1=true;
        }
        if (requestCode==2 && resultCode==RESULT_OK){
            done2=true;
        }
        this.mFinalButton.setClickable(done1&&done2);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_report);
//        Toast.makeText(getApplicationContext(),getIntent().getStringExtra("team1Players"),Toast.LENGTH_LONG).show();
        mFairPlay = findViewById(R.id.fairplay);

        mFairPlay.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory() + "/gammelstad_cup_results/fairplay.png");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 1);

            }
        });


        mResult = findViewById(R.id.res);

        mResult.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = new File(Environment.getExternalStorageDirectory() + "/gammelstad_cup_results/result.png");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 2);
            }
        });


        LinearLayout relativeLayout;

        relativeLayout = findViewById(R.id.gamereport);
        LinearLayout ll = findViewById(R.id.viewcontainer);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        relativeLayout.removeView(mFinalButton);

        view = new SketchSheetView(this, width, height);


        mFinalButton = new Button(getApplicationContext());
        mFinalButton.setText("Finalize and send report");
        mFinalButton.setOnClickListener(view -> {
            createPdf();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getIntent().getStringExtra("email")});
            i.putExtra(Intent.EXTRA_SUBJECT, "Game Report ");
            i.putExtra(Intent.EXTRA_TEXT, "");
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/gammelstad_cup_results/"
                    +getIntent().getStringExtra("matchCode")
                    +"_"+getIntent().getStringExtra("year")
                    +".pdf");
            Uri uri = Uri.fromFile(file);
            i.putExtra(Intent.EXTRA_STREAM, uri);
            try {
                File f = new File(Environment.getExternalStorageDirectory()+"/gammelstad_cup_results/signatureRef.png");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG,95,fos);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                startActivity(Intent.createChooser(i, "Send email"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });
        mFinalButton.setClickable(false);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.height=3*height/5;
//
        view.setLayoutParams(p);
        relativeLayout.addView(view,1);

        relativeLayout.addView(mFinalButton);

        paint = new Paint();

        path2 = new Path();

        paint.setDither(true);

        paint.setColor(Color.parseColor("#000000"));

        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStrokeWidth(2);

        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "gammelstad_cup_results");
        directory.mkdirs();



    }

    class SketchSheetView extends View {

        public SketchSheetView(Context context, int width, int height) {

            super(context);

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

            canvas = new Canvas(bitmap);

            this.setBackgroundColor(Color.WHITE);

        }

        private ArrayList<DrawingClass> DrawingClassArrayList = new ArrayList<DrawingClass>();

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            DrawingClass pathWithPaint = new DrawingClass();

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


}
