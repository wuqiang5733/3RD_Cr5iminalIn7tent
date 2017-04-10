package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        // 在 activity_masterdetail 当中会根据屏幕的大小来决定显示几个Fragment
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_fragment_container);
        frameLayout.setVisibility((frameLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        if (findViewById(R.id.detail_fragment_container) == null) {
            // 如果没有 detail_fragment_container 说明是手机
            // 那么启动 CrimePagerActivity
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            // 如果是平板，那么把 CrimeFragment 嵌到 detail_fragment_container 当中
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onEmotionSelected() {

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_fragment_container);
        frameLayout.setVisibility((frameLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);


        if (findViewById(R.id.detail_fragment_container) == null) {
            return;
        } else {
            // 如果是平板，那么把 CrimeFragment 嵌到 detail_fragment_container 当中
//            ViewPager pager = (ViewPager)findViewById(R.id.emotion_pager);
//            pager.setAdapter(new PageViewAdapter(this, getFragmentManager());

            Fragment emotionPageView = EmotionPageView.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, emotionPageView)
                    .commit();
        }
    }

    public void onCrimeUpdated(Crime crime) {
        // 找到 ListFragment ，让其在编辑 Crime 的时候，更新其 View
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
