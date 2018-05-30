package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class InvestScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public InvestScreenAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    public class InvestorAdvantagesViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * title
         * description*/

        public InvestorAdvantagesViewHolder(View itemView) {
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


}
