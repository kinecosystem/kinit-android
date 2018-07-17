package org.kinecosystem.kinit.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickEngagementPush;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickMenuItem;
import org.kinecosystem.kinit.analytics.Events.Event;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.view.BottomTabNavigation.PageSelectionListener;
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements PageSelectionListener {

    public static final String REPORT_PUSH_ID_KEY = "report_push_id_key";
    public static final String REPORT_PUSH_TEXT_KEY = "report_push_text_key";
    private static final String SELECTED_TAB_INDEX_KEY = "selected_tab_index_key";

    @Inject
    Analytics analytics;
    @Inject
    UserRepository userRepository;
    private ViewPager viewPager;
    private TabsAdapter tabsAdapter;
    private BottomTabNavigation bottomTabNavigation;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        KinitApplication.coreComponent.inject(this);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.main_activity);
        bottomTabNavigation = findViewById(R.id.navigation);
        int selectedTabIndex = (savedInstanceState != null) ? savedInstanceState.getInt(SELECTED_TAB_INDEX_KEY, 0) : 0;
        bottomTabNavigation.setSelectedTabIndex(selectedTabIndex);
        enablePageSelection();
        tabsAdapter = new TabsAdapter();
        tabsAdapter.setPageSelectionListener(this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsAdapter);
        if (!tabsAdapter.shouldShowAnimation()) {
            viewPager.setCurrentItem(TabsAdapter.EARN_TAB_INDEX, false);
        }
        handleIntentExtras(getIntent().getExtras());
    }

    @Override
    public void enablePageSelection() {
        bottomTabNavigation.setPageSelectionListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_TAB_INDEX_KEY, bottomTabNavigation.getSelectedTabIndex());
        super.onSaveInstanceState(outState);
    }

    private void handleIntentExtras(Bundle intentExtras) {
        if (intentExtras != null && intentExtras.containsKey(REPORT_PUSH_ID_KEY)) {
            analytics.logEvent(new ClickEngagementPush(intentExtras.getString(REPORT_PUSH_ID_KEY),
                intentExtras.getString(REPORT_PUSH_TEXT_KEY)));
        }
    }

    @Override
    public void onPageSelected(int index, String tabTitle) {
        viewPager.setCurrentItem(index, false);
        Event event = new ClickMenuItem(tabTitle);
        analytics.logEvent(event);
        tabsAdapter.onTabVisibleToUser(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabsAdapter.onTabVisibleToUser(viewPager.getCurrentItem());

        userRepository.setFirstTimeUser(false);
        if (userRepository.isPhoneVerificationEnabled() && !userRepository.isPhoneVerified()) {
            showPhoneVerifyPopup();
        }
    }

    private void showPhoneVerifyPopup() {
        Builder builder = new Builder(this, R.style.CustomAlertDialog);
        builder.setTitle(R.string.pop_verify_phone_title)
            .setMessage(R.string.pop_verify_phone_sub_title)
            .setPositiveButton(R.string.pop_verify_phone_possitive, (dialog, which) -> {
                analytics.logEvent(new Events.Analytics.ClickVerifyButtonOnPhoneAuthPopup());
                startActivity(PhoneVerifyActivity.getIntent(MainActivity.this, false));
                finish();
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        analytics.logEvent(new Events.Analytics.ViewPhoneAuthPopup());
    }


}
