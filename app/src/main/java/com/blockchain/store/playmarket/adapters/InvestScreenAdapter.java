package com.blockchain.store.playmarket.adapters;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.InvestBody;
import com.blockchain.store.playmarket.data.entities.InvestMainItem;
import com.blockchain.store.playmarket.data.entities.InvestMember;
import com.blockchain.store.playmarket.data.entities.InvestTempPojo;
import com.blockchain.store.playmarket.data.entities.InvestTitle;
import com.blockchain.store.playmarket.data.entities.InvestYoutube;
import com.blockchain.store.playmarket.data.entities.ScreenShotBody;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.blockchain.store.playmarket.interfaces.InvestAdapterCallback;
import com.blockchain.store.playmarket.utilities.Constants;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.text.SimpleDateFormat;

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

    public InvestScreenAdapter(AppInfo appInfo, InvestAdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
        investTempPojo = new InvestTempPojo(appInfo);
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
        Object object = investTempPojo.objects.get(position);
        if (holder instanceof InvestMainViewHolder) {
            ((InvestMainViewHolder) holder).bind((InvestMainItem) object);
        }
        if (holder instanceof InvestYoutubeViewHolder) {
            ((InvestYoutubeViewHolder) holder).bind((InvestYoutube) object);
        }
        if (holder instanceof InvestBodyMessageViewHolder) {
            InvestBody investBody = (InvestBody) investTempPojo.objects.get(position);
            ((InvestBodyMessageViewHolder) holder).bind(investBody);
        }
        if (holder instanceof InvestTitleViewHolder) {
            InvestTitle investTitle = (InvestTitle) investTempPojo.objects.get(position);
            ((InvestTitleViewHolder) holder).bind(investTitle);
        }
        if (holder instanceof InvestMemberViewHolder) {
            InvestMember investMember = (InvestMember) investTempPojo.objects.get(position);
            ((InvestMemberViewHolder) holder).bind(investMember);
        }
        if (holder instanceof InvestGalleryViewHolder) {
            ScreenShotBody screenShotBody = (ScreenShotBody) investTempPojo.objects.get(position);
            ((InvestGalleryViewHolder) holder).bind(screenShotBody);
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
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        private Button investButton;
        private TextView timeRemains;
        private TextView currentStage;
        private TextView currentEarned;
        private TextView totalEarned;
        private SimpleDraweeView iconView;
        private ProgressBar progressBar;
        private TextView icoCurrency;

        public InvestMainViewHolder(View itemView) {
            super(itemView);
            timeRemains = itemView.findViewById(R.id.invest_time_remains);
            investButton = itemView.findViewById(R.id.invest_btn);
            currentStage = itemView.findViewById(R.id.invest_current_stage);
            currentEarned = itemView.findViewById(R.id.invest_current_earned);
            totalEarned = itemView.findViewById(R.id.invest_total_earned);
            iconView = itemView.findViewById(R.id.invest_app_logo);
            progressBar = itemView.findViewById(R.id.invest_progress_bar);
            icoCurrency = itemView.findViewById(R.id.invest_earned_currency);
        }

        public void bind(InvestMainItem investMainItem) {
            icoCurrency.setText(investMainItem.icoSymbol);
            currentStage.setText(String.valueOf(investMainItem.stageCurrent));
            progressBar.setMax(Integer.parseInt(investMainItem.icoTotalSupply));
            progressBar.setProgress((int) Double.parseDouble(investMainItem.soldTokens));
            currentEarned.setText(investMainItem.soldTokens);
            totalEarned.setText(investMainItem.icoTotalSupply);
            iconView.setImageURI(investMainItem.iconUrl);
            investButton.setOnClickListener(v -> adapterCallback.onInvestBtnClicked(investMainItem.adrIco));
            if (countDownTimer == null) {
                countDownTimer = new CountDownTimer(investMainItem.totalTime * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        unixTimeToDays(millisUntilFinished);
                        String formattedString = unixTimeToDays(millisUntilFinished);
                        //String formattedString = simpleDateFormat.format(new Date(millisUntilFinished));
                        timeRemains.setText(formattedString);
                    }

                    @Override
                    public void onFinish() {
//                        timeRemains.setText("00:00:00:00");
                    }
                }.start();
            }
        }
    }

    private String unixTimeToDays(long unixTimeSec){

        long divReminder = unixTimeSec;
        String result = "";

        int[] dimensionArr  = {86400000, 3600000, 60000, 1000};

        for (int dimension : dimensionArr) {
            if (divReminder > dimension) {

                long timeValue = divReminder / dimension;
                timeValue = (int) timeValue;
                String timeValueStr = ((timeValue <= 9) ? "0" + String.valueOf(timeValue) : String.valueOf(timeValue));
                result = result + ((dimension == dimensionArr[0])? timeValueStr + " days " : (dimension == dimensionArr[3])? timeValueStr : timeValueStr + ':');

                divReminder = unixTimeSec % dimension;
            } else {
                result = result + ((dimension == dimensionArr[0])? "00 days " : (dimension == dimensionArr[3])? "00" : "00:");
            }
        }

        return result;
    }

    public class InvestYoutubeViewHolder extends RecyclerView.ViewHolder {
        private YouTubePlayerView youTubePlayerView;
        private boolean isInitialized = false;

        public InvestYoutubeViewHolder(View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.invest_youtube_player);
        }

        public YouTubePlayerView getYouTubePlayerView() {
            return youTubePlayerView;
        }

        public void bind(InvestYoutube investYoutuber) {
            if (!isInitialized) {
                isInitialized = true;
                youTubePlayerView.initialize(Constants.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                        if (!wasRestored) {
                            youTubePlayer.cueVideo(investYoutuber.videoId);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
            }

        }
    }

    public class InvestBodyMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView bodyView;

        public InvestBodyMessageViewHolder(View itemView) {
            super(itemView);
            bodyView = itemView.findViewById(R.id.body_message);
        }

        public void bind(InvestBody investBody) {
            bodyView.setText(Html.fromHtml(investBody.body));
        }
    }

    public class InvestMemberViewHolder extends RecyclerView.ViewHolder {
        private TextView investMemberTitle;
        private TextView investMemberDescription;
        private SimpleDraweeView investMemberAvatar;
        private TextView gplusIcon;
        private TextView facebookIcon;
        private TextView linkedinIcon;
        private TextView twitterIcon;
        private TextView instagramIcon;
        private TextView vkIcon;
        private TextView youtubeIcon;
        private TextView telegramIcon;
        private TextView gitIcon;

        public InvestMemberViewHolder(View itemView) {
            super(itemView);
            investMemberTitle = itemView.findViewById(R.id.invest_member_name);
            investMemberDescription = itemView.findViewById(R.id.invest_member_description);
            investMemberAvatar = itemView.findViewById(R.id.invest_member_avatar);
            gplusIcon = itemView.findViewById(R.id.icon_gplus);
            facebookIcon = itemView.findViewById(R.id.icon_fb);
            linkedinIcon = itemView.findViewById(R.id.icon_linkedin);
            twitterIcon = itemView.findViewById(R.id.icon_twitter);
            instagramIcon = itemView.findViewById(R.id.icon_instagram);
            vkIcon = itemView.findViewById(R.id.icon_vk);
            youtubeIcon = itemView.findViewById(R.id.icon_youtube);
            telegramIcon = itemView.findViewById(R.id.icon_telegram);
            gitIcon = itemView.findViewById(R.id.icon_git);
        }

        public void bind(InvestMember investMember) {
            investMemberTitle.setText(investMember.name);
            investMemberDescription.setText(investMember.description);
            investMemberAvatar.setImageURI(investMember.imagePath);

            gplusIcon.setVisibility(investMember.socialLinks.googlePlus != null && investMember.socialLinks.googlePlus.isEmpty() ? View.VISIBLE : View.GONE);
            facebookIcon.setVisibility(investMember.socialLinks.facebook != null && investMember.socialLinks.facebook.isEmpty() ? View.VISIBLE : View.GONE);
            linkedinIcon.setVisibility(investMember.socialLinks.linkedin != null && investMember.socialLinks.linkedin.isEmpty() ? View.VISIBLE : View.GONE);
//            twitterIcon.setVisibility(investMember.socialLinks. != null && investMember.socialLinks.facebook.isEmpty() ? View.VISIBLE : View.GONE);
            twitterIcon.setVisibility(View.GONE);
            instagramIcon.setVisibility(investMember.socialLinks.instagram != null && investMember.socialLinks.instagram.isEmpty() ? View.VISIBLE : View.GONE);
            vkIcon.setVisibility(investMember.socialLinks.vk != null && investMember.socialLinks.vk.isEmpty() ? View.VISIBLE : View.GONE);
            youtubeIcon.setVisibility(investMember.socialLinks.youtube != null && investMember.socialLinks.youtube.isEmpty() ? View.VISIBLE : View.GONE);
            telegramIcon.setVisibility(investMember.socialLinks.telegram != null && investMember.socialLinks.telegram.isEmpty() ? View.VISIBLE : View.GONE);
            gitIcon.setVisibility(investMember.socialLinks.git != null && investMember.socialLinks.git.isEmpty() ? View.VISIBLE : View.GONE);
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

        public void bind(InvestTitle investTitle) {
            titleView.setText(investTitle.title);
        }
    }

    public class InvestGalleryViewHolder extends RecyclerView.ViewHolder implements ImageListAdapterCallback {

        private ImageViewer.Builder imageViewerBuilder;
        private ImageListAdapter imageAdapter;
        private RecyclerView recyclerView;

        public InvestGalleryViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        public void bind(ScreenShotBody screenShotBody) {
            if (imageAdapter == null) {
                imageAdapter = new ImageListAdapter(screenShotBody.screenShotsList, this);
                imageViewerBuilder = new ImageViewer.Builder(itemView.getContext(), screenShotBody.screenShotsList);
                recyclerView.setAdapter(imageAdapter);
            }

        }

        @Override
        public void onImageGalleryItemClicked(int position) {
            imageViewerBuilder.setStartPosition(position).show();
        }
    }


}
