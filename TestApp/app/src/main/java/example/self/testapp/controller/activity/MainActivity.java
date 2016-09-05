package example.self.testapp.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import example.self.testapp.R;
import example.self.testapp.controller.adapter.ListAdapter;
import example.self.testapp.model.application.TestApp;
import example.self.testapp.model.data.SeasonPOJO;
import example.self.testapp.model.data.ShowPOJO;
import example.self.testapp.model.dataRequest.DataRequest;
import example.self.testapp.model.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int INVALID_INDEX = -1;
    private static final String KEY_CURRENT_SHOW_INDEX = "currentShowID";

    private ArrayList<ShowPOJO> shows;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView messageSelectShow;
    private static int currentShowIndex;
    private static ShowPOJO currentShow;


    public MainActivity(){
        currentShowIndex = INVALID_INDEX;
        currentShow = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Harcoded shows
        shows = ShowPOJO.getShows();

        configureView();

        if(savedInstanceState != null){
            currentShowIndex = savedInstanceState.getInt(KEY_CURRENT_SHOW_INDEX);
            if(currentShowIndex != INVALID_INDEX){
                currentShow = shows.get(currentShowIndex);
                DataRequest.getInstance().getSeasonsFromDeviceStorage(this, currentShow.getIds().getSlug(), listener);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_SHOW_INDEX,currentShowIndex);
    }


    private void configureView(){
        messageSelectShow = (TextView) findViewById(R.id.tv_select_show);

        //List title
        ((TextView)findViewById(R.id.main_list_title)).setText(getString(R.string.seasons_title));

        //Create and assign the adapter to View
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setAdapter(new ListAdapter(ListAdapter.TYPE_SEASON));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /** Retrieves all seasons for a show specified by showId.
     * ShowId: Trakt.tv ID, Trakt.tv slug, or IMDB ID**/
    private void getSeasonsForShow(final String showId){
        DataRequest.getInstance().getSeasonsForShow(showId, listener);
    }

    /** Listener for update UI once we have the seasons for a show **/
    private DataRequest.OnCompleteListener<ArrayList<SeasonPOJO>> listener = new DataRequest.OnCompleteListener<ArrayList<SeasonPOJO>>() {
        @Override
        public void onSuccess(ArrayList<SeasonPOJO> seasons) {
            //Hide message
            messageSelectShow.setVisibility(View.INVISIBLE);
            //Update toolbar title
            toolbar.setTitle(currentShow.getTitle());
            //Show the seasons on the UI
            ((ListAdapter)recyclerView.getAdapter()).updateUI(seasons,null);
        }

        @Override
        public void onFailure() {
            Utils.showSimpleAlert(MainActivity.this,
                    TestApp.getAppContext().getString(R.string.title_error),
                    TestApp.getAppContext().getString(R.string.message_error));
            messageSelectShow.setVisibility(View.INVISIBLE);
            ((ListAdapter)recyclerView.getAdapter()).updateUI(new ArrayList<SeasonPOJO>(),null);
        }
    };

    public static ShowPOJO getCurrentShow(){
        return (currentShowIndex != INVALID_INDEX)?  currentShow : null;
    }


    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        currentShowIndex = INVALID_INDEX;
        switch (item.getItemId()){
            case R.id.action_show1:
                currentShowIndex = 0;
                break;
            case R.id.action_show2:
                currentShowIndex = 1;
                break;
            case R.id.action_show3:
                currentShowIndex = 2;
                break;
            default:
                break;
        }
        currentShow = shows.get(currentShowIndex);
        getSeasonsForShow(currentShow.getIds().getSlug());
        return true;
    }
}
