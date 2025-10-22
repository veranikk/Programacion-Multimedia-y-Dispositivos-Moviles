package com.example.tema4togglebutton;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView txtEstado;
    private ToggleButton toggleButton;
    private ImageButton imgBtn;

    private String inicial= "play";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.togglebutton1);

        txtEstado= findViewById(R.id.txtEstado);
        toggleButton= findViewById(R.id.miToggleButton);
        imgBtn= findViewById(R.id.miImageButton);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txtEstado.setText("Estado: Pulsado");
                }else {
                    txtEstado.setText("Estado: No Pulsado");
                }
            }
        });{
        }

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Has pulsado el ImageButton", Toast.LENGTH_SHORT).show();


                if (inicial.contains("play")){
                    imgBtn.setImageResource(R.drawable.pause);
                    inicial="pausa";
                } else{
                    imgBtn.setImageResource(R.drawable.play);
                    inicial="play";
                }

            }
        });

    }
}