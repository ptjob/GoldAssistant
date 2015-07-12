package com.carson.https;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpsUtils {

	/**
	 * Post请求连接Https服务
	 * 
	 * @param serverURL
	 *            请求地址
	 * @param jsonStr
	 *            请求报文
	 * @return
	 * @throws Exception
	 */
	public static synchronized String doHttpsPost(String serverURL,
			List<NameValuePair> params) throws Exception {
		// 参数
		HttpParams httpParameters = new BasicHttpParams();
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(httpParameters, 3000);
		// 获取HttpClient对象 （认证）
		HttpClient hc = initHttpClient(httpParameters);
		HttpPost post = new HttpPost(serverURL);
		// 发送数据类型
		// post.addHeader("Content-Type", "application/json;charset=utf-8");
		// 接受数据类型
		post.addHeader("Accept", "application/json");
		// 请求报文
		// StringEntity entity = new StringEntity(jsonStr, "UTF-8");
		StringEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		post.setEntity(entity);
		post.setParams(httpParameters);
		HttpResponse response = null;
		try {
			response = hc.execute(post);
		} catch (UnknownHostException e) {
			throw new Exception("Unable to access " + e.getLocalizedMessage());
		} catch (SocketException e) {
			e.printStackTrace();
		}

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else {
			return "request_error";
		}
	}

	private static HttpClient client = null;

	/**
	 * 初始化HttpClient对象
	 * 
	 * @param params
	 * @return
	 */
	public static synchronized HttpClient initHttpClient(HttpParams params) {
		if (client == null) {
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
				// 允许所有主机的验证
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				// 设置http和https支持
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);

				return new DefaultHttpClient(ccm, params);
			} catch (Exception e) {
				e.printStackTrace();
				return new DefaultHttpClient(params);
			}
		}
		return client;
	}

	public static class SSLSocketFactoryImp extends SSLSocketFactory {
		final SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryImp(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
