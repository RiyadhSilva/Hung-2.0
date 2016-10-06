package com.example.riyad.hung;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by riyad on 03/10/2016.
 */
public class NotificationUtil {
    //Cria a PendingIntent para abrir a activity da intent
    private static PendingIntent getPendingIntent(Context context, Intent intent, int id){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //Esta linha mantem a activity pai na pilha de activities
        stackBuilder.addParentStack(intent.getComponent());
        //Configura a intent que vai abrir ao clicar na notificacao
        stackBuilder.addNextIntent(intent);
        //Cria a PendingIntent e atualiza caso exista um mesmo id
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public static void create (Context context, Intent intent, String contentTitle, String
            contentText, int id){
        //Cria a PendingIntent(contem a intent original)
        PendingIntent p = getPendingIntent(context, intent, id);
        //Cria a notificacao
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_SOUND); //Ativa a configuracao padrao
        b.setSmallIcon(R.drawable.ic_hung);
        b.setContentTitle(contentTitle);
        b.setContentText(contentText);
        b.setContentIntent(p); //Intent que sera chamada ao clicar na notificacao
        b.setAutoCancel(true); //Autocancela a notificacao ao clicar nela
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void cancel(Context context, int id){
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancel(id);
    }

    public static void cancelAll(Context context){
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancelAll();
    }
}
