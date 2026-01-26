package com.example.gestioneventos;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String nombreEvento = intent.getStringExtra("nombre_evento");
        String fechaEvento = intent.getStringExtra("fecha_evento");
        int tipo = intent.getIntExtra("tipo_notificacion", 0);

        if (tipo == 1) {
            // Notificación de 5 segundos
            mostrarNotificacionRetrasada(context, nombreEvento, fechaEvento,
                    "Recordatorio de prueba",
                    "Este es un recordatorio de prueba 5 segundos después de crear el evento");
        } else if (tipo == 2) {
            // Notificación en fecha del evento
            mostrarNotificacionRetrasada(context, nombreEvento, fechaEvento,
                    "¡Evento programado!",
                    "Es hora de tu evento programado");
        }
    }

    private void mostrarNotificacionRetrasada(Context context, String nombre,
                                              String fecha, String titulo, String texto) {
        // Intent para abrir la app
        Intent appIntent = new Intent(context, MainActivity.class);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Crear canal si no existe (con importancia alta)
        crearCanalNotificacionAlta(context);

        // Construir notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "eventos_alarm_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(nombre + " - " + texto + "\nFecha: " + fecha)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Mostrar notificación
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void crearCanalNotificacionAlta(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "eventos_alarm_channel",
                    "Recordatorios de Eventos",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de recordatorios programados");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 100, 200});

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
