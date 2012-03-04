package net.urban_theory.contents_convert.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import net.urban_theory.contents_convert.data.RequestQueueDataLoader;
import net.urban_theory.contents_convert.data.RequestQueueDataWriter;
import net.urban_theory.contents_convert.entity.RequestQueueItem;
import net.urban_theory.contents_convert.util.FileUtil;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class RequestQueueActivity extends Activity {

    
    private String dataFilePath;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        setContentView(R.layout.main);

        ArrayList<RequestQueueItem> requestList = new ArrayList<RequestQueueItem>();
        
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
            
            RequestQueueItem newItem = new RequestQueueItem();
            newItem.setUrl(getIntent().getCharSequenceExtra("android.intent.extra.TEXT").toString());
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
    

}
