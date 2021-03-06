package org.opendroidphp.app.util;

import android.content.Context;
import android.net.Uri;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.util.Map;

/**
 * Created by Harold Montenegro on 10/08/16.
 */
public class NetUtilities {


    private final Context context;
    private OnNetUtilsActions onNetUtilsActions;

    public interface OnNetUtilsActions {
        void onInitRequest(String url);

        void onFinishRequest(String url, Exception e, String response, int status);
    }

    public NetUtilities(Context context, OnNetUtilsActions onNetUtilsActions) {
        this.context = context;
        this.onNetUtilsActions = onNetUtilsActions;
    }

    public void postRequest(final String url, Json data) {
        onNetUtilsActions.onInitRequest(url);
        Builders.Any.B b = Ion.with(context).load("POST", url);
        getData(b, data)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        sendResult(url, e, result);
                    }
                });
    }

    private void sendResult(String url, Exception e, Response<String> response) {
        onNetUtilsActions.onFinishRequest(url, e, (response == null) ? "" : response.getResult(),
                (response == null) ? 0 : response.getHeaders().code());
    }

    private Builders.Any.B getData(Builders.Any.B builder, Json data) {
        if (data != null && data.isObject()) {
            for (Map.Entry<String, Json> uno : data.asJsonMap().entrySet()) {
                builder.setBodyParameter(uno.getKey(), uno.getValue().asString());
            }
        }
        return builder;
    }

    public void getRequest(String url, JsonObject data) {
        onNetUtilsActions.onInitRequest(url);
        url = getUrlWithData(url, data);
        Utilities.log("init get request to: " + url);
        final String finalUrl = url;
        Builders.Any.B b = Ion.with(context).load("GET", url);
        b.asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        sendResult(finalUrl, e, result);
                    }
                });
    }

    private String getUrlWithData(String url, JsonObject data) {

        if (data != null && data.isJsonObject()) {
            boolean first = true;
            for (Map.Entry<String, JsonElement> uno : data.entrySet()) {
                if (first) {
                    url += "?";
                    first = false;
                } else {
                    url += "&";
                }
                url += (Uri.encode(uno.getKey()) + "=" + Uri.encode(uno.getValue().getAsString()));
            }
        }
        return url;
    }

}
