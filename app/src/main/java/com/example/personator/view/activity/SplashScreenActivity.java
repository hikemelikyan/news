package com.example.personator.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.personator.R;
import com.example.personator.databinding.ActivitySplashScreenBinding;
import com.example.personator.shared.helpers.SharedPreferencesHelper;
import com.example.personator.view.activity.mainActivity.MainActivity;

import static com.example.personator.shared.config.Common.API_KEY;
import static com.example.personator.shared.config.Common.FIELDS_TO_SHOW;
import static com.example.personator.shared.config.Common.JSON;
import static com.example.personator.shared.config.Common.PAGE_SIZE;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesHelper mShared = new SharedPreferencesHelper(getApplication());
        mShared.addStringSharedPreferences(API_KEY, "853259f1-c362-4af3-9d8a-ed881b9ea454");
        mShared.addStringSharedPreferences(JSON, "json");
        mShared.addStringSharedPreferences(FIELDS_TO_SHOW, "headline,bodyText,thumbnail,firstPublicationDate");
        mShared.addStringSharedPreferences(PAGE_SIZE, "10");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        mBinding.splashLogo.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
