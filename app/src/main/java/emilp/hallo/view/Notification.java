package emilp.hallo.view;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import emilp.hallo.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jonas on 2017-04-16.
 */

public class Notification {

    public Notification() {

    }

    public void show(Context context) {
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
 //       contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
 //       contentView.setTextViewText(R.id.title, "Custom notification");
//        contentView.setTextViewText(R.id.text, "This is a custom layout");

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_naked)
                .setContent(contentView);

        android.app.Notification notification = mBuilder.build();
        notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= android.app.Notification.DEFAULT_SOUND;
        notification.defaults |= android.app.Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }
}
