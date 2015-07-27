/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.seasonsread.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.seasonsread.app.base.Constants;
import com.seasonsread.app.model.ViewpagerImageItem;
import com.seasonsread.app.ui.DetailsActivity;
import com.seasonsread.app.util.ListUtils;
import com.seasonsread.app.view.LazyImageView;

import java.util.List;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImageViewPagerAdapter extends RecyclingPagerAdapter{

    private Context       context;
    private List<ViewpagerImageItem> imageIdList;

    private int           size;

    public ImageViewPagerAdapter(Context context, List<ViewpagerImageItem> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
    }

    @Override
    public int getCount() {
        // Infinite loop
        return ListUtils.getSize(imageIdList);
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    public int getPosition(int position) {
        return position % size;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new LazyImageView(context);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.imageView.setImageUrl(imageIdList.get(getPosition(position)).getUrl());
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final int articalId = imageIdList.get(getPosition(position)).getArticleID();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Constants.DETAILS_EXTRA_ARTICLE_ID, Integer.toString(articalId));
                intent.putExtra(Constants.DETAILS_EXTRA_COLUMN, "");
                intent.putExtra(Constants.DETAILS_EXTRA_TITLE, "");
                intent.setClass(context, DetailsActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public static class ViewHolder {

        LazyImageView imageView;
    }
}
