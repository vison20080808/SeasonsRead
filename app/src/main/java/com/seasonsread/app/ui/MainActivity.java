package com.seasonsread.app.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seasonsread.app.R;
import com.seasonsread.app.model.ViewpagerImageItem;
import com.seasonsread.app.ui.adapter.ImageViewPagerAdapter;
import com.seasonsread.app.ui.fragment.PlaceholderFragment;
import com.seasonsread.app.util.ListUtils;
import com.seasonsread.app.view.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class MainActivity extends AppCompatActivity{

    private AutoScrollViewPager mAutoScrollViewPager;
    private CirclePageIndicator mIndicator;
    private List<ViewpagerImageItem> mImagesList;

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private PlaceholderFragment mPlaceholderFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        mAutoScrollViewPager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        mAutoScrollViewPager.startAutoScroll();
    }

    private void initUI(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        onSectionAttached(0);
        ab.setTitle(mTitle);
        mPlaceholderFragment = PlaceholderFragment.newInstance(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mPlaceholderFragment)
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        initViewPager();
    }

    private void initViewPager(){
        mAutoScrollViewPager = (AutoScrollViewPager)findViewById(R.id.viewpager);
        mIndicator = (CirclePageIndicator)findViewById(R.id.page_indicator);

        mImagesList = new ArrayList<ViewpagerImageItem>();
        ViewpagerImageItem item = new ViewpagerImageItem();
        item.setUrl("http://seasonsread-seasonsread.stor.sinaapp.com/images/2014/03/24/170029_7034.jpg");
        item.setArticleID(88);
        mImagesList.add(item);

        item = new ViewpagerImageItem();
        item.setUrl("http://seasonsread-seasonsread.stor.sinaapp.com/images/2013/09/10/155053_1364.jpg");
        item.setArticleID(58);
        mImagesList.add(item);

        item = new ViewpagerImageItem();
        item.setUrl("http://seasonsread-seasonsread.stor.sinaapp.com/images/2013/08/20/161811_4762.jpg");
        item.setArticleID(18);
        mImagesList.add(item);

        item = new ViewpagerImageItem();
        item.setUrl("http://seasonsread-seasonsread.stor.sinaapp.com/images/2013/09/05/093544_2695.jpg");
        item.setArticleID(34);
        mImagesList.add(item);

        mAutoScrollViewPager.setAdapter(new ImageViewPagerAdapter(this, mImagesList));
        mIndicator.setViewPager(mAutoScrollViewPager);

        mAutoScrollViewPager.setInterval(5000);
        mAutoScrollViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % ListUtils.getSize(mImagesList));

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        mPlaceholderFragment = PlaceholderFragment.newInstance(
                                getIndexAsMenuItemId(menuItem.getItemId()));
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mPlaceholderFragment)
                                .commit();

                        onSectionAttached(getIndexAsMenuItemId(menuItem.getItemId()));
                        getSupportActionBar().setTitle(mTitle);
                        return true;
                    }
                });
    }

    public int getIndexAsMenuItemId(int menuItemID){
        switch (menuItemID){
            case R.id.nav_0:
                return 0;
            case R.id.nav_1:
                return 1;
            case R.id.nav_2:
                return 2;
            case R.id.nav_3:
                return 3;
            default:
                return 0;
        }
    }
    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            /*case R.id.action_refresh:
                mPlaceholderFragment.loadFirstAndScrollToTop();
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
}