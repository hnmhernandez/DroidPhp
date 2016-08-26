package org.opendroidphp.app;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opendroidphp.R;
import org.opendroidphp.app.util.DroidPhpActivity;
import org.opendroidphp.app.util.ImageUtils;
import org.opendroidphp.app.util.Json;
import org.opendroidphp.app.util.SocketUtils;
import org.opendroidphp.app.util.TimeHandler;


/**
 * Created by Harold Montenegro on 10/08/16.
 */
public class testSocket extends DroidPhpActivity {

    private TextView textView;
    private SocketUtils socketUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_socket);

        TextView et = (TextView) findViewById(R.id.Text01);
        et.setText(Build.MANUFACTURER + " " + Build.MODEL + " " + Build.ID);
        textView = (TextView) findViewById(R.id.message);

        WebSocket();

        Button myButton = (Button) findViewById(R.id.myButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socketUtils.connectWebSocket();
            }
        });
    }

    private void WebSocket() {
        socketUtils = new SocketUtils(this);
        socketUtils.setOnConnectionListener(new SocketUtils.OnConnectionListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onMessage(String payload) {
                final Json json = Json.read(payload);
                if (json.has("type")) {
                    if (json.at("type").asString().equals("capture")) {
                        textView.setText("Han solicitado una captura de pantalla desde el servidor");
                        TimeHandler timeHandler = new TimeHandler(500, new TimeHandler.OnTimeComplete() {
                            @Override
                            public void onFinishTime() {
                                json.set("image", ImageUtils.captureScreen(testSocket.this));
                                socketUtils.sendMessage(json.toString());
                            }
                        });
                        timeHandler.start();
                    }
                }
            }

            @Override
            public void onClosed(int code, String reason) {

            }
        });
    }


}
