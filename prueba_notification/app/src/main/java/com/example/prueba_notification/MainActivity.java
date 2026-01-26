package com.example.prueba_notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // Identificador del canal de notificación
    private static final String CHANNEL_ID = "canal_demo";
    // Nombre visible del canal
    private static final String CHANNEL_NAME = "Canal Demo";
    // Identificador de la notificación
    private static final int NOTIFICATION_ID = 1;
    // Código para solicitar el permiso de notificaciones
    private static final int REQ_POST_NOTIFICATIONS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cargamos el layout con el botón
        setContentView(R.layout.activity_main);
        // Creamos el canal de notificación (obligatorio desde Android 8)
        createNotificationChannel();
        // Referencia al botón
        Button btnNotify = findViewById(R.id.btnNotify);
        // Acción al pulsar el botón
        btnNotify.setOnClickListener(v -> {
            if (canPostNotifications()) {
                showNotification();
            } else {
                requestPostNotificationsPermission();
            }
        });
    }
    // Comprueba si tenemos permiso para mostrar notificaciones
    private boolean canPostNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    // Solicita el permiso de notificaciones al usuario
    private void requestPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQ_POST_NOTIFICATIONS
            );
        }
    }
// Crea el canal de notificación

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager nm =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);
        }
    }
    // Construye y envía la notificación
    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, flags);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Notificación de prueba")
                        .setContentText("Has pulsado el botón y Android muestra esta notificación.")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
        NotificationManager nm =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, builder.build());
    }
}