package example.self.testapp.model.application;

import android.app.Application;
import android.content.Context;

/**
 * Custom application class.
 * Implements tasks we want to execute only once per application start
 */
public class TestApp extends Application {
    //Permission constants

    //Application instance
    private static TestApp mInstance;

    //Application context
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // This method fires once as well as constructor
        // & here we have application context
        mAppContext = getApplicationContext();
    }

    public static TestApp getInstance(){return mInstance;}
    public static Context getAppContext(){return mAppContext;}
}
