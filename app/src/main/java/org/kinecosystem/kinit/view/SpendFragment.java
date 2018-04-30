package org.kinecosystem.kinit.view;

import static org.kinecosystem.kinit.view.BaseFragmentKt.TAB_INDEX;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.SpendFragmentBinding;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.view.adapter.OfferListAdapter;
import org.kinecosystem.kinit.viewmodel.spend.SpendViewModel;

public class SpendFragment extends BaseFragment implements TabFragment {

    public static SpendFragment newInstance(int tabIndex) {
        SpendFragment fragment = new SpendFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_INDEX, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private SpendViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        SpendFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.spend_fragment, container, false);
        model = new SpendViewModel(getCoreComponents(), new Navigator(getActivity()));
        binding.setModel(model);
        binding.offersList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.offersList.setAdapter(new OfferListAdapter(getActivity(), model));
        return binding.getRoot();
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
