package com.example.gestioneventos;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "eventos_channel";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String EXTRA_EVENTO_NOMBRE = "nombre_evento";
    private static final String EXTRA_EVENTO_FECHA = "fecha_evento";
    private static final String EXTRA_TIPO_NOTIFICACION = "tipo_notificacion";
    private static final int TIPO_INMEDIATA = 1;
    private static final int TIPO_RECORDATORIO = 2;

    private Button btnAgregarEvento;
    private ListView listViewEventos;
    private List<Evento> listaEventos;
    private ArrayAdapter<Evento> adapter;
    private Calendar fechaSeleccionada;

    private static final int NOTIFICATION_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        btnAgregarEvento = findViewById(R.id.btnAgregarEvento);
        listViewEventos = findViewById(R.id.listViewEventos);

        // Verificar que los componentes se encuentran
        if (btnAgregarEvento == null) {
            Toast.makeText(this, "Error: No se encontró el botón", Toast.LENGTH_LONG).show();
            return;
        }

        // Inicializar lista
        listaEventos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaEventos);
        listViewEventos.setAdapter(adapter);

        // Crear canal de notificación
        crearCanalNotificacion();

        // Solicitar permiso para notificaciones
        solicitarPermisoNotificaciones();

        // Fecha actual por defecto
        fechaSeleccionada = Calendar.getInstance();

        // Botón para agregar evento
        btnAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarEvento();
            }
        });

        // Click en evento para mostrar Toast personalizado
        listViewEventos.setOnItemClickListener((parent, view, position, id) -> {
            Evento evento = listaEventos.get(position);
            mostrarToastPersonalizado(evento);
        });
    }

    // AlertDialog para crear evento
    private void mostrarDialogoAgregarEvento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Evento");

        // Inflar el layout del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_nuevo_evento, null);

        final EditText etNombre = dialogView.findViewById(R.id.etNombreEvento);
        Button btnFecha = dialogView.findViewById(R.id.btnSeleccionarFecha);
        Button btnHora = dialogView.findViewById(R.id.btnSeleccionarHora);

        // Configurar botones de fecha y hora
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePickerDialog();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePickerDialog();
            }
        });

        builder.setView(dialogView);

        // Configurar botones del diálogo
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombre.getText().toString().trim();
                if (nombre.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Por favor, ingresa un nombre para el evento",
                            Toast.LENGTH_SHORT).show();
                } else {
                    agregarEvento(nombre);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // DatePickerDialog para fecha
    private void mostrarDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechaSeleccionada.set(year, month, dayOfMonth);
                        Toast.makeText(MainActivity.this,
                                "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                fechaSeleccionada.get(Calendar.YEAR),
                fechaSeleccionada.get(Calendar.MONTH),
                fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // TimePickerDialog para hora
    private void mostrarTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        fechaSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        fechaSeleccionada.set(Calendar.MINUTE, minute);

                        String horaFormateada = String.format("%02d:%02d", hourOfDay, minute);
                        Toast.makeText(MainActivity.this,
                                "Hora seleccionada: " + horaFormateada,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                fechaSeleccionada.get(Calendar.HOUR_OF_DAY),
                fechaSeleccionada.get(Calendar.MINUTE),
                true // formato 24 horas
        );
        timePickerDialog.show();
    }

    // Agregar evento a la lista
    private void agregarEvento(String nombre) {
        // Crear copia de la fecha seleccionada
        Calendar fechaHoraEvento = (Calendar) fechaSeleccionada.clone();

        Evento nuevoEvento = new Evento(nombre, fechaHoraEvento);
        listaEventos.add(nuevoEvento);
        adapter.notifyDataSetChanged();

        // 1. Notificación inmediata (la que ya tienes)
        mostrarNotificacion(nuevoEvento);

        // 2. Notificación para 5 segundos después (nueva)
        programarNotificacionRetrasada(nuevoEvento, 5);

        // 3. Notificación para la fecha/hora exacta del evento (nueva)
        programarNotificacionEnFechaEvento(nuevoEvento);

        Toast.makeText(this, "Evento '" + nombre + "' creado", Toast.LENGTH_SHORT).show();
    }


    // Método para notificación en la fecha del evento
    private void programarNotificacionEnFechaEvento(Evento evento) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Pasar datos del evento
        intent.putExtra(EXTRA_EVENTO_NOMBRE, evento.getNombre());
        intent.putExtra(EXTRA_EVENTO_FECHA, evento.getFechaFormateada());
        intent.putExtra(EXTRA_TIPO_NOTIFICACION, TIPO_RECORDATORIO); // Tipo 2 = fecha evento

        // ID único basado en el hash del evento
        int requestCode = evento.hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Obtener el tiempo en milisegundos de la fecha del evento
        long triggerTime = evento.getFechaHora().getTimeInMillis();

        // Verificar que no sea una fecha pasada
        if (triggerTime <= System.currentTimeMillis()) {
            Toast.makeText(this,
                    "La fecha del evento ya pasó, no se programará recordatorio",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Programar alarma
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }

        // Mostrar confirmación
        String fechaFormateada = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(new java.util.Date(triggerTime));

        Toast.makeText(this,
                "Recordatorio programado para:\n" + fechaFormateada,
                Toast.LENGTH_LONG).show();

        Log.d("Alarma", "Notificación de evento programada para: " + fechaFormateada);
    }

    private void programarNotificacionRetrasada(Evento evento, int segundos) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Pasar datos del evento
        intent.putExtra(EXTRA_EVENTO_NOMBRE, evento.getNombre());
        intent.putExtra(EXTRA_EVENTO_FECHA, evento.getFechaFormateada());
        intent.putExtra(EXTRA_TIPO_NOTIFICACION, TIPO_INMEDIATA); // Tipo 1 = 5 segundos

        // ID único basado en timestamp
        int requestCode = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Programar para X segundos después
        long triggerTime = System.currentTimeMillis() + (segundos * 1000);

        // Usar el método apropiado según la versión de Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }

        Log.d("Alarma", "Notificación de 5 segundos programada para: " +
                new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(triggerTime)));
    }
    private void cancelarNotificacionesProgramadas() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
    }

    // Toast personalizado con icono
    private void mostrarToastPersonalizado(Evento evento) {
        try {
            // Inflar el layout del toast personalizado
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_personalizado, null);

            // Configurar el contenido
            ImageView icon = layout.findViewById(R.id.toast_icon);
            TextView tvNombre = layout.findViewById(R.id.toast_nombre);
            TextView tvFecha = layout.findViewById(R.id.toast_fecha);

            // Establecer los datos del evento
            tvNombre.setText(evento.getNombre());
            tvFecha.setText(evento.getFechaFormateada());

            // Crear y mostrar el Toast
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        } catch (Exception e) {
            // Si hay error, mostrar toast normal
            Toast.makeText(this,
                    evento.getNombre() + "\n" + evento.getFechaFormateada(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // Notificación al crear evento
    private void mostrarNotificacion(Evento evento) {
        try {
            // Intent para abrir la app al pulsar la notificación
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Construir notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Nuevo Evento: " + evento.getNombre())
                    .setContentText("Fecha: " + evento.getFechaFormateada())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Mostrar notificación
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());

        } catch (Exception e) {
            // Ignorar si falla la notificación
        }
    }

    // Crear canal de notificación (Android 8+)
    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Eventos Agenda",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificaciones de eventos");

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Solicitar permiso para notificaciones (Android 13+)
    private void solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE
                );
            }
        }
    }
}