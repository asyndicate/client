package io.gitlab.asyndicate.asyndicate;

import android.app.Application;

import io.gitlab.asyndicate.asyndicate.helpers.Settings;

public class Asyndicate extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Settings.getInstance(this);
    }
}
