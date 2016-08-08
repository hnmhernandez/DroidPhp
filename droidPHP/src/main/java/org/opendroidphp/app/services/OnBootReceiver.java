package org.opendroidphp.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.opendroidphp.app.ui.FullscreenActivity;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences.getBoolean("Servidor Activo", false)) {
                Intent i = new Intent(context, FullscreenActivity.class);
                context.startService(i);
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            BackgroundIntentService.performAction(context, BackgroundIntentService.ACTION_PACKAGE_REMOVED);
        }
        */
        try {
            Intent appIntent = new Intent(context, FullscreenActivity.class);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(appIntent);
        } catch (Exception e) {

        }

        try {
            Intent myIntent = new Intent(context, ServerService.class);
            context.startService(myIntent);
        } catch (Exception e) {

        }

    }
}