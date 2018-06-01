package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;


public class InvestScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int INVEST_VIEWTYPE_MAIN = 0;
    public static final int INVEST_VIEWTYPE_YOUTUBE = 1;
    public static final int INVEST_VIEWTYPE_BODY = 2;
    public static final int INVEST_VIEWTYPE_TITLE = 3;
    public static final int INVEST_VIEWTYPE_MEMBER = 4;
    public static final int INVEST_VIEWTYPE_SOCIAL = 5;

    public InvestScreenAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case INVEST_VIEWTYPE_MAIN:
                View investMainViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_main_view, parent, false);
                break;
            case INVEST_VIEWTYPE_YOUTUBE:
                View investYoutubeViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_youtube_view, parent, false);
                break;
            case INVEST_VIEWTYPE_BODY:
                View investBodyMessageViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_body_view, parent, false);

                break;
            case INVEST_VIEWTYPE_TITLE:
                View investTitleViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_title_view, parent, false);
                break;
            case INVEST_VIEWTYPE_MEMBER:
                View investMemberViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_member_view, parent, false);
                break;
            case INVEST_VIEWTYPE_SOCIAL:

                break;

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class InvestMainViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * App icon
         * app name
         * app description
         * progress bar limits,current value
         * button 'invest'
         * */
        public InvestMainViewHolder(View itemView) {
            super(itemView);
        }

        public void bind() {

        }
    }

    public class InvestYoutubeViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * Youtube's video id
         * */
        public InvestYoutubeViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class InvestBodyMessageViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * title
         * description*/

        public InvestBodyMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class InvestScreenshotsViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * screenshots
         * */
        public InvestScreenshotsViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class InvestMemberViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * photo, name, description, social media icons and links*/
        public InvestMemberViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class InvestSocialMediaViewHolder extends RecyclerView.ViewHolder {

        public InvestSocialMediaViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class InvestTitleViewHolder extends RecyclerView.ViewHolder {
        /* One line text */
        public InvestTitleViewHolder(View itemView) {
            super(itemView);
        }
    }


}
