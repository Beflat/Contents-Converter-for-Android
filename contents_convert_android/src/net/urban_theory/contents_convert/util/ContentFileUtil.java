package net.urban_theory.contents_convert.util;

public class ContentFileUtil {
    
    public static String getContentFileNameFromId(int id) {
        return String.format("cc_%06d.epub", id);
    }
    
}