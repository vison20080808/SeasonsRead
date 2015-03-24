package com.seasonsread.app.data.db;

import android.provider.BaseColumns;

/**
 * Created by ZhanTao on 1/13/15.
 */
public class FeedsDBTable implements BaseColumns {

    private  FeedsDBTable(){

    }

    public static final String TABLE_NAME = "feeds_list";

    public static final String ROW_ID = "_id";
    public static final String ROW_TITLE = "title";
    public static final String ROW_AUTHOR = "Author";
    public static final String ROW_ARTICLE_ID = "article_id";
    public static final String ROW_TYPE_ID = "type_id";
    public static final String ROW_HITNUM = "hitnum";
    public static final String ROW_CREATE_TIME = "createtime";
    public static final String ROW_IS_RESERVED = "is_reserved";

}
