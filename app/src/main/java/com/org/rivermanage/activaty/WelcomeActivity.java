package com.org.rivermanage.activaty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.org.rivermanage.R;

public class WelcomeActivity extends Activity {

    private final static int TIME = 3000;
    private final static int GO_GUIDE = 2000;
    private final static int GO_HOME = 2001;

    private boolean isFirstIn;

    private Intent intent = new Intent();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case GO_GUIDE:
                    goGuide();
                    break;

                case GO_HOME:
                    goHome();
                    break;

            }

        }
    };

    /**
     * 进入主页
     */
    private void goHome() {

        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * 进入引导页
     */
    private void goGuide() {

        intent.setClass(this, GuideActivity.class);

        intent.putExtra("welcome", true);

        startActivity(intent);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        SharedPreferences preferences = getSharedPreferences(DataUtil.sharedPreferenceFile, MODE_PRIVATE);
        isFirstIn = preferences.getBoolean(DataUtil.isFirstInKey, true);

        if (isFirstIn) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
        }

    }
}
