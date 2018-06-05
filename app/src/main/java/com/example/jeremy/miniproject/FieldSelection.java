package com.example.jeremy.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.*;
import android.widget.Button;

public class FieldSelection extends AppCompatActivity {

    Button mA11;
    Button mB11;
    Button mC17;
    Button mC27;
    Button mC35;
    Button mC45;
    Button mD7;
    Button mE1;
    Button mE2;
    Button mE3;
    Button mS1;
    Button mS2;
    Button mR1;
    Button mR2;
    Button mR3;
    Button mR4;
    String year;
    Intent intent;
    String passcode;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_selection);
        mA11=findViewById(R.id.A11);
        mB11=findViewById(R.id.B11);
        mC17=findViewById(R.id.C17);
        mC27=findViewById(R.id.C27);
        mC35=findViewById(R.id.C35);
        mC45=findViewById(R.id.C45);
        mD7=findViewById(R.id.D7);
        mE1=findViewById(R.id.E1);
        mE2=findViewById(R.id.E2);
        mE3=findViewById(R.id.E3);
        mS1=findViewById(R.id.S1);
        mS2=findViewById(R.id.S2);
        mR1=findViewById(R.id.R1);
        mR2=findViewById(R.id.R2);
        mR3=findViewById(R.id.R3);
        mR4=findViewById(R.id.R4);
        year=getIntent().getStringExtra("year");
        passcode=getIntent().getStringExtra("passcode");
        email=getIntent().getStringExtra("email");
        intent= new Intent(getApplicationContext(),MatchSelection.class);
        intent.putExtra("year",year);
        intent.putExtra("passcode",passcode);
        intent.putExtra("email",email);
        intent.putExtra("birthmax",getIntent().getStringExtra("birthmax"));


        mA11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.putExtra("field","A%2011");
                startActivity(intent);
            }

        });

        mB11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.putExtra("field","B%2011");
                startActivity(intent);
            }
        });

        mC17.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","C1%207");
                startActivity(intent);
            }
        });

        mC27.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","C2%207");
                startActivity(intent);
            }
        });

        mC35.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","C3%205");
                startActivity(intent);
            }
        });

        mC45.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","C4%205");
                startActivity(intent);
            }
        });

        mD7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","D%207");
                startActivity(intent);
            }
        });

        mE1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","E1%207");
                startActivity(intent);
            }
        });

        mE2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","E2%205");
                startActivity(intent);
            }
        });
        mE3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","E3%205");
                startActivity(intent);
            }
        });

        mS1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","S1%2011");
                startActivity(intent);
            }
        });

        mS2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","S2");
                startActivity(intent);
            }
        });

        mR1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","R%205");
                startActivity(intent);
            }
        });

        mR2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","R2%205");
                startActivity(intent);
            }
        });
        mR3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","R3%207");
                startActivity(intent);
            }
        });
        mR4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("field","R4%207");
                startActivity(intent);
            }
        });

    }


}
