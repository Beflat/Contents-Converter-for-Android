package net.urban_theory.contents_convert.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import net.urban_theory.contents_convert.ContentListLoadHandler;
import net.urban_theory.contents_convert.Parser.ContentListParser;
import net.urban_theory.contents_convert.data.ContentDataWriter;
import net.urban_theory.contents_convert.entity.Content;
import net.urban_theory.contents_convert.util.FileUtil;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParserException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class RetrieveContentListService extends IntentService {

    private String threadName;
    
    public RetrieveContentListService(String name) {
        super(name);
        threadName = name;
    }

    public RetrieveContentListService() {
        super("RetrieveContentListService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        
        try {
            Log.d("cc-android", "RetrieveContentListService Started");

            ArrayList<Content> list = getContentList();
            
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContentsConvert/content_list.json";
            File file = new File(filePath);
            File dir = new File(file.getParent());
            dir.mkdirs();
            
            BufferedWriter bw = FileUtil.getBufferedWriterFromFilePath(filePath);
            ContentDataWriter writer = new ContentDataWriter(bw);
            writer.setItems(list);
            
            writer.write();
            
            bw.close();
            
        } catch (Exception e) {
            Log.d("cc-android", e.getMessage());
        }
    }

    
    private ArrayList<Content> getContentList() throws ClientProtocolException, IOException, UnsupportedEncodingException, XmlPullParserException {
        ContentListLoadHandler handler = new ContentListLoadHandler();
        URL url = new URL("http://cc.urban-theory.net/api/content/get");
        InputStream is = url.openConnection().getInputStream();
        
        ContentListParser parser = new ContentListParser();
        parser.init(is, handler);
        parser.parse();
            
        return handler.getResult();
    }
    
    
}
