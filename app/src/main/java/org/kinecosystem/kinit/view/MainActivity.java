package org.kinecosystem.kinit.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import java.util.List;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickEngagementPush;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickMenuItem;
import org.kinecosystem.kinit.analytics.Events.Event;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.view.BottomTabNavigation.TabSelectionListener;
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity;

public class MainActivity extends BaseActivity implements TabSelectionListener {

    public static final String REPORT_PUSH_ID_KEY = "report_push_id_key";
    public static final String REPORT_PUSH_TEXT_KEY = "report_push_text_key";
    private static final String SELECTED_TAB_INDEX_KEY = "selected_tab_index_key";

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private ViewPager viewPager;
    private BottomTabNavigation bottomTabNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.main_activity);
        bottomTabNavigation = findViewById(R.id.navigation);
        int selectedTabIndex = (savedInstanceState != null) ? savedInstanceState.getInt(SELECTED_TAB_INDEX_KEY, 0) : 0;
        bottomTabNavigation.setSelectedTabIndex(selectedTabIndex);
        bottomTabNavigation.setTabSelectionListener(this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        handleIntentExtras(getIntent().getExtras());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_TAB_INDEX_KEY, bottomTabNavigation.getSelectedTabIndex());
        super.onSaveInstanceState(outState);
    }

    private void handleIntentExtras(Bundle intentExtras) {
        if (intentExtras != null && intentExtras.containsKey(REPORT_PUSH_ID_KEY)) {
            getCoreComponents().analytics().logEvent(new ClickEngagementPush(intentExtras.getString(REPORT_PUSH_ID_KEY),
                intentExtras.getString(REPORT_PUSH_TEXT_KEY)));
        }
    }

    @Override
    public void onTabSelected(int index, String tabTitle) {
        viewPager.setCurrentItem(index, false);
        Event event = new ClickMenuItem(tabTitle);
        getCoreComponents().analytics().logEvent(event);
        onScreenVisibleToUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onScreenVisibleToUser();
        UserRepository userRepo = getCoreComponents().userRepo();
        if (userRepo.isPhoneVerificationEnabled() && !userRepo.isPhoneVerified()) {
            showPhoneVerifyPopup();
        }
    }

    private void showPhoneVerifyPopup() {
        Builder builder = new Builder(this, R.style.CustomAlertDialog);
        builder.setTitle(R.string.pop_verify_phone_title)
            .setMessage(R.string.pop_verify_phone_sub_title)
            .setPositiveButton(R.string.pop_verify_phone_possitive, (dialog, which) -> {
                startActivity(PhoneVerifyActivity.getIntent(MainActivity.this, false));
                finish();
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        getCoreComponents().analytics().logEvent(new Events.Analytics.ViewPhoneAuthPopup());
    }

    // Cannot rely on Fragment onResume to be called when fragment is visible
    // because of the way that ViewPager/FragmentPagerAdapter work
    // I tried overriding 'setUserVisibleHint' in the Fragments as suggested
    // here: https://stackoverflow.com/questions/10024739/how-to-determine-when-fragment-becomes-visible-in-viewpager
    // but that did not work as expected either.
    // Activity onResume always works well - so relying on that
    // only problem with this solution is that when the activity resumes for the first time
    // the first fragment is displayed (and it's onResume method called) but here
    // the list of fragments is still empty!! Solved this with a hack in EarnFragment.java
    // Will soon change tabs implementation to use plain Views instead of fragments so
    // no hacks will be needed !!
    private void onScreenVisibleToUser() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            TabFragment fragment = (TabFragment) f;
            if (fragment != null && fragment.getTabIndex() == viewPager.getCurrentItem()) {
                fragment.onScreenVisibleToUser();
            }
        }
    }

}
