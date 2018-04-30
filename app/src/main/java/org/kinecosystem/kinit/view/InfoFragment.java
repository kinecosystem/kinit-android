package org.kinecosystem.kinit.view;

import static org.kinecosystem.kinit.view.BaseFragmentKt.TAB_INDEX;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.kinecosystem.kinit.BuildConfig;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickSupportButton;
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewProfilePage;
import org.kinecosystem.kinit.analytics.Events.Business.SupportRequestSent;
import org.kinecosystem.kinit.util.SupportUtil;


public class InfoFragment extends BaseFragment implements TabFragment {

    public static InfoFragment newInstance(int tabIndex) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_INDEX, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        view.findViewById(R.id.email_support).setOnClickListener(email -> {
            getCoreComponents().analytics().logEvent(new ClickSupportButton());
            getCoreComponents().analytics().logEvent(new SupportRequestSent());
            SupportUtil.openEmailSupport(getActivity(), getCoreComponents().userRepo());
        });
        TextView versionView = view.findViewById(R.id.version);
        if (BuildConfig.DEBUG) {
            versionView.setText(getString(R.string.version, "DEBUG-" + BuildConfig.VERSION_NAME));
        } else {
            versionView.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        }
        return view;
    }

    @Override
    public void onScreenVisibleToUser() {
        getCoreComponents().analytics().logEvent(new ViewProfilePage());
    }

}
