package example.self.testapp.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class IdsPOJO{
    public static final String SLUG_TAG = "slug";

    private Integer trakt;
    private String slug;
    private Integer tvdb;
    private String imdb;
    private Integer tmdb;
    private Integer tvrage;

    public IdsPOJO(Integer trakt, String slug, Integer tvdb, String imdb, Integer tmdb, Integer tvrage){
        this.trakt = trakt;
        this.slug = slug;
        this.tvdb = tvdb;
        this.imdb = imdb;
        this.tmdb = tmdb;
        this.tvrage = tvrage;
    }

    public IdsPOJO(JSONObject jsonObject){
        try {
            this.slug = new String( jsonObject.getString(SLUG_TAG).getBytes("ISO-8859-1") , "UTF-8");
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException e) {
        }
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
