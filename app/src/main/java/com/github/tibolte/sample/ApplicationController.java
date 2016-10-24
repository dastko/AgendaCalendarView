package com.github.tibolte.sample;

import android.app.Application;
import android.content.res.Configuration;
import android.widget.ImageView;

import java.util.Locale;

/**
 * Created by dastko on 8/12/16.
 */
public class ApplicationController extends Application {

    private static ApplicationController mSingleton;
    private ImageView mImageView;

    @Override
    public void onCreate() {
        super.onCreate();
//        String languageToLoad  = "nl_NL"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());

        mSingleton = this;
//        Locale locale = new Locale("nl_NL");
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getApplicationContext().getResources().updateConfiguration(config, null);

    }

    public static ApplicationController getInstance() {
        return mSingleton;
    }


    public ImageView getmImageView() {
        return mImageView;
    }

    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }
}
