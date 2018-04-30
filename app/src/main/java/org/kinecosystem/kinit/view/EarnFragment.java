package org.kinecosystem.kinit.view;

import static org.kinecosystem.kinit.view.BaseFragmentKt.TAB_INDEX;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.EarnFragmentBinding;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.viewmodel.earn.EarnViewModel;

public class EarnFragment extends BaseFragment implements TabFragment {

    // hack needed because of the strange way ViewPager / FragmentPagerAdapter work.
    // The first fragment requested and displayed (in our case EarnFragment)
    // cannot be found in the list of fragments retrieved from the fragment manager
    // when the activity is resumed. (see MainActivity onScreenVisibleToUser)
    // Will soon change tabs implementation to use plain Views instead of fragments
    // and happily remove this hack.
    private boolean visibilityReportedOnce = false;

    public static EarnFragment newInstance(int tabIndex) {
        EarnFragment fragment = new EarnFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_INDEX, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private EarnViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        EarnFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.earn_fragment, container, false);
        model = new EarnViewModel(getCoreComponents(), new Navigator(getActivity()));
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        model.onResume();
        if (!visibilityReportedOnce) {
            onScreenVisibleToUser();
        }
    }

    public void onScreenVisibleToUser() {
        model.onScreenVisibleToUser();
        visibilityReportedOnce = true;
    }
}
