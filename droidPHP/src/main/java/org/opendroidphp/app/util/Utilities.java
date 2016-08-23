package org.opendroidphp.app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

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

    public static String getFingerPrint(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String captureScreen(Context context) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = ((Activity)context).getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            return convertBase64(imageFile.getPath());

//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
        return null;
    }

    public static String convertBase64(String path) {
        /*Utilidad para convertir una imagen contenida en "path" en Base64*/
        InputStream inputStream;//You can get an inputStream using any IO API
        ByteArrayOutputStream output;
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
            return "data:image/jpeg;base64," + clean(output.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
