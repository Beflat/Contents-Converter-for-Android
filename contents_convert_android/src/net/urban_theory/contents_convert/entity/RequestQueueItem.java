package net.urban_theory.contents_convert.entity;

public class RequestQueueItem {
    
    private String url;
    
    private int status;
    
    public static final int STATUS_UNSENT = 0;
    public static final int STATUS_SENT = 10;
    
    public RequestQueueItem() {
        url = "";
        status = 0;
    }
    
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String newUrl) {
        url = newUrl;
    }
    
    
    public int getStatus() {
        return status;
    }
    
    
    public void setStatus(int newStatus) {
        status = newStatus;
    }
    
}
