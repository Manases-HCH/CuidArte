package com.example.cuidarte.notificaciones;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cuidarte.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token generado: " + token);
        // Aquí puedes enviarlo a tu backend
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String titulo = "CuidArte";
        String cuerpo = "Tienes una nueva notificación";

        if (message.getNotification() != null) {
            titulo = message.getNotification().getTitle();
            cuerpo = message.getNotification().getBody();
        }

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
        mostrarNotificacion(titulo, cuerpo);
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void mostrarNotificacion(String titulo, String cuerpo) {

        // 🔒 Verificar permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("FCM", "Permiso POST_NOTIFICATIONS no otorgado. No se puede mostrar la notificación.");
                return;
            }
        }

        String channelId = "CUIDARTE_CH";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Notificaciones CuidArte",
                    NotificationManager.IMPORTANCE_HIGH
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

}
