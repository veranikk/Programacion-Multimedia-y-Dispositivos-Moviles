package com.example.tema4_textview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    protected void onStart(){
        super.onStart();

        TextView miTexto = (TextView) findViewById(R.id.texto);
        miTexto.setText("Nuevo texto a mostrar");

        //opción 1 para cambiar el color
        miTexto.setTextColor(Color.parseColor("#0000FF"));

        //opción 2 para cambiar el color
        miTexto.setTextColor(Color.RED);

        //cambio del Texto a Negrita
        miTexto.setTypeface(null, Typeface.ITALIC);

        //Cambio del tamaño del texto
        miTexto.setTextSize(24);

        //cambiar tipo de letra
        miTexto.setTypeface(Typeface.SANS_SERIF);

        //instanciamos y cargamos la animación
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.animacion);
        //aqui configuramos la animación
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(20);
        //por último se aplica la animación al texto
        miTexto.startAnimation(animation);
    }
}