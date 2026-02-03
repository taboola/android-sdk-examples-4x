package com.taboola.sdk4example.sdk_classic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.taboola.android.listeners.TBLExploreMoreClassicListener;
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.R;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.Taboola;

/**
 * An activity demonstrating the integration of the Taboola SDK's Explore More feature.
 * <p>
 * This activity initializes a Taboola Classic Page and provides UI to demonstrate
 * both the {@code showExploreMore} and {@code setExploreMoreBackButtonTrigger} APIs.
 * <p>
 * The activity displays a loading indicator ("Explore More Loading") while Explore More
 * is being loaded. Once loading completes successfully, two buttons appear:
 * <ul>
 *   <li>"Show Explore More" - Manually triggers the Explore More modal</li>
 *   <li>"Set Back Button Trigger" - Sets up automatic Explore More display on back button press</li>
 * </ul>
 * Both buttons disappear after either one is clicked to indicate that an action has been taken.
 * <p>
 * The back button trigger will automatically show Explore More on back button press if:
 * - Explore More has been loaded successfully
 * - The screen is a root screen (back button would exit the app)
 */
public class ExploreMoreActivity extends AppCompatActivity {

    private static final String TAG = "ExploreMoreActivity";
    private TBLClassicPage tblClassicPage;
    private Button showExploreMoreButton;
    private Button setBackButtonTriggerButton;
    private LinearLayout exploreMoreLoadingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_more);

        tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);

        initExploreMore(this);
        exploreMoreLoadingContainer = findViewById(R.id.loading_container);
        setUpButtons();
    }

    /**
     * Configures the Explore More feature by initializing it with the Taboola SDK.
     * <p>
     * Sets up listeners that handle the loading state UI:
     * - Hides the loading indicator when Explore More loads successfully or fails
     * - Shows the action buttons when Explore More loads successfully
     *
     * @param context The application or activity context required for Taboola initialization.
     */
    private void initExploreMore(Context context) {
        TBLExploreMoreClassicListener tblExploreMoreClassicListener;
        tblExploreMoreClassicListener = new TBLExploreMoreClassicListener() {
            @Override
            public void onAdReceiveSuccess() {
                super.onAdReceiveSuccess();
                Log.d(TAG, "Taboola | onAdReceiveSuccess");
                // Hide loading indicator
                exploreMoreLoadingContainer.setVisibility(View.GONE);
                // Show buttons after Explore More is successfully loaded
                if (showExploreMoreButton != null && setBackButtonTriggerButton != null) {
                    showExploreMoreButton.setVisibility(View.VISIBLE);
                    setBackButtonTriggerButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void exploreMoreDidOpen() {
                super.exploreMoreDidOpen();
                Log.d(TAG, "Taboola | exploreMoreDidOpen");
            }

            @Override
            public void onAdReceiveFail(String error) {
                super.onAdReceiveFail(error);
                Log.d(TAG, "Taboola | onAdReceiveFail: " + error);
                // Hide loading indicator
                exploreMoreLoadingContainer.setVisibility(View.GONE);
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
     * Sets up the UI buttons to demonstrate both {@code showExploreMore} and
     * {@code setExploreMoreBackButtonTrigger} APIs.
     * <p>
     * The buttons are hidden initially and will appear only after Explore More is
     * successfully loaded
     * <p>
     * When the "Show Explore More" button is clicked, it immediately displays the
     * Explore More modal and both buttons disappear.
     * <p>
     * When the "Set Back Button Trigger" button is clicked, it configures the back
     * button to automatically show Explore More (if conditions are met) and both
     * buttons disappear.
     */
    private void setUpButtons() {
        showExploreMoreButton = findViewById(R.id.show_explore_more_btn);
        setBackButtonTriggerButton = findViewById(R.id.set_back_button_trigger_btn);

        // Hide buttons initially - they will appear after Explore More is loaded
        showExploreMoreButton.setVisibility(View.GONE);
        setBackButtonTriggerButton.setVisibility(View.GONE);

        showExploreMoreButton.setOnClickListener(view -> {
            Log.d(TAG, "Show Explore More button pressed");
            tblClassicPage.showExploreMore(getSupportFragmentManager());
            showExploreMoreButton.setVisibility(View.GONE);
            setBackButtonTriggerButton.setVisibility(View.GONE);
        });

        setBackButtonTriggerButton.setOnClickListener(view -> {
            Log.d(TAG, "Set Back Button Trigger button pressed");
            setUpBackButtonTrigger();
            showExploreMoreButton.setVisibility(View.GONE);
            setBackButtonTriggerButton.setVisibility(View.GONE);
        });
    }

    /**
     * Sets up the back button trigger using the SDK's built-in method.
     * <p>
     * This configures the system back button to automatically show Explore More
     * when pressed, but only if:
     * <ul>
     *   <li>Explore More has been loaded successfully</li>
     *   <li>The screen is a root screen (back button would exit the app)</li>
     * </ul>
     * This method is called when the user clicks the "Set Back Button Trigger" button.
     */
    private void setUpBackButtonTrigger() {
        Activity activity = this;
        LifecycleOwner viewLifecycleOwner = this;

        tblClassicPage.setExploreMoreBackButtonTrigger(
                activity,
                getOnBackPressedDispatcher(),
                viewLifecycleOwner,
                getSupportFragmentManager()
        );
    }
}

