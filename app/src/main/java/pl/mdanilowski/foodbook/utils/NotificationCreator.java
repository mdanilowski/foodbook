package pl.mdanilowski.foodbook.utils;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;

public class NotificationCreator {

    public static void createRecipeNotification(Context context, String ownerId, String rid, String title, String contentText, int notificationId) {
        NotificationCompat.Builder BUILDER = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL);
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.USER_ID, ownerId);
        intent.putExtra(RecipeDetailsActivity.RECIPE_ID, rid);
        BUILDER.setSmallIcon(R.mipmap.ic_launcher);
        BUILDER.setContentTitle(title);
        BUILDER.setContentText(contentText);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(RecipeDetailsActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            BUILDER.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.notify(notificationId, BUILDER.build());
    }
}
