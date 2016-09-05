package org.opendroidphp.app.util;

import android.content.Context;
import android.os.Build;

import org.opendroidphp.app.ui.FullscreenActivity;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Created by Harold Montenegro on 23/08/16.
 */
public class SocketUtils {

    //Local
//    private static final int SERVERPORT = 9000;
//    private static final String SERVER_IP = "192.168.1.171";

    //Remoto
    private static final int SERVERPORT = 9999;
    private static final String SERVER_IP = "138.36.236.142";

    private final WebSocketConnection mConnection = new WebSocketConnection();
    private final Context context;
    private TimerCustom timerCustom;

    public OnConnectionListener onConnectionListener;

    public void setOnConnectionListener(OnConnectionListener onConnectionListener) {
        this.onConnectionListener = onConnectionListener;
    }

    public interface OnConnectionListener {
        void onOpen();
        void onMessage(String payload);
        void onClosed(int code, String reason);
    }

    public SocketUtils(Context context) {
        this.context = context;
        connectWebSocket();
        initTimerCustom();
    }

    private void initTimerCustom() {
        timerCustom = new TimerCustom(5000);
        timerCustom.setOnTimeRun(new TimerCustom.OnTimeRun() {
            @Override
            public void onTime() {
                if (!mConnection.isConnected()) {
                    connectWebSocket();
                }
            }
        });
    }

    public void connectWebSocket() {

        final String wsuri = "ws://" + SERVER_IP + ":" + SERVERPORT;

        try {
            WebSocketOptions options = new WebSocketOptions();
            options.setMaxMessagePayloadSize(1048576); //max size of message
            options.setMaxFramePayloadSize(1048576); //max size of frame
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Utilities.log("Websocket--> " + "Status: Connected to " + wsuri);
                    if (onConnectionListener != null) {
                        onConnectionListener.onOpen();
                    }
                    Json json = Json.object();
                    json.set("deviceNew", Build.MANUFACTURER + " " + Build.MODEL + " " + Build.ID);
                    json.set("mac", Utilities.getMacAddress(context));
                    json.set("fingerPrint", Utilities.getFingerPrint(context));
                    json.set("idClient", FullscreenActivity.idClient);
                    json.set("emailClient", SharedUtils.getEmail(context));
                    sendMessage(json.toString());
                }

                @Override
                public void onTextMessage(String payload) {
                    Utilities.log("Websocket--> " + "Got echo: " + payload);
                    if (onConnectionListener != null) {
                        onConnectionListener.onMessage(payload);
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Utilities.log("Websocket--> " + "Connection lost.");
                    if (onConnectionListener != null) {
                        onConnectionListener.onClosed(code, reason);
                    }
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

    public void stopTimer(){
        timerCustom.stop();
    }

}
