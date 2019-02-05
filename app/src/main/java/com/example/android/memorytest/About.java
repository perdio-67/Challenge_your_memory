package com.example.android.memorytest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView credit=(TextView)findViewById(R.id.credit);
        credit.setText("Developed by: perdió\n" +
                "Idea by: olik09\n" +
                "Contact us on the following:\n" +
                "ThreshHold6@gmail.com");
        //back button
        final Button back_about = (Button) findViewById(R.id.back_About);
        back_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(About.this, DesignActivity.class);
                //startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
