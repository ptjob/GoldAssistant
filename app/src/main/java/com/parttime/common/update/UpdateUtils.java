package com.parttime.common.update;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 *
 * Created by luhua on 15/7/12.
 *
 * 更新应用
 */
public class UpdateUtils {
    /**
     * 获取广告的json文件对象
     *
     */
    public String getJsonByPhp(String url) {
        HttpPost httpPost = null;
        ArrayList<NameValuePair> params = new ArrayList<>();
        try {
            httpPost = new HttpPost(url);
            // 设置字符集
            HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
            // 请求httpRequest
            httpPost.setEntity(httpentity);
            // 取得默认的HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 请求超时
            httpclient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            // 读取超时
            httpclient.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, 5000);
            // 取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpPost);
            // HttpStatus.SC_OK表示连接成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                String strResult = EntityUtils.toString(
                        httpResponse.getEntity(), "gbk");
                return strResult;
            }
            // 关闭连接
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            return "connect_fail";
        }
        return "connect_fail";
    }

}
