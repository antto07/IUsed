package com.iused.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sujith on 02-10-2015.
 */
public class HttpAsync extends AsyncTask<String, String, String> {

	public Context context = null;
	public String URL = null;
	public HashMap<String, String> params = null;
	private AsyncTaskListener async = null;
	public int METHOD = 0;
	public String tag = null;
	private java.net.URL url = null;
	protected String result = "";
	private String response = null;


	public HttpAsync(Context context, AsyncTaskListener listener, String URL, HashMap<String, String> params, int REQUESTMETHOD, String tag) {

		this.context = context;
		this.URL = URL;
		this.params = params;
		this.METHOD = REQUESTMETHOD;
		this.async = listener;
		this.tag = tag;

	}




	@Override
	protected String doInBackground(String... param) {

		switch (METHOD)
		{
			case 1:
				response = GETCALL(URL, params);
				Log.e("response is", response);
				break;

			case 2:
				response = POSTCALL(URL, params);
				Log.e("response is", response);
				break;
		}
		return response;
	}


	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);

		if(async!=null)
		{
			async.onTaskComplete(s, tag);
		}
		else
		{
			async.onTaskCancelled("error");
		}
	}


	public String POSTCALL(String requestUrl, HashMap<String, String> params)
	{
		try {
			java.net.URL url = new URL(requestUrl);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setUseCaches(false);
			httpConnection.setDoOutput(true);

			StringBuffer requestParams = new StringBuffer();

			if(params!=null && params.size()>0)
			{
				httpConnection.setDoOutput(true);
				Iterator<String> paramIterator = params.keySet().iterator();
				while (paramIterator.hasNext()) {
					String key = paramIterator.next();
					String value = params.get(key);
					if(params.get(key)!=null)
					{
						value = params.get(key);
					}
					else {
						value = "";
						Log.e("null value in key", key);
					}

					requestParams.append(URLEncoder.encode(key, "UTF-8"));
					requestParams.append("=").append(
							URLEncoder.encode(value, "UTF-8"));
					requestParams.append("&");
				}

				Log.e("request params", requestParams.toString());
			}

			OutputStreamWriter outputWriter = new OutputStreamWriter(httpConnection.getOutputStream());
			outputWriter.write(requestParams.toString());
			outputWriter.flush();

			int responsecode = httpConnection.getResponseCode();

			if(responsecode==HttpsURLConnection.HTTP_OK)
			{
				String line;
				BufferedReader buffreader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

				while ((line = buffreader.readLine())!=null)
				{
					result += line;
				}
			}

		} catch (MalformedURLException e) {
			result = "fail";
			e.printStackTrace();
		} catch (IOException e) {
			result = "fail";
			e.printStackTrace();
		}
		if(result.startsWith("<br"))
		{
			result = "fail";
		}
		return  result.trim();
	}



	public String GETCALL(String URL, HashMap<String, String> para)
	{
		try {

			StringBuilder result1 = new StringBuilder();
			boolean first = true;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (first)
					first = false;
				else
					result1.append("&");

				result1.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				result1.append("=");
				if(entry.getValue()!=null)
				{
					result1.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				}
				else
				{
					result1.append(URLEncoder.encode("", "UTF-8"));
				}

			}

			url = new URL(URL + result1.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					result += line;
				}
			} else {
				result = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		if(result.startsWith("<br"))
		{
			result = "fail";
		}
		return result.trim();
	}

}
