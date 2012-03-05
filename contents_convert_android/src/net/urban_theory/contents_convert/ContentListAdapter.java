package net.urban_theory.contents_convert;

import java.util.List;

import net.urban_theory.contents_convert.entity.Content;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ContentListAdapter extends ArrayAdapter<Content> {
    
    private LayoutInflater inflater;
    private TextView titleLabel;
    private TextView ruleLabel;
    private Button downloadButton;
    
    
    public ContentListAdapter(Context context, List<Content> objects) {
        super(context, 0, objects);
        
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
    }
    
    
    public View getView(int position, View counterView, ViewGroup parent) {
        View view = counterView;
        
        if(view == null) {
            view = inflater.inflate(R.layout.item_row, null);
        }
        
        Content row = this.getItem(position);
        if(row != null) {
            String title = row.getTitle().toString();
            titleLabel = (TextView)view.findViewById(R.id.content_title);
            titleLabel.setText(row.getTitle());
            
            ruleLabel = (TextView)view.findViewById(R.id.rule_title);
            ruleLabel.setText(row.getRuleName());
            
            Button dlButton = (Button)view.findViewById(R.id.btn_download);
            
            switch(row.getStatus()) {
            case Content.STATE_INPROCESS:
            case Content.STATE_FAILED:
                dlButton.setEnabled(false);
                break;
                
            case Content.STATE_DOWNLOADED:
                titleLabel.setTextColor(Color.rgb(0x88, 0x88, 0x88));
                //dlButton.setBackgroundDrawable(R.id.);
                break;
                
            case Content.STATE_READ:
                //dlButton.setBackgroundColor(Color.rgb(0x66, 0x66, 0x66));
                titleLabel.setTextColor(Color.rgb(0xdd, 0xdd, 0xdd));
                break;
            }
            
        }
        
        return view;
    }
}
