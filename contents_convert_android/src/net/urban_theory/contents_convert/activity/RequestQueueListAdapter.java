package net.urban_theory.contents_convert.activity;

import java.util.List;

import net.urban_theory.contents_convert.R;
import net.urban_theory.contents_convert.entity.RequestQueueItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RequestQueueListAdapter extends ArrayAdapter<RequestQueueItem> {

    LayoutInflater inflater;
    
    public RequestQueueListAdapter(Context context, List<RequestQueueItem> objects) {
        super(context, 0, objects);
        // TODO Auto-generated constructor stub
        
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View view = convertView;
        
        if(view == null) {
            view = inflater.inflate(R.layout.request_queue_row, null);
        }
        
        RequestQueueItem item = this.getItem(position);
        if(item != null) {
            
            TextView requestUrl = (TextView)view.findViewById(R.id.request_url);
            TextView requestStatus = (TextView)view.findViewById(R.id.request_status);
            
            requestUrl.setText(item.getUrl());
            String statusText = "";
            switch(item.getStatus()) {
            case RequestQueueItem.STATUS_UNSENT:
                statusText = view.getResources().getString(R.string.request_queue_item_name_unsent);
                break;
            case RequestQueueItem.STATUS_SENT:
                statusText = view.getResources().getString(R.string.request_queue_item_name_sent);
                break;
            }
            requestStatus.setText(statusText);
        }
        
        return view;
    }

}
