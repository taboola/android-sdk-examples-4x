package com.taboola.sdk4example.sdk_native;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taboola.android.TBLPublisherInfo;
import com.taboola.android.Taboola;
import com.taboola.android.listeners.TBLNativeListener;
import com.taboola.android.tblnative.TBLNativePage;
import com.taboola.android.tblnative.TBLNativeUnit;
import com.taboola.android.tblnative.TBLPlacement;
import com.taboola.android.tblnative.TBLRecommendationRequestCallback;
import com.taboola.android.tblnative.TBLRecommendationsResponse;
import com.taboola.android.tblnative.TBLRequestData;
import com.taboola.sdk4example.R;

import java.util.ArrayList;

/**
 * To implement a Taboola Feed in "Native Integration" we use a RecyclerView to layout incoming items.
 */
public class FeedFragment extends Fragment {
    private static final String TAG = FeedFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ArrayList<Object> mData = new ArrayList<>();
    private TBLNativeUnit mNativeUnit = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  Define a RecyclerView in the XML layout file
        View root = inflater.inflate(R.layout.fragment_native_feed, container, false);
        mRecyclerView = root.findViewById(R.id.endless_feed_recycler_view);

        // Setup RecyclerView
        setupRecyclerView(root.getContext());

        // Create and return a Taboola Unit
        getTaboolaUnit();

        // Fetch content for Unit
        fetchRecommendations();

        return root;
    }

    /**
     * Basic setup for the RecyclerView with an emphasis on pulling additional items when user scrolls to bottom of View.
     */
    private void setupRecyclerView(Context context) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Load more Taboola content when reaching scroll bottom
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Fetching more content if we got to the end of the scrolling. if recycler can't scroll sown any more and the state is IDLE try to load next recommendations
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fetchRecommendations();
                }
            }
        });

        mRecyclerView.setAdapter(new NativeFeedAdapter(mData));
    }

    /**
     * Taboola "Native Integration" fetchRecommendations call handle first and subsequent content fetch calls.
     * This method fetches items for this Feed implementation.
     */
    private void fetchRecommendations() {
        if (mNativeUnit != null) {
            mNativeUnit.fetchRecommendations(new TBLRecommendationRequestCallback() {
                @Override
                public void onRecommendationsFetched(TBLRecommendationsResponse tblRecommendationsResponse) {
                    Log.d(TAG, "Taboola | fetchInitialContent | onRecommendationsFetched");
                    // Send content to RecyclerView Adapter
                    addRecommendationToFeed(tblRecommendationsResponse);
                }

                @Override
                public void onRecommendationsFailed(Throwable throwable) {
                    Log.d(TAG, String.format("Taboola | onRecommendationsFailed: %s", throwable.getLocalizedMessage()));
                }
            });
        } else {
            Log.d(TAG, "Taboola | mNativeUnit is null");
        }
    }

    /**
     * This method parses the response with its items and adds them to the RecyclerView Adapter.
     */
    private void addRecommendationToFeed(TBLRecommendationsResponse recommendationsResponse) {
        if (recommendationsResponse != null) {
            TBLPlacement placement = recommendationsResponse.getPlacementsMap().values().iterator().next();

            if (placement != null) {
                // Update data structure
                mData.addAll(placement.getItems());

                // Update data in RecyclerView adapter
                int itemCount = mRecyclerView.getAdapter().getItemCount();
                if (itemCount > 0) {
                    mRecyclerView.getAdapter().notifyItemRangeInserted(itemCount, placement.getItems().size());
                }
            }
        } else {
            Log.d(TAG, "Taboola | recommendationsResponse is null");
        }
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     * Notice: A Unit of unlimited items, called "Feed" in Taboola, can be set in TBL_PLACEMENT_TYPE.PAGE_BOTTOM only.
     */
    private void getTaboolaUnit() {
        // Define a page to control all Unit placements on this screen
        TBLNativePage nativePage = Taboola.getNativePage("text", "https://blog.taboola.com");

        // Define a publisher info with publisher name and api key
        TBLPublisherInfo tblPublisherInfo = new TBLPublisherInfo("sdk-tester-demo").setApiKey("30dfcf6b094361ccc367bbbef5973bdaa24dbcd6");

        // Define a fetch request (with desired number of content items in setRecCount())
        TBLRequestData requestData = new TBLRequestData().setRecCount(4);

        // Define a single Unit to display
        mNativeUnit = nativePage.build("list_item", tblPublisherInfo, requestData, new TBLNativeListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                Log.d(TAG, String.format("Taboola | onItemClick | isOrganic = %s", isOrganic));
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }
        });
    }
}
