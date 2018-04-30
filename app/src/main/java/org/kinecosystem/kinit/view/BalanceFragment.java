package org.kinecosystem.kinit.view;

import static org.kinecosystem.kinit.view.BaseFragmentKt.TAB_INDEX;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.BalanceFragmentBinding;
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel;


public class BalanceFragment extends BaseFragment implements TabFragment {

    private BalanceViewModel model;

    public static BalanceFragment newInstance(int tabIndex) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_INDEX, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        BalanceFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.balance_fragment, container, false);
        model = new BalanceViewModel(getCoreComponents());
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void onScreenVisibleToUser() {
        model.onScreenVisibleToUser();
    }
}
