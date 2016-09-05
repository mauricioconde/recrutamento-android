package example.self.testapp.model.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import example.self.testapp.model.application.TestApp;

/**
 * Utility methods
 */
public class Utils {

    public static void showSimpleAlert(final Context pContext, String pTitle, String pMessage){
        new AlertDialog.Builder(pContext)
                .setTitle(pTitle)
                .setMessage(pMessage)
                .setCancelable(true)
                .show();
    }

    public static boolean isOnline(){
        //Getting the ConnectivityManager.
        ConnectivityManager cm = (ConnectivityManager) TestApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Getting NetworkInfo from the Connectivity manager.
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //If I received an info and isConnectedOrConnecting return true then there is an Internet connection.
        if (netInfo != null && netInfo.isConnectedOrConnecting()) return true;
        return false;
    }
}
