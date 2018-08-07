package org.kinecosystem.kinit.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import org.kinecosystem.kinit.R;

public abstract class SingleFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_layout);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, getFragment()).commitNowAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitNowAllowingStateLoss();
    }

    protected abstract Fragment getFragment();
}