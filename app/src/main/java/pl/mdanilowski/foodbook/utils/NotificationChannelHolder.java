package pl.mdanilowski.foodbook.utils;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import pl.mdanilowski.foodbook.R;

public class NotificationChannelHolder {

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String id = Constants.NOTIFICATION_CHANNEL;
        CharSequence name = context.getString(R.string.app_name);
        String description = "Foodbook notification";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(context.getColor(R.color.accent));
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }
}
