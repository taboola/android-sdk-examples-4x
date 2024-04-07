package com.taboola.sdk4example.sdk_native;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taboola.sdk4example.R;

public class SDKNativeMenuFragment extends Fragment implements View.OnClickListener {

    private SDKNativeMenuFragment.OnFragmentInteractionListener onFragmentInteractionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SDKNativeMenuFragment.OnFragmentInteractionListener) {
            onFragmentInteractionListener = (SDKNativeMenuFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.main_menu_lyt);
        addButton(getString(R.string.native_widget), R.id.native_widget, viewGroup);
        addButton(getString(R.string.native_feed), R.id.native_feed, viewGroup);

    }

    @Override
    public void onClick(View v) {
        String screenName = v.getTag().toString();
        Fragment fragmentToOpen = null;
        switch (v.getId()) {

            case R.id.native_widget:
                fragmentToOpen = new WidgetFragment();
                break;
            case R.id.native_feed:
                fragmentToOpen = new FeedFragment();
                break;
        }

        if (fragmentToOpen != null) {
            openFragment(fragmentToOpen, screenName);
        }
    }

    private void openFragment(Fragment fragment, String screenName) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onMenuItemClicked(fragment, screenName);
        }
    }

    private void addButton(String screenName, int id, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false);
        textView.setText(screenName);
        textView.setTag(screenName);
        textView.setId(id);
        textView.setOnClickListener(this);

        viewGroup.addView(textView, viewGroup.getChildCount() - 1);
    }

    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(Fragment fragment, String screenName);
    }
}