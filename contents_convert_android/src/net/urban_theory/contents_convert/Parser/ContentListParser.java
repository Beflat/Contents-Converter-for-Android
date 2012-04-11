package net.urban_theory.contents_convert.Parser;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import net.urban_theory.contents_convert.entity.Content;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;


public class ContentListParser {

    private InputStream input;
    private LoadHandler<Content> handler;
    
    public ContentListParser() {
        input = null;
        handler = null;
    }
    
    
    public void init(InputStream input, LoadHandler<Content> handler) {
        this.input = input;
        this.handler = handler;
    }
    
    
    public void parse() throws XmlPullParserException, IOException{
        
        XmlPullParser parser = Xml.newPullParser();
        try {
            
            parser.setInput(input, null);
            int eventType = parser.getEventType();
            
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                case XmlPullParser.START_TAG:
                    parseStartTag(parser);
                    break;
                }
                eventType = parser.next();
            }
            
        } catch (XmlPullParserException xmlException) {
            throw xmlException;
        } catch (IOException ioException) {
            throw ioException;
        }
    }
    
    
    private void parseStartTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        String tagName = parser.getName().toLowerCase();
        
        if(tagName.equals("content")) {
            parseContentTag(parser);
        }
    }
    
    
    private void parseContentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        
        int event = parser.getEventType();
        
        Assert.assertTrue(event == XmlPullParser.START_TAG);
        
        CharSequence id;
        CharSequence status;
        CharSequence title;
        CharSequence rule;
        CharSequence date;
        Content currentContent = null;
        
        while(event != XmlPullParser.END_DOCUMENT) {
            switch(event) {
            case XmlPullParser.START_TAG:
                id = parser.getAttributeValue(null, "id");
                status = parser.getAttributeValue(null, "status");
                rule = parser.getAttributeValue(null, "rule");
                date = parser.getAttributeValue(null, "date");
                title = parser.nextText();
                
                if(id == null) {
                    throw new IOException("content/@idが定義されていません。");
                }
                if(status == null) {
                    throw new IOException("content/@statusが定義されていません。");
                }
                
                if(rule == null) {
                    throw new IOException("content/@ruleが定義されていません。");
                }
                
                currentContent = new Content();
                currentContent.setId(Integer.parseInt(id.toString()));
                currentContent.setStatus(Integer.parseInt(status.toString()));
                currentContent.setTitle(title);
                currentContent.setDate(date);
                currentContent.setRuleName(rule);
                break;
            case XmlPullParser.END_TAG:
                if(!parser.getName().toLowerCase().equals("content")) {
                    break;
                }
                
                handler.onLoadItem(currentContent);
                return;
            }
            
            event = parser.next();
        }
    }
    
}
