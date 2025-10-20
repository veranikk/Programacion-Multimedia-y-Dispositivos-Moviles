package com.example.practicacalculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView texto1;
    private String input = ""; // Guarda la expresión que el usuario escribe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculadora);

        texto1 = findViewById(R.id.miTexto);

        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);

        findViewById(R.id.btnSum).setOnClickListener(this);
        findViewById(R.id.btnMenos).setOnClickListener(this);
        findViewById(R.id.btnMulti).setOnClickListener(this);
        findViewById(R.id.btnDiv).setOnClickListener(this);

        findViewById(R.id.btnPunto).setOnClickListener(this);
        findViewById(R.id.btnIgual).setOnClickListener(this);
        findViewById(R.id.btnClearAll).setOnClickListener(this);
        findViewById(R.id.btnClear).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Button boton = (Button) view; // Convertimos la vista en botón
        String textoBoton = boton.getText().toString(); // Obtenemos el texto del botón
        String actual = texto1.getText().toString();  // Obtenemos el texto actual de la pantalla

        switch (textoBoton) {
            case "=":
                // Cuando se pulsa "=" evaluamos la expresión
                try {
                    input = hacerOperacion(input); // Calcula el resultado
                    texto1.setText(input); // Lo muestra en la pantalla
                } catch (Exception e) {
                    texto1.setText("Error"); // Si hay error de sintaxis
                    input = "";
                }
                break;

            case "C":
                // Borra el último carácter
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                }
                if (input.isEmpty()) input = "0"; // Si queda vacío, mostramos 0
                texto1.setText(input);
                break;

            case "CA":
                // Borra toda la expresión
                input = "0";
                texto1.setText(input);
                break;

            default:
                // Si es número, símbolo o punto lo agregamos a la expresión
                input = meterNumSim(input, textoBoton);
                texto1.setText(input);
                break;
        }
    }

    // Función para agregar un carácter a la calculadora
    private String meterNumSim(String expr, String caracter) {
        if (expr == null || expr.equals("0")) expr = ""; // Reiniciamos la calculadora

        char ultimo = expr.isEmpty() ? ' ' : expr.charAt(expr.length() - 1);

        // Evitar símbolos repetidos seguidos
        if (esSimbolo(caracter.charAt(0))) {
            if (expr.isEmpty() || esSimbolo(ultimo)) return expr;
        }

        // Evitar más de un punto en el mismo número
        if (caracter.equals(".")) {
            int i = expr.length() - 1;
            while (i >= 0 && !esSimbolo(expr.charAt(i))) {
                if (expr.charAt(i) == '.') return expr; // Ya hay un punto
                i--;
            }
            if (expr.isEmpty() || esSimbolo(ultimo)) caracter = "0."; // Si empieza con punto, ponemos "0."
        }

        return expr + caracter; // Devolvemos la expresión actualizada
    }

    // Función que dice si un carácter es un operador
    private boolean esSimbolo(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Función que dice si un string es un operador
    private boolean esSimbolo(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    // Evalúa la expresión respetando la prioridad de multiplicación y división
    private String hacerOperacion(String expr) throws Exception {
        expr = expr.replace(" ", ""); // Quitamos espacios

        if (expr.isEmpty()) return "0"; // Si está vacío, devolvemos 0
        if (expr.startsWith("+") || expr.startsWith("*") || expr.startsWith("/")) {
            expr = "0" + expr; // Si empieza con operador no válido, agregamos 0
        }

        // Guardamos números y operadores por separado
        java.util.List<Double> numeros = new java.util.ArrayList<>();
        java.util.List<Character> operadores = new java.util.ArrayList<>();

        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);

            // Extraemos el número
            StringBuilder numBuffer = new StringBuilder();
            while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                numBuffer.append(expr.charAt(i));
                i++;
            }
            if (numBuffer.length() == 0) throw new Exception("Error de sintaxis");
            numeros.add(Double.parseDouble(numBuffer.toString()));

            // Extraemos operador si existe
            if (i < expr.length()) {
                operadores.add(expr.charAt(i));
                i++;
            }
        }

        // Primero multiplicaciones y divisiones
        for (i = 0; i < operadores.size(); i++) {
            char op = operadores.get(i);
            if (op == '*' || op == '/') {
                double a = numeros.get(i);
                double b = numeros.get(i + 1);
                double res = (op == '*') ? a * b : a / b;
                numeros.set(i, res);
                numeros.remove(i + 1);
                operadores.remove(i);
                i--; // Ajustamos el índice
            }
        }

        // Luego sumas y restas
        double resultado = numeros.get(0);
        for (i = 0; i < operadores.size(); i++) {
            char op = operadores.get(i);
            double b = numeros.get(i + 1);
            if (op == '+') resultado += b;
            else resultado -= b;
        }

        // Si el resultado es entero, mostramos sin decimal
        if (resultado == (long) resultado) return String.valueOf((long) resultado);
        else return String.valueOf(resultado);
    }
}