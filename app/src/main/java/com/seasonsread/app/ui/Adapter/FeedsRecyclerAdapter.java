package com.seasonsread.app.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seasonsread.app.R;
import com.seasonsread.app.data.db.FeedsDBTable;
import com.seasonsread.app.model.Feed;
import com.seasonsread.app.util.RecyclerViewCursorAdapter;

/**
 * Created by ZhanTao on 7/20/15.
 */
public class FeedsRecyclerAdapter extends RecyclerViewCursorAdapter<FeedsRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private OnItemClickLitener mOnItemClickLitener;


    protected View customLoadMoreView;

    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int FOOTER = 1;
    }

    public FeedsRecyclerAdapter(Context context) {
        super(context, null, 0);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
//        void onItemLongClick(View view , int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPES.FOOTER) {
            ViewHolder viewHolder = new ViewHolder(customLoadMoreView, false);
            if (super.getItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        } else {
            View view = mLayoutInflater.inflate(R.layout.listview_item_feed, null);
            ViewHolder viewHolder = new ViewHolder(view, true);
            view.setTag(viewHolder);
            return viewHolder;
        }
    }

    /**
     * Using a custom LoadMoreView
     *
     * @param customview the inflated view
     */
    public void setCustomLoadMoreView(View customview) {
        customLoadMoreView = customview;
    }

    public View getCustomLoadMoreView() {
        return customLoadMoreView;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
        if (cursor != null && viewHolder.isNormalItem == true) {
            String strTitle = cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_TITLE));
            viewHolder.tvTitle.setText(strTitle);
            viewHolder.tvCreateTime.setText(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_CREATE_TIME)));
            viewHolder.tvAuthor.setText(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_AUTHOR)));

            if (mOnItemClickLitener != null) {
                viewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = viewHolder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                    }
                });
            }
        }
    }

    @Override
    public Feed getItem(int position) {
        Cursor cursor = getCursor();
        if (cursor == null)
            return null;
        if (!cursor.moveToPosition(position))
            return null;
        Feed feed = new Feed();
        feed.setArticle_id(cursor.getInt(cursor.getColumnIndex(FeedsDBTable.ROW_ARTICLE_ID)));
        feed.setTitle(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_TITLE)));
        feed.setType_id(cursor.getInt(cursor.getColumnIndex(FeedsDBTable.ROW_TYPE_ID)));
        feed.setBlog_name(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_AUTHOR)));
        feed.setCreatetime(cursor.getString(cursor.getColumnIndex(FeedsDBTable.ROW_CREATE_TIME)));
        feed.setHitnum(cursor.getInt(cursor.getColumnIndex(FeedsDBTable.ROW_HITNUM)));
        feed.setIs_reserved(cursor.getInt(cursor.getColumnIndex(FeedsDBTable.ROW_IS_RESERVED)));
        return feed;
    }


    @Override
    protected void onContentChanged() {

    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null && customLoadMoreView != null) {
            return VIEW_TYPES.FOOTER;
        } else
            return VIEW_TYPES.NORMAL;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public boolean isNormalItem = false;
        public RelativeLayout layoutItem;
        public TextView tvTitle;
        public TextView tvCreateTime;
        public TextView tvAuthor;

        public ViewHolder(View itemView, boolean isNormalItem) {
            super(itemView);
            this.isNormalItem = isNormalItem;
            if (isNormalItem) {
                layoutItem = (RelativeLayout) itemView.findViewById(R.id.layout_item);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
                tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            }
        }
    }

}
