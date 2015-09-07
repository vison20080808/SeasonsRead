package com.seasonsread.app.web;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.seasonsread.app.SeasonsReadApp;
import com.seasonsread.app.util.LogUtils;

import java.io.File;

/**
 * Created by ZhanTao on 1/9/15.
 */
public class WebRequestManager {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private WebRequestManager() {
        // no instances
    }

    public static void init(Context context) {

        if (mRequestQueue == null) {
//          mRequestQueue = Volley.newRequestQueue(context);
            File cacheDir = prepareCacheDir("SeasonsRead");

            String userAgent = "volley/0";

            try {
                String network = context.getPackageName();
                PackageInfo queue = context.getPackageManager().getPackageInfo(network, 0);
                userAgent = network + "/" + queue.versionCode;
            } catch (PackageManager.NameNotFoundException var6) {
                ;
            }

            HttpStack stack;
//            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
//            } else {
//                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
//            }

            BasicNetwork network1 = new BasicNetwork(stack);
            mRequestQueue = new RequestQueue(new DiskBasedCache(cacheDir), network1);
            mRequestQueue.start();
        }

        if (mImageLoader == null) {

            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            // Use 1/8th of the available memory for this memory cache.
            int cacheSize = 1024 * 1024 * memClass / 16;
            mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
        }
    }

    /**
     * Create the cache dir on SD card or in phone storage.
     */
    protected static File prepareCacheDir(String cacheDirName) {
        File dir;
        if (isExternalStorageWritable()) {
            dir = new File(Environment.getExternalStorageDirectory(), cacheDirName);
            if (!dir.exists())
                dir.mkdirs();
            LogUtils.i("cache dir in external storage");
        } else {
            dir = SeasonsReadApp.getAppContext().getDir(cacheDirName, Context.MODE_PRIVATE);
            LogUtils.i("cache dir in phone storage");
        }

        if (!dir.exists())
            LogUtils.w("cache dir %s, doesn't exist", dir);
        return dir;
    }

    private static boolean isExternalStorageWritable(){
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File tmp = new File(Environment.getExternalStorageDirectory(), "__ppy_tmp");
                if (tmp.exists())
                    tmp.delete();
                if (tmp.createNewFile()) {
                    tmp.delete();
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtils.d("Failed to test external storage writable: " + e);
        }
        return false;
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache}
     * which effectively means that no memory caching is used. This is useful
     * for images that you know that will be show only once.
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static void destroy(){
        if(mImageLoader != null){
            mImageLoader = null;
        }
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(SeasonsReadApp.getAppContext());
            mRequestQueue.stop();
            mRequestQueue = null;
        }
    }
}
