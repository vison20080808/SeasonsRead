package com.seasonsread.app.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.seasonsread.app.R;
import com.seasonsread.app.base.Constants;
import com.seasonsread.app.base.Urls;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class DetailsActivity extends BaseActivity {



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
        setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND); //启动分享发送的属性
                intent.setType("text/plain");                                    //分享发送的数据类型
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");    //分享的主题
                intent.putExtra(Intent.EXTRA_TEXT, "分享文章《" + shareTitle + "》，来自“四季阅读”（www.seasonsread.sinaapp.com）。");    //分享的内容
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//这个也许是分享列表的背景吧
                DetailsActivity.this.startActivity(Intent.createChooser(intent, "分享"));//目标应用选择对话框的标题
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    private final Random RANDOM = new Random();

    private int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screen_width = dm.widthPixels;
    }

    private void initControl() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(shareTitle);
        collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));

        loadBackdrop();

        mWebView = (WebView) findViewById(R.id.detail_webView);
        this.mWebView.setBackgroundColor(0);
        this.mWebView.setBackgroundResource(R.color.detail_bgColor);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        executeRequest(new StringRequest(Method.GET, mArticleUrl, responseListener(),
                errorListener()));
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
        mWebView.setBackgroundResource(R.color.detail_bgColor);
        mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }
}
