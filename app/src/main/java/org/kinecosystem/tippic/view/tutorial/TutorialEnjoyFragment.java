package org.kinecosystem.tippic.view.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.tippic.R;


public class TutorialEnjoyFragment extends Fragment {

    public static TutorialEnjoyFragment newInstance() {
        return new TutorialEnjoyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_enjoy, container, false);

        return view;
    }

}
