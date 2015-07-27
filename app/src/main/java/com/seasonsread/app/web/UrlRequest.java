package com.seasonsread.app.web;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.seasonsread.app.data.GsonRequest;
import com.seasonsread.app.util.LogUtils;
import com.seasonsread.app.util.WebUtils;

import java.net.URL;
import java.util.HashMap;

/**
 * @author Bo Hu
 */
public class UrlRequest<T> {

    public interface RequestDelegate {
        public void requestFailed(UrlRequest request, int statusCode);

        public void requestFinished(UrlRequest request);
    }

    private static final int MY_SOCKET_TIMEOUT_MS = 45 * 1000;

    private Object mTag;

    // private
    private String mUrl;
    protected RequestDelegate/* SoftReference<RequestDelegate> */delegateRef;
    private HashMap<String, String> mPostParams;
    private byte[] mPostBody;
    private int mMethod = Method.GET;

    private String mStringData;

    private int mMaxWidth = 0, mMaxHeight = 0;
    private Config mConfig = Config.ARGB_8888;
    private boolean mIsImg;
    private Bitmap mImgData;


    private boolean mIsGson;
    private Class<T> mClazz;
    private T mGsonData;

    public UrlRequest(String url, Object tag) {
        autoCompleUrl(url, null);
        mTag = tag;
    }


    /**
     * f d
     * To add params of URL
     *
     * @param url
     * @param params
     */
    public UrlRequest(String url, HashMap<String, Object> params, Object tag) {
        autoCompleUrl(url, params);
        mTag = tag;
    }

    public void setUrl(String url, HashMap<String, Object> params) {
        autoCompleUrl(url, params);
    }


    public void setDelegate(UrlRequest.RequestDelegate delegate) {
        if (delegate == null)
            delegateRef = null;
        else
            delegateRef = delegate;// new SoftReference<RequestDelegate>(delegate);
    }

    /**
     * Add post data into request
     *
     * @param key
     * @param value
     */
    public void addPostParam(String key, Object value) {
        if (mPostParams == null) {
            mPostParams = new HashMap<String, String>();
        }
        mPostParams.put(key, String.valueOf(value));
    }

    public String getUrl() {
        return mUrl;
    }

    public RequestDelegate getDelegate() {
        // if (CardConfig.DEV_BUILD) {
        // if (delegateRef != null && delegateRef.get() == null)
        // w("the delegate has been GCed, please check if it caused any problems. %s", getUrl());
        // }
        return delegateRef;// delegateRef != null ? delegateRef.get() : null;
    }


    /**
     * Get String data
     *
     * @return
     */
    public String getStringData() {
        return mStringData;
    }


    /**
     * Get bitmap data
     *
     * @return
     */
    public Bitmap getImageData() {
        return mImgData;
    }

    public T getGsonData() {
        return mGsonData;
    }

    /**
     * Config bitmap params
     *
     * @param maxWidth  Max width of the bitmap
     * @param maxHeight Max height of the bitmap
     * @param config    Config param
     */
    public void setImageParams(int maxWidth, int maxHeight, Config config) {
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
        mConfig = config;
        mIsImg = true;
    }

    public void setGsonType(Class<T> clazz) {
        mIsGson = true;
        mClazz = clazz;
    }

    /**
     * Start request as current type(Default is String, it would be Image type after invoked
     * setImageParams) and normal priority
     */
    public void start() {
        start(mIsImg, false);
    }

    /**
     * Start request as @param img type type and normal priority
     *
     * @param img Whether is bitmap
     */
    public void start(boolean img) {
        start(mIsImg, false);
    }

    /**
     * Start requestas as @param img type type and @param isHighPriority priority
     *
     * @param img            true : Image type, false : String type
     * @param isHighPriority true : High priority false : Normal priority
     */
    public void start(boolean img, boolean isHighPriority) {

        Request<?> r;

//        if (mIsGson) {
            r = new GsonRequest<T>(mUrl, mClazz, new Response.Listener<T>() {
                @Override
                public void onResponse(T t) {
                    mGsonData = t;
                    fireDelegate(true, 0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
                    LogUtils.w("onErrorResponse %s", error);
//                    if (error instanceof NoConnectionError) {
//                        CardSessionManager.getSessionManager().setSessionMode(SessionMode.OffLine);
//                    }
                    fireDelegate(false, code);
                }
            });

        r.setShouldCache(false);
//        } else {

//      if (img) {
//        r = new ImageRequestPriority(mUrl, new Listener<Bitmap>() {
//
//          public void onResponse(Bitmap response) {
//            mImgData = response;
//            fireDelegate(true, 0);
//          }
//        }, mMaxWidth, mMaxHeight, ScaleType.CENTER, mConfig, new Response.ErrorListener() {
//
//          public void onErrorResponse(VolleyError error) {
//            int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
//            fireDelegate(false, code);
//          }
//        });
//        ((ImageRequestPriority) r).setPriority(isHighPriority ? Priority.IMMEDIATE : Priority.NORMAL);
//      } else {
//
//        if (mPostParams != null && mPostParams.size() > 0) {
//          mMethod = Method.POST;
//        }
//        StringRequestPriority sr =
//                new StringRequestPriority(mMethod, mUrl, new Listener<String>() {
//
//                  public void onResponse(String response) {
//                    mStringData = response;
//                    fireDelegate(true, 0);
//                  }
//                }, new Response.ErrorListener() {
//
//                  public void onErrorResponse(VolleyError error) {
//                    int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
//                    w("onErrorResponse %s", error);
//                    if (error instanceof NoConnectionError) {
//                      CardSessionManager.getSessionManager().setSessionMode(SessionMode.OffLine);
//                    }
//                    fireDelegate(false, code);
//                  }
//
//                });
//        if (mMethod == Method.POST) {
//          sr.setPostBody(mPostBody);
//          sr.setPostParams(mPostParams);
//
//        }
//        sr.setShouldCache(false);
//        sr.setPriority(isHighPriority ? Priority.IMMEDIATE : Priority.NORMAL);
//        r = sr;
//      }
//        }
        r.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        WebRequestManager.addRequest(r, mTag);
    }

    /**
     * Start request as current type(Default is String, it would be Image type after invoked
     * setImageParams) and High priority
     */
    public void startImmediate() {
        startImmediate(mIsImg);
    }

    /**
     * Start request as @param img type type and High priority
     *
     * @param img Whether is bitmap
     */
    public void startImmediate(boolean img) {
        start(img, true);
    }

    /************ Private Methods **************/
    /**
     * Auto insert host into @param url if need and auto parse @param params to add into @param url
     *
     * @param url
     * @param params
     */
    private void autoCompleUrl(String url, HashMap<String, Object> params) {
        URL u = WebUtils.createURL(params == null ? url : WebUtils.compositeUrl(url, params));
        if (u != null) {
            mUrl = u.toString();
        }
    }

    private void fireDelegate(boolean result, int code) {
        RequestDelegate d = getDelegate();
        if (!result) {
            LogUtils.w("requst failed, url = %s code = %s", getUrl(), code);
        }
        if (d != null) {
            if (result) {
                d.requestFinished(this);
            } else {
                d.requestFailed(this, code);
            }
        }
    }
}
