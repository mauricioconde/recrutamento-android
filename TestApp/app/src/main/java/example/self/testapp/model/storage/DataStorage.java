package example.self.testapp.model.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for accessing and modifying preference data stored inside app
 */
public class DataStorage {
    private static final String FILE_NAME = "photoDir_prefs";

    /**
     * Save a string value
     * @param pKey
     * @param pValue
     * @param pContext
     */
    public static  void persistStringValue(String pKey, String pValue, Context pContext){
        SharedPreferences prefs = pContext.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pKey,pValue);
        editor.commit();
    }

    /**
     * Returns the String value associated with the 'pKey' if exists, otherwise returns an empty String
     * @param pKey
     * @param pContext
     * @return
     */
    public static  String retrieveStringValue(String pKey, Context pContext){
        SharedPreferences prefs = pContext.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(pKey,"");
    }
}
