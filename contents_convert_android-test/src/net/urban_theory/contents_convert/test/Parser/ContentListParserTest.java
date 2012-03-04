package net.urban_theory.contents_convert.test.Parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.urban_theory.contents_convert.Content;
import net.urban_theory.contents_convert.Parser.ContentListParser;
import net.urban_theory.contents_convert.test.Parser.TestTool.DummyContentListLoadHandler;

public class ContentListParserTest extends TestCase{
    
    protected void setUp() {
    }
    
    
    protected void tearDown() {
    }
    
    
    /**
     * 1件をパース出来るかどうか
     */
    public void testParse() throws Exception{
        
        ContentListParser parser = new ContentListParser();
        DummyContentListLoadHandler handler = new DummyContentListLoadHandler();
        
        String testXml = "<ContentList>"
            + "<Content id=\"100\" status=\"0\" rule=\"aaa\">testTitle</Content>"
            + "</ContentList>";
        InputStream is = new ByteArrayInputStream(testXml.getBytes());
        
        parser.init(is, handler);
        parser.parse();
        
        ArrayList<Content> resultList = handler.getList();
        
        Assert.assertEquals(1, resultList.size());
        
        Content content = resultList.get(0);
        Assert.assertEquals(100, content.getId());
        Assert.assertEquals("testTitle", content.getTitle());
    }
    
    
    /**
     * 2件をパース出来るかどうか
     */
    public void testParse2Contents() throws Exception{
        
        ContentListParser parser = new ContentListParser();
        DummyContentListLoadHandler handler = new DummyContentListLoadHandler();
        
        String testXml = "<ContentList>"
            + "<Content id=\"100\" status=\"0\" rule=\"aaa\">testTitle</Content>"
            + "<Content id=\"101\" status=\"0\" rule=\"aaa\">testTitle2</Content>"
            + "</ContentList>";
        InputStream is = new ByteArrayInputStream(testXml.getBytes());
        
        parser.init(is, handler);
        parser.parse();
        
        ArrayList<Content> resultList = handler.getList();
        
        Assert.assertEquals(2, resultList.size());
        
        Content content = resultList.get(0);
        Assert.assertEquals(100, content.getId());
        Assert.assertEquals("testTitle", content.getTitle());
        
        content = resultList.get(1);
        Assert.assertEquals(101, content.getId());
        Assert.assertEquals("testTitle2", content.getTitle());
        
    }

}
