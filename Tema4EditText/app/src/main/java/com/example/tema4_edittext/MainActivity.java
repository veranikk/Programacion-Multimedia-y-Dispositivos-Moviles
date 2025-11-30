package com.example.tema4_edittext;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
        setContentView(R.layout.linear);

        String[] opciones ={"Opción 1","Opción 2","Opción 3","Opción 4","Opción 5"};

        AutoCompleteTextView textoLeido= findViewById(R.id.miTexto);

        ArrayAdapter<String> adaptador= new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, opciones);

        textoLeido.setAdapter(adaptador);

        radioGroup=findViewById(R.id.radioGroup);
        buttonCheck=findViewById(R.id.radioButton1);


        buttonCheck.setOnclickListener(new View.OnClickListener){
            public void onClick(View v){
                int selectedId=
            }
        }
    }
}