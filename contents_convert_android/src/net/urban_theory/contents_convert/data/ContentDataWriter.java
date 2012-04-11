package net.urban_theory.contents_convert.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.urban_theory.contents_convert.entity.Content;
import net.urban_theory.contents_convert.entity.RequestQueueItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ContentDataWriter {

    ArrayList<Content> itemList;
    
    private BufferedWriter writer;
    
    public ContentDataWriter(BufferedWriter writer) {
        this.writer = writer;
        itemList = new ArrayList<Content>();
    }
    
    
    public void write() throws DataWriterException, IOException {
        try {
            
            JSONObject rootObject = new JSONObject();
            
            JSONArray jsonArray = new JSONArray();
            int arrayLength = itemList.size();
            for(int i=0; i < arrayLength; i++) {
                JSONObject jsonObj = new JSONObject();
                
                jsonObj.put("id", itemList.get(i).getId());
                jsonObj.put("title", itemList.get(i).getTitle());
                jsonObj.put("rule", itemList.get(i).getRuleName());
                jsonObj.put("date", itemList.get(i).getDate());
                jsonObj.put("status", itemList.get(i).getStatus());
                
                jsonArray.put(jsonObj);
            }
            
            rootObject.put("contents", jsonArray);
            
            writer.write(rootObject.toString(4));
            Log.d("cc-android", rootObject.toString(4));
            
        } catch(JSONException jsonException) {
            throw new DataWriterException(jsonException.getMessage());
        }
    }
    
    
    public void setItems(ArrayList<Content> items) {
        itemList = items;
    }
}
