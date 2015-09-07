package com.seasonsread.app.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.salvage.RecyclingPagerAdapter;

/**
 * Created by ZhanTao on 7/10/15.
 */
public class ImageViewPagerAdapterDecorator extends RecyclingPagerAdapter{

    private ImageViewPagerAdapter adapter;

    public ImageViewPagerAdapterDecorator(ImageViewPagerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        return adapter.getView(position, convertView, container);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}
