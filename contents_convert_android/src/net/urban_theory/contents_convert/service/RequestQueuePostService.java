package net.urban_theory.contents_convert.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.urban_theory.contents_convert.data.DataLoaderException;
import net.urban_theory.contents_convert.data.RequestQueueDataLoader;
import net.urban_theory.contents_convert.entity.RequestQueueItem;
import net.urban_theory.contents_convert.util.FileUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class RequestQueuePostService extends IntentService {

    private String threadName;
    
    public RequestQueuePostService(String name) {
        super(name);
        threadName = name;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        
        try {
            ArrayList<RequestQueueItem> list = loadRequestQueueItems();
            
            sendRequest(list);
        } catch (Exception e) {
            Log.d("cc-android", e.getMessage());
        }
    }

    
    private ArrayList<RequestQueueItem> loadRequestQueueItems() throws IOException, DataLoaderException {
        String filePath = "";
        
        ArrayList<RequestQueueItem> list = new ArrayList<RequestQueueItem>();
        File file = new File(filePath);
        if(!file.exists()) {
            return list;
        }
        
        BufferedReader br = FileUtil.getBufferedReaderFromFilePath(filePath);
        RequestQueueDataLoader loader = new RequestQueueDataLoader(br);
        
        loader.load();
        list = loader.getResult();
        
        br.close();
        return list;
    }
    
    
    private void sendRequest(ArrayList<RequestQueueItem> list) throws ClientProtocolException, IOException, UnsupportedEncodingException {
        
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http")
            .authority("cc.urban-theory.net")
            .path("/api/request/post");
        
        String uri = uriBuilder.toString();
        
//        HttpClient client = new DefaultHttpClient();
//        HttpParams httpParams = client.getParams();
//        HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
//        HttpConnectionParams.setSoTimeout(httpParams, 1000);
//        
//        HttpPost httpRequest = new HttpPost();
//        httpRequest.
        
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        for(RequestQueueItem item : list) {
            
            if(item.getStatus() == RequestQueueItem.STATUS_SENT) {
                continue;
            }
            
            params.add(new BasicNameValuePair("urls[]", item.getUrl()));
        }
        
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        
        HttpResponse response = client.execute(httpPost);
        
        if(response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("リクエスト結果のHTTPコード異常：" + Integer.toString(response.getStatusLine().getStatusCode()) );
        }
        
        //とてもきわどいやり方...
        for(RequestQueueItem item : list) {
            if(item.getStatus() == RequestQueueItem.STATUS_SENT) {
                continue;
            }
            
            
        }
        
    }
}
