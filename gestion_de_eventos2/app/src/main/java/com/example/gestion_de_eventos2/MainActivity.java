package com.example.gestion_de_eventos2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView listaEventos;
    ArrayList<String> eventos;
    ArrayAdapter<String> adaptador;

    String nombreEvento = "";
    int año, mes, dia, hora, minuto;

    public static final String CHANNEL_ID = "canal_eventos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Permiso de notificaciones Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaEventos = findViewById(R.id.listaEventos);
        eventos = new ArrayList<>();

        // Cargar eventos guardados
        var guardados = getSharedPreferences("eventos", MODE_PRIVATE)
                .getStringSet("lista", null);

        if (guardados != null) {
            eventos.addAll(guardados);
        }

        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventos);
        listaEventos.setAdapter(adaptador);

        crearCanalNotificaciones();

        findViewById(R.id.btnAnadir).setOnClickListener(v -> mostrarDialogoNombre());

        listaEventos.setOnItemClickListener((adapterView, view, i, l) -> mostrarToastPersonalizado(eventos.get(i)));
    }

    private void mostrarDialogoNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo evento");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Siguiente", (dialog, which) -> {
            nombreEvento = input.getText().toString();
            mostrarDatePicker();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        año = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            año = year;
            mes = month;
            dia = dayOfMonth;
            mostrarTimePicker();
        }, año, mes, dia);

        dp.show();
    }

    private void mostrarTimePicker() {
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);

        TimePickerDialog tp = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            hora = hourOfDay;
            minuto = minute;
            guardarEvento();
        }, hora, minuto, true);

        tp.show();
    }

    private void guardarEvento() {
        String evento = nombreEvento + " - " + dia + "/" + (mes + 1) + "/" + año +
                " " + hora + ":" + String.format("%02d", minuto);

        eventos.add(evento);
        adaptador.notifyDataSetChanged();


        getSharedPreferences("eventos", MODE_PRIVATE)
                .edit()
                .putStringSet("lista", new HashSet<>(eventos))
                .apply();


        mostrarNotificacion(evento);


        new android.os.Handler().postDelayed(() -> {
            mostrarNotificacion("Recordatorio rápido: " + evento);
        }, 5000);
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal Eventos",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de eventos");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void mostrarNotificacion(String texto) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_event)
                .setContentTitle("Nuevo evento creado")
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void mostrarToastPersonalizado(String evento) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(40, 30, 40, 30);
        layout.setBackgroundResource(R.drawable.toast_background);

        layout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icono = new ImageView(this);
        icono.setImageResource(R.drawable.ic_event);
        icono.setPadding(0, 0, 25, 0);

        int iconSize = (int) (getResources().getDisplayMetrics().density * 28);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        icono.setLayoutParams(iconParams);

        TextView texto = new TextView(this);
        texto.setText(evento);
        texto.setTextColor(Color.WHITE);
        texto.setTextSize(10);
        texto.setTypeface(null, Typeface.BOLD);
        texto.setShadowLayer(3, 1, 1, Color.parseColor("#60000000"));

        layout.addView(icono);
        layout.addView(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.setView(layout);
        toast.show();
    }
}