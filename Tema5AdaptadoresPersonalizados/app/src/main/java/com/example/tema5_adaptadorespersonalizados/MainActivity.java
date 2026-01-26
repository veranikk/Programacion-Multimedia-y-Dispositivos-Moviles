package com.example.tema5_adaptadorespersonalizados;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listview);

        Datos[] datos = new Datos[]{
                new Datos("Hola buenas", "Dime tu nombre"),
                new Datos("De donde eres", "Qué país"),
                new Datos("Dime tus apellidos", "Los dos a ser posible"),
                new Datos("Yo me llamo Vera", "Vivo en España")
        };

        ListView listado= findViewById(R.id.listView);
        Adaptador miAdaptador = new Adaptador(this, datos);
        listado.setAdapter(miAdaptador);

        View miCabecera= getLayoutInflater().inflate(R.layout.cabecera, null);
        listado.addHeaderView(miCabecera);

        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem= ((Datos)parent.getItemAtPosition(position)).getTexto1();
                Toast.makeText(MainActivity.this, "Elemento pulsado "+ selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

    }
}