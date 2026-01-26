package com.example.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<ListItem> items;
    private int selected_position =-1;

    public CustomAdapter(Context context, List<ListItem> items){
        this.context= context;
        this.items= items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        ImageView imageView= convertView.findViewById(R.id.itemImage);
        TextView titleTextView= convertView.findViewById(R.id.itemTitle);
        TextView contentTextView= convertView.findViewById(R.id.itemContent);
        RadioButton radioButton= convertView.findViewById(R.id.itemRadioButton);
        ListItem item =



        return convertView;
    }
}
