package com.taboola.sdk4example.sdk_classic;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.FlagsConst;
import com.taboola.sdk4example.R;
import com.taboola.sdk4example.tabs.BaseTaboolaFragment;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.TBLClassicUnit;
import com.taboola.android.Taboola;
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE;
import com.taboola.android.listeners.TBLClassicListener;
import com.taboola.android.utils.TBLSdkDetailsHelper;

import java.util.HashMap;
import java.util.List;


public class FeedLazyLoadInsideRecyclerViewFragment extends BaseTaboolaFragment {


    // In RecyclerView the widget will be rendered only when the user will scroll to widget
    // so no need to fetch when page is selected

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.feed_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(viewId));
    }

    public static FeedLazyLoadInsideRecyclerViewFragment getInstance(String viewId) {
        FeedLazyLoadInsideRecyclerViewFragment baseTaboolaFragment = new FeedLazyLoadInsideRecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIEW_ID, viewId);
        baseTaboolaFragment.setArguments(bundle);
        return baseTaboolaFragment;
    }


    static TBLClassicUnit createTaboolaWidget(Context context, boolean infiniteWidget) {
        TBLClassicPage tblClassicPage =
                Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);

        TBLClassicUnit tblClassicUnit = tblClassicPage.build(context, Const.FEED_PLACEMENT_NAME, Const.FEED_MODE, TBL_PLACEMENT_TYPE.FEED, new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }
        });


        int height = infiniteWidget ? TBLSdkDetailsHelper.getDisplayHeight(context) * 2 : ViewGroup.LayoutParams.WRAP_CONTENT;
        tblClassicUnit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return tblClassicUnit;
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private TBLClassicUnit mInfiniteTaboolaView;
        private String mViewId;


        RecyclerViewAdapter(String viewId) {
            mData = ListItemsGenerator.getGeneratedData(false);
            mViewId = viewId;
        }

        private void buildBelowArticleWidget(TBLClassicUnit tblClassicUnit) {
            tblClassicUnit
                    .setTargetType("mix")
                    .setInterceptScroll(true);

            //optional
            if (!TextUtils.isEmpty(mViewId)) {
                tblClassicUnit.setPageId(mViewId);
            }

            //used for enable horizontal scroll
            HashMap<String, String> extraProperties = new HashMap<>();
            extraProperties.put(FlagsConst.ENABLE_HORIZONTAL_SCROLL, "true");
            extraProperties.put(FlagsConst.USE_ONLINE_TEMPLATE, "true");
            tblClassicUnit.setUnitExtraProperties(extraProperties);
            mInfiniteTaboolaView.setUnitExtraProperties(extraProperties);

            tblClassicUnit.fetchContent();
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

                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                    if (mInfiniteTaboolaView == null) {
                        mInfiniteTaboolaView = createTaboolaWidget(parent.getContext(), true);
                        buildBelowArticleWidget(mInfiniteTaboolaView);
                    }

                    return new ViewHolderTaboola(mInfiniteTaboolaView);

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
}
