package net.urban_theory.contents_convert.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GeneralPreference {

    private String prefName = "general"; 
    private SharedPreferences pref;
    private Editor preferenceEditor;
    
    public GeneralPreference(Context context) {
        pref = context.getSharedPreferences(prefName, Activity.MODE_WORLD_READABLE | Activity.MODE_WORLD_WRITEABLE);
        preferenceEditor = null;
    }
    
    public boolean isExists() {
        return pref.contains("auto_send_request");
    }
    
    public boolean getAutoSendRequest() {
        return pref.getBoolean("auto_send_request", false);
    }
    
    public void setAutoSendRequest(boolean newFlag) {
        Editor editor = getEditor();
        editor.putBoolean("auto_send_request", newFlag);
    }
    
    
    public boolean commit() {
        if(preferenceEditor == null) {
            return true;
        }
        return preferenceEditor.commit();
    }
    
    public void initialize() {
        setAutoSendRequest(false);
        commit();
    }
    
    private Editor getEditor() {
        if(preferenceEditor == null) {
            preferenceEditor = pref.edit();
        }
        return preferenceEditor;
    }
}
