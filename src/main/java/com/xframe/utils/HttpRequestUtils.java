package com.xframe.utils;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

	public static String post(String url, Map<String, String> reqParams) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<NameValuePair> form = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : reqParams.entrySet()) {
			form.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		ResponseHandler<String> handler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (null != entity) {
						return EntityUtils.toString(entity, "UTF-8");
					}
				} else {
					logger.debug("tocke status: " + status.getStatusCode());
				}
				return null;
			}
		};

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, "UTF-8");
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		String content = httpClient.execute(httpPost, handler);
		return content;

	}

	public static String get(String url, Map<String, String> reqParams) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(url);
		for (Entry<String, String> entry : reqParams.entrySet()) {
			builder.setParameter(entry.getKey(), entry.getValue());
		}
		URI uri = builder.build();

		ResponseHandler<String> handler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (null != entity) {
						return EntityUtils.toString(entity, "UTF-8");
					}
				}
				return null;
			}
		};

		HttpGet httpGet = new HttpGet(uri);
		String content = httpClient.execute(httpGet, handler);
		return content;

	}

	public static String delete(String url, Map<String, String> reqParams) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(url);
		for (Entry<String, String> entry : reqParams.entrySet()) {
			builder.setParameter(entry.getKey(), entry.getValue());
		}
		URI uri = builder.build();
		logger.debug(uri.toURL().toString());
		ResponseHandler<String> handler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (null != entity) {
						return EntityUtils.toString(entity, "UTF-8");
					}
				}
				return null;
			}
		};

		HttpDelete httpDelete = new HttpDelete(uri);
		String content = httpClient.execute(httpDelete, handler);
		return content;

	}

	/**
	 * 发送 http post 请求，支持文件上传
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String httpPostFormMultipart(String url, Map<String, String> params, File file,
			Map<String, String> headers, String encode) throws ClientProtocolException, IOException {
		if (encode == null) {
			encode = "utf-8";
		}
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(url);

		// 设置header
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
		mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		mEntityBuilder.setCharset(Charset.forName(encode));

		// 普通参数
		ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));// 解决中文乱码
		if (params != null && params.size() > 0) {
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				// mEntityBuilder.addTextBody(key, params.get(key), ContentType.TEXT_PLAIN);
				mEntityBuilder.addPart(key, new StringBody(params.get(key), contentType));
				logger.debug(key + "  " + params.get(key));
			}
		}
		// 二进制参数
		if (file != null) {
			// mEntityBuilder.addBinaryBody("file", file);
			mEntityBuilder.addPart("file", new FileBody(file));
		}
		httpost.setEntity(mEntityBuilder.build());
		String content = null;

		ResponseHandler<String> handler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (null != entity) {
						return EntityUtils.toString(entity, "UTF-8");
					}
				}
				return null;
			}
		};

		content = closeableHttpClient.execute(httpost, handler);
		return content;

	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 1.获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 2.中文有乱码的需要将PrintWriter改为如下
			// out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
