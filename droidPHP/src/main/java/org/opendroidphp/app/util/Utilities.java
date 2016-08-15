package org.opendroidphp.app.util;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

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

    public static String convertBase64(String path) {
        /*Utilidad para convertir una imagen contenida en "path" en Base64*/
        InputStream inputStream;//You can get an inputStream using any IO API
        ByteArrayOutputStream output;
        String type;
        try {
            inputStream = new FileInputStream(path);
            byte[] buffer = new byte[8192];
            int bytesRead;
            output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();

            String extension = MimeTypeMap.getFileExtensionFromUrl(path);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                return "data:" + type + ";base64," + output.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFingerPrint(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
