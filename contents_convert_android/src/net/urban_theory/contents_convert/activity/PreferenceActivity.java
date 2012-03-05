package net.urban_theory.contents_convert.activity;

import net.urban_theory.contents_convert.R;
import net.urban_theory.contents_convert.service.RequestQueuePostService;
import net.urban_theory.contents_convert.service.RetrieveContentListService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    private boolean prevAutoSendRequest = false;
    private boolean prevAutoSyncContent = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PreferenceManager.setDefaultValues(this,
                R.xml.pref, false);
        
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);;
        prevAutoSendRequest = pref.getBoolean("pref_auto_send_request", false);
        prevAutoSyncContent = pref.getBoolean("pref_auto_sync_content", false);
        
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);;
        
        boolean autoSendRequest = pref.getBoolean("pref_auto_send_request", false);
        boolean autoSyncContent = pref.getBoolean("pref_auto_sync_content", false);
        
        if(prevAutoSendRequest != autoSendRequest) {
            if(autoSendRequest) {
                enableAutoSendRequest();
            } else {
                disableAutoSendRequest();
            }
        }
        if(prevAutoSyncContent != autoSyncContent) {
            if(autoSyncContent) {
                enableSyncContentList();
            } else {
                disableSyncContentList();
            }
        }
        
        super.onDestroy();
    }

    private void enableAutoSendRequest() {
        
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getApplicationContext(), RequestQueuePostService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 2 * 60 * 60 * 1000, pendingIntent);
        
        Log.d("cc-android", "Alarm Enabled.");
    }
    
    
    private void disableAutoSendRequest() {
        
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getApplicationContext(), RequestQueuePostService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmManager.cancel(pendingIntent);
        
        Log.d("cc-android", "Alarm disabled.");
    }

    
    private void enableSyncContentList() {
        
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getApplicationContext(), RetrieveContentListService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 2 * 60 * 60 * 1000, pendingIntent);
        Log.d("cc-android", "enableSyncContentList.");
    }

    private void disableSyncContentList() {
        
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getApplicationContext(), RetrieveContentListService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmManager.cancel(pendingIntent);
        
        Log.d("cc-android", "disableSyncContentList");
    }

    
}
