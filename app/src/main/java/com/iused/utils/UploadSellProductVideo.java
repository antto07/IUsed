package com.iused.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.iused.main.Sell_Products_Activity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Antto on 16/12/2016.
 */
public class UploadSellProductVideo extends AsyncTask<String, String, String> {

    Context context;

    File file;
    String picturePath;
    String auth = null;
    String response;
    String str_file_path;
    String str_img_url_final;

    public UploadSellProductVideo(Context context, String picturePath,String file_path) {
        this.context = context;
        this.picturePath = picturePath;
        this.str_file_path=file_path;
    }

    private ProgressDialog progressLoadingDialog = null;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressLoadingDialog = new ProgressDialog(context);
        progressLoadingDialog.setMessage("Uploading...");
        progressLoadingDialog.setCancelable(false);
        progressLoadingDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {

        try {

            AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( "AKIAI43OM3NCQPZIXUCA", "LvhnjsublxcZUruRB1ffUjxG6PP1PRAULwyxoi/y" ) );
//				s3Client.createBucket( "mymarketbucket" );
            File filePath = new File(str_file_path);
            PutObjectRequest por = new PutObjectRequest( "mymarketapp",picturePath, filePath);
            por.setCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject( por );

//                ResponseHeaderOverrides override = new ResponseHeaderOverrides();
//                override.setContentType( "image/jpeg" );

            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( "mymarketapp", picturePath);
//                urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ) );  // Added an hour's worth of milliseconds to the current time.
//                urlRequest.setResponseHeaders( override );
            urlRequest.setMethod(HttpMethod.PUT);

            URL url = s3Client.generatePresignedUrl( urlRequest );
            Log.e("url_final",url.toString());
            str_img_url_final="https://s3-us-west-2.amazonaws.com/mymarketapp/"+picturePath;
            Log.e("url_final_server",str_img_url_final);
            Sell_Products_Activity.str_video_url=str_img_url_final;
        }catch (Exception e){

            progressLoadingDialog.dismiss();
//            Toast.makeText(context,"Oops! Please try again",Toast.LENGTH_SHORT).show();
        }


        return str_img_url_final;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressLoadingDialog.dismiss();



//        try {
//                        JSONObject jsonObject = new JSONObject(s);
//                        if(jsonObject!=null)
//                        {
//                            if (jsonObject.optBoolean("success", true)) {
//                                Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
    }


    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "AaB03x87yxdkjnxvi7";

    public String upload(URL url, File file, String fileParameterName,
                         HashMap<String, String> parameters) throws IOException {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        FileInputStream fileInputStream = null;

        byte[] buffer;
        int maxBufferSize = 20 * 1024;
        try {
            // ------------------ CLIENT REQUEST
            fileInputStream = new FileInputStream(file);

            // open a URL connection to the Servlet
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\""
                    + fileParameterName + "\"; filename=\"" + "hotel_image"
                    + ".jpg" + "\"" + lineEnd);
            dos.writeBytes("Content-Type:image/jpg" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of maximum size
            buffer = new byte[Math.min((int) file.length(), maxBufferSize)];
            int length;
            // read file and write it into form...
            while ((length = fileInputStream.read(buffer)) != -1) {
                dos.write(buffer, 0, length);
            }

            for (String name : parameters.keySet()) {
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\""
                        + name + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(parameters.get(name));
            }

            // send multipart form data necessary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
            if (dos != null)
                dos.close();
        }

        // ------------------ read the SERVER RESPONSE
        try {
            dis = new DataInputStream(conn.getInputStream());
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = dis.readLine()) != null) {
                response.append(line).append('\n');
            }

            Log.e("multiXXXX", "Upload file responce:"
                    + response.toString());
            return response.toString();
        } finally {
            if (dis != null)
                dis.close();
        }
    }

}
