package example.self.testapp.model.dataRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import example.self.testapp.R;
import example.self.testapp.model.application.TestApp;
import example.self.testapp.model.data.EpisodePOJO;
import example.self.testapp.model.data.SeasonPOJO;
import example.self.testapp.model.data.ShowPOJO;
import example.self.testapp.model.storage.DataStorage;
import example.self.testapp.model.utils.Utils;

public class DataRequest {
    private static final String TAG = DataRequest.class.getName();
    private static final String KEY_SEASONS = "seasons";
    //TRAKT.TV API constants
    private String CLIENT_ID = "765eaeed83a064bd5088d0881422bc69c8f859508d619087681ffe7e14fb8d9f";
    private String CLIENT_SECRET = "65baee673e6f4b1a9478f591754eb475a90dbc3b374df5fdb6578d7d3a";
    //REST SERVICES URLs
    private static final String TRAKT_TV_SHOWS_SERVICE_URL = "https://api.trakt.tv/search/show?query=%s";
    private static final String TRAKT_TV_SEASONS_SERVICE_URL = "https://api.trakt.tv/shows/%s/seasons?extended=images";
    private static final String TRAKT_TV_EPISODES_SERVICE_URL = "https://api.trakt.tv/shows/%s/seasons/%s";
    //Volley required
    private RequestQueue rq;


    //Constructor
    private DataRequest(){
        rq = Volley.newRequestQueue(TestApp.getAppContext());
    }


    //Singleton
    private static DataRequest instance;
    public static synchronized DataRequest getInstance(){
        if(instance == null){
            instance= new DataRequest();
        }
        return instance;
    }


    //Methods
    /**
     *  Gets all seasons for a show specified by showId which
     * can be: Trakt.tv ID, Trakt.tv slug, or IMDB ID
     */
    public void getSeasonsForShow(final String showId, final OnCompleteListener<ArrayList<SeasonPOJO>> listener){
        try{
            String URL = String.format(TRAKT_TV_SEASONS_SERVICE_URL, URLEncoder.encode(showId, "UTF-8"));
            executeGetRequest(URL, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String response) {
                    try {
                        ArrayList<SeasonPOJO> seasons = new ArrayList<>();
                        JSONArray parsedResponse = new JSONArray(response);
                        for (int i = 1; i < parsedResponse.length(); i++) {
                            seasons.add(new SeasonPOJO(parsedResponse.getJSONObject(i), showId));
                        }
                        listener.onSuccess(seasons);
                        DataStorage.persistStringValue(KEY_SEASONS, response, TestApp.getAppContext());
                    } catch (JSONException e) {
                        listener.onFailure();
                    }
                }

                @Override
                public void onFailure() {
                    listener.onFailure();
                }
            });
        } catch (UnsupportedEncodingException e) {
            listener.onFailure();
        }
    }

    /**
     * Gets all episodes for a specific season of a show.
     * The showId must be one of the following:
     * Trakt.tv ID, Trakt.tv slug, or IMDB ID
     */
    public void getEpisodesForSeason(String showId, String seasonNumber, final OnCompleteListener<ArrayList<EpisodePOJO>> listener){
        try{
            String URL = String.format(TRAKT_TV_EPISODES_SERVICE_URL,
                    URLEncoder.encode(showId, "UTF-8"),
                    URLEncoder.encode(seasonNumber, "UTF-8"));
            executeGetRequest(URL, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String response) {
                    try {
                        ArrayList<EpisodePOJO> episodes = new ArrayList<>();
                        JSONArray parsedResponse = new JSONArray(response);
                        for (int i = 0; i < parsedResponse.length(); i++) {
                            episodes.add(new EpisodePOJO(parsedResponse.getJSONObject(i)));
                        }
                        listener.onSuccess(episodes);
                    } catch (JSONException e) {
                        listener.onFailure();
                    }
                }

                @Override
                public void onFailure() {
                    listener.onFailure();
                }
            });
        } catch (UnsupportedEncodingException e) {
            listener.onFailure();
        }
    }

    private void executeGetRequest(String url, final OnCompleteListener<String> listener){
        if(!Utils.isOnline())listener.onFailure();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFailure();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("trakt-api-version", "2");
                params.put("trakt-api-key", CLIENT_ID);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        rq.add(stringRequest);
    }

    /**
     * Gets all seasons for a show specified by showId
     * from the device storage
     */
    public void getSeasonsFromDeviceStorage(Context context, String showId, final OnCompleteListener<ArrayList<SeasonPOJO>> listener){
        String response = DataStorage.retrieveStringValue(KEY_SEASONS, context);

        try {
            ArrayList<SeasonPOJO> seasons = new ArrayList<>();
            JSONArray parsedResponse = new JSONArray(response);
            for (int i = 1; i < parsedResponse.length(); i++) {
                seasons.add(new SeasonPOJO(parsedResponse.getJSONObject(i), showId));
            }
            listener.onSuccess(seasons);
        } catch (JSONException e) {
            listener.onFailure();
        }
    }

    public static abstract class OnCompleteListener<T> {
        public abstract void onSuccess(T param);
        public abstract void onFailure();
    }
}
