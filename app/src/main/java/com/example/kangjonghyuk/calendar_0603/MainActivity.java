package com.example.kangjonghyuk.calendar_0603;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager m_pager;
    private PagerAdapter m_pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_pager = (ViewPager)findViewById(R.id.pager);
        m_pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        m_pager.setAdapter(m_pagerAdapter);
        m_pager.setCurrentItem(5);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter{

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentPage.newInstance(position);
        }

        @Override
        public int getCount() {
            return 12;
        }
    }
}
