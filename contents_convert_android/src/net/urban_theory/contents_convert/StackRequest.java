package net.urban_theory.contents_convert;

class StackRequest {
    private String url;
    private int status;
    
    public static final int STATUS_UNSENT = 0;
    public static final int STATUS_SENT   = 1;
    
    public StackRequest() {
        url = "";
        status = STATUS_UNSENT;
    }
    
    
    public void setUrl(String newUrl) {
        url = newUrl;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setStatus(int newStatus) {
        status = newStatus;
    }
    
    public int getStatus() {
        return status;
    }
}
