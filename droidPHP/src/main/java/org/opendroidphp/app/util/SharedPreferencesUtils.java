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
    private static String passwordUnlock = "passwordUnlock";
    private static String deviceUnlock = "deviceUnlock";
    private static String email = "email";

    public static String getPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(password, "");
    }

    public static void setPassword(Context context, String newPassword) {
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPreference.edit();
        editor.putString(password, newPassword);
        editor.apply();
    }

    public static String getPasswordUnlock(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(passwordUnlock, "");
    }

    public static void setPasswordUnlock(Context context, String newPassword) {
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPreference.edit();
        editor.putString(passwordUnlock, newPassword);
        editor.apply();
    }

    public static boolean getDeviceUnlock(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(deviceUnlock, false);
    }

    public static void setDeviceUnlock(Context context, boolean unlock) {
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPreference.edit();
        editor.putBoolean(deviceUnlock, unlock);
        editor.apply();
    }

    public static String getEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(email, "");
    }

    public static void setEmail(Context context, String emailNew) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(email, emailNew);
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
