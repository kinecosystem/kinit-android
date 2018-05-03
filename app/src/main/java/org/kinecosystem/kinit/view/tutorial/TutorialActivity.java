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
import android.view.View;
import android.widget.TextView;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.view.BaseActivity;
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity;

public class TutorialActivity extends BaseActivity {


    public static Intent getIntent(Context context) {
        return new Intent(context, TutorialActivity.class);
    }

    final static int NUM_ITEMS = 3;
    private View[] pages = new View[NUM_ITEMS];
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        String tos_url = getCoreComponents().userRepo().getTos();
        if (tos_url == null || tos_url.isEmpty()) {
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
        getCoreComponents().analytics().logEvent(new Events.Analytics.ViewOnboardingPage(currentPage));
    }

    private void onStartClicked() {
        getCoreComponents().analytics().logEvent(new Events.Analytics.ClickStartButtonOnOnboardingPage(currentPage));
        startActivity(PhoneVerifyActivity.getIntent(TutorialActivity.this));
        finish();
    }

    private void initPagesIndicator() {
        pages[0] = findViewById(R.id.page0);
        pages[1] = findViewById(R.id.page1);
        pages[2] = findViewById(R.id.page2);
        pages[currentPage].setSelected(true);
    }

    private void clearPagesIndicator() {
        for (int i = 0; i < pages.length; i++) {
            pages[i].setSelected(false);
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
