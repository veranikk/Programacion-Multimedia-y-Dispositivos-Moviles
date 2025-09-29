package com.example.ciclodevidadeactividades;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Ejemplo","Estoy en on create");
    }

    protected void onStart(){
        super.onStart();
        Log.i("Ejemplo","Estoy en on Start");
    }
    protected void onRestart(){
        super.onRestart();
        Log.i("Ejemplo","Estoy en on Restart");
    }
    protected void onResume(){
        super.onResume();
        Log.i("Ejemplo","Estoy en on Resume");
    }
    protected void onPause(){
        super.onPause();
        Log.i("Ejemplo","Estoy en on Pause");
    }
    protected void onStop(){
        super.onStop();
        Log.i("Ejemplo","Estoy en on Stop");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i("Ejemplo","Estoy en on Destroy");
    }
}