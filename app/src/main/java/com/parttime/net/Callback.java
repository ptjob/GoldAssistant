package com.parttime.net;

import org.json.JSONException;

/**
 *
 * Created by luhua on 15/7/16.
 */
public interface Callback {
    void success(Object obj) throws JSONException;
    void failed(Object obj);
}
