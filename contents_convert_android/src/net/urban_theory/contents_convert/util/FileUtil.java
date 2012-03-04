package net.urban_theory.contents_convert.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileUtil {

    public static BufferedReader getBufferedReaderFromFilePath(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        return br;
    }
    
    
    public static BufferedReader getBufferedReaderFromString(String buffer) throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.getBytes()), "UTF-8"));
    }
    
    
    public static BufferedWriter getBufferedWriterFromFilePath(String path) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        return bw;
    }
    
    public static BufferedWriter getBufferedWriterFromString(ByteArrayOutputStream bos) throws UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(bos, "UTF-8"));
    }
    
}
