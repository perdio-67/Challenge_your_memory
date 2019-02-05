package com.example.android.memorytest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class settings extends AppCompatActivity {
    String mySetting;
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        //intij
        preferences =getSharedPreferences("mySettings", Context.MODE_PRIVATE);
        mySetting = preferences.getString("mySetting", null);
        final EditText sec= (EditText)findViewById(R.id.NumOfSec);
        //to check
        if(mySetting!=null && !mySetting.equals(""))
            sec.setText(mySetting, TextView.BufferType.EDITABLE);
        else
            sec.setText("5", TextView.BufferType.EDITABLE);
        //back button
        final Button back_settings = (Button) findViewById(R.id.back_Settings);
        back_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(settings.this, DesignActivity.class);
               // startActivity(intent);
                finish();
            }
        });
        //apply button
        final Button apply = (Button) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText sec= (EditText)findViewById(R.id.NumOfSec);
                preferences = getSharedPreferences("mySettings", Context.MODE_PRIVATE);
                mySetting = preferences.getString("mySetting", null);
                String tmp=sec.getText().toString();
                //to check the box isn't empty
                if(tmp.matches("")) {
                    Snackbar.make(v, "You Have To Enter A Number!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                //to check if a change did happen
                else if(sec.getText().toString().equals(mySetting)){
                    Snackbar.make(v, "You didn't make any change!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                //greater then 1
                else if(Integer.parseInt(sec.getText().toString())<=0 || Integer.parseInt(sec.getText().toString())>30){
                    Snackbar.make(v, "Number of Sec must be between 1-30", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                //to apply changes
                else {
                    editor = preferences.edit();
                    editor.putString("mySetting", sec.getText().toString());
                    editor.apply();
                    Snackbar.make(v, "Saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}





