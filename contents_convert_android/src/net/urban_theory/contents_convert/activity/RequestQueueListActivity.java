package net.urban_theory.contents_convert.activity;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

import net.urban_theory.contents_convert.R;
import net.urban_theory.contents_convert.data.RequestQueueDataLoader;
import net.urban_theory.contents_convert.entity.RequestQueueItem;
import net.urban_theory.contents_convert.util.FileUtil;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class RequestQueueListActivity extends ListActivity {

    private RequestQueueListAdapter adapter;
    
    private ArrayList<RequestQueueItem> requestList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.request_queue_list);
        
        requestList = null;
        try {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContentsConvert/requests2.json";
            File file = new File(filePath);
            if(file.exists()) {
                BufferedReader br = FileUtil.getBufferedReaderFromFilePath(filePath);
                RequestQueueDataLoader loader = new RequestQueueDataLoader(br);
                loader.load();
                
                requestList = loader.getResult();
                
                br.close();
            }
        } catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        if(requestList == null) {
            requestList = new ArrayList<RequestQueueItem>();
        }
        adapter = new RequestQueueListAdapter(this, requestList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
