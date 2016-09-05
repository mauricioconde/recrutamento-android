package example.self.testapp.controller.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import example.self.testapp.R;
import example.self.testapp.controller.activity.DetailActivity;
import example.self.testapp.controller.activity.MainActivity;
import example.self.testapp.model.application.TestApp;
import example.self.testapp.model.data.EpisodePOJO;
import example.self.testapp.model.data.SeasonPOJO;

/**
 * Custom subclass of RecyclerView.Adapter
 * Used to provide the seasons & episodes previews to the UI
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {
    public static final int TYPE_SEASON = 1;
    public static final int TYPE_EPISODE = 2;
    private ArrayList<EpisodePOJO> episodes;
    private ArrayList<SeasonPOJO> seasons;
    private int totalItems;
    int type;



    //Constructor
    public ListAdapter(int type) {
        this.type = type;
        this.totalItems = 0;
        if(type == TYPE_SEASON) this.seasons = new ArrayList<>();
        else this.episodes = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if(type == TYPE_SEASON) holder.bindItem(type, seasons.get(position), null);
        else holder.bindItem(type, null,episodes.get(position));
    }

    @Override
    public int getItemCount() { return totalItems; }

    public void updateUI(ArrayList<SeasonPOJO> seasons, ArrayList<EpisodePOJO> episodes) {
        if(type == TYPE_SEASON){
            this.seasons = seasons;
            this.totalItems = seasons.size();
        }
        else {
            this.episodes = episodes;
            this.totalItems = episodes.size();
        }
        notifyDataSetChanged();
    }

    //Custom View Holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static String SEASON_TAG;
        private TextView itemTitle;
        private TextView itemIcon;
        private ImageView itemIconImg;
        private SeasonPOJO season;
        private int type;

        public ItemViewHolder(View view) {
            super(view);

            SEASON_TAG = view.getContext().getString(R.string.season_tag);
            itemTitle = (TextView) view.findViewById(R.id.item_list_title);
            itemIcon = (TextView) view.findViewById(R.id.item_list_icon);
            itemIconImg = (ImageView) view.findViewById(R.id.item_list_icon_img);
            view.setOnClickListener(this);
        }

        public void bindItem(int type, SeasonPOJO seasonPOJO, EpisodePOJO episodePOJO) {
            this.type = type;
            if(type == TYPE_SEASON){
                itemTitle.setText(SEASON_TAG + " " + seasonPOJO.getNumber());
                itemIcon.setVisibility(View.GONE);
                itemIconImg.setVisibility(View.VISIBLE);
                Picasso.with(TestApp.getAppContext())
                        .load(seasonPOJO.getImages().getPoster().getThumb())
                        .placeholder(R.mipmap.serie_thumbnail_placeholder)
                        .into(itemIconImg);
                season = seasonPOJO;
            }else if(type == TYPE_EPISODE){
                itemTitle.setText(episodePOJO.getTitle());
                itemIcon.setText(episodePOJO.getTag());
                itemIcon.setVisibility(View.VISIBLE);
                itemIconImg.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            if(type == TYPE_SEASON){
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(DetailActivity.KEY_SEASON_SHOW_ID, season.getIds().getSlug());
                bundle.putString(DetailActivity.KEY_SEASON_NUMBER, season.getNumber() + "");
                bundle.putString(DetailActivity.KEY_SEASON_HEADER_BG, season.getImages().getThumb());
                bundle.putString(DetailActivity.KEY_SERIE_THUMB, season.getImages().getPoster().getThumb());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }else if(type == TYPE_EPISODE){
                Toast.makeText(TestApp.getAppContext(), "coming soon", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
