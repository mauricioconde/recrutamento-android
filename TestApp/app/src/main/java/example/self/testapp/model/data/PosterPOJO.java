package example.self.testapp.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PosterPOJO {
    private static final String THUMB_TAG = "thumb";
    private static final String MEDIUM_TAG = "medium";
    private static final String FULL_TAG = "full";
    private String full;
    private String medium;
    private String thumb;


    public PosterPOJO(JSONObject jsonObject){
        try {
            this.thumb = new String( jsonObject.getString(THUMB_TAG).getBytes("ISO-8859-1") , "UTF-8");
            this.medium = new String( jsonObject.getString(MEDIUM_TAG).getBytes("ISO-8859-1") , "UTF-8");
            this.full = new String( jsonObject.getString(FULL_TAG).getBytes("ISO-8859-1") , "UTF-8");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getFull() {return full;}

    public String getMedium() {
        return medium;
    }

    public String getThumb() {
        return thumb;
    }
}
