package com.seasonsread.app.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.seasonsread.app.R;
import com.seasonsread.app.SeasonsReadApp;
import com.seasonsread.app.base.Constants;
import com.seasonsread.app.base.Urls;
import com.seasonsread.app.data.FeedsContentProviderHelper;
import com.seasonsread.app.data.GsonRequest;
import com.seasonsread.app.model.Feed;
import com.seasonsread.app.ui.Adapter.FeedsAdapter;
import com.seasonsread.app.ui.DetailsActivity;
import com.seasonsread.app.ui.MainActivity;
import com.seasonsread.app.util.TaskUtils;
import com.seasonsread.app.util.ToastUtils;
import com.seasonsread.app.view.LoadMoreFooter;
import com.seasonsread.app.view.LoadMoreListView;

import java.util.ArrayList;

/**
 * Created by ZhanTao on 1/8/15.
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    /*
     * The fragment argument representing the section number for thisØ
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mSectionIndex = 0;
    private String mPage = "0";

    private FeedsContentProviderHelper mFeedsContentProviderHelper;
    private SwipeRefreshLayout mSwipeLayout;
    private LoadMoreListView mLoadMoreListView;
    private FeedsAdapter mFeedsAdapter;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void parseArgument() {
        Bundle bundle = getArguments();
        mSectionIndex = bundle.getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        parseArgument();
        mFeedsContentProviderHelper = new FeedsContentProviderHelper(SeasonsReadApp.getAppContext(),
                mSectionIndex);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mLoadMoreListView = (LoadMoreListView) rootView.findViewById(R.id.pull_refresh_list);

        mLoadMoreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadNext();
            }
        });

        mFeedsAdapter = new FeedsAdapter(getActivity());
        mLoadMoreListView.setAdapter(mFeedsAdapter);
        mLoadMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feed feed = mFeedsAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.DETAILS_EXTRA_ARTICLE_ID, Integer.toString(feed.getArticle_id()));
                intent.putExtra(Constants.DETAILS_EXTRA_COLUMN,
                        ((MainActivity)getActivity()).getSupportActionBar().getTitle());
                intent.putExtra(Constants.DETAILS_EXTRA_TITLE, feed.getTitle());
                intent.setClass(getActivity(), DetailsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, this);
        loadFirst();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onRefresh() {
        loadFirst();
    }

    public void loadFirstAndScrollToTop() {
        loadFirst();
    }

    public void loadFirst() {
        mPage = "0";
        loadData(mPage);
    }

    public void loadNext() {
        loadData(mPage);
    }

    public void loadData(String strPage) {
        if (!mSwipeLayout.isRefreshing() && strPage.endsWith("0"))
            setRefreshing(true);
        StringBuilder url = new StringBuilder(Urls.FEED_LIST_NEW);
        url.append("?type=");
        url.append(mSectionIndex);
        url.append("&page_index=");
        url.append(mPage);
        executeRequest(new GsonRequest(url.toString(),
                Feed.GsonRequestData.class, responseListener(), errorListener()));
    }

    private Response.Listener<Feed.GsonRequestData> responseListener() {
        final boolean isRefreshFromTop = ("0".equals(mPage));
        return new Response.Listener<Feed.GsonRequestData>() {
            @Override
            public void onResponse(final Feed.GsonRequestData response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... params) {
                        boolean bHasUpdatingData = false;
                        ArrayList<Feed> feeds = response.feeds;
                        mFeedsContentProviderHelper.bulkInsert(feeds);
                        if (feeds != null && feeds.size() > 0) {
                            mPage = feeds.get(feeds.size() - 1).getCreatetime();
                            mPage = mPage.replaceAll(" ", "%20");
                            bHasUpdatingData = true;
                        }

                        return bHasUpdatingData;
                    }

                    @Override
                    protected void onPostExecute(Boolean bHasUpdatingData) {
                        super.onPostExecute(bHasUpdatingData);

                        setRefreshing(false);

                        if (isRefreshFromTop) {
                            if (bHasUpdatingData && mFeedsAdapter.getCount() > 0) {
                                mLoadMoreListView.setSelectionAfterHeaderView();//smoothScrollToPosition(0);
                            }
                            ToastUtils.showShort("Refreshing done!");
                        }

                        if (bHasUpdatingData)
                            mLoadMoreListView.setState(LoadMoreFooter.State.IDLE);
                        else
                            mLoadMoreListView.setState(LoadMoreFooter.State.END);
                    }
                });
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRefreshing(false);
                if (error.getMessage() != null && error.getMessage().startsWith("com.google.gson.JsonSyntaxException"))
                    mLoadMoreListView.setState(LoadMoreFooter.State.END);
                else {
                    mLoadMoreListView.setState(LoadMoreFooter.State.IDLE, 10000);
                    ToastUtils.showShort("Loading failed!");
                }
            }
        };
    }

    private void setRefreshing(boolean bRefreshing) {
        if (mSwipeLayout.isRefreshing() != bRefreshing)
            mSwipeLayout.setRefreshing(bRefreshing);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mFeedsContentProviderHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFeedsAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedsAdapter.changeCursor(null);
    }
}


