package net.urban_theory.contents_convert.Parser;

abstract public class LoadHandler<T> {
    
    public LoadHandler() {
    }
    
    
    abstract public void onLoadItem(T target);
}
