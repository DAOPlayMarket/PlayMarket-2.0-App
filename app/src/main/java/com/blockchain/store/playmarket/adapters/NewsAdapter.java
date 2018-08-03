package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeedItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private PlaymarketFeed playmarketFeed;

    public NewsAdapter(PlaymarketFeed playmarketFeed) {
        this.playmarketFeed = playmarketFeed;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_adapter_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(playmarketFeed.items.get(position));
    }

    @Override
    public int getItemCount() {
        return playmarketFeed.items.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_title) TextView title;
        @BindView(R.id.news_body) TextView body;
        @BindView(R.id.news_read_more) TextView readMore;
        @BindView(R.id.news_publication_date) TextView publicationDate;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PlaymarketFeedItem playmarketFeedItem) {
            title.setText(playmarketFeedItem.title);
            body.setText(playmarketFeedItem.content);
            try {
                Date dateWithOldFormat = inputFormat.parse(playmarketFeedItem.pubDate);
                publicationDate.setText(outputFormat.format(dateWithOldFormat));
            } catch (ParseException e) {
                e.printStackTrace();
                publicationDate.setVisibility(View.GONE);
            }
        }
    }
}
