package org.kinecosystem.kinit.view.tutorial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.blockchain.Wallet;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.view.BaseActivity;

import javax.inject.Inject;

public class TutorialActivity extends BaseActivity {


    final static int NUM_ITEMS = 3;
    @Inject
    Analytics analytics;
    @Inject
    UserRepository userRepository;
    @Inject
    Wallet wallet;

    private View[] pages = new View[NUM_ITEMS];
    private int currentPage = 0;

    public static Intent getIntent(Context context) {
        return new Intent(context, TutorialActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        KinitApplication.coreComponent.inject(this);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.tutorial_activity);
        initTosText();
        initPagesIndicator();
        ViewPager pager = findViewById(R.id.view_pager);
        pager.setAdapter(new TutorialPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clearPagesIndicator();
                currentPage = position;
                pages[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.start_app).setOnClickListener(view -> {
            onStartClicked();
        });
    }

    private void initTosText() {
        TextView tos = findViewById(R.id.tos_text);
        String tos_url = userRepository.getTos();
        if (TextUtils.isEmpty(tos_url)) {
            tos.setText("");
        } else {
            tos.setText(Html.fromHtml(getResources().getString(R.string.tos)));
            tos.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(tos_url));
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        analytics.logEvent(new Events.Analytics.ViewOnboardingPage(currentPage));
    }

    private void onStartClicked() {
        analytics.logEvent(new Events.Analytics.ClickStartButtonOnOnboardingPage(currentPage));
        Navigator navigator = new Navigator(this);
        if (userRepository.isPhoneVerificationEnabled()) {
            navigator.navigateTo(Navigator.Destination.PHONE_VERIFY);
        } else if (!wallet.getReady().get()) {
            navigator.navigateTo(Navigator.Destination.WALLET_CREATE);
        } else if (!wallet.getKin3Ready().get()){
            navigator.navigateTo(Navigator.Destination.WALLET_MIGRATE);
        } else {
            navigator.navigateTo(Navigator.Destination.MAIN_SCREEN);
        }
        finish();
    }

    private void initPagesIndicator() {
        pages[0] = findViewById(R.id.page0);
        pages[1] = findViewById(R.id.page1);
        pages[2] = findViewById(R.id.page2);
        pages[currentPage].setSelected(true);
    }

    private void clearPagesIndicator() {
        for (View page : pages) {
            page.setSelected(false);
        }
    }

    private static class TutorialPagerAdapter extends FragmentPagerAdapter {

        public TutorialPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TutorialWelcomeFragment.newInstance();
                case 1:
                    return TutorialEarnFragment.newInstance();
                case 2:
                    return TutorialEnjoyFragment.newInstance();
                default:
                    return null;
            }
        }
    }

}
