package net.urban_theory.contents_convert.test.service;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class RequestQueuePostServiceTest extends TestCase {

    
    public void testResolveUrl() {
        
        //URL展開のテスト
        String url = "http://bit.ly/z6qp8l";
        String resolvedUrl = resolveUrl(url, 10);
        Assert.assertEquals("URLが正しく展開される", "http://www.47news.jp/CN/201203/CN2012031801001470.html", resolvedUrl);
        
        //
        url = "http://cc.urban-theory.net/";
        resolvedUrl = resolveUrl(url, 10);
        Assert.assertEquals("展開不要なURLはそのまま返却される", "http://cc.urban-theory.net/", resolvedUrl);
    }
    
    private String resolveUrl(String url, int maxDepth) {
        String resolvedUrl = url;
        
        HttpClient client = new DefaultHttpClient();
        HttpHead httpHead = new HttpHead(url);
        Header[] responseHeaders = null;
        client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
        
        try {
            HttpResponse response = client.execute(httpHead);
            
            responseHeaders = response.getHeaders("Location");
            if(responseHeaders.length > 0) {
                resolvedUrl = responseHeaders[0].getValue();
                if(maxDepth > 0) {
                    resolvedUrl = resolveUrl(resolvedUrl, maxDepth - 1);
                }
            }
        } catch (ClientProtocolException clientException) {
            Log.d("cc-android", clientException.getMessage());
        } catch (IOException ioException) {
            Log.d("cc-android", ioException.getMessage());
        }
        
        return resolvedUrl;
    }
    
}
