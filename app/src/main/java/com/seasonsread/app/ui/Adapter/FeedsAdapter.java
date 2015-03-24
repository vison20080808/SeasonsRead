package com.seasonsread.app.ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seasonsread.app.R;
import com.seasonsread.app.data.db.FeedsDBTable;
import com.seasonsread.app.model.Feed;

/**
 * Created by ZhanTao on 1/16/15.
 */
public class FeedsAdapter extends CursorAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public FeedsAdapter(Context context) {
        super(context, null, false);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_item_feed, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.tvCreateTime = (TextView) view.findViewById(R.id.tv_create_time);
        viewHolder.tvAuthor = (TextView) view.findViewById(R.id.tv_author);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public Feed getItem(int position) {
        mCursor.moveToPosition(position);
        Feed feed = new Feed();
        feed.setArticle_id(mCursor.getInt(mCursor.getColumnIndex(FeedsDBTable.ROW_ARTICLE_ID)));
        feed.setTitle(mCursor.getString(mCursor.getColumnIndex(FeedsDBTable.ROW_TITLE)));
        feed.setType_id(mCursor.getInt(mCursor.getColumnIndex(FeedsDBTable.ROW_TYPE_ID)));
        feed.setBlog_name(mCursor.getString(mCursor.getColumnIndex(FeedsDBTable.ROW_AUTHOR)));
        feed.setCreatetime(mCursor.getString(mCursor.getColumnIndex(FeedsDBTable.ROW_CREATE_TIME)));
        feed.setHitnum(mCursor.getInt(mCursor.getColumnIndex(FeedsDBTable.ROW_HITNUM)));
        feed.setIs_reserved(mCursor.getInt(mCursor.getColumnIndex(FeedsDBTable.ROW_IS_RESERVED)));
        return feed;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        String strTitle = cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_TITLE));
        viewHolder.tvTitle.setText(strTitle);
        viewHolder.tvCreateTime.setText(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_CREATE_TIME)));
        viewHolder.tvAuthor.setText(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_AUTHOR)));

    }

    public static class ViewHolder{
        public TextView tvTitle;
        public TextView tvCreateTime;
        public TextView tvAuthor;
    }
}
