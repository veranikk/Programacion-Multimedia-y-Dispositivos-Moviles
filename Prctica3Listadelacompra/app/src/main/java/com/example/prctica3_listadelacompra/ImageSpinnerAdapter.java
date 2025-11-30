package com.example.prctica3_listadelacompra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSpinnerAdapter extends BaseAdapter {
    private Context context;
    private int[] images;
    private String[] imageNames;

    public ImageSpinnerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        this.imageNames = context.getResources().getStringArray(R.array.spinner_categories);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.spinner_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.spinnerImage);
            holder.textView = view.findViewById(R.id.spinnerText);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(images[position]);
        holder.textView.setText(imageNames[position]);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.dropdownImage);
            holder.textView = view.findViewById(R.id.dropdownText);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(images[position]);
        holder.textView.setText(imageNames[position]);

        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
