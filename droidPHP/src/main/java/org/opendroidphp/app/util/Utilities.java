package org.opendroidphp.app.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Harold Montenegro on 27/07/16.
 */
public class Utilities {
    private static final String LOGTAG = "droidphptag";

    public static void log(String message){
        Log.d(Utilities.LOGTAG, message);
    }

    public static void loge(String message){
        Log.e(Utilities.LOGTAG, message);
    }

    public static Integer[] getScreenDimensionsWH(Context context) {
        //Devuelve en un array de enteros, en la posición 0, el ancho en píxeles de la pantalla del
        //teléfono, y en la posició 1 devuelve el alto.
        Integer res[] = new Integer[2];
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        res[0] = displaymetrics.widthPixels;
        res[1] = displaymetrics.heightPixels;
        return res;
    }

    public static String getFingerPrint(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String clean(String string) {
        /*Quitar salto de linea en string */
        return string.replaceAll("\n", "");
    }

    public static String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String extractNumberString(String string) {
        String result = "";
        for (int i = 0;i<string.length();i++){
            try{
                result += Integer.parseInt(String.valueOf(string.charAt(i)));
            }catch (Exception ignored){
            }
        }
        return result;
    }
}
