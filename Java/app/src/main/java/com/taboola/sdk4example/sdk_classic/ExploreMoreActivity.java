package com.taboola.sdk4example.sdk_classic;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.taboola.android.listeners.TBLExploreMoreClassicListener;
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.R;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.Taboola;

/**
 * An activity demonstrating the integration of the Taboola SDK's Explore More feature.
 * <p>
 * This activity initializes a Taboola Classic Page and sets up the back button trigger
 * that conditionally displays the "Explore More" screen upon a system back button press,
 * allowing users to view more content before exiting the view.
 * <p>
 * The activity uses the SDK's built-in back button trigger which automatically handles
 * showing Explore More only if it has been loaded and the screen is a root screen.
 */
public class ExploreMoreActivity extends AppCompatActivity {
    private TBLClassicPage tblClassicPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_more);

        tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);

        initExploreMore(this);
        setUpBackButtonTrigger();
    }

    /**
     * Configures the Explore More feature.
     *
     * @param context The application or activity context required for Taboola initialization.
     */
    private void initExploreMore(Context context) {
        TBLExploreMoreClassicListener tblExploreMoreClassicListener;
        tblExploreMoreClassicListener = new TBLExploreMoreClassicListener() {
            @Override
            public void onAdReceiveSuccess() {
                super.onAdReceiveSuccess();
            }

            @Override
            public void exploreMoreDidOpen() {
                super.exploreMoreDidOpen();
            }
        };

        tblClassicPage.initExploreMore(
                context,
                tblExploreMoreClassicListener,
                Const.EXPLORE_MORE_PLACEMENT_NAME,
                Const.FEED_MODE,
                Const.EXPLORE_MORE_CUSTOM_SEGMENT_SUBSCRIBER);
    }

    /**
     * Sets up the back button trigger using the SDK's built-in method.
     * This will automatically show Explore More on back button press if:
     * - Explore More has been loaded successfully
     * - The screen is a root screen (back button would exit the app)
     * Note that Explore More can also be manually triggered by any other action.
     */
    private void setUpBackButtonTrigger() {
        tblClassicPage.setExploreMoreBackButtonTrigger(
                this,
                getOnBackPressedDispatcher(),
                this,
                getSupportFragmentManager()
        );
    }
}

