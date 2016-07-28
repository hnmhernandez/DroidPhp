package org.opendroidphp.app.util;

import android.app.Activity;
import android.content.Context;
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
        //Devuelve en un array de enteros, en la posición 0, el ancho en píxeles de la pantalla del teléfono, y en la posición
        //1 devuelve el alto.
        Integer res[] = new Integer[2];
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        res[0] = displaymetrics.widthPixels;
        res[1] = displaymetrics.heightPixels;
        return res;
    }
}
