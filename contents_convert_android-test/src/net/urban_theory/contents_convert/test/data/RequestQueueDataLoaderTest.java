package net.urban_theory.contents_convert.test.data;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.urban_theory.contents_convert.data.RequestQueueDataLoader;
import net.urban_theory.contents_convert.entity.RequestQueueItem;

public class RequestQueueDataLoaderTest extends TestCase {

    protected void setUp() {
    }
    
    
    protected void tearDown() {
    }
    
    
    public void testLoad() throws Exception {
        
        String testBuffer = "{\"requests\":\n"
                + "[\n"
                + "    {\"url\": \"http://testurl.com/aaa.html\", \"status\": \"0\"}, \n"
                + "    {\"url\": \"http://testurl2.com/aaa.html\", \"status\": \"1\"} \n"
                + "]\n"
                + "}";
        BufferedReader bf = new BufferedReader(new StringReader(testBuffer));
        RequestQueueDataLoader loader = new RequestQueueDataLoader(bf);
        
        loader.load();
        
        ArrayList<RequestQueueItem> list = loader.getResult();
        
        Assert.assertEquals("2件がロードされる", 2, list.size());
        
        RequestQueueItem item = list.get(0);
        Assert.assertEquals("1件目のURLが正しい", "http://testurl.com/aaa.html", item.getUrl());
        Assert.assertEquals("1件目の状態が正しい", 0, item.getStatus());
    }
    
}
