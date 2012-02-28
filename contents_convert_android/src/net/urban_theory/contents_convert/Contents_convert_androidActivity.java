package net.urban_theory.contents_convert;


import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
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
        
        contentList = new ArrayList<Content>();
        
        contentListAdapter = new ContentListAdapter(this, contentList);
        setListAdapter(contentListAdapter);
        
//        Content row1 = new Content();
//        row1.setTitle("test1");
//        contentListAdapter.add(row1);
        
        RetrieveContentListTask loadTask = new RetrieveContentListTask(this, contentListAdapter);
        loadTask.execute("http://cc.urban-theory.net/api/content/get");
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
        
        switch(item.getItemId()) {
        case R.id.menu_request_list:
            toast = Toast.makeText(this, "Show request list", Toast.LENGTH_LONG);
            toast.show();
            consumed = true;
            break;
            
        case R.id.menu_config:
            toast = Toast.makeText(this, "Show config", Toast.LENGTH_LONG);
            toast.show();
            consumed = true;
            break;
            
        default:
            consumed = super.onOptionsItemSelected(item);
        }
        
        return consumed;
    }
}