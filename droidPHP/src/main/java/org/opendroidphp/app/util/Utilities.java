package org.opendroidphp.app.util;

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
}
