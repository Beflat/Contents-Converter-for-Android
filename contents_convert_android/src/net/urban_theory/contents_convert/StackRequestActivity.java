package net.urban_theory.contents_convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class StackRequestActivity extends Activity {

    
    private String dataFilePath;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
        try {
            dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContentsConvert/requests2.json";
            File file = new File(dataFilePath);
            
            File dir = new File(file.getParent());
            dir.mkdirs();
            
            JSONObject root = new JSONObject();
            
            
            
            //root.put("requests", parent);
            
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            
            bw.append(root.toString(4));
            
            bw.close();
            
        } catch(Exception e) {
            Log.d("cc-android", e.getMessage());
        }
        Toast.makeText(this, "Stacking request...", Toast.LENGTH_LONG).show();
        
        this.finish();
    }
    
    
    private ArrayList<StackRequest> loadRequestData() throws FileNotFoundException, UnsupportedEncodingException, IOException, JSONException {
        
        ArrayList<StackRequest> result = new ArrayList<StackRequest>();
        File file = new File(dataFilePath);
        
        if(!file.exists()) {
            return result;
        }
        
        String jsonText = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            
            String line = "";
            while((line = br.readLine()) != null) {
                jsonText += line;
            }
            br.close();
            
        } catch(FileNotFoundException fileNotFoundException ) {
            throw fileNotFoundException;
        } catch(UnsupportedEncodingException unsupportedEncodingException) {
            throw unsupportedEncodingException;
        } catch(IOException ioException) {
            throw ioException;
        }
        
        try {
            JSONObject rootObject = new JSONObject(jsonText);
            
            if(!rootObject.has("requests")) {
                return result;
            }
            
            JSONArray requestArray = rootObject.getJSONArray("requests");
            int arrayLength = requestArray.length();
            StackRequest stackRequest = null;;            
            for(int i=0; i < arrayLength; i++) {
                JSONObject item = requestArray.getJSONObject(i);
                
                if(!item.has("url") || !item.has("status")) {
                    continue;
                }
                
                stackRequest = new StackRequest();
                stackRequest.setUrl(item.getString("url"));
                stackRequest.setStatus(item.getInt("status"));
                
                result.add(stackRequest);
            }
                
        } catch(JSONException jsonException) {
            throw jsonException;
        }
        
        return result;
    }
    
}
