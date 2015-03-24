package com.seasonsread.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.seasonsread.app.R;
import com.seasonsread.app.base.Constants;
import com.seasonsread.app.base.Urls;

import java.io.UnsupportedEncodingException;

public class DetailsActivity extends BaseActivity {


    private ImageView imgShare;
    private TextView detailTitle;

    private LinearLayout loadLayout;
    private ImageView imgGoHome;
    private TextView tv_loading_tip;
    private WebView mWebView;
    private String mArticleId;
    private String mArticleUrl;
    private String mColumn;
    private String shareTitle;

    private int screen_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        Intent i = getIntent();
        mArticleId = i.getStringExtra(Constants.DETAILS_EXTRA_ARTICLE_ID);
        mArticleUrl = Urls.LIST_ITEM_CONTENT + "?article_id=" + mArticleId;
        mColumn = i.getStringExtra(Constants.DETAILS_EXTRA_COLUMN);
        shareTitle = i.getStringExtra(Constants.DETAILS_EXTRA_TITLE);
        initData();
        initControl();
    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screen_width = dm.widthPixels;
    }

    private void initControl() {
        detailTitle = (TextView) findViewById(R.id.details_textview_title);
        detailTitle.setText(mColumn);
        loadLayout = (LinearLayout) findViewById(R.id.view_loading);
        tv_loading_tip = (TextView) findViewById(R.id.tv_loading_tip);
        mWebView = (WebView) findViewById(R.id.detail_webView);
        this.mWebView.setBackgroundColor(0);
        this.mWebView.setBackgroundResource(R.color.detail_bgColor);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        imgShare = (ImageView) findViewById(R.id.details_imageview_share);
        imgShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_SEND); //启动分享发送的属性
                intent.setType("text/plain");                                    //分享发送的数据类型
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");    //分享的主题
                intent.putExtra(Intent.EXTRA_TEXT, "分享文章《" + shareTitle + "》，来自“四季阅读”（www.seasonsread.sinaapp.com）。");    //分享的内容
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//这个也许是分享列表的背景吧
                DetailsActivity.this.startActivity(Intent.createChooser(intent, "分享"));//目标应用选择对话框的标题
            }
        });

        imgGoHome = (ImageView) findViewById(R.id.details_imageview_gohome);
        imgGoHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        tv_loading_tip.setText("正在获取...");
        executeRequest(new StringRequest(Method.GET, mArticleUrl, responseListener(),
                errorListener()));
        mWebView.setVisibility(View.GONE);
        loadLayout.setVisibility(View.VISIBLE);
    }

    private Response.Listener<String> responseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDetail(response);
            }
        };
    }
    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showDetail(null);
            }
        };
    }

    private void showDetail(String response){
        if (response == null)
            response = "文章获取失败，请确保网络畅通！";

        try {
            response = java.net.URLDecoder.decode(response, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String linkCss = "<link rel=\"stylesheet\" href=\"file:///android_asset/pygments.css\" type=\"text/css\"/>";
        String content = linkCss + "<h2 align=\"left\" style=\"color:#0080C0\">" + shareTitle + "</h2>" + response;
        try {
            content = content.replace(
                    "img{}",
                    "img{width:"
                            + screen_width + "}");
            //content = content.replaceAll("<br />", "");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        loadLayout.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.setBackgroundResource(R.color.detail_bgColor);
        mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}
