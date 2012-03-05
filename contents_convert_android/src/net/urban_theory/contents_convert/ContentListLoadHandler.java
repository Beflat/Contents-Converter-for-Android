package net.urban_theory.contents_convert;

import java.util.ArrayList;

import net.urban_theory.contents_convert.Parser.LoadHandler;
import net.urban_theory.contents_convert.entity.Content;

public class ContentListLoadHandler extends LoadHandler<Content> {

    private ArrayList<Content> loadedList;
    
    public ContentListLoadHandler() {
        this.loadedList = new ArrayList<Content>();
    }
    
    
    public void onLoadItem(Content target) {
        //adapter.add(target);
        this.loadedList.add(target);
    }
    
    
    public ArrayList<Content> getResult() {
        return loadedList;
    }
}
