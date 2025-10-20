package com.example.animacionconbotones;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

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

    }

    protected void onStart(){
        super.onStart();
        Button button = findViewById(R.id.boton1);
        TextView texto = findViewById(R.id.miTexto);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Animation animationT= AnimationUtils.loadAnimation(this,R.anim.translacion);
                animationT.setRepeatMode(Animation.RESTART);
                animationT.setRepeatCount(20);
                texto.startAnimation(animationT);
            }
        });
    }

    public void onClick(View view){
        TextView texto = findViewById(R.id.miTexto);

        int id = view.getId();
        if(id == R.id.boton2){
            Animation animationR= AnimationUtils.loadAnimation(this,R.anim.rotacion);
            animationR.setRepeatMode(Animation.RESTART);
            animationR.setRepeatCount(20);
            texto.startAnimation(animationR);
        }else if (id == R.id.boton3){
            detenerAnimacionActual();
        }
    }

    private void detenerAnimacionActual(){
        texto.
        if(animacionActual!=null){
            texto.clearAnimation();
            animacionActual.cancel();
            animacionActual = null;
        }
    }


}