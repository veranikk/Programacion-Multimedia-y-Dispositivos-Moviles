package com.example.materialdesignapp_tarea;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> items;
    private OnItemRemovedListener listener;

    public ShoppingListAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    public void setOnItemRemovedListener(OnItemRemovedListener listener) {
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_shopping, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.itemImage);
        TextView name = convertView.findViewById(R.id.itemName);
        TextView quantity = convertView.findViewById(R.id.itemQuantity);
        Button deleteBtn = convertView.findViewById(R.id.btnDelete);

        final Item item = items.get(position);

        image.setImageResource(item.getImagenResId());
        name.setText(item.getNombre());
        quantity.setText("Cantidad: " + item.getCantidad());

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();

                // Notificar al fragment que se eliminó un item
                if (listener != null) {
                    listener.onItemRemoved();
                }
            }
        });

        return convertView;
    }
}
