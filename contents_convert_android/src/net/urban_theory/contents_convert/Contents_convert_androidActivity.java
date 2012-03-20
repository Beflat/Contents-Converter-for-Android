package net.urban_theory.contents_convert;


import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

import net.urban_theory.contents_convert.activity.PreferenceActivity;
import net.urban_theory.contents_convert.activity.RequestQueueListActivity;
import net.urban_theory.contents_convert.data.ContentDataLoader;
import net.urban_theory.contents_convert.entity.Content;
import net.urban_theory.contents_convert.service.ContentDownloadService;
import net.urban_theory.contents_convert.util.FileUtil;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;


public class Contents_convert_androidActivity extends ListActivity {
    /** Called when the activity is first created. */
    
    private ArrayList<Content> contentList;
    
    private ContentListAdapter contentListAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        RetrieveContentListTask loadTask = new RetrieveContentListTask(this, contentListAdapter);
//        loadTask.execute("http://cc.urban-theory.net/api/content/get");
        
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContentsConvert/content_list.json";
        File file = new File(filePath);
        
        if(file.exists()) {
            try {
                BufferedReader br = FileUtil.getBufferedReaderFromFilePath(filePath);
                ContentDataLoader loader = new ContentDataLoader(br);
                
                loader.load();
                contentList = loader.getResult();
                
                br.close();
                
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("cc-android", e.getMessage());
            }
        } else {
            contentList = new ArrayList<Content>();
        }
        contentListAdapter = new ContentListAdapter(this, contentList);
        setListAdapter(contentListAdapter);
        
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater menuInflater = getMenuInflater();
        
        menuInflater.inflate(R.menu.main_menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        
        boolean consumed = false;
        Toast toast;
        
        Intent intent;
        switch(item.getItemId()) {
        case R.id.menu_request_list:
            intent = new Intent(getApplicationContext(), RequestQueueListActivity.class);
            startActivity(intent);
            consumed = true;
            break;
            
        case R.id.menu_config:
            intent = new Intent(getApplicationContext(), PreferenceActivity.class);
            startActivity(intent);
            consumed = true;
            break;
            
        default:
            consumed = super.onOptionsItemSelected(item);
        }
        
        return consumed;
    }
    
    
}
