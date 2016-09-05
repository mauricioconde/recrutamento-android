package example.self.testapp.controller.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import example.self.testapp.R;
import example.self.testapp.controller.adapter.ListAdapter;
import example.self.testapp.model.application.TestApp;
import example.self.testapp.model.data.EpisodePOJO;
import example.self.testapp.model.dataRequest.DataRequest;
import example.self.testapp.model.utils.Utils;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getName();
    public static final String KEY_SEASON_SHOW_ID = "season.showId";
    public static final String KEY_SEASON_HEADER_BG = "season.headerBG";
    public static final String KEY_SEASON_NUMBER = "season.number";
    public static final String KEY_SERIE_THUMB = "season.show.thum";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private ImageView headerBG, serieThumbnail;
    private TextView rating, messageNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        messageNoResult = (TextView) findViewById(R.id.tv_no_results);
        rating = (TextView) findViewById(R.id.tv_rating);
        headerBG = (ImageView) findViewById(R.id.img_toolbar_bg);
        serieThumbnail = (ImageView) findViewById(R.id.img_serie_thumbnail);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        configureView();
    }

    private void configureView(){
        //Header BG
        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra(KEY_SEASON_HEADER_BG))
                .placeholder(R.mipmap.season_background_placeholder)
                .into(headerBG);
        //Serie thumbnial
        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra(KEY_SERIE_THUMB))
                .placeholder(R.mipmap.serie_thumbnail_placeholder)
                .into(serieThumbnail);

        //Rating
        rating.setText(MainActivity.getCurrentShow().getScore());

        //Toolbar title
        String toolbarTitle = getString(R.string.season_tag) + " " + getIntent().getStringExtra(KEY_SEASON_NUMBER);
        collapsingToolbarLayout.setTitle(toolbarTitle);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        //List title
        ((TextView)findViewById(R.id.detail_list_title)).setText(getString(R.string.episodes_title));

        //Create and assign the adapter to View
        recyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);
        recyclerView.setAdapter(new ListAdapter(ListAdapter.TYPE_EPISODE));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getEpisodesForSeason();
    }

    /** Retrieves all episodes corresponding to a specific season **/
    private void getEpisodesForSeason(){
        String showId = getIntent().getStringExtra(KEY_SEASON_SHOW_ID);
        String seasonNumber = getIntent().getStringExtra(KEY_SEASON_NUMBER);

        DataRequest.getInstance().getEpisodesForSeason(showId, seasonNumber, listener);
    }

    /** Listener for update UI once we have the episodes for a season **/
    private DataRequest.OnCompleteListener<ArrayList<EpisodePOJO>> listener = new DataRequest.OnCompleteListener<ArrayList<EpisodePOJO>>() {
        @Override
        public void onSuccess(ArrayList<EpisodePOJO> episodes) {
            //Show the episodes on the UI
            ((ListAdapter)recyclerView.getAdapter()).updateUI(null,episodes);
            messageNoResult.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure() {
            Utils.showSimpleAlert(DetailActivity.this,
                    TestApp.getAppContext().getString(R.string.title_error),
                    TestApp.getAppContext().getString(R.string.message_error));
            ((ListAdapter)recyclerView.getAdapter()).updateUI(null, new ArrayList<EpisodePOJO>());
            messageNoResult.setVisibility(View.VISIBLE);
        }
    };

    //Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); //Close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
