package org.opendroidphp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opendroidphp.R;

public class testWebView extends Activity {

    WebView myBrowser;
    EditText Msg;
    Button btnSendMsg;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.loadUrl("http://stackoverflow.com/questions/34746626/uncaught-typeerror-window-htmlout-showhtml-is-not-a-function");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
            }

            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

    }

    public  class MyJavaScriptInterface{

        @JavascriptInterface
        public void showHTML(String html_data) {
            Log.e("HOLA", " ======>  HTML Data : "+  html_data);
        }
    }
}