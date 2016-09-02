package org.opendroidphp.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Harold Montenegro on 02/09/16.
 */
public class SharedUtils {
    private static String email = "email";

    public static String getEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(email, "");
    }

    public static void setEmail(Context context, String emailNew) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(email, emailNew);
        editor.apply();
    }
}
