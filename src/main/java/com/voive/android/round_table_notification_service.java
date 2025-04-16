package com.voive.android;

import static com.voive.android.App.BAN_OR_KICKED;
import static com.voive.android.App.INVITATION_SENT;
import static com.voive.android.App.POEPLE_NOTIFY_WHEN_THEY_START_TO_TALK;
import static com.voive.android.App.TABLE_ACTIVITY;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.parse.GetCallback;
import com.parse.ManifestInfo;
import com.parse.PLog;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;


/**
 * A {@link BroadcastReceiver} for rendering and reacting to to Notifications.
 * <p/>
 * This {@link BroadcastReceiver} must be registered in order to use the {@link ParsePush}
 * subscription methods. As a security precaution, the intent filters for this
 * {@link BroadcastReceiver} must not be exported. Add the following lines to your
 * {@code AndroidManifest.xml} file, inside the &lt;application&gt; element to properly register the
 * {@code ParsePushBroadcastReceiver}:
 * <p/>
 * <pre>
 * &lt;receiver android:name="com.parse.ParsePushBroadcastReceiver" android:exported=false&gt;
 *  &lt;intent-filter&gt;
 *     &lt;action android:name="com.parse.push.intent.RECEIVE" /&gt;
 *     &lt;action android:name="com.parse.push.intent.OPEN" /&gt;
 *     &lt;action android:name="com.parse.push.intent.DELETE" /&gt;
 *   &lt;/intent-filter&gt;
 * &lt;/receiver&gt;
 * </pre>
 * <p/>
 * The {@code ParsePushBroadcastReceiver} is designed to provide maximal configurability with
 * minimal effort. To customize the push icon, add the following line as a child of your
 * &lt;application&gt; element:
 * <p/>
 * <pre>
 *   &lt;meta-data android:name=&quot;com.parse.push.notification_icon&quot;
 *              android:resource=&quot;@drawable/icon&quot;/&gt;
 * </pre>
 * <p/>
 * where {@code drawable/icon} may be the path to any drawable resource. The
 * <a href="http://developer.android.com/design/style/iconography.html#notification">Android style
 * guide</a> for Notifications suggests that push icons should be flat monochromatic images.
 * <p/>
 * To achieve further customization, {@code ParsePushBroadcastReceiver} can be subclassed. When
 * providing your own implementation of {@code ParsePushBroadcastReceiver}, be sure to change
 * {@code com.parse.PushBroadcastReceiver} to the name of your custom subclass in your
 * AndroidManifest.xml. You can intercept and override the behavior of entire portions of the
 * push lifecycle by overriding {@link #onPushReceive(Context, Intent)},
 * {@link #onPushOpen(Context, Intent)}, or {@link #onPushDismiss(Context, Intent)}.
 * To make minor changes to the appearance of a notification, override
 * {@link #getSmallIconId(Context, Intent)} or {@link #getLargeIcon(Context, Intent)}. To completely
 * change the Notification generated, override {@link #getNotification(Context, Intent)}. To
 * change the NotificationChannel generated, override {@link #getNotificationChannel(Context, Intent)}. To
 * change how the NotificationChannel is created, override {@link #createNotificationChannel(Context, NotificationChannel)}.
 * To change the Activity launched when a user opens a Notification, override
 * {@link #getActivity(Context, Intent)}.
 */
// Hack note: Javadoc smashes the last two paragraphs together without the <p> tags.
@SuppressWarnings("unused")

public class round_table_notification_service extends BroadcastReceiver {
    /**
     * The name of the Intent extra which contains a channel used to route this notification.
     * May be {@code null}.
     */
    public static final String KEY_PUSH_CHANNEL = "com.parse.Channel";

    /**
     * The name of the Intent extra which contains the JSON payload of the Notification.
     */
    public static final String KEY_PUSH_DATA = "com.parse.Data";
    /**
     * The name of the Intent fired when a push has been received.
     */
    public static final String ACTION_PUSH_RECEIVE = "com.parse.push.intent.RECEIVE";
    /**
     * The name of the Intent fired when a notification has been opened.
     */
    public static final String ACTION_PUSH_OPEN = "com.parse.push.intent.OPEN";
    /**
     * The name of the Intent fired when a notification has been dismissed.
     */
    public static final String ACTION_PUSH_DELETE = "com.parse.push.intent.DELETE";
    /**
     * The name of the meta-data field used to override the icon used in Notifications.
     */
    public static final String PROPERTY_PUSH_ICON = "com.parse.push.notification_icon";
    protected static final int SMALL_NOTIFICATION_MAX_CHARACTER_LIMIT = 38;
    public static boolean is_open=false;
    private static final String TAG = "com.parse.ParsePushReceiver";

    public static NotificationManagerCompat notificationManagerCompat;


    public static NotificationCompat.Builder notificationBuilder;

    /**
     * Delegates the generic {@code onReceive} event to a notification lifecycle event.
     * Subclasses are advised to override the lifecycle events and not this method.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @see round_table_notification_service#onPushReceive(Context, Intent)
     * @see round_table_notification_service#onPushOpen(Context, Intent)
     * @see round_table_notification_service#onPushDismiss(Context, Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {


        String intentAction = intent.getAction();
        if (intentAction != null) {
            switch (intentAction) {
                case ACTION_PUSH_RECEIVE:
                    onPushReceive(context, intent);
                    break;
                case ACTION_PUSH_DELETE:
                    onPushDismiss(context, intent);
                    break;
                case ACTION_PUSH_OPEN:
                    onPushOpen(context, intent);
                    break;
            }
        }
    }

    /**
     * Called when the push notification is received. By default, a broadcast intent will be sent if
     * an "action" is present in the data and a notification will be show if "alert" and "title" are
     * present in the data.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     */
    protected void onPushReceive(Context context, Intent intent) {
        String pushDataStr = intent.getStringExtra(KEY_PUSH_DATA);
        if (pushDataStr == null) {
            PLog.e(TAG, "Can not get push data from intent.");
            return;
        }
        PLog.v(TAG, "Received push data: " + pushDataStr);

        JSONObject pushData = null;
        try {
            pushData = new JSONObject(pushDataStr);
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        // If the push data includes an action string, that broadcast intent is fired.
        String action = null;
        if (pushData != null) {
            action = pushData.optString("action", null);
        }
        if (action != null) {
            Bundle extras = intent.getExtras();
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtras(extras);
            broadcastIntent.setAction(action);
            broadcastIntent.setPackage(context.getPackageName());
            context.sendBroadcast(broadcastIntent);
        }

        final NotificationCompat.Builder notificationBuilder = getNotification(context, intent);

        Notification notification = null;
        if (notificationBuilder != null) {
            notification = notificationBuilder.build();
        }

        if (notification != null) {


            ParseNotificationManager.getInstance().showNotification(context, notification);
        }
    }

    /**
     * Called when the push notification is dismissed. By default, nothing is performed
     * on notification dismissal.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     */
    protected void onPushDismiss(Context context, Intent intent) {
        // do nothing
    }

    /**
     * Called when the push notification is opened by the user. Sends analytics info back to Parse
     * that the application was opened from this push notification. By default, this will navigate
     * to the {@link Activity} returned by {@link #getActivity(Context, Intent)}. If the push contains
     * a 'uri' parameter, an Intent is fired to view that URI with the Activity returned by
     * {@link #getActivity} in the back stack.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     */
    protected void onPushOpen(Context context, Intent intent) {
        // Send a Parse Analytics "push opened" event
        ParseAnalytics.trackAppOpenedInBackground(intent);

        String uriString = null;
        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
            uriString = pushData.optString("uri", null);
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        Class<? extends Activity> cls = getActivity(context, intent);
        Intent activityIntent;
        if (uriString != null) {
            activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        } else {
            activityIntent = new Intent(context, cls);
        }

        activityIntent.putExtras(intent.getExtras());
    /*
      In order to remove dependency on android-support-library-v4
      The reason why we differentiate between versions instead of just using context.startActivity
      for all devices is because in API 11 the recommended conventions for app navigation using
      the back key changed.
     */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //TaskStackBuilderHelper.startActivities(context, cls, activityIntent);
        } else {
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(activityIntent);
        }
    }

