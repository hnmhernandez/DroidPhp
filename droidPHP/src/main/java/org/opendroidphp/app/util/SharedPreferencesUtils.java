package org.opendroidphp.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Harold Montenegro on 27/7/2016.
 */

/*Clase encargada para un mejor manejo del SharedPreferences y evitar tener que inicializarlo en cada
* clase que se deba usar*/

public class SharedPreferencesUtils {
    private static String password = "password";

    public static String getPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(password, "");
    }

    public static void setPassword(Context context, String newPassword) {
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPreference.edit();
        editor.putString(password, newPassword);
        editor.apply();
    }

    public static boolean removeAll(Context context) {
        try {
            SharedPreferences.Editor editor =
                    PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.clear();
            editor.apply();
            return true;
        } catch (Exception e) {
            Utilities.loge("SharedPreferences removeAll" + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
