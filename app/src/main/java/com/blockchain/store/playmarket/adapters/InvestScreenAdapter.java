package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.InvestTempPojo;
import com.blockchain.store.playmarket.interfaces.InvestAdapterCallback;
import com.blockchain.store.playmarket.utilities.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.stfalcon.frescoimageviewer.ImageViewer;

public class InvestScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int INVEST_VIEWTYPE_MAIN = 0;
    public static final int INVEST_VIEWTYPE_YOUTUBE = 1;
    public static final int INVEST_VIEWTYPE_BODY = 2;
    public static final int INVEST_VIEWTYPE_TITLE = 3;
    public static final int INVEST_VIEWTYPE_MEMBER = 4;
    public static final int INVEST_VIEWTYPE_SOCIAL = 5;
    public static final int INVEST_VIEWETYPE_IMAGE_GALLERY = 6;
    private final InvestAdapterCallback adapterCallback;
    private InvestTempPojo investTempPojo;

    public InvestScreenAdapter(InvestAdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
        investTempPojo = new InvestTempPojo();
    }

    @Override
    public int getItemViewType(int position) {
        return investTempPojo.objectViewType.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case INVEST_VIEWTYPE_MAIN:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_main_view, parent, false);
                return new InvestMainViewHolder(view);
            case INVEST_VIEWTYPE_YOUTUBE:
                View investYoutubeViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_youtube_view, parent, false);

                InvestYoutubeViewHolder youtubeViewHolder = new InvestYoutubeViewHolder(investYoutubeViewHolder);
                youtubeViewHolder.youTubePlayerView.initialize(Constants.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                        if (!wasRestored) {
                            youTubePlayer.cueVideo("QYjyfCt6gWc");
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
                return youtubeViewHolder;
            case INVEST_VIEWTYPE_BODY:
                View investBodyMessageViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_body_view, parent, false);

                return new InvestBodyMessageViewHolder(investBodyMessageViewHolder);
            case INVEST_VIEWTYPE_TITLE:
                View investTitleViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_title_view, parent, false);
                return new InvestTitleViewHolder(investTitleViewHolder);
            case INVEST_VIEWTYPE_MEMBER:
                View investMemberViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_member_view, parent, false);
                return new InvestMemberViewHolder(investMemberViewHolder);
            case INVEST_VIEWTYPE_SOCIAL:

                break;
            case INVEST_VIEWETYPE_IMAGE_GALLERY:
                View investGalleryViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.invest_gallery_view, parent, false);
                return new InvestGalleryViewHolder(investGalleryViewHolder);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InvestMainViewHolder) {
            ((InvestMainViewHolder) holder).bind();
        }
        if (holder instanceof InvestYoutubeViewHolder) {
            ((InvestYoutubeViewHolder) holder).bind();
        }
        if (holder instanceof InvestBodyMessageViewHolder) {
            ((InvestBodyMessageViewHolder) holder).bind();
        }
        if (holder instanceof InvestTitleViewHolder) {
            ((InvestTitleViewHolder) holder).bind();
        }
        if (holder instanceof InvestMemberViewHolder) {
            ((InvestMemberViewHolder) holder).bind();
        }
        if (holder instanceof InvestGalleryViewHolder) {
            ((InvestGalleryViewHolder) holder).bind();
        }
//        if (holder instanceof InvestMainViewHolder) {
//            ((InvestMainViewHolder) holder).bind();
//        }

    }

    @Override
    public int getItemCount() {
        return investTempPojo.objects.size();
    }

    public class InvestMainViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * App icon
         * app name
         * app description
         * progress bar limits,current value
         * button 'invest'
         * */
        private Button investButton;

        public InvestMainViewHolder(View itemView) {
            super(itemView);
            investButton = itemView.findViewById(R.id.invest_btn);
        }

        public void bind() {
            investButton.setOnClickListener(v -> adapterCallback.onInvestBtnClicked("test address"));
        }
    }

    public class InvestYoutubeViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * Youtube's video id
         * */
        private YouTubePlayerView youTubePlayerView;

        public InvestYoutubeViewHolder(View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.invest_youtube_player);
        }

        public YouTubePlayerView getYouTubePlayerView() {
            return youTubePlayerView;
        }

        public void bind() {

        }
    }

    public class InvestBodyMessageViewHolder extends RecyclerView.ViewHolder {
        /* requires:
         * title
         * description*/

        public InvestBodyMessageViewHolder(View itemView) {
            super(itemView);
        }

        public void bind() {

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

        public void bind() {

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

        public void bind() {

        }
    }

    public class InvestGalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageViewer.Builder imageViewerBuilder;
        private ImageListAdapter imageAdapter;
        private RecyclerView recyclerView;

        public InvestGalleryViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//            imageAdapter = new ImageListAdapter(null,null);
//            recyclerView.setAdapter(imageAdapter);
        }

        public void bind() {

        }
    }


}
