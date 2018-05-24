package org.kinecosystem.kinit.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.BalanceFragmentBinding;
import org.kinecosystem.kinit.view.adapter.BalancePagerViewsAdapter;
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel;

import static org.kinecosystem.kinit.view.BaseFragmentKt.TAB_INDEX;


public class BalanceFragment extends BaseFragment implements TabFragment {

    private BalanceViewModel model;
    private BalancePagerViewsAdapter adapter;

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
        ViewPager viewPager = binding.tabsContent;
        adapter = new BalancePagerViewsAdapter(getContext(), getCoreComponents(), binding);
        viewPager.setAdapter(adapter);
        binding.balanceNavTabs.setupWithViewPager(viewPager);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        model.onResume();
    }

    @Override
    public void onScreenVisibleToUser() {
        model.onScreenVisibleToUser();
    }
}
