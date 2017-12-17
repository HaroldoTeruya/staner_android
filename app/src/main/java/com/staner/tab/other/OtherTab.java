package com.staner.tab.other;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.tab.other.fragment.all.AllMusicPageFragment;
import com.staner.tab.other.fragment.artist.ArtistMusicFragment;
import com.staner.tab.other.fragment.artist.ArtistPageFragment;
import com.staner.tab.other.fragment.genre.GenrePageFragment;
import com.staner.util.Util;

/**
 * Created by Teruya on 25/09/15.
 */
public class OtherTab implements TabHost.TabContentFactory
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity;
    public static final String TAG = "Other";
    private static final int NUM_PAGES = 3;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    public static final int ARTIST = 0;
    public static final int GENRE = 1;
    public static final int OTHER = 2;

    private int previus = 0;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    public OtherTab(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static LinearLayout tabHeader(final FragmentActivity homeActivity)
    {
        LinearLayout tabLinearLayout = (LinearLayout) Util.inflate(homeActivity, R.layout.home_tab_header_layout);
        ((TextView)tabLinearLayout.findViewById( R.id.tab_header_textview )).setText(R.string.other);
        return tabLinearLayout;
    }

    @Override
    public View createTabContent( String tag )
    {
        View view = Util.inflate(mainActivity, R.layout.other_tab_content_layout);

        ((RadioGroup)view.findViewById(R.id.category_radiogroup)).setOnCheckedChangeListener(onCategoryRadioGroupChangeListener);

        viewPager = (ViewPager) view.findViewById(R.id.pager_content);
        pagerAdapter = new ScreenSlidePagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                Fragment fragment = pagerAdapter.getItem(previus);
                switch ( previus )
                {
                    case ARTIST: ((ArtistPageFragment)fragment).filter("");   break;
                    case GENRE:  ((GenrePageFragment)fragment).filter("");    break;
                    case OTHER:  ((AllMusicPageFragment)fragment).filter(""); break;
                }
                previus = position;
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        return view;
    }

    public void filter(String text)
    {
        Fragment fragment = pagerAdapter.getItem(viewPager.getCurrentItem());
        switch ( viewPager.getCurrentItem() )
        {
            case ARTIST:
                ((ArtistPageFragment)fragment).filter(text);
                break;
            case GENRE:
                ((GenrePageFragment)fragment).filter(text);
                break;
            case OTHER:
                ((AllMusicPageFragment)fragment).filter(text);
                break;
        }
    }

    //=================================================================================================
    //============================================== EVENTS ===========================================
    //=================================================================================================

    private RadioGroup.OnCheckedChangeListener onCategoryRadioGroupChangeListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i)
        {
            switch ( i )
            {
                case R.id.artist_radio:
                    viewPager.setCurrentItem(ARTIST);
                    break;
                case R.id.genre_radio:
                    viewPager.setCurrentItem(GENRE);
                    break;
                case R.id.all_radio:
                    viewPager.setCurrentItem(OTHER);
                    break;
            }
        }
    };

    //=================================================================================================
    //======================================== SETTERS  & GETTERS =====================================
    //=================================================================================================

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {
        private ArtistPageFragment artistPageFragment;
        private GenrePageFragment genrePageFragment;
        private AllMusicPageFragment allMusicPageFragment;

        public ScreenSlidePagerAdapter()
        {
            super(mainActivity.getSupportFragmentManager());

            artistPageFragment = new ArtistPageFragment();
            genrePageFragment = new GenrePageFragment();
            allMusicPageFragment = new AllMusicPageFragment();
        }

        @Override
        public Fragment getItem(int position)
        {
            switch ( position )
            {
                case ARTIST: return artistPageFragment;
                case GENRE: return genrePageFragment;
                case OTHER: return allMusicPageFragment;
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }
    }
}