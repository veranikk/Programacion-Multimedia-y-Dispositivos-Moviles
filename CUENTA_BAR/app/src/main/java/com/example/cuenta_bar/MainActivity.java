package com.example.cuenta_bar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText total;
    CheckBox propina;
    SeekBar seekBar;
    TextView porcentaje;
    TextView resultado;
    TextView resultadoMas;
    RadioGroup radio_group;
    RadioButton efectivo;
    RadioButton tarjeta;
    RatingBar rating;
    Button calcular;
    AutoCompleteTextView camarero;
    double total_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.linearlayout);

        total= findViewById(R.id.totalCuenta);
        propina= findViewById(R.id.propina);
        seekBar= findViewById(R.id.seek_propina);
        porcentaje= findViewById(R.id.porcentaje_propina);
        resultado= findViewById(R.id.resultado);
        resultadoMas= findViewById(R.id.resultadoMas);
        radio_group= findViewById(R.id.radio_group);
        efectivo=findViewById(R.id.efectivo);
        tarjeta=findViewById(R.id.tarjeta);
        rating= findViewById(R.id.rating);
        calcular=findViewById(R.id.calcular_total);
        camarero=findViewById(R.id.camarero);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentaje.setText("Propina: "+ progress+ "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        String[] camareros= {"Ricardo","David", "Iván","Vera", "Daniel"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, camareros);
        camarero.setAdapter(adapter);
        camarero.setThreshold(3);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularTotal();
            }
        });

    }

    private void calcularTotal(){

        String total1 = total.getText().toString();
        if (total1.isEmpty()){
            resultado.setTextColor(Color.RED);
            resultado.setText("Falta introducir el valor de la cuenta");
        }


        try {
            total_double= Double.parseDouble(total1);
            if (total_double<=0){
                resultado.setTextColor(Color.RED);
                resultado.setText("El valor de la cuenta debe ser positivo");
            }
        } catch (NumberFormatException e) {
            resultado.setTextColor(Color.RED);
            resultado.setText("Formato numérico incorrecto");
        }

        double propina1 =0;
        if (propina.isChecked()){
            propina1 = total_double * seekBar.getProgress()/ 100.0;
        }

        total_double+=propina1;

        int metodo_de_pago = radio_group.getCheckedRadioButtonId();

        String metodoPago="";
        if (metodo_de_pago== efectivo.getId()){
            metodoPago= "Efectivo";
        }else if (metodo_de_pago== tarjeta.getId()){
            metodoPago= "Tarjeta";
        }

        int califiacion =(int) rating.getRating();

        String camarero_atenido= camarero.getText().toString();

        resultado.setTextColor(Color.BLACK);
        resultado.setText("Total: "+ total_double);

        resultadoMas.setTextColor(Color.BLACK);
        resultadoMas.setText("Método de pago: " + metodoPago+ "\nCalificación: "+ califiacion + "\nCamarero: " +camarero_atenido);

    }
}

























