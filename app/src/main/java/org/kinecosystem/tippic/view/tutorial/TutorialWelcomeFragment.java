package org.kinecosystem.tippic.view.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.tippic.R;


public class TutorialWelcomeFragment extends Fragment {

    public static TutorialWelcomeFragment newInstance() {
        return new TutorialWelcomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_welcome, container, false);

        return view;
    }

}
