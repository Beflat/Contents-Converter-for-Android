package net.urban_theory.contents_convert;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class StackRequestService extends Service {

    
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
    }
    
    
    public void onDestroy() {
        Toast.makeText(this, "Sending request...", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
