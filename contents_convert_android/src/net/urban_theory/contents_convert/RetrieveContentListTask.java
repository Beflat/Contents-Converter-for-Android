package net.urban_theory.contents_convert;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import net.urban_theory.contents_convert.Parser.ContentListParser;
import net.urban_theory.contents_convert.entity.Content;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RetrieveContentListTask extends
        AsyncTask<String, Integer, ArrayList<Content>> {
    
    private Contents_convert_androidActivity activity;
    private ContentListAdapter adapter;
    private ProgressDialog progressDialog;
    
    private ArrayList<String> loadingErrors;
    
    public RetrieveContentListTask(Contents_convert_androidActivity activity, ContentListAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingErrors = new ArrayList<String>();
    }

    
    
    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
    }


    @Override
    protected void onPostExecute(ArrayList<Content> list) {
        if(loadingErrors.size() != 0) {
            Log.d("cc-android", loadingErrors.toString());
            Toast toast = Toast.makeText(activity, loadingErrors.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
        
        for(Content content: list) {
            adapter.add(content);
        }
        
        progressDialog.dismiss();
        activity.setListAdapter(adapter);
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("コンテンツ一覧を取得中");
        progressDialog.show();
        loadingErrors.clear();
    }


    @Override
    protected ArrayList<Content> doInBackground(String... params) {
        
        ContentListLoadHandler handler = new ContentListLoadHandler();
        try {
            URL url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            
//            String testXmlDoc = "<ContentList>"
//                    + "<Content id=\"1\" status=\"0\">Test Content</Content>"
//                    + "</ContentList>";
//            InputStream is = new ByteArrayInputStream(testXmlDoc.getBytes());
            
            
            ContentListParser parser = new ContentListParser();
            parser.init(is, handler);
            parser.parse();
            
        } catch(Exception e) {
            //Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
            loadingErrors.add("コンテンツ情報の取得に失敗しました。");
            Log.d("cc-android", e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        
        return handler.getResult();
    }
    
}
