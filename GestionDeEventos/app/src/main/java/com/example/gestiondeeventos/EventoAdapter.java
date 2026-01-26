package com.example.gestiondeeventos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class EventoAdapter extends ArrayAdapter<Evento> {
    private Context context;
    private List<Evento> eventos;

    public EventoAdapter(Context context, List<Evento> eventos) {
        super(context, R.layout.item_evento, eventos);
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_evento, null);

        TextView tvNombre = item.findViewById(R.id.tvNombre);
        TextView tvFecha = item.findViewById(R.id.tvFecha);
        ImageView ivIcono = item.findViewById(R.id.ivIcono);

        Evento evento = eventos.get(position);
        tvNombre.setText(evento.getNombre());
        tvFecha.setText(evento.getFechaFormateada());

        // Puedes cambiar el icono según el tipo de evento
        ivIcono.setImageResource(R.drawable.ic_event);

        return item;
    }
}
