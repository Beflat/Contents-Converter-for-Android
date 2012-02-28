package net.urban_theory.contents_convert.test.Parser.TestTool;

import java.util.ArrayList;

import net.urban_theory.contents_convert.Content;
import net.urban_theory.contents_convert.Parser.LoadHandler;

public class DummyContentListLoadHandler extends LoadHandler<Content>{

    private ArrayList<Content> contentList;
    
    public DummyContentListLoadHandler() {
        contentList = new ArrayList<Content>();
    }

    @Override
    public void onLoadItem(Content target) {
        // TODO Auto-generated method stub
        contentList.add(target);
    }
    
    
    public ArrayList<Content> getList() {
        return contentList;
    }
}
