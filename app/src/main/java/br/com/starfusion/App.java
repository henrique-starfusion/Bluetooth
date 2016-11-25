package br.com.starfusion;

import android.app.Application;
import android.content.Context;

import butterknife.ButterKnife;

/**
 * Created by Henrique on 09/08/2016.
 */
public class App extends Application {
    private static App mInstance;

    public App(){
        mInstance = App.this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }

    public static App instance(){
        return mInstance;
    }

    public static Context context(){
        return mInstance.getApplicationContext();
    }
}