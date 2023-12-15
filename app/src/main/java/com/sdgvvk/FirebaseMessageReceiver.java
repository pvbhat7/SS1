package com.sdgvvk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sdgvvk.v1.ProjectConstants;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.activity.NotificationActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FirebaseMessageReceiver
        extends FirebaseMessagingService {

    String name = "";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        /*if(user != null){
            ApiCallUtil.sendTokenToServer(new FcmTokenModal(loggedinCustomer.getProfileId(), token));
        }*/

    }

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Log.i("local_logs","Notification received");
            showNotification(remoteMessage);
        }
    }

    public void showNotification(RemoteMessage remoteMessage) {
        NotificationCompat.Builder builder = null;
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Uri imageUrl = remoteMessage.getNotification().getImageUrl();
        Map<String, String> dataMap = remoteMessage.getData();

        int noti_id = NotificationID.getID();
        Log.i("local_logs","adding noti_id "+noti_id);

        String notificationType = dataMap.get(ProjectConstants.NOTIFICATION_TYPE);



        Intent intent = getNotificationIntent(dataMap , noti_id , remoteMessage);

        if(!notificationType.isEmpty()){

            if(notificationType.equalsIgnoreCase(ProjectConstants.SIMPLE)){
                builder = getSimpleBuilder(noti_id , intent , title , message,String.valueOf(noti_id));
                getNotiMgr(noti_id).notify(noti_id, builder.build());
            }
            else if(notificationType.equalsIgnoreCase(ProjectConstants.EXPANDED)){
                new SendNotificationTaskUsingGlide(imageUrl,noti_id,title,intent,getSimpleBuilder(noti_id , intent , title , message,String.valueOf(noti_id)), ProjectConstants.EXPANDED).execute();
            }
            else if(notificationType.equalsIgnoreCase(ProjectConstants.COLLAPSED)){
                new SendNotificationTaskUsingGlide(imageUrl,noti_id,title,intent,getSimpleBuilder(noti_id , intent , title , message,String.valueOf(noti_id)), ProjectConstants.COLLAPSED).execute();
            }
        }

    }

    private Intent getNotificationIntent(Map<String, String> dataMap, int noti_id, RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, NotificationActivity.class);

        intent.putExtra(ProjectConstants.TARGET_CLASS, dataMap.get(ProjectConstants.TARGET_CLASS));
        intent.putExtra(ProjectConstants.NOTI_ID, noti_id);
        intent.putExtra(ProjectConstants.CLIENT_NAME, dataMap.get(ProjectConstants.CLIENT_NAME));
        intent.putExtra(ProjectConstants.TITLE, dataMap.get(ProjectConstants.TITLE));
        intent.putExtra(ProjectConstants.MESSAGE, dataMap.get(ProjectConstants.MESSAGE));
        intent.putExtra(ProjectConstants.IMAGE, dataMap.get(ProjectConstants.IMAGE));
        intent.putExtra(ProjectConstants.TOKEN, dataMap.get(ProjectConstants.TOKEN));
        intent.putExtra(ProjectConstants.NOTIFICATION_TYPE, dataMap.get(ProjectConstants.NOTIFICATION_TYPE));
        intent.putExtra(ProjectConstants.SINGLE_USER, dataMap.get(ProjectConstants.SINGLE_USER));
        intent.putExtra(ProjectConstants.CPID, dataMap.get(ProjectConstants.CPID));
        intent.putExtra(ProjectConstants.CATEGORY, dataMap.get(ProjectConstants.CATEGORY));
        intent.putExtra(ProjectConstants.LONG_TEXT, dataMap.get(ProjectConstants.LONG_TEXT));
        intent.putExtra(ProjectConstants.BODY, dataMap.get(ProjectConstants.BODY));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );

        return intent;


    }

    static class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(100);

        public static int getID() {
            return c.incrementAndGet();
        }
    }

    private NotificationCompat.Builder getSimpleBuilder(int noti_id, Intent intent, String title, String message,String channelId) {
        int color = ContextCompat.getColor(this, R.color.colorPrimary);
        //sentPI = PendingIntent.getBroadcast(activity.getApplicationContext(),0,new Intent("SMS_SENT").setAction(Long.toString(System.currentTimeMillis())), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, noti_id, intent.setAction(Long.toString(System.currentTimeMillis())), PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(),
                channelId)
                .setSmallIcon(R.drawable.appicon)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentTitle(HtmlCompat.fromHtml("<font color=\"" + color + "\">"+title + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setContentText(HtmlCompat.fromHtml("<b><i>"+message+"! </i></b>", HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setContentIntent(pendingIntent);

        return builder;
    }

    private NotificationManager getNotiMgr(int noti_id) {
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel( String.valueOf(noti_id), "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        return notificationManager;
    }
    private class SendNotificationTaskUsingGlide extends AsyncTask<String, Void, Bitmap> {

        Uri imageUrl;
        Bitmap bitmap = null;
        int noti_id;
        Intent intent ;
        String title;
        NotificationCompat.Builder builder;
        String notification_type;

        public SendNotificationTaskUsingGlide(Uri imageUrl_, int noti_id, String title , Intent intent, NotificationCompat.Builder builder,String notification_type) {
            super();
            this.notification_type = notification_type;
            this.imageUrl = imageUrl_;
            this.noti_id = noti_id;
            this.builder = builder;
            this.intent = intent;
            this.title = title;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {

                URL url = new URL(imageUrl.toString());
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream in = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {

                if(bitmap != null){
                    if(notification_type.equalsIgnoreCase(ProjectConstants.EXPANDED)){
                        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_picture_notification);
                        remoteViews.setTextViewText(R.id.title, title);
                        remoteViews.setImageViewResource(R.id.icon,R.drawable.progym_icon);
                        remoteViews.setImageViewBitmap(R.id.bigPicture , bitmap);

                        builder.setContent(remoteViews);
                        builder.setContentTitle(null);
                        builder.setContentText(null);
                    }
                    else if(notification_type.equalsIgnoreCase(ProjectConstants.COLLAPSED)){
                        builder.setLargeIcon(bitmap);
                        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
                    }

                    getNotiMgr(noti_id).notify(noti_id , builder.build());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


 /*



    public static Bitmap convertUrlToBitmap(String urlStr){
        Log.i("local_logs","convertUrlToBitmap called");
        Bitmap image = null;
        try {
            Log.i("local_logs","trying to convert url : "+urlStr);
            URL url = new URL(urlStr);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch(IOException e) {
            Log.i("local_logs","error :"+e);
            System.out.println(e);
        }
        Log.i("local_logs","convertUrlToBitmap ended");
        return image;
    }



   private class SendNotificationTaskUsingHttpUrl extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;

        public SendNotificationTaskUsingHttpUrl(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            message = params[0] + params[1];
            try {

                URL url = new URL(params[2]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                NotificationManager notificationManager = (NotificationManager) ctx
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                intent.putExtra("isFromBadge", false);


                Notification notification = new Notification.Builder(ctx)
                        .setContentTitle(
                                ctx.getResources().getString(R.string.app_name))
                        .setContentText(message)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(result).build();

                // hide the notification after its selected
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(1, notification);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    */