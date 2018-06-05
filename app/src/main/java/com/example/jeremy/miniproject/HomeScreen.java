package com.example.jeremy.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {
    Button mLetsGoButton ;
    EditText mYear;
    EditText mAdminCode;
    EditText mAdminCode2;
    EditText mEmail;
    EditText mEmail2;
    EditText mbirthYear;
    boolean yearField=false;
    boolean pass1=false;
    boolean pass2=false;
    boolean email1=false;
    boolean email2=false;
    boolean birthyear=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mYear=findViewById(R.id.year);
        mAdminCode=findViewById(R.id.admincode);
        mAdminCode2=findViewById(R.id.admincodeagain);
        mbirthYear=findViewById(R.id.maxbirthdate);
        mEmail=findViewById(R.id.adminemail);
        mEmail2=findViewById(R.id.adminemailagain);
        mLetsGoButton=findViewById(R.id.letsgobutton);
        mYear.setText("17");
        mYear.setSelection(mYear.getText().length());



        mLetsGoButton.setOnClickListener(view -> {
            if (!mYear.getText().toString().isEmpty() &&
                    !mAdminCode2.getText().toString().isEmpty() &&
                    !mAdminCode.getText().toString().isEmpty() &&
                    !mEmail.getText().toString().isEmpty() &&
                    !mEmail2.getText().toString().isEmpty() &&
                    (mbirthYear.getText().toString().length()==2)){


                if (mEmail2.getText().toString().equals(mEmail.getText().toString())
                        && mAdminCode.getText().toString().equals(mAdminCode2.getText().toString()) )
                {
                    Intent intent = new Intent(getApplicationContext(), FieldSelection.class);
                    intent.putExtra("year", mYear.getText().toString());
                    intent.putExtra("email", mEmail.getText().toString());
                    intent.putExtra("passcode", mAdminCode.getText().toString());
                    intent.putExtra("birthmax",mbirthYear.getText().toString().substring(0,2));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Password or email fields do not match",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
            }
        });


    }

}
