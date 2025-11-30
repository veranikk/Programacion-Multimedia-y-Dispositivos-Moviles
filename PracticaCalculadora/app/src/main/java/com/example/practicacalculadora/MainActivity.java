package com.example.practicacalculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView texto1;
    private String input = ""; //Guarda la expresión que el usuario escribe

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculadora);

        //Variable 'texto1' referencia a TextView con id 'miTexto' del layout
        texto1 = findViewById(R.id.miTexto);

        //Asigna el listener al botón 0
        findViewById(R.id.btn0).setOnClickListener(this);
        //Asigna el listener al botón 1
        findViewById(R.id.btn1).setOnClickListener(this);
        //Asigna el listener al botón 2
        findViewById(R.id.btn2).setOnClickListener(this);
        //Asigna el listener al botón 3
        findViewById(R.id.btn3).setOnClickListener(this);
        //Asigna el listener al botón 4
        findViewById(R.id.btn4).setOnClickListener(this);
        //Asigna el listener al botón 5
        findViewById(R.id.btn5).setOnClickListener(this);
        //Asigna el listener al botón 6
        findViewById(R.id.btn6).setOnClickListener(this);
        //Asigna el listener al botón 7
        findViewById(R.id.btn7).setOnClickListener(this);
        //Asigna el listener al botón 8
        findViewById(R.id.btn8).setOnClickListener(this);
        //Asigna el listener al botón 9
        findViewById(R.id.btn9).setOnClickListener(this);

        //Asigna el listener al botón de suma +
        findViewById(R.id.btnSum).setOnClickListener(this);
        //Asigna el listener al botón de resta −
        findViewById(R.id.btnMenos).setOnClickListener(this);
        //Asigna el listener al botón de multiplicación ×
        findViewById(R.id.btnMulti).setOnClickListener(this);
        //Asigna el listener al botón de división /
        findViewById(R.id.btnDiv).setOnClickListener(this);

        //Asigna el listener al botón del punto decimal .
        findViewById(R.id.btnPunto).setOnClickListener(this);
        //Asigna el listener al botón de igual =
        findViewById(R.id.btnIgual).setOnClickListener(this);
        //Asigna el listener al botón para borrar todo CA
        findViewById(R.id.btnClearAll).setOnClickListener(this);
        //Asigna el listener al botón para borrar un carácter C
        findViewById(R.id.btnClear).setOnClickListener(this);
    }

    /**
     * Le damos funciones a los botones cuando son clickeados
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        Button boton = (Button) view; //Convertimos la vista en botón
        String textoBoton = boton.getText().toString(); //Obtenemos el texto del botón
        String actual = texto1.getText().toString();  //Obtenemos el texto actual de la pantalla

        //Hacemos un switch donde verificamos el texto del botón
        switch (textoBoton) {
            case "=":
                //Cuando se pulsa "=" calculamos la operacion
                try {
                    input = hacerOperacion(input); //Calcula el resultado
                    texto1.setText(input); //Lo muestra en la pantalla
                } catch (Exception e) {
                    texto1.setText("Error"); //Si hay error de sintaxis muestra Error
                    input = "";
                }
                break;

            case "C":
                //Borra el último carácter
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    texto1.setText(input);
                }else {
                    input = "0"; //Si queda vacío, mostramos 0
                    texto1.setText(input);
                }
                break;

            case "CA":
                //Borra toda la expresión, ponemos 0
                input = "0";
                texto1.setText(input);
                break;

            default:
                //Si es número, símbolo o punto lo agregamos a la expresión
                input = meterNumSim(actual, textoBoton);
                texto1.setText(input);
                break;
        }
    }

    /**
     * Función para agregar un carácter a la calculadora
     * @param expr
     * @param caracter
     * @return
     */
    private String meterNumSim(String expr, String caracter) {

        //Reiniciamos la calculadora si la expr es null o igual a 0
        if (expr == null || expr.equals("0")){
            expr = "";
        }

        /**
         * Si expr está vacío, le damos nada a char y si tiene algo le suprimimos lo último
         */
        char ultimo;
        if (expr.isEmpty()) {
            ultimo = ' ';
        } else {
            ultimo = expr.charAt(expr.length() - 1);
        }

        /**
         * Evitamos símbolos repetidos
         */
        if (esSimbolo(caracter.charAt(0))) {
            if (expr.isEmpty() || esSimbolo(ultimo)){
                return expr;
            }
        }

        /**
         *  Evitamos más de un punto en el mismo número
         *  y cuando empiezan con . ponemos 0.
         */
        if (caracter.equals(".")) {
            int i = expr.length() - 1;
            while (i >= 0 && !esSimbolo(expr.charAt(i))) {
                if (expr.charAt(i) == '.'){
                    return expr;
                }// Ya hay un punto
                i--;
            }
            if (expr.isEmpty() || esSimbolo(ultimo)){
                caracter = "0.";
            }
        }

        return expr + caracter; // Devolvemos la expresión actualizada
    }

    /**
     * Función que dice si un carácter es un símbolo
     * @param c
     * @return
     */
    private boolean esSimbolo(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * Hace la operación respetando la prioridad de multiplicación y división
     * metodo hecho con IA
     *
     * @param expr
     * @return
     * @throws Exception
     */
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