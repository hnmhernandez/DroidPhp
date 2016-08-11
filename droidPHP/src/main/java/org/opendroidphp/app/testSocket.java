package org.opendroidphp.app;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

import org.java_websocket.handshake.ServerHandshake;
import org.opendroidphp.R;
import org.opendroidphp.app.util.DroidPhpActivity;
import org.opendroidphp.app.util.Utilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by Harold Montenegro on 10/08/16.
 */
public class testSocket extends DroidPhpActivity {

    private Socket socket;

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
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void connectWebSocket() {

        final String wsuri = "ws://192.168.1.171:9000";

        try {
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Utilities.log("Websocket--> " + "Status: Connected to " + wsuri);
                    mConnection.sendTextMessage("Device:" + Build.MANUFACTURER + " " + Build.MODEL + " " + Build.ID);
                }

                @Override
                public void onTextMessage(String payload) {
                    Utilities.log("Websocket--> " + "Got echo: " + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Utilities.log("Websocket--> " + "Connection lost.");
                }
            });
        } catch (WebSocketException e) {

            Utilities.log("Websocket--> " + e.toString());
        }
    }

    public void sendMessage() {
        mConnection.sendTextMessage(et.getText().toString());
    }


//
//    private void connectWebSocket() {
//        URI uri;
//        try {
//            uri = new URI("ws://" + SERVER_IP + ":" + SERVERPORT + "/echobot");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        mWebSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                Utilities.log("Websocket--> " + "Opened");
//                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
//            }
//
//            @Override
//            public void onMessage(String s) {
//                final String message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(textView.getText() + "\n" + message);
//                    }
//                });
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                Utilities.log("Websocket--> " + "Closed  " + s + "   " + b + "   " + i);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.i("Websocket", "Error " + e.getMessage());
//            }
//        };
//        mWebSocketClient.connect();
//    }
//
//    public void sendMessage() {
//        connectWebSocket();
//        mWebSocketClient.send(et.getText().toString());
//        et.setText("");
//    }
}
