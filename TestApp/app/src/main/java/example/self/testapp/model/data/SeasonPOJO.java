package example.self.testapp.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import example.self.testapp.controller.activity.MainActivity;

/**
 * Wrapper for manage a Season
 */
public class SeasonPOJO{
    public static final String NUMBER_TAG = "number";
    public static final String IDS_TAG = "ids";
    public static final String IMAGES_TAG = "images";

    private int number = -1;
    private IdsPOJO ids;
    private ImagesPOJO images;



    public SeasonPOJO(JSONObject jsonObject, String showId){
        try {
            this.number = jsonObject.getInt(NUMBER_TAG);
            this.ids = new IdsPOJO(jsonObject.getJSONObject(IDS_TAG));
            this.images = new ImagesPOJO(jsonObject.getJSONObject(IMAGES_TAG));
            this.ids.setSlug(showId);
        } catch (JSONException e) {
        }
    }

    public int getNumber() {
        return number;
    }

    public IdsPOJO getIds() {
        return ids;
    }

    public ImagesPOJO getImages() {
        return images;
    }
}
