package com.example.gestiondeeventos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Evento implements Serializable {
    private String nombre;
    private Calendar fechaHora;

    public Evento(String nombre, Calendar fechaHora) {
        this.nombre = nombre;
        this.fechaHora = fechaHora;
    }

    public String getNombre() {
        return nombre;
    }

    public Calendar getFechaHora() {
        return fechaHora;
    }

    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(fechaHora.getTime());
    }

    @Override
    public String toString() {
        return nombre + " - " + getFechaFormateada();
    }
}
