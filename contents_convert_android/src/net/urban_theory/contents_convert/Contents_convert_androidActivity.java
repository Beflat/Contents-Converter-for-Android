package net.urban_theory.contents_convert;


import java.util.ArrayList;

import net.urban_theory.contents_convert.activity.RequestQueueListActivity;
import net.urban_theory.contents_convert.preference.GeneralPreference;
import net.urban_theory.contents_convert.service.RequestQueuePostService;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class Contents_convert_androidActivity extends ListActivity {
    /** Called when the activity is first created. */
    
    private ArrayList<Content> contentList;
    
    private ContentListAdapter contentListAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initApplication();
        
        contentList = new ArrayList<Content>();
        
        contentListAdapter = new ContentListAdapter(this, contentList);
        setListAdapter(contentListAdapter);
        
        RetrieveContentListTask loadTask = new RetrieveContentListTask(this, contentListAdapter);
        loadTask.execute("http://cc.urban-theory.net/api/content/get");
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater menuInflater = getMenuInflater();
        
        menuInflater.inflate(R.menu.main_menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    
    public boolean onPrepareOptionsMenu(Menu menu) {
        
        MenuItem item = menu.findItem(R.id.menu_toggle_auto_send_request);
        GeneralPreference gPref = new GeneralPreference(this);
        boolean autoSendStatus = gPref.getAutoSendRequest();
        
        if(autoSendStatus) {
            item.setTitle(R.string.menu_toggle_auto_send_request_off);
        } else {
            item.setTitle(R.string.menu_toggle_auto_send_request_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        
        boolean consumed = false;
        Toast toast;
        
        switch(item.getItemId()) {
        case R.id.menu_request_list:
            Intent intent = new Intent(getApplicationContext(), RequestQueueListActivity.class);
            startActivity(intent);
            consumed = true;
            break;
            
        case R.id.menu_config:
            Toast.makeText(this, "Show config", Toast.LENGTH_LONG).show();
            consumed = true;
            break;
            
        case R.id.menu_toggle_auto_send_request:
            GeneralPreference gPref = new GeneralPreference(this);
            boolean autoSendStatus = gPref.getAutoSendRequest();
            
            if(autoSendStatus) {
                //Off
                disableAutoSendRequest();
                Toast.makeText(this, "Request queue send service disabled.", Toast.LENGTH_LONG).show();
            } else {
                //On
                enableAutoSendRequest();
                Toast.makeText(this, "Request queue send service enabled.", Toast.LENGTH_LONG).show();
            }
            consumed = true;
            break;
            
        default:
            consumed = super.onOptionsItemSelected(item);
        }
        
        return consumed;
    }
    
    
    private void initApplication() {
        GeneralPreference gPref = new GeneralPreference(this);
        
        if(!gPref.isExists()) {
            gPref.initialize();
        }
    }
    
    
    private void enableAutoSendRequest() {
        
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getApplicationContext(), RequestQueuePostService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 15000, pendingIntent);
        
        GeneralPreference gPref = new GeneralPreference(this);
        gPref.setAutoSendRequest(true);
        gPref.commit();
        
        Log.d("cc-android", "Alarm Enabled.");
    }
    
    
    
    private void disableAutoSendRequest() {
        
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getApplicationContext(), RequestQueuePostService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmManager.cancel(pendingIntent);
        
        GeneralPreference gPref = new GeneralPreference(this);
        gPref.setAutoSendRequest(false);
        gPref.commit();
        
        Log.d("cc-android", "Alarm disabled.");
    }
    
}