package com.example.prctica3_listadelacompra;

public class Item {

    private String nombre;
    private int cantidad;
    private int imagenResId;

    public Item(String nombre, int cantidad, int imagenResId){
        this.nombre= nombre;
        this.cantidad= cantidad;
        this.imagenResId=imagenResId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getImagenResId() {
        return imagenResId;
    }
}
