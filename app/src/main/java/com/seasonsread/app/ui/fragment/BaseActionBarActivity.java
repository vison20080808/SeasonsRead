package com.seasonsread.app.ui.fragment;

import android.support.v7.app.ActionBarActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.seasonsread.app.manager.WebRequestManager;
import com.seasonsread.app.util.ToastUtils;

/**
 * Created by ZhanTao on 1/21/15.
 */
public class BaseActionBarActivity extends ActionBarActivity {
    @Override
    public void onDestroy() {
        super.onDestroy();
        WebRequestManager.cancelAll(this);
    }

    protected void executeRequest(Request<?> request) {
        WebRequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong(error.getMessage());
            }
        };
    }
}
