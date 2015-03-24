package com.seasonsread.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by ZhanTao on 1/17/15.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener{

    public static interface OnLoadMoreListener{
        public void onLoadMore();
    }

    private LoadMoreFooter mLoadMoreFooter;
    private OnLoadMoreListener mOnLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreListView(Context context){
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        mLoadMoreFooter = new LoadMoreFooter(context);
        addFooterView(mLoadMoreFooter.getView());
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.d("seasonsread", String.format("firstVisibleItem=%d; visibleItemCount=%d; totalItemCount=%d",
//                firstVisibleItem, visibleItemCount, totalItemCount));

        if(mLoadMoreFooter.getState() == LoadMoreFooter.State.LOADING
                || mLoadMoreFooter.getState() == LoadMoreFooter.State.END)
            return;
        if (firstVisibleItem + visibleItemCount >= totalItemCount
                && totalItemCount != 0
                && totalItemCount != getHeaderViewsCount()
                + getFooterViewsCount()
                && mOnLoadMoreListener != null) {
            mLoadMoreFooter.setState(LoadMoreFooter.State.LOADING);
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void setState(LoadMoreFooter.State state){
        mLoadMoreFooter.setState(state);
    }

    public void setState(LoadMoreFooter.State state, long delay){
        mLoadMoreFooter.setState(state, delay);
    }

    public LoadMoreFooter.State getState(){
        return mLoadMoreFooter.getState();
    }
}
