package net.urban_theory.contents_convert.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.urban_theory.contents_convert.data.RequestQueueDataLoader;
import net.urban_theory.contents_convert.data.RequestQueueDataWriter;
import net.urban_theory.contents_convert.entity.RequestQueueItem;
import net.urban_theory.contents_convert.util.FileUtil;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class RequestQueueActivity extends Activity {

    
    private String dataFilePath;
    private int serviceState = ServiceState.STATE_OUT_OF_SERVICE;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        setContentView(R.layout.main);

        ArrayList<RequestQueueItem> requestList = new ArrayList<RequestQueueItem>();
        
        TelephonyManager telManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);
        
        Toast.makeText(this, "Stacking request...", Toast.LENGTH_LONG).show();
        try {
            dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContentsConvert/requests2.json";
            File file = new File(dataFilePath);
            
            File dir = new File(file.getParent());
            dir.mkdirs();
            
            
            //既存データがあればロードする
            if(file.exists()) {
                BufferedReader br = FileUtil.getBufferedReaderFromFilePath(dataFilePath);
                RequestQueueDataLoader loader = new RequestQueueDataLoader(br);
                
                loader.load();
                requestList = loader.getResult();
                br.close();
            }
            
            String intentParam = getIntent().getCharSequenceExtra("android.intent.extra.TEXT").toString();
            String urlString = getUrlFromParameter(intentParam);
            
            //圏外でなければURLを展開する
            if(serviceState == ServiceState.STATE_IN_SERVICE ) {
                urlString = resolveUrl(urlString, 10);
            }
            
            RequestQueueItem newItem = new RequestQueueItem();
            newItem.setUrl(urlString);
            newItem.setStatus(RequestQueueItem.STATUS_UNSENT);
            
            requestList.add(newItem);
            
            BufferedWriter bw = FileUtil.getBufferedWriterFromFilePath(dataFilePath);
            RequestQueueDataWriter writer = new RequestQueueDataWriter(bw);
            writer.setItems(requestList);
            
            writer.write();
            
            bw.close();
            
        } catch(Exception e) {
            Log.d("cc-android", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        this.finish();
    }
    
    
    protected void onResume() {
        super.onResume();
        
    }
    
    
    protected void onPause() {
        super.onPause();
        
        TelephonyManager telManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }
    
    
    private String getUrlFromParameter(String param) {
        String[] lines = param.split("\\s");
        
        String urlPattern = "^http(?:s{0,1})://";
        Pattern regexPattern = Pattern.compile(urlPattern);
        Matcher m;
        
        for(String line : lines) {
            m = regexPattern.matcher(line);
            if(m.find()) {
                return line;
            }
        }
        return "";
    }

    private String resolveUrl(String url, int maxDepth) {
        String resolvedUrl = url;
        
        HttpClient client = new DefaultHttpClient();
        HttpHead httpHead = new HttpHead(url);
        Header[] responseHeaders = null;
        client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
        
        try {
            HttpResponse response = client.execute(httpHead);
            
            responseHeaders = response.getHeaders("Location");
            if(responseHeaders.length > 0) {
                resolvedUrl = responseHeaders[0].getValue();
                if(maxDepth > 0) {
                    resolvedUrl = resolveUrl(resolvedUrl, maxDepth - 1);
                }
            }
        } catch (ClientProtocolException clientException) {
            Log.d("cc-android", clientException.getMessage());
        } catch (IOException ioException) {
            Log.d("cc-android", ioException.getMessage());
        }
        
        return resolvedUrl;
    }
    
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onServiceStateChanged(ServiceState newServiceState) {
            super.onServiceStateChanged(newServiceState);
            serviceState = newServiceState.getState();
            Log.d("cc-android", "new service state: " + Integer.toString(serviceState));
        }
    };
}
