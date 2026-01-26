package com.example.gestiondeeventos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAgregarEvento;
    private ListView listViewEventos;
    private List<Evento> listaEventos;
    private EventoAdapter adapter;
    private NotificationHelper notificationHelper;
    private Calendar fechaSeleccionada;
    private int horaSeleccionada, minutoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicitar permiso para notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        // Inicializar componentes
        btnAgregarEvento = findViewById(R.id.btnAgregarEvento);
        listViewEventos = findViewById(R.id.listViewEventos);

        // Inicializar lista
        listaEventos = new ArrayList<>();
        adapter = new EventoAdapter(this, listaEventos);
        listViewEventos.setAdapter(adapter);

        // Inicializar helper de notificaciones
        notificationHelper = new NotificationHelper(this);

        // Fecha actual por defecto
        fechaSeleccionada = Calendar.getInstance();
        horaSeleccionada = fechaSeleccionada.get(Calendar.HOUR_OF_DAY);
        minutoSeleccionado = fechaSeleccionada.get(Calendar.MINUTE);

        // Botón para agregar evento
        btnAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarEvento();
            }
        });

        // Click en item de lista para mostrar Toast personalizado
        listViewEventos.setOnItemClickListener((parent, view, position, id) -> {
            mostrarToastPersonalizado(listaEventos.get(position));
        });
    }

    private void mostrarDialogoAgregarEvento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Evento");

        // Layout personalizado para el diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nuevo_evento, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreEvento);
        Button btnFecha = dialogView.findViewById(R.id.btnSeleccionarFecha);
        Button btnHora = dialogView.findViewById(R.id.btnSeleccionarHora);

        // Configurar botón de fecha
        btnFecha.setOnClickListener(v -> mostrarDatePickerDialog());

        // Configurar botón de hora
        btnHora.setOnClickListener(v -> mostrarTimePickerDialog());

        builder.setView(dialogView);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombre.getText().toString().trim();
                if (!nombre.isEmpty()) {
                    agregarEvento(nombre);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int dayOfMonth) {
                        fechaSeleccionada.set(year, month, dayOfMonth);
                        Toast.makeText(MainActivity.this,
                                "Fecha seleccionada: " + dayOfMonth + "/" +
                                        (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                    }
                },
                fechaSeleccionada.get(Calendar.YEAR),
                fechaSeleccionada.get(Calendar.MONTH),
                fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void mostrarTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaSeleccionada = hourOfDay;
                        minutoSeleccionado = minute;
                        fechaSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        fechaSeleccionada.set(Calendar.MINUTE, minute);

                        String horaFormateada = String.format("%02d:%02d", hourOfDay, minute);
                        Toast.makeText(MainActivity.this,
                                "Hora seleccionada: " + horaFormateada,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                horaSeleccionada,
                minutoSeleccionado,
                true // 24 horas
        );
        timePickerDialog.show();
    }

    private void agregarEvento(String nombre) {
        // Crear evento con fecha y hora seleccionadas
        Calendar fechaHoraEvento = (Calendar) fechaSeleccionada.clone();
        fechaHoraEvento.set(Calendar.HOUR_OF_DAY, horaSeleccionada);
        fechaHoraEvento.set(Calendar.MINUTE, minutoSeleccionado);

        Evento nuevoEvento = new Evento(nombre, fechaHoraEvento);
        listaEventos.add(nuevoEvento);
        adapter.notifyDataSetChanged();

        // Mostrar notificación
        String mensajeNotificacion = "Evento creado: " + nombre +
                "\nFecha: " + nuevoEvento.getFechaFormateada();
        notificationHelper.sendNotification("Nuevo Evento", mensajeNotificacion);

        Toast.makeText(this, "Evento agregado", Toast.LENGTH_SHORT).show();
    }

    private void mostrarToastPersonalizado(Evento evento) {
        // Inflar layout personalizado
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,
                findViewById(R.id.toast_layout));

        // Configurar contenido
        android.widget.TextView tvMensaje = layout.findViewById(R.id.tvToastMessage);
        tvMensaje.setText(evento.getNombre() + "\n" + evento.getFechaFormateada());

        // Crear y mostrar Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}