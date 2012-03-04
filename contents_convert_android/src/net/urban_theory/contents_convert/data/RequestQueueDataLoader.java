package net.urban_theory.contents_convert.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import net.urban_theory.contents_convert.entity.RequestQueueItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestQueueDataLoader {

    ArrayList<RequestQueueItem> itemList;
    
    private BufferedReader inputReader;
    private String inputBuffer;
    
    public RequestQueueDataLoader(BufferedReader reader) {
        inputReader = reader;
        inputBuffer = "";
        itemList = new ArrayList<RequestQueueItem>();
    }
    
    
    public void load() throws DataLoaderException, IOException {
        try {
            
            if(inputBuffer.isEmpty()) {
                loadBufferFromReader();
            }
            
            JSONObject rootObject = new JSONObject(inputBuffer);
            
            if(!rootObject.has("requests")) {
                return;
            }
            
            JSONArray requestArray = rootObject.getJSONArray("requests");
            int arrayLength = requestArray.length();
            RequestQueueItem item = null;;            
            for(int i=0; i < arrayLength; i++) {
                JSONObject jsonObj = requestArray.getJSONObject(i);
                
                if(!jsonObj.has("url") || !jsonObj.has("status")) {
                    continue;
                }
                
                item = new RequestQueueItem();
                item.setUrl(jsonObj.getString("url"));
                item.setStatus(jsonObj.getInt("status"));
                
                itemList.add(item);
            }
                
        } catch(JSONException jsonException) {
            throw new DataLoaderException(jsonException.getMessage());
        }
    }
    
    
    public ArrayList<RequestQueueItem> getResult() {
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
