package com.example.kangjonghyuk.calendar_0603;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by kangjonghyuk on 2016. 5. 26..
 */
public class IntroActivity extends Activity {

    private static int progress;
    private ProgressBar mProgressStick;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introlayout);

        progress = 0;
        mProgressStick = (ProgressBar)findViewById(R.id.introBar);
        mProgressStick.setMax(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 3){
                    progressStatus = doSomeWork();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressStick.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressStick.setVisibility(View.GONE);
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
            private int doSomeWork(){
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                return ++progress;
            }
        }).start();

    }
}
