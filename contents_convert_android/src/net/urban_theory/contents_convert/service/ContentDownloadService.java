package net.urban_theory.contents_convert.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.urban_theory.contents_convert.R;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ContentDownloadService extends IntentService {

    private Handler periodicEventHandler = new Handler();
    
    public ContentDownloadService(String name) {
        super(name);
    }

    public ContentDownloadService() {
        super("ContentDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        
        Log.d("cc-android", "Passed1");
        
        String externalRoot = getString(R.string.config_external_root);
        String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + externalRoot;
        
        int contentId = intent.getIntExtra("net.urban-theory.content_convert.content_id", 0);
        if(contentId == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_content_id), Toast.LENGTH_LONG).show();
            stopSelf();
            return;
        }
        
        String hostName = getString(R.string.config_server_host);
        String url = "http://" + hostName + "/content/download/" + Integer.toString(contentId);
        
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        
        CharSequence finishTitle = getString(R.string.title_download_success);
        CharSequence finishMessage = "";
        try {
            int contentLength = -1;
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if(statusCode == HttpStatus.SC_OK) {
                for(Header header: httpResponse.getAllHeaders()) {
                    Log.d("cc-android", " Get Header - " + header.getName() + ": " + header.getValue());
                }
                
                String attachmentName = "";
                Header[] contentDispositionHeaders = httpResponse.getHeaders("content-disposition");
                if(contentDispositionHeaders.length > 0) {
                    attachmentName = getFileNameFromHeaderString(contentDispositionHeaders[0].getValue());
                }
                if(attachmentName == "") {
                    attachmentName = "untitled.epub";
                }
                
                String filePath = rootDir + "/" + attachmentName;
                File file = new File(filePath);
                
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                
                contentLength = (int)httpResponse.getEntity().getContentLength();
                Log.d("cc-android", "Content-Length: " + Integer.toString(contentLength));
                byte[] buffer = new byte[1024 * 20];
                
                int size = 0;
                int downloadedBytes = 0;
                while(-1 != (size = bis.read(buffer))) {
                    bos.write(buffer, 0, size);
                    
                    downloadedBytes += size;
                    updateNotification(contentLength, downloadedBytes);
                }
                bos.flush();
                
                bos.close();
                bis.close();
                
                finishMessage = getString(R.string.title_download_success) + ": " + file.getName();
                
            } else {
                throw new IOException("Error: HttpResponse=" + Integer.toString(statusCode));
            }
            
        } catch (ClientProtocolException clientProtocolException) {
            finishTitle = getString(R.string.title_download_error);
            finishMessage = clientProtocolException.getMessage();
            Log.d("cc-android", finishMessage.toString());
        } catch (IOException ioException) {
            finishTitle = getString(R.string.title_download_error);
            finishMessage = ioException.getMessage();
            Log.d("cc-android", finishMessage.toString());
        }
        
        createFinishNotification(finishTitle, finishMessage);
        
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    private PendingIntent createNotificationIntent() {
        Intent notificationIntent = new Intent(ContentDownloadService.this, ContentDownloadService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ContentDownloadService.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        return pendingIntent;
    }
    
    private void startNotification() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(android.R.drawable.stat_sys_download, getString(R.string.start_download_content), System.currentTimeMillis());
        
        PendingIntent pendingIntent = createNotificationIntent();
        
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.download_progress);
        remoteView.setTextViewText(R.id.download_progress_title, getString(R.string.start_download_content));
        remoteView.setProgressBar(R.id.download_progress, 100, 0, false);
        notification.contentView = remoteView;
        notification.contentIntent = pendingIntent;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(1, notification);
        
    }
    
    
    private void updateNotification(int max, int progress) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(android.R.drawable.stat_sys_download, getString(R.string.start_download_content), System.currentTimeMillis());
        
        PendingIntent pendingIntent = createNotificationIntent();
        
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.download_progress);
        remoteView.setTextViewText(R.id.download_progress_title, getString(R.string.start_download_content));
        remoteView.setProgressBar(R.id.download_progress, max, progress, false);
        notification.contentView = remoteView;
        notification.contentIntent = pendingIntent;
        notificationManager.notify(1, notification);
    }
    
    
    private void createFinishNotification(CharSequence title, CharSequence message) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(android.R.drawable.stat_sys_download, getString(R.string.start_download_content), System.currentTimeMillis());
        
        PendingIntent pendingIntent = createNotificationIntent();
        notification.setLatestEventInfo(getApplicationContext(), title, message, pendingIntent);
        
        
        notificationManager.notify(2, notification);
        
    }
    
    
    private String getFileNameFromHeaderString(String headerString) {
        Pattern regExPattern = Pattern.compile("filename=\"(.+)\"");
        Matcher m = regExPattern.matcher(headerString);
        
        String fileName = "untitled.epub";
        if(m.find()) {
            fileName = m.group(1);
        }
        
        return fileName;
    }
}
