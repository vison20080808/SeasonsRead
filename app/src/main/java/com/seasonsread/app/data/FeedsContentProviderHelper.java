package com.seasonsread.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.seasonsread.app.model.Feed;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhanTao on 1/14/15.
 */
public class FeedsContentProviderHelper extends BaseContentProviderHelper {

    private int mSectionIndex = 0;

    public FeedsContentProviderHelper(Context context, int nSectionIndex){
        super(context);
        this.mSectionIndex = nSectionIndex;
    }

    @Override
    protected Uri getContentUri() {
        return FeedsContentProvider.FEEDS_CONTENT_URI;
    }

    public CursorLoader getCursorLoader() {
        if(mSectionIndex == 0)
            return new CursorLoader(getContext(), getContentUri(), null, null, null,
                    Feed.ROW_ID + " ASC");
        else
            return new CursorLoader(getContext(), getContentUri(), null, Feed.ROW_TYPE_ID + "=?",
                new String[] {
                        String.valueOf(mSectionIndex - 1)
                }, Feed.ROW_ID + " ASC");
    }

    public void bulkInsert(List<Feed> feeds){
        ArrayList<ContentValues> values = new ArrayList<ContentValues>();
        for(Feed feed : feeds){
            values.add(getContentValues(feed));
        }
        ContentValues[] valuesArray = new ContentValues[values.size()];
        bulkInsert(values.toArray(valuesArray));
    }

    private ContentValues getContentValues(Feed feed){
        ContentValues cv = new ContentValues();
        cv.put(Feed.ROW_TITLE, decodeSeasonsReadContent(feed.getTitle()));
        cv.put(Feed.ROW_ARTICLE_ID, feed.getArticle_id());
        cv.put(Feed.ROW_AUTHOR, decodeSeasonsReadContent(feed.getBlog_name()));
        cv.put(Feed.ROW_TYPE_ID, feed.getType_id());
        cv.put(Feed.ROW_HITNUM, feed.getHitnum());
        cv.put(Feed.ROW_CREATE_TIME, decodeSeasonsReadContent(feed.getCreatetime()));
        cv.put(Feed.ROW_IS_RESERVED, feed.getIs_reserved());
        return cv;
    }

    private String decodeUrlToUTF8(String s){
        String result = "";
        try {
            result = java.net.URLDecoder.decode(s,
                    "UTF-8");
        }catch (UnsupportedEncodingException e){
            throw new AssertionError("UTF-8 is unknown");
        }
        return result;
    }


    private String decodeSeasonsReadContent(String content){
        content = decodeUrlToUTF8(content);
        content = content.replace("_*~*_", "\"");
        content = content.replace("_&*&_", ":");
        content = content.replace("_%^%_", "{");
        content = content.replace("_^$^_", "}");
        content = content.replace("_@#@_", "[");
        content = content.replace("_~$~_", "]");
        return content;
    }


}
