package net.urban_theory.contents_convert.entity;

public class Content {

    private int id;
    private CharSequence title;
    private CharSequence ruleName;
    private int status;

    public static final int STATE_INPROCESS  =  0;
    public static final int STATE_FAILED     = 10;
    public static final int STATE_UNREAD     = 20;
    public static final int STATE_DOWNLOADED = 30;
    public static final int STATE_READ       = 40;
    
    public Content() {
        title = "";
        ruleName = "";
        status = 0;
    }

    public int getId() {
        return id;
    }
    
    public CharSequence getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public void setTitle(CharSequence newTitle) {
        title = newTitle;
    }
    
    
    public void setStatus(int newStatus) {
        status = newStatus;
    }
    
    public void setId(int newId) {
        id = newId;
    }
    
    public CharSequence getRuleName() {
        return ruleName;
    }
    
    public void setRuleName(CharSequence newName) {
        ruleName = newName;
    }
}
