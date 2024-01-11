package com.zaroslikov.mycountjava2.db;

import android.app.Application;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.AppMetricaConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Creating an extended library configuration.
        AppMetricaConfig config = AppMetricaConfig.newConfigBuilder("dcce2d98-8c24-4d1e-bed3-74473a400fdb").build();
        // Initializing the AppMetrica SDK.
        AppMetrica.activate(this, config);
    }
}