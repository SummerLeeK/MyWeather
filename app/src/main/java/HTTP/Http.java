package HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by u on 2016/10/17.
 */
public class Http {


    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }

    /**
     * 作用：实现网络访问文件，先给服务器通过“POST”方式提交数据，再返回相应的数据
     *
     * @param url
     *            ：访问网络的url地址
     * @param params
     *            String url：访问url时，需要传递给服务器的参数。 Map<String , Object>
     * @return byte[]
     */
    public static String doPostSubmit(String url, Map<String, Object> params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost requestPost = new HttpPost(url);
//        System.out.println("dopost1");
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();

        try {
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    BasicNameValuePair nameValuePair = new BasicNameValuePair(
                            key, value);
                    parameters.add(nameValuePair);
                }
            }
            HttpEntity httpEntityR = new UrlEncodedFormEntity(parameters, "UTF-8");
            requestPost.setEntity(httpEntityR);
//            System.out.println("dopost2");
            HttpResponse httpResponse = httpClient.execute(requestPost);
//            System.out.println("dopost3");

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String request(String httpUrl) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(httpUrl);

        HttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String server_response = EntityUtils.toString(response.getEntity());

                return server_response;
            } else {
               return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Bitmap getGossipImage(String url,String imagename){
        String httpUrl = url+imagename+".png";
        Bitmap bitmap = null;
        HttpGet httpRequest = new HttpGet(httpUrl);
        //取得HttpClient 对象
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //请求httpClient ，取得HttpRestponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //取得相关信息 取得HttpEntiy
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            }else{
                return null;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
