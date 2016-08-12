package org.opendroidphp.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

import org.opendroidphp.R;
import org.opendroidphp.app.util.DroidPhpActivity;
import org.opendroidphp.app.util.Json;
import org.opendroidphp.app.util.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Harold Montenegro on 10/08/16.
 */
public class testSocket extends DroidPhpActivity {

    private static final int SERVERPORT = 9000;
    private static final String SERVER_IP = "192.168.1.171";
    private EditText et;
    private TextView textView;
    private final WebSocketConnection mConnection = new WebSocketConnection();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_socket);

        et = (EditText) findViewById(R.id.EditText01);
        textView = (TextView) findViewById(R.id.message);
        connectWebSocket();

        Button myButton = (Button) findViewById(R.id.myButton);
//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendMessage();
//            }
//        });
    }

    private void connectWebSocket() {

        final String wsuri = "ws://" + SERVER_IP + ":" + SERVERPORT;

        try {
            WebSocketOptions options = new WebSocketOptions();
            options.setMaxMessagePayloadSize(1048576); //max size of message
            options.setMaxFramePayloadSize(1048576); //max size of frame
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Utilities.log("Websocket--> " + "Status: Connected to " + wsuri);
                    mConnection.sendTextMessage(Build.MANUFACTURER + " " + Build.MODEL + " " + Build.ID);
                }

                @Override
                public void onTextMessage(String payload) {
                    Utilities.log("Websocket--> " + "Got echo: " + payload);
                    Json json= Json.read(payload);
                    if(json.at("type").asString().equals("capture")){
                        json.set("image", captureScreen());
                        sendMessage(json.toString());
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Utilities.log("Websocket--> " + "Connection lost.");
                }
            }, options);
        } catch (WebSocketException e) {

            Utilities.log("Websocket--> " + e.toString());
        }
    }

    public void sendMessage(String message) {
        Utilities.log("MENSAJE--> " + message);
        mConnection.sendTextMessage(message);
    }

    public String captureScreen(){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
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

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
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

//            String extension = MimeTypeMap.getFileExtensionFromUrl(path);
//            if (extension != null) {
//                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Utilities.log("BASE64 REAL--> " + clean(output.toString()));

                return "data:image/jpeg;base64," + clean(output.toString());
//            } else {
//                return null;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String clean(String url) {
        /*Quitar doble slash en las urls */
        return url.replaceAll("\n", "");
    }
}
