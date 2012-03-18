package net.urban_theory.contents_convert.test.data;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.urban_theory.contents_convert.data.RequestQueueDataWriter;
import net.urban_theory.contents_convert.entity.RequestQueueItem;
import net.urban_theory.contents_convert.util.FileUtil;
import android.util.Log;

public class RequestQueueDataWriterTest extends TestCase {

    protected void setUp() {
    }
    
    
    protected void tearDown() {
    }
    
    
    public void testWrite() throws Exception {
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedWriter bw = FileUtil.getBufferedWriterFromString(bos);
        RequestQueueDataWriter writer = new RequestQueueDataWriter(bw);
        
        ArrayList<RequestQueueItem> list = new ArrayList<RequestQueueItem>();
        
        RequestQueueItem item = new RequestQueueItem();
        item.setUrl("test1");
        item.setStatus(10);
        list.add(item);
        
        item.setUrl("test2");
        item.setStatus(20);
        list.add(item);
        
        writer.setItems(list);
        
        writer.write();
        bw.close();
        
        String result = bos.toString();
        String expected = "a";
        
        //Assert.assertEquals("バッファ上の文字列長が正しい", 1, bos.toByteArray().length);
        //Assert.assertEquals("エンコーディングに成功", expected, result);
        
        
    }
    
    
    public void testByteStream() throws Exception{
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter os = new OutputStreamWriter(baos, "UTF-8");
        BufferedWriter writer = new BufferedWriter(os);
        
        writer.write("some text");
        
        writer.close();
        Assert.assertEquals("toStringで、書いたものと同じ内容を取得できる。", true, baos.toString().equals("some text"));
    }
}
