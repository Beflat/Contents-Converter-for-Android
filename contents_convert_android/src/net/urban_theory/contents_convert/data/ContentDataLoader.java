package net.urban_theory.contents_convert.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import net.urban_theory.contents_convert.entity.Content;
import net.urban_theory.contents_convert.entity.RequestQueueItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentDataLoader {

    ArrayList<Content> itemList;
    
    private BufferedReader inputReader;
    private String inputBuffer;
    
    public ContentDataLoader(BufferedReader reader) {
        inputReader = reader;
        inputBuffer = "";
        itemList = new ArrayList<Content>();
    }
    
    
    public void load() throws DataLoaderException, IOException {
        try {
            
            if(inputBuffer.isEmpty()) {
                loadBufferFromReader();
            }
            
            JSONObject rootObject = new JSONObject(inputBuffer);
            
            if(!rootObject.has("contents")) {
                return;
            }
            
            JSONArray requestArray = rootObject.getJSONArray("contents");
            int arrayLength = requestArray.length();
            Content item = null;;            
            for(int i=0; i < arrayLength; i++) {
                JSONObject jsonObj = requestArray.getJSONObject(i);
                
                if(!jsonObj.has("id") || !jsonObj.has("title") || !jsonObj.has("rule") || !jsonObj.has("status") || !jsonObj.has("date")) {
                    continue;
                }
                
                item = new Content();
                item.setId(jsonObj.getInt("id"));
                item.setTitle(jsonObj.getString("title"));
                item.setRuleName(jsonObj.getString("rule"));
                item.setStatus(jsonObj.getInt("status"));
                item.setDate(jsonObj.getString("date"));
                
                itemList.add(item);
            }
                
        } catch(JSONException jsonException) {
            throw new DataLoaderException(jsonException.getMessage());
        }
    }
    
    
    public ArrayList<Content> getResult() {
        return itemList;
    }
    
    
    private void loadBufferFromReader() throws IOException{
        inputBuffer = "";
        String line = "";
        while((line = inputReader.readLine()) != null) {
            inputBuffer += line;
        }
    }
}
