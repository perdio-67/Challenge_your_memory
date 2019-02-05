package com.example.android.memorytest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Collections;

public class Phase2 extends AppCompatActivity {
    SharedPreferences preferences;
    String mySetting;
    final ArrayList<Num> Num = new ArrayList<>();
    time Time = new time();
    Hide hide = new Hide();
    int sec;
    //boolean to check
    boolean ready=false;
    //ad stuff
    private InterstitialAd mInterstitialAd;

    //for the sync
    Object lk1 = new Object();
    //handler for threads
    @SuppressLint("HandlerLeak")
    Handler handler1 = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            if (msg.arg2 == 0) {
                TextView secTxt = (TextView) findViewById(R.id.sec);
                secTxt.setTextSize(45);
                secTxt.setText(Integer.toString(msg.arg1));
                if(msg.arg1==0) {
                    ready = true;
                }

            } else if (msg.arg2 == 1) {
                final ArrayList<Button> Btn = new ArrayList<>();
                Btn.add((Button) findViewById(R.id.num1));
                Btn.add((Button) findViewById(R.id.num2));
                Btn.add((Button) findViewById(R.id.num3));
                Btn.add((Button) findViewById(R.id.num4));
                Btn.add((Button) findViewById(R.id.num5));
                Btn.add((Button) findViewById(R.id.num6));
                Btn.add((Button) findViewById(R.id.num7));
                Btn.add((Button) findViewById(R.id.num8));
                Btn.get(msg.arg1).setText("#");
                Btn.get(msg.arg1).setTextColor(Color.BLACK);
                Btn.get(msg.arg1).setBackgroundColor(Color.CYAN);
                if (msg.arg1 == 7) {
                    TextView secTxt = (TextView) findViewById(R.id.sec);
                    secTxt.setTextColor(Color.DKGRAY);
                    secTxt.setTextSize(24);
                    secTxt.setText("Go");
                    //enable restart button
                    final Button restart = (Button) findViewById(R.id.restart);
                    restart.setVisibility(View.VISIBLE);
                    restart.setEnabled(true);
                    for (int i = 0; i < 8; i++) {
                        Num.get(i).Button.setEnabled(true);
                    }
                }

            } else if (msg.arg2 == 2) {
                final ArrayList<Button> Btn = new ArrayList<>();
                Btn.add((Button) findViewById(R.id.num1));
                Btn.add((Button) findViewById(R.id.num2));
                Btn.add((Button) findViewById(R.id.num3));
                Btn.add((Button) findViewById(R.id.num4));
                Btn.add((Button) findViewById(R.id.num5));
                Btn.add((Button) findViewById(R.id.num6));
                Btn.add((Button) findViewById(R.id.num7));
                Btn.add((Button) findViewById(R.id.num8));
                Btn.get(msg.arg1).setText(msg.obj.toString());
                Btn.get(msg.arg1).setBackgroundColor(Color.RED);


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //action start
        setContentView(R.layout.phase2);
        //get the sttings values
        preferences = getSharedPreferences("mySettings", Context.MODE_PRIVATE);
        mySetting = preferences.getString("mySetting", null);
        if (mySetting != null && !mySetting.equals(""))
            sec = Integer.parseInt(mySetting) - 1;
        else
            sec = 5;
        //identifying all the buttons 1-8
        Num.add(new Num((Button) findViewById(R.id.num1), "1", 0));
        Num.add(new Num((Button) findViewById(R.id.num2), "2", 1));
        Num.add(new Num((Button) findViewById(R.id.num3), "3", 2));
        Num.add(new Num((Button) findViewById(R.id.num4), "4", 3));
        Num.add(new Num((Button) findViewById(R.id.num5), "5", 4));
        Num.add(new Num((Button) findViewById(R.id.num6), "6", 5));
        Num.add(new Num((Button) findViewById(R.id.num7), "7", 6));
        Num.add(new Num((Button) findViewById(R.id.num8), "8", 7));
        Num.add(new Num((Button) findViewById(R.id.num9), "9", 8));
        Num.add(new Num((Button) findViewById(R.id.num10), "10", 9));
        Num.add(new Num((Button) findViewById(R.id.num11), "11", 10));
        Num.add(new Num((Button) findViewById(R.id.num12), "12", 11));
        //arranging the array and shuffling
        final ArrayList<String> numbers = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            numbers.add(Integer.toString(i + 1));
        }
        Collections.shuffle(numbers);
        //setting the random values
        for (int i = 0; i < Num.size(); i++) {
            Num.get(i).Button.setText(numbers.get(i));
            Num.get(i).setNum(numbers.get(i));
            Num.get(i).Button.setEnabled(false);
        }
        Collections.sort(numbers);
        lk1=new Object();
        //Time thread starts
        Time.start();
        //set text to be #
        hide.start();
        //the number buttons
        Num.get(0).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(0).Button.getText().equals("#")) {
                    if (Num.get(0).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(0).Button.setBackgroundColor(Color.GREEN);
                        Num.get(0).Button.setText(Num.get(0).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(0).Button.setBackgroundColor(Color.RED);
                        Num.get(0).Button.setText(Num.get(0).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i <12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });

        Num.get(1).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(1).Button.getText().equals("#")) {
                    if (Num.get(1).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(1).Button.setBackgroundColor(Color.GREEN);
                        Num.get(1).Button.setText(Num.get(1).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(1).Button.setBackgroundColor(Color.RED);
                        Num.get(1).Button.setText(Num.get(1).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(2).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(2).Button.getText().equals("#")) {
                    if (Num.get(2).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(2).Button.setBackgroundColor(Color.GREEN);
                        Num.get(2).Button.setText(Num.get(2).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(2).Button.setBackgroundColor(Color.RED);
                        Num.get(2).Button.setText(Num.get(2).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 8; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(3).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(3).Button.getText().equals("#")) {
                    if (Num.get(3).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(3).Button.setBackgroundColor(Color.GREEN);
                        Num.get(3).Button.setText(Num.get(3).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(3).Button.setBackgroundColor(Color.RED);
                        Num.get(3).Button.setText(Num.get(3).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(4).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(4).Button.getText().equals("#")) {
                    if (Num.get(4).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(4).Button.setBackgroundColor(Color.GREEN);
                        Num.get(4).Button.setText(Num.get(4).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(4).Button.setBackgroundColor(Color.RED);
                        Num.get(4).Button.setText(Num.get(4).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(5).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(5).Button.getText().equals("#")) {
                    if (Num.get(5).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(5).Button.setBackgroundColor(Color.GREEN);
                        Num.get(5).Button.setText(Num.get(5).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(5).Button.setBackgroundColor(Color.RED);
                        Num.get(5).Button.setText(Num.get(5).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(6).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(6).Button.getText().equals("#")) {
                    if (Num.get(6).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(6).Button.setBackgroundColor(Color.GREEN);
                        Num.get(6).Button.setText(Num.get(6).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(6).Button.setBackgroundColor(Color.RED);
                        Num.get(6).Button.setText(Num.get(6).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(7).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(7).Button.getText().equals("#")) {
                    if (Num.get(7).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(7).Button.setBackgroundColor(Color.GREEN);
                        Num.get(7).Button.setText(Num.get(7).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(7).Button.setBackgroundColor(Color.RED);
                        Num.get(7).Button.setText(Num.get(7).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(8).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(8).Button.getText().equals("#")) {
                    if (Num.get(8).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(8).Button.setBackgroundColor(Color.GREEN);
                        Num.get(8).Button.setText(Num.get(8).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(8).Button.setBackgroundColor(Color.RED);
                        Num.get(8).Button.setText(Num.get(8).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(9).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(9).Button.getText().equals("#")) {
                    if (Num.get(9).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(9).Button.setBackgroundColor(Color.GREEN);
                        Num.get(9).Button.setText(Num.get(9).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(9).Button.setBackgroundColor(Color.RED);
                        Num.get(9).Button.setText(Num.get(9).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(10).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(10).Button.getText().equals("#")) {
                    if (Num.get(10).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(10).Button.setBackgroundColor(Color.GREEN);
                        Num.get(10).Button.setText(Num.get(10).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(10).Button.setBackgroundColor(Color.RED);
                        Num.get(10).Button.setText(Num.get(10).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        Num.get(11).Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Num.get(11).Button.getText().equals("#")) {
                    if (Num.get(11).getNum() == numbers.get(0)) {
                        numbers.remove(0);
                        Num.get(11).Button.setBackgroundColor(Color.GREEN);
                        Num.get(11).Button.setText(Num.get(11).Num);
                        if (numbers.size() == 0) {
                            TextView secTxt = (TextView) findViewById(R.id.sec);
                            secTxt.setText("You Won");
                        }
                    } else {
                        Num.get(11).Button.setBackgroundColor(Color.RED);
                        Num.get(11).Button.setText(Num.get(11).Num);
                        TextView secTxt = (TextView) findViewById(R.id.sec);
                        secTxt.setTextColor(Color.RED);
                        secTxt.setText("You Lost");
                        for (int i = 0; i < 12; i++) {
                            Num.get(i).Button.setEnabled(false);
                        }
                        Show show = new Show(Num, Integer.parseInt(numbers.get(0)));
                        show.start();
                    }
                }
            }
        });
        //add stuff
        AdView mAdView=(AdView)findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1377227789295092~6728332750");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }
        });
        //restart button
        final Button restart = (Button) findViewById(R.id.restart);
        restart.setEnabled(false);
        restart.setVisibility(View.INVISIBLE);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartActivity();
            }
        });
        //back button
        final Button back_design = (Button) findViewById(R.id.back_design);
        back_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Design.this, DesignActivity.class);
                //startActivity(intent);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    finish();
                }
            }
        });

    }

    public void restartActivity() {
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }

    public class Num {
        final private Button Button;
        private String Num;
        int order;

        public Num(Button Button, String Num, int order) {
            this.Button = Button;
            this.Num = Num;
            this.order = order;
        }

        public android.widget.Button getButton() {
            return Button;
        }

        public String getNum() {
            return Num;
        }

        public void setNum(String num) {
            Num = num;
        }
    }

    class time extends Thread {

        public void run() {
            super.run();

            for (int i = sec; i >= 0; i--) {
                Message msg = Message.obtain();
                msg.arg1 = i;
                msg.arg2 = 0;
                handler1.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    class Hide extends Thread {
        private ArrayList Num;

        public void run() {
            super.run();
            synchronized (lk1) {
                while(!ready){

                }
                for (int i = 0; i < 8; i++) {
                    Message msg = Message.obtain();
                    msg.arg1 = i;
                    msg.arg2 = 1;
                    handler1.sendMessage(msg);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Show extends Thread {
        private ArrayList<Num> Num;
        private int size;
        int tmp = size;

        public Show(ArrayList<Num> Num, int size) {
            this.Num = Num;
            this.size = size;
            this.tmp = size;
        }

        public void run() {
            super.run();
            for (int i = size - 1; i < 8; i++, tmp++) {
                Message msg = Message.obtain();
                for (int j = 0; j < 8; j++) {
                    if (Integer.parseInt(Num.get(j).getNum()) == tmp) {
                        msg.arg1 = Num.get(j).order;
                        msg.obj = Num.get(j).getNum();
                    }
                }
                msg.arg2 = 2;
                handler1.sendMessage(msg);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //the dadicated back button "do nothing xD"
    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }


    }

}
