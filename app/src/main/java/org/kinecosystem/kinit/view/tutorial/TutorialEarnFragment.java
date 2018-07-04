package org.kinecosystem.kinit.view.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;


public class TutorialEarnFragment extends Fragment {

    public static TutorialEarnFragment newInstance() {
        return new TutorialEarnFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_earn, container, false);

        return view;
    }

}
