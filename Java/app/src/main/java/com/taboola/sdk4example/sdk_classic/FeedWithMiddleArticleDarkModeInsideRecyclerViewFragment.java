package com.taboola.sdk4example.sdk_classic;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.R;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.TBLClassicUnit;
import com.taboola.android.Taboola;
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE;
import com.taboola.android.listeners.TBLClassicListener;

import java.util.HashMap;
import java.util.List;

public class FeedWithMiddleArticleDarkModeInsideRecyclerViewFragment extends Fragment {

    private static final String TAG = "Feed+MidArticleDarkMode";

    private static TBLClassicUnit tblClassicUnitMiddle;
    private static TBLClassicUnit tblClassicUnitBottom;
    private HashMap<String, String> extraProperties = new HashMap<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TBLClassicPage tblClassicPage =
                Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);

        //set dark mode according to the device theme setting
        setDarkModeFlag();

        tblClassicUnitMiddle = createTaboolaWidget(tblClassicPage);
        tblClassicUnitBottom = createTaboolaFeed(inflater.getContext(), tblClassicPage);


        return inflater.inflate(R.layout.fragment_rv_sample, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.feed_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(tblClassicUnitMiddle, tblClassicUnitBottom));
    }

    public TBLClassicUnit createTaboolaWidget(TBLClassicPage tblClassicPage) {
        TBLClassicUnit tblClassicUnit = tblClassicPage.build(getContext(), Const.WIDGET_MIDDLE_PLACEMENT_NAME, Const.WIDGET_MIDDLE_MODE_1X2, TBL_PLACEMENT_TYPE.PAGE_MIDDLE, new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }

            @Override
            public void onAdReceiveSuccess() {
                super.onAdReceiveSuccess();
                Log.d(TAG, "onAdReceiveSuccess");
            }

            @Override
            public void onAdReceiveFail(String error) {
                Log.d(TAG, "onAdReceiveFailed" + error);
                switch (error) {
                    case "NO_ITEMS":
                        Log.d(TAG, "Taboola server returned a valid response, but without any items");
                        break;
                    case "TIMEOUT":
                        Log.d(TAG, "no response from Taboola server after 10 seconds");
                        break;
                    case "WRONG_PARAMS":
                        Log.d(TAG, "wrong Taboola mode");
                        break;
                    case "RESPONSE_ERROR":
                        Log.d(TAG, "Taboola server is not reachable, or it returned a bad response");
                        break;
                    default:
                        Log.d(TAG, "UNKNOWN_ERROR");
                }
            }

            @Override
            public void onResize(int height) {
                super.onResize(height);
                Log.d(TAG, "taboolaViewResized" + height);
            }
        });

        tblClassicUnit.setUnitExtraProperties(extraProperties);
        tblClassicUnit.fetchContent();
        return tblClassicUnit;
    }

    public TBLClassicUnit createTaboolaFeed(Context context, TBLClassicPage tblClassicPage) {
        TBLClassicUnit tblClassicUnit = tblClassicPage.build(getContext(), "Feed without video", "thumbs-feed-01", TBL_PLACEMENT_TYPE.FEED, new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }

            @Override
            public void onAdReceiveSuccess() {
                super.onAdReceiveSuccess();
                Log.d(TAG, "onAdReceiveSuccess");
            }
        });

        tblClassicUnit.setUnitExtraProperties(extraProperties);

        tblClassicUnit.fetchContent();
        return tblClassicUnit;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private final TBLClassicUnit tblClassicUnitMiddle;
        private final TBLClassicUnit tblClassicUnitBottom;


        RecyclerViewAdapter(TBLClassicUnit tblClassicUnitMiddleWidget, TBLClassicUnit tblClassicUnitBottomWidget) {
            mData = ListItemsGenerator.getGeneratedDataForWidgetDynamic(true);
            tblClassicUnitMiddle = tblClassicUnitMiddleWidget;
            tblClassicUnitBottom = tblClassicUnitBottomWidget;
        }


        @Override
        public int getItemViewType(int position) {
            ListItemsGenerator.FeedListItem item = getItem(position);
            return item.type;
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        @NonNull
        private ListItemsGenerator.FeedListItem getItem(int position) {
            return mData.get(position);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {

                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_MID_ITEM:
                    return new ViewHolderTaboola(tblClassicUnitMiddle);

                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                    return new ViewHolderTaboola(tblClassicUnitBottom);

                default:
                case ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM:
                    View appCompatImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_item, parent, false);
                    return new RandomImageViewHolder(appCompatImageView);
            }
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ListItemsGenerator.FeedListItem item = getItem(position);

            if (item.type == ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM) {
                RandomImageViewHolder vh = (RandomImageViewHolder) holder;
                ListItemsGenerator.RandomItem randomItem = (ListItemsGenerator.RandomItem) item;
                final ImageView imageView = vh.imageView;
                imageView.setBackgroundColor(randomItem.color);
                vh.textView.setText(randomItem.randomText);
            }
        }


        static class RandomImageViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;
            private final TextView textView;

            RandomImageViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.feed_item_iv);
                textView = view.findViewById(R.id.feed_item_tv);
            }
        }

        static class ViewHolderTaboola extends RecyclerView.ViewHolder {

            ViewHolderTaboola(View view) {
                super(view);
            }
        }
    }

    private void setDarkModeFlag() {
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Device dark mode on
                extraProperties.put("darkMode", "true");
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Device dark mode off
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }
}