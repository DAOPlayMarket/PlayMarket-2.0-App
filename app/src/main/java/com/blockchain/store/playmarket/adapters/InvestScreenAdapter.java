package com.blockchain.store.playmarket.adapters;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.InvestTempPojo;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.blockchain.store.playmarket.interfaces.InvestAdapterCallback;
import com.blockchain.store.playmarket.utilities.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvestScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int INVEST_VIEWTYPE_MAIN = 0;
    public static final int INVEST_VIEWTYPE_YOUTUBE = 1;
    public static final int INVEST_VIEWTYPE_BODY = 2;
    public static final int INVEST_VIEWTYPE_TITLE = 3;
    public static final int INVEST_VIEWTYPE_MEMBER = 4;
    public static final int INVEST_VIEWTYPE_SOCIAL = 5;
    public static final int INVEST_VIEWETYPE_IMAGE_GALLERY = 6;

    private static final long MILLIS_30_DAYS = 2646000000L;
    private static final String DATE_FORMAT = "DD:HH:mm:ss";

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
            String body = (String) investTempPojo.objects.get(position);
            ((InvestBodyMessageViewHolder) holder).bind(body);
        }
        if (holder instanceof InvestTitleViewHolder) {
            String title = (String) investTempPojo.objects.get(position);
            ((InvestTitleViewHolder) holder).bind(title);
        }
        if (holder instanceof InvestMemberViewHolder) {
            ((InvestMemberViewHolder) holder).bind();
        }
        if (holder instanceof InvestGalleryViewHolder) {
            ((InvestGalleryViewHolder) holder).bind();
        }
        if (holder instanceof InvestSocialMediaViewHolder) {
            ((InvestSocialMediaViewHolder) holder).bind();
        }

    }

    @Override
    public int getItemCount() {
        return investTempPojo.objects.size();
    }

    public class InvestMainViewHolder extends RecyclerView.ViewHolder {
        private CountDownTimer countDownTimer;
        private SimpleDateFormat simpleDateFormat;
        private Button investButton;
        private TextView timeRemains;

        public InvestMainViewHolder(View itemView) {
            super(itemView);
            timeRemains = itemView.findViewById(R.id.invest_time_remains);

            simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            investButton = itemView.findViewById(R.id.invest_btn);
            countDownTimer = new CountDownTimer(MILLIS_30_DAYS, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String formattedString = simpleDateFormat.format(new Date(millisUntilFinished));
                    timeRemains.setText(formattedString);

                }

                @Override
                public void onFinish() {

                }
            }.start();
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
        private TextView bodyView;

        public InvestBodyMessageViewHolder(View itemView) {
            super(itemView);
            bodyView = itemView.findViewById(R.id.body_message);
        }

        public void bind(String body) {
            bodyView.setText(body);
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

        public void bind() {

        }
    }

    public class InvestTitleViewHolder extends RecyclerView.ViewHolder {
        /* One line text */
        private TextView titleView;

        public InvestTitleViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.invest_title_view);
        }

        public void bind(String title) {
            titleView.setText(title);
        }
    }

    public class InvestGalleryViewHolder extends RecyclerView.ViewHolder implements ImageListAdapterCallback {

        private ImageViewer.Builder imageViewerBuilder;
        private ImageListAdapter imageAdapter;
        private RecyclerView recyclerView;
        private ArrayList<String> imagePaths = new ArrayList<>();

        public InvestGalleryViewHolder(View itemView) {
            super(itemView);
            imagePaths.add("https://n000001.playmarket.io:3000/data/IPFS/QmdwWfuR3Y4ZsYGB7uJLn9Xj1PKJ9oMdJ8BxNaPEdMK7CX/pictures/1.jpg");
            imagePaths.add("https://n000001.playmarket.io:3000/data/IPFS/QmdwWfuR3Y4ZsYGB7uJLn9Xj1PKJ9oMdJ8BxNaPEdMK7CX/pictures/2.jpg");
            imagePaths.add("https://n000001.playmarket.io:3000/data/IPFS/QmdwWfuR3Y4ZsYGB7uJLn9Xj1PKJ9oMdJ8BxNaPEdMK7CX/pictures/3.jpg");
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            imageAdapter = new ImageListAdapter(imagePaths, this);
            imageViewerBuilder = new ImageViewer.Builder(itemView.getContext(), imagePaths);
            recyclerView.setAdapter(imageAdapter);
        }

        public void bind() {

        }

        @Override
        public void onImageGalleryItemClicked(int position) {
            imageViewerBuilder.setStartPosition(position).show();
        }
    }


}