    /**
     * Used by {@link #onPushOpen} to determine which activity to launch or insert into the back
     * stack. The default implementation retrieves the launch activity class for the package.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The default {@code Activity} class of the package or {@code null} if no launch intent is
     * defined in {@code AndroidManifest.xml}.
     */
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent == null) {
            return null;
        }
        String className = launchIntent.getComponent().getClassName();
        Class<? extends Activity> cls = null;
        try {
            cls = (Class<? extends Activity>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            // do nothing
        }
        return cls;
    }

    /**
     * Retrieves the channel to be used in a {@link Notification} if API >= 26, if not null. The default returns a new channel
     * with id "parse_push", name "Push notifications" and default importance.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The notification channel
     */
    @TargetApi(Build.VERSION_CODES.O)
    protected NotificationChannel getNotificationChannel(Context context, Intent intent) {
        return new NotificationChannel("parse_push", "Push notifications", NotificationManager.IMPORTANCE_DEFAULT);
    }

    /**
     * Creates the notification channel with the NotificationManager. Channel is not recreated
     * if the channel properties are unchanged.
     *
     * @param context             The {@code Context} in which the receiver is running.
     * @param notificationChannel The {@code NotificationChannel} to be created.
     */
    @TargetApi(Build.VERSION_CODES.O)
    protected void createNotificationChannel(Context context, NotificationChannel notificationChannel) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(notificationChannel);
    }

    /**
     * Retrieves the small icon to be used in a {@link Notification}. The default implementation uses
     * the icon specified by {@code com.parse.push.notification_icon} {@code meta-data} in your
     * {@code AndroidManifest.xml} with a fallback to the launcher icon for this package. To conform
     * to Android style guides, it is highly recommended that developers specify an explicit push
     * icon.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The resource id of the default small icon for the package
     * @see <a href="http://developer.android.com/design/style/iconography.html#notification">Android Notification Style Guide</a>
     */
    protected int getSmallIconId(Context context, Intent intent) {
        Bundle metaData = ManifestInfo.getApplicationMetadata(context);
        int explicitId = 0;
        if (metaData != null) {
            explicitId = metaData.getInt(PROPERTY_PUSH_ICON);
        }
        return explicitId != 0 ? explicitId : ManifestInfo.getIconId();
    }

    /**
     * Retrieves the large icon to be used in a {@link Notification}. This {@code Bitmap} should be
     * used to provide special context for a particular {@link Notification}, such as the avatar of
     * user who generated the {@link Notification}. The default implementation returns {@code null},
     * causing the {@link Notification} to display only the small icon.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return Bitmap of the default large icon for the package
     * @see <a href="http://developer.android.com/guide/topics/ui/notifiers/notifications.html#NotificationUI">Android Notification UI Overview</a>
     */
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        return null;
    }

    /**
     * Get the push data as a parsed JSONObject
     *
     * @param intent the intent of the notification
     * @return the parsed JSONObject, or null
     */
    protected JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
            return null;
        }
    }

    /**
     * Get the content intent, which is the intent called when a notification is tapped. Note that if
     * you override this, you will want to set the action to {@link round_table_notification_service#ACTION_PUSH_OPEN} in order
     * to still trigger {@link #onPushOpen(Context, Intent)}
     *
     * @param extras      the extras
     * @param packageName the app package name
     * @return the intent
     */
    protected Intent getContentIntent(Bundle extras, String packageName) {
        Intent contentIntent = new Intent(round_table_notification_service.ACTION_PUSH_OPEN);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);
        return contentIntent;
    }

    /**
     * Get the delete intent, which is the intent called when a notification is deleted (swiped away). Note that if
     * you override this, you will want to set the action to {@link round_table_notification_service#ACTION_PUSH_DELETE} in order
     * to still trigger {@link #onPushOpen(Context, Intent)}
     *
     * @param extras      the extras
     * @param packageName the app package name
     * @return the intent
     */
    protected Intent getDeleteIntent(Bundle extras, String packageName) {
        Intent contentIntent = new Intent(round_table_notification_service.ACTION_PUSH_DELETE);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);
        return contentIntent;
    }

    /**
     * Creates a {@link Notification} with reasonable defaults. If "alert" and "title" are
     * both missing from data, then returns {@code null}. If the text in the notification is longer
     * than 38 characters long, the style of the notification will be set to
     * {@link android.app.Notification.BigTextStyle}.
     * <p/>
     * As a security precaution, developers overriding this method should be sure to set the package
     * on notification {@code Intent}s to avoid leaking information to other apps.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The notification builder to be displayed.
     * @see round_table_notification_service#onPushReceive(Context, Intent)
     */
    @Nullable
    protected NotificationCompat.Builder getNotification(Context context, Intent intent) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        JSONObject pushData = getPushData(intent);
        if (pushData == null || (!pushData.has("alert") && !pushData.has("title"))) {
            return null;
        }
        String title = pushData.optString("title", ManifestInfo.getDisplayName(context));
        String table_id = pushData.optString("Id", "NO");
        String alert = pushData.optString("alert", "Notification received.");
        String type = pushData.optString("Type", "Just_Fallback");
        String USER_ID = pushData.optString("USER_ID", "Nothinf_Found");
        String pic_url = pushData.optString("URL", "Just_Fallback");
        String tickerText = String.format(Locale.getDefault(), "%s: %s", title, alert);

        Bundle extras = intent.getExtras();

        Random random = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode = random.nextInt();

        // Security consideration: To protect the app from tampering, we require that intent filters
        // not be exported. To protect the app from information leaks, we restrict the packages which
        // may intercept the push intents.
        String packageName = context.getPackageName();

        String channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = getNotificationChannel(context, intent);
            createNotificationChannel(context, notificationChannel);
            channelId = notificationChannel.getId();
        }


        SharedPreferences sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
        String lng = sharedPreferences.getString("lng", "en");
        Context language_context = LocaleHelper.setLocale(context, lng);
        Resources resourcesX = language_context.getResources();


        notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ParseUser.getCurrentUser().getBoolean("NOTIFICATION_UPDATES")) {

            if (type.equals(Constant.USER_SPEAKING_ACTIVITY)) {
                if(!table_id.equals(App.WHICH_TABLE)){
                    ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
                    parseUserParseQuery.getInBackground(USER_ID, new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            Glide.with(context.getApplicationContext()).asBitmap().circleCrop().load(object.getParseFile("speaker_profile_pic").getUrl())
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                            Intent notificationIntent = new Intent(context, sound_preview.class);
                                            notificationIntent.putExtra("ID",table_id);
                                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            NotificationCompat.Builder notificationBuilder = null;

                                            notificationBuilder = new NotificationCompat.Builder(context, POEPLE_NOTIFY_WHEN_THEY_START_TO_TALK);
                                            notificationBuilder.setSmallIcon(R.drawable.app_main_v_logo);
                                            notificationBuilder.setContentIntent(contentIntent);
                                            notificationBuilder.setContentTitle(title);
                                            notificationBuilder.setContentText(alert);
                                            notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
                                            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText(alert));
                                            //    notificationBuilder.setLargeIcon(other_speaker_account.bbtt);
                                            notificationBuilder.setAutoCancel(true);
                                            notificationBuilder.setLargeIcon(resource);
                                            notificationBuilder.setOnlyAlertOnce(true);
                                            // notificationBuilder.setContentIntent(contentIntent);

                                            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                                            notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
                                            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                            notificationBuilder.build();

                                            notificationManagerCompat.notify(1, notificationBuilder.build());
                                        }
                                    });
                        }
                    });
                }
            }
            else if(type.equals(Constant.BAN_OR_KICKED)){
                ParseQuery<ParseObject> parseObjectParseQuer=new ParseQuery<ParseObject>("live_tables");
                parseObjectParseQuer.getInBackground(table_id, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        Glide.with(context.getApplicationContext()).asBitmap().circleCrop().load(object.getParseFile("table_image").getUrl())
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                        Intent notificationIntent = new Intent(context, MainActivity.class);
                                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                        NotificationCompat.Builder notificationBuilder = null;

                                        notificationBuilder = new NotificationCompat.Builder(context, BAN_OR_KICKED);
                                        notificationBuilder.setSmallIcon(R.drawable.app_main_v_logo);
                                        notificationBuilder.setContentIntent(contentIntent);
                                        notificationBuilder.setContentTitle(title);
                                        notificationBuilder.setContentText(alert);
                                        notificationBuilder.setColor(context.getResources().getColor(R.color.red));
                                        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(alert));
                                        //    notificationBuilder.setLargeIcon(other_speaker_account.bbtt);
                                        notificationBuilder.setAutoCancel(true);
                                        notificationBuilder.setLargeIcon(resource);
                                        notificationBuilder.setOnlyAlertOnce(true);
                                        // notificationBuilder.setContentIntent(contentIntent);

                                        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                                        notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
                                        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                        notificationBuilder.build();

                                        notificationManagerCompat.notify(2, notificationBuilder.build());
                                    }
                                });


                    }
                });
            }
            else if(type.equals(Constant.TABLE_NOTIFICATION) || type.equals(Constant.TABLE_OTHER_NOTIFICATION)){
                if(!App.WHICH_TABLE.equals(table_id)){
                    ParseQuery<ParseObject> parseObjectParseQuer=new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuer.getInBackground(table_id, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            Glide.with(context.getApplicationContext()).asBitmap().circleCrop().load(object.getParseFile("table_image").getUrl())
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                            Intent notificationIntent = new Intent(context, special_speaker_live_table.class);
                                            notificationIntent.putExtra("ID",table_id);
                                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            NotificationCompat.Builder notificationBuilder = null;

                                            notificationBuilder = new NotificationCompat.Builder(context, TABLE_ACTIVITY);
                                            notificationBuilder.setSmallIcon(R.drawable.app_main_v_logo);
                                            notificationBuilder.setContentIntent(contentIntent);
                                            notificationBuilder.setContentTitle(title);
                                            notificationBuilder.setContentText(alert);
                                            notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
                                            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText(alert));
                                            //    notificationBuilder.setLargeIcon(other_speaker_account.bbtt);
                                            notificationBuilder.setAutoCancel(true);
                                            notificationBuilder.setLargeIcon(resource);
                                            notificationBuilder.setOnlyAlertOnce(true);
                                            // notificationBuilder.setContentIntent(contentIntent);

                                            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                                            notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
                                            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                            notificationBuilder.build();

                                            notificationManagerCompat.notify(3, notificationBuilder.build());
                                        }
                                    });


                        }
                    });
                }
            }
            else if(type.equals(Constant.SOME_ONE_SEND_INVITATION)){
                if(is_open){

                    Intent intent1=new Intent(context,invitation_of_table_activity.class);
                    intent1.putExtra("ID",table_id);
                    context.startActivity(intent1);

                }
                else {

                    ParseQuery<ParseObject> parseObjectParseQuer=new ParseQuery<ParseObject>("live_tables");
                    parseObjectParseQuer.getInBackground(table_id, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            Glide.with(context.getApplicationContext()).asBitmap().circleCrop().load(object.getParseFile("table_image").getUrl())
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                            Intent notificationIntent = new Intent(context, invitation_of_table_activity.class);
                                            notificationIntent.putExtra("ID",table_id);
                                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            NotificationCompat.Builder notificationBuilder = null;

                                            notificationBuilder = new NotificationCompat.Builder(context, INVITATION_SENT);
                                            notificationBuilder.setSmallIcon(R.drawable.app_main_v_logo);
                                            notificationBuilder.setContentIntent(contentIntent);
                                            notificationBuilder.setContentTitle(title);
                                            notificationBuilder.setContentText(alert);
                                            notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
                                            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText(alert));
                                            //    notificationBuilder.setLargeIcon(other_speaker_account.bbtt);
                                            notificationBuilder.setAutoCancel(true);
                                            notificationBuilder.setLargeIcon(resource);
                                            notificationBuilder.setOnlyAlertOnce(true);
                                            // notificationBuilder.setContentIntent(contentIntent);

                                            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                                            notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
                                            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                            notificationBuilder.build();

                                            notificationManagerCompat.notify(4, notificationBuilder.build());
                                        }
                                    });


                        }
                    });

                }
            }
            else if(type.equals(Constant.SOME_ONE_FOLLOW)){



                ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
                parseUserParseQuery.getInBackground(USER_ID, new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        Glide.with(context.getApplicationContext()).asBitmap().circleCrop().load(object.getParseFile("speaker_profile_pic").getUrl())
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                        Intent notificationIntent = new Intent(context, other_speaker_account.class);
                                        notificationIntent.putExtra("ID",USER_ID);
                                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                        NotificationCompat.Builder notificationBuilder = null;

                                        notificationBuilder = new NotificationCompat.Builder(context, INVITATION_SENT);
                                        notificationBuilder.setSmallIcon(R.drawable.app_main_v_logo);
                                        notificationBuilder.setContentIntent(contentIntent);
                                        notificationBuilder.setContentTitle(title);
                                        notificationBuilder.setContentText(alert);
                                        notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
                                        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(alert));
                                        //    notificationBuilder.setLargeIcon(other_speaker_account.bbtt);
                                        notificationBuilder.setAutoCancel(true);
                                        notificationBuilder.setLargeIcon(resource);
                                        notificationBuilder.setOnlyAlertOnce(true);
                                        // notificationBuilder.setContentIntent(contentIntent);

                                        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                                        notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
                                        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                        notificationBuilder.build();

                                        notificationManagerCompat.notify(5, notificationBuilder.build());
                                    }
                                });
                    }
                });

            }
        }

        return notificationBuilder;
    }


}