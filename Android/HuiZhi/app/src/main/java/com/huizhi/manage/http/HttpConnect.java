package com.huizhi.manage.http;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

public class HttpConnect {

	public static String getHttpConnect(String server_url, List<BasicNameValuePair> params)throws Exception{
		String param = URLEncodedUtils.format(params, "UTF-8");//对参数编码
		Log.i("HuiZhi", "The url:" + server_url + "?" + param);
		HttpGet httpGet = new HttpGet(server_url + "?" + param);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);// 请求超时
		HttpResponse response = httpClient.execute(httpGet); //发起GET请求
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}
		return null;
	}

	public static String postHttpConnect(String server_url, List <BasicNameValuePair> params) throws Exception{
//		System.out.println("The url:" + server_url + "?" + params);
		Log.i("HuiZhi", "The url:" + server_url + "?" + params);
		HttpPost httppost = new HttpPost(server_url);
		httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpClient client = new DefaultHttpClient();
		// 请求超时
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		HttpResponse httpResponse = client.execute(httppost);

		/*若状态码为200 ok*/
		if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			/*取出响应字符串*/
			HttpEntity responsetEntity = httpResponse.getEntity();
			String result = EntityUtils.toString(responsetEntity, "UTF-8");
			//				InputStream inputStream = responsetEntity.getContent();
			if(TextUtils.isEmpty(result))
				return null;
			return  result;
		}
		return null;
	}

	public static String postHttpConnect(String server_url, String params) throws Exception{
		System.out.println("The url:" + server_url + "?" + params);
		URL url = new URL(server_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
		httpURLConnection.setConnectTimeout(2000);
		httpURLConnection.setReadTimeout(5000);
		httpURLConnection.setDoOutput(true);//设置允许输出
		httpURLConnection.setRequestMethod("POST");//设置请求的方式
		httpURLConnection.setRequestProperty("ser-Agent", "Fiddler");

		//把上面访问方式改为异步操作,就不会出现 android.os.NetworkOnMainThreadException异常
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
		out.print(params);//写入输出流
		out.flush();//立即刷新

		out.close();
		int code = httpURLConnection.getResponseCode();
		if(code == 200 && ((url.toString()).equals(httpURLConnection.getURL().toString()))){
			//获取服务器响应后返回的数据
			InputStream is = httpURLConnection.getInputStream();
			byte b[] = new byte[1024];
			int len = 0;
			int temp=0;          //所有读取的内容都使用temp接收
			while((temp=is.read())!=-1){    //当没有读取完时，继续读取
				b[len]=(byte)temp;
				len++;
			}
			is.close();
			is.close();
			return URLDecoder.decode(new String(b,0,len), "utf-8");
		}
		return null;
	}

	public static String getHttpConnect(String url)throws Exception{
		System.out.println("The url is:" + url);
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);// 请求超时
		HttpResponse response = httpClient.execute(httpGet); //发起GET请求
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}
		return null;
	}

	public static InputStream getStreamFromURL(String fileurl) {
		System.out.println("The url is:" + fileurl);
		InputStream in=null;
		try {
			URL url=new URL(fileurl);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			in=connection.getInputStream();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
		
	}
}
