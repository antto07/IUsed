package com.iused.introduction;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("https://www.rovealliance.com", session);
        }
    };

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
        }

        // try parse the string to a JSON object
        try {
//            Log.e("result", json);
            jObj = new
                    JSONObject(json);
        } catch (JSONException e) {
        }

        // return JSON String
        return jObj;

    }

    public String https(List<NameValuePair> nameValuePair) {
        String result = null;
        try {

            URL url1 = new URL("https://www.rovealliance.com/service/syncprofile");

            HttpsURLConnection urlConnection = (HttpsURLConnection) url1.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setHostnameVerifier(hostnameVerifier);
            String paramsString = URLEncodedUtils
                    .format(nameValuePair, "UTF-8");


            SocketFactory sf = SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket("https://www.rovealliance.com", 8443);
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            SSLSession s = socket.getSession();
            if (!hv.verify("https://rovealliance.com", s)) {
                throw new SSLHandshakeException("Expected mail.google.com, found " + s.getPeerPrincipal());
            }
            socket.close();

            OutputStreamWriter outputWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputWriter.write(paramsString);
            outputWriter.flush();

            int responsecode = urlConnection.getResponseCode();

            if (responsecode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((line = buffreader.readLine()) != null) {
                    result += line;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}

