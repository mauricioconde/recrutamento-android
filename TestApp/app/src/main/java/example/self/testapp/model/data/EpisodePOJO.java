package example.self.testapp.model.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Wrapper for manage an Episode
 */
public class EpisodePOJO {
    public static final String SEASON_TAG = "season";
    public static final String NUMBER_TAG = "number";
    public static final String TITLE_TAG = "title";
    public static final String IDS_TAG = "ids";

    private int season;
    private int number;
    private String title;
    private IdsPOJO ids;
    private String tag;



    public EpisodePOJO(JSONObject jsonObject){
        try {
            this.season = jsonObject.getInt(SEASON_TAG);
            this.number = jsonObject.getInt(NUMBER_TAG);
            this.title = new String( jsonObject.getString(TITLE_TAG).getBytes("ISO-8859-1") , "UTF-8");
            this.ids = new IdsPOJO(jsonObject.getJSONObject(IDS_TAG));
            this.tag = "E" + number;
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException e) {
        }
    }

    //Getters & setters
    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }
}
