package example.self.testapp.model.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ImagesPOJO {
    private static final String THUMB_TAG = "thumb";
    private static final String POSTER_TAG = "poster";

    private String thumb;
    private PosterPOJO poster;



    public ImagesPOJO(JSONObject jsonObject){
        try {
            this.thumb = new String( (jsonObject.getJSONObject(THUMB_TAG)).getString("full").getBytes("ISO-8859-1") , "UTF-8");
            this.poster = new PosterPOJO(jsonObject.getJSONObject(POSTER_TAG));
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException e) {
        }
    }

    public String getThumb() {
        return thumb;
    }

    public PosterPOJO getPoster() {
        return poster;
    }
}
