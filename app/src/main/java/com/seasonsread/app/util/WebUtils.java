package com.seasonsread.app.util;

import android.net.Uri;

import com.seasonsread.app.base.SRConfig;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.seasonsread.app.util.LangUtils.acquireStringBuilder;
import static com.seasonsread.app.util.LangUtils.releaseStringBuilder;

/**
 * Created by ZhanTao on 7/15/15.
 */
public class WebUtils {


    private static final ObjectCache<String, URL> url_cache = new ObjectCache<String, URL>(100);

    /**
     * Create a URL object from String.
     *
     * @param relativeString
     * @return URL
     */
    public static URL createURL(String relativeString) {

        return createURL(relativeString, SRConfig.DEFAULT_WEB_URL);
    }

    /**
     * Create a URL object based on original URL.
     *
     * @param relativeString
     * @param base
     * @return null means malformed uri
     */
    public static URL createURL(String relativeString, URL base) {
        try {
            if (relativeString == null)
                return base;

            URL url = url_cache.get(relativeString);

            if (url == null) {
                if (base == null)
                    url = new URL(relativeString);
                else
                    url = new URL(base, relativeString);
            }

            if (url != null)
                url_cache.put(relativeString, url);

            return url;
        } catch (Exception e) {
            LogUtils.e("Failed to createURI %s, %s", relativeString, base);
        }

        return null;
    }

    /**
     * Modify Url's content to a String.
     *
     * @param url
     * @param params
     * @return String
     */
    public static String compositeUrl(String url, Map<String, Object> params) {
        StringBuilder b = acquireStringBuilder(url.length()).append(url);
        if (params != null && !params.isEmpty()) {
            if (!url.contains("?"))
                b.append('?');
            else
                b.append('&');
            LogUtils.d("compositeUrl: url is %s", b.toString());
            int i = 0;
            Set<Entry<String, Object>> set = params.entrySet();
            for (Entry<String, Object> entry : set) {
                i++;
                b.append(entry.getKey()).append('=')
                        .append(entry.getValue() == null ? "" : Uri.encode(entry.getValue().toString()));
                if (i < set.size())
                    b.append('&');
            }
        }

        return releaseStringBuilder(b);
    }

}
