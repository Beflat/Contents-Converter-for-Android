package net.urban_theory.contents_convert.test.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;
import android.util.Log;

public class RegExTest  extends TestCase {
    
    
//    public void testFindUrl() {
//        
//        //タイトル付きURLにマッチする。
//        String str = "some_title\n\n"
//                + "https://www.test.com/~aaa/?param=abc#anchor\n\n"
//                + "description\n";
//        
//        String regex = "(http(?:s{0,1}):(?:.*?))(?:\\s*)(?:.*?)$";
//        
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(str);
//        
//        Assert.assertTrue("タイトル付きURLにマッチする。", m.find());
//        Assert.assertEquals("https://www.test.com/~aaa/?param=abc#anchor", m.group(1));
//        
//        
//        //通常のURLにマッチする。
//        str = "https://www.test.com/~aaa/?param=abc&def=123#anchor";
//        m = p.matcher(str);
//        Assert.assertTrue("通常のURLにマッチする。", m.find());
//        Log.d("cc-android", "通常のURLにマッチする。: " + m.group(1));
//        Assert.assertEquals("https://www.test.com/~aaa/?param=abc&def=123#anchor", m.group(1));
//        
//      //2つ以上あった場合、先の方にマッチする。
//        str = "https://www.test.com/~aaa/?param=abc&def=123#anchor\n\nhttp://www.www.com/aaa?bbb=ccc123";
//        m = p.matcher(str);
//        Assert.assertTrue("2つ以上あった場合、先の方にマッチする。", m.find());
//        Log.d("cc-android", "2つ以上あった場合、先の方にマッチする。: " + m.group(1));
//        Assert.assertEquals("https://www.test.com/~aaa/?param=abc&def=123#anchor", m.group(1));
//        
//    }
    
}
