package org.opendroidphp.app.ui;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opendroidphp.R;
import org.opendroidphp.app.Constants;
import org.opendroidphp.app.NumpadTool;
import org.opendroidphp.app.common.utils.FileUtils;
import org.opendroidphp.app.services.ServerService;
import org.opendroidphp.app.tasks.CommandTask;
import org.opendroidphp.app.tasks.DescargarArchivo;
import org.opendroidphp.app.util.AnimationUtilities;
import org.opendroidphp.app.util.DateUtils;
import org.opendroidphp.app.util.DroidPhpActivity;
import org.opendroidphp.app.util.ImageUtils;
import org.opendroidphp.app.util.Json;
import org.opendroidphp.app.util.NetUtilities;
import org.opendroidphp.app.util.SharedPreferencesUtils;
import org.opendroidphp.app.util.SocketUtils;
import org.opendroidphp.app.util.SystemUiHider;
import org.opendroidphp.app.util.TimeHandler;
import org.opendroidphp.app.util.TimerCustom;
import org.opendroidphp.app.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eu.chainfire.libsuperuser.Shell;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */

public class FullscreenActivity extends DroidPhpActivity {
    WebView myWebView;
    String proyectoActivo;
    private static final int REQUEST_FILE_PICKER = 1;
    private ValueCallback<Uri> mUploadMessage;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    private String android_id;
    public Button buttonContinuar;
    public static String idClient = "";
    private String postDataStr = "";
    public static String emailClient = "";
    private String fechaExpiracion = "";
    private boolean numPadLaunched = false;
    private View viewBlackScreen;
    private TimerCustom timerCustom;

    @Override
    protected void onStart() {
        super.onStart();
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //aca inicia la web
        myWebView = (WebView) this.findViewById(R.id.webView);

        WebSettings settings = myWebView.getSettings();
        //activamos que se pueda ver dentro de la apk no que abra el navegador
        myWebView.setWebViewClient(new WebViewClient() {
                                       @Override
                                       public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                           super.onReceivedError(view, errorCode, description, failingUrl);
                                           view.loadUrl("http://localhost:8080/404.php");
                                       }
                                   }
        );
        settings.setJavaScriptEnabled(true);
        //activamos caracteristica de chrome para que puedan subir imagen.
//        myWebView.addJavascriptInterface(new WebAppInterface(this, myWebView, FullscreenActivity.this), "Android");
        myWebView.setWebChromeClient(new WebChromeClient() {

        });


        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (queSeInstalo.equals("core")) {
            myWebView.setVisibility(View.VISIBLE);
            String postData = "fingerprint=" + android_id + "&login_password=myPassword";
            myWebView.postUrl("http://neosepel.ferozo.net/neoinnovaciones/RegistroClientes/view/registro.php", EncodingUtils.getBytes(postData, "utf-8"));
            myWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (url.equals("http://neosepel.ferozo.net/neoinnovaciones/RegistroClientes/view/successful.php")) {
                        DescargarArchivo descarga = new DescargarArchivo(getApplicationContext(), lista, null, FullscreenActivity.this, "core");
                        descarga.execute();
                    }
                }
            });
        } else {
            cambiarRootCrearUser();
        }
    }

    public void cambiarRootCrearUser() {
        final Thread hiloConsulta = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(8000);
                    //Conexion a archivo local
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpContext localContext = new BasicHttpContext();
                    HttpPost httpPost = new HttpPost("http://localhost:8080/clasesFuncionesWebService/android_webservice.php");
                    //Asignacion de Parametros en POST
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("accion", "CPR"));
                    params.add(new BasicNameValuePair("servidor", "localhost"));
                    params.add(new BasicNameValuePair("user", "root"));
                    params.add(new BasicNameValuePair("pwd", ""));
                    params.add(new BasicNameValuePair("database", "mysql"));
                    params.add(new BasicNameValuePair("databaseQuery1", "UPDATE mysql.user SET Password=PASSWORD('5Du1WaFGfkhS2qmYAmE3') WHERE User='root';"));
                    params.add(new BasicNameValuePair("databaseQuery2", "CREATE USER 'user'@'localhost' IDENTIFIED BY '" + android_id + "';"));
                    params.add(new BasicNameValuePair("databaseQuery3", "GRANT SELECT , INSERT , UPDATE , DELETE , CREATE , DROP , INDEX , ALTER , CREATE TEMPORARY TABLES , CREATE VIEW , EVENT, TRIGGER, SHOW VIEW , CREATE ROUTINE, ALTER ROUTINE, EXECUTE ON  `neosepel_ni_cv` . * TO  'user'@'localhost';"));
                    params.add(new BasicNameValuePair("databaseQuery4", "GRANT ALL ON *.* to 'root'@'%' IDENTIFIED BY '5Du1WaFGfkhS2qmYAmE3';"));
                    params.add(new BasicNameValuePair("databaseQuery5", "FLUSH PRIVILEGES;"));
                    //Ejecucion de consulta en Apache
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    HttpResponse response = httpClient.execute(httpPost, localContext);
                    //Manejo de resultado de consulta
                    String resultado;
                    final String resultadoFinal;
                    HttpEntity entity = response.getEntity();
                    resultado = EntityUtils.toString(entity, "UTF-8");
                    if (!(resultado.substring(0, 2).equals("OK") || resultado.substring(0, 2).equals("ER") || resultado.substring(0, 2).equals("No") || resultado.equals("Access denied for user 'root'@'localhost' (using password: NO)ERROR SQL 1"))) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(FullscreenActivity.this)
                                        .setTitle("Error en Instalacion")
                                        .setMessage("Ocurrio un error en la instalacion por favor desinstale y vuelva a instalar la aplicacion.")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                noti.cancel(143);
                                                finishAffinity();
                                            }
                                        })
                                        .show();
                            }
                        });
                        return;
                    }
                    resultadoFinal = resultado;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            consultarLicenciaWeb();
                        }
                    });

                } catch (IOException e) {
                    Log.e("MYAPP", "exception: " + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        hiloConsulta.start();

    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private void dialogoRetry() {
        new AlertDialog.Builder(FullscreenActivity.this)
                .setTitle("No Liceciado")
                .setMessage("No se encontro licencia valida activa para este dispositivo desea reintentar?")
                .setPositiveButton("Si, Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        consultarLicenciaWeb();
                    }
                })
                .setNegativeButton("No, Salir", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        noti.cancel(143);
                        finishAffinity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void consultarLicenciaWeb() {
        final ProgressDialog dialogoEspere = new ProgressDialog(this);
        final Activity actividad = this;
        dialogoEspere.setTitle("Descargando");
        dialogoEspere.setMessage("Espere por favor...");
        dialogoEspere.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                noti.cancel(143);
                finishAffinity();
            }
        });
        dialogoEspere.show();
        Thread hiloConsulta = new Thread(new Runnable() {
            public void run() {
                try {
                    //Conexion
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpContext localContext = new BasicHttpContext();
                    HttpPost httpPost = new HttpPost("http://localhost:8080/clasesFuncionesWebService/android_webservice.php");
                    //Asignacion de Parametros en POST
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("accion", "verificarDispositivoExisteLocal"));
                    params.add(new BasicNameValuePair("servidor", "localhost"));
                    params.add(new BasicNameValuePair("user", "root"));
                    params.add(new BasicNameValuePair("pwd", "5Du1WaFGfkhS2qmYAmE3"));
                    params.add(new BasicNameValuePair("database", "db_licenciamiento"));
                    params.add(new BasicNameValuePair("fingerprint", android_id));
                    //Ejecucion de consulta en Apache
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    HttpResponse response = httpClient.execute(httpPost, localContext);
                    //Manejo de resultado de consulta
                    String resultado;
                    HttpEntity entity = response.getEntity();
                    resultado = EntityUtils.toString(entity, "UTF-8");
                    final JSONObject jsonObjConsulta1 = new JSONObject(resultado);
                    if (resultado.equals("No pudo conectarse: No such file or directory")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(FullscreenActivity.this)
                                        .setTitle("Error en Instalacion")
                                        .setMessage("Ocurrio un error en la instalacion por favor desinstale y vuelva a instalar la aplicacion.")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                noti.cancel(143);
                                                finishAffinity();
                                            }
                                        })
                                        .show();
                            }
                        });
                        return;
                    }
                    JSONObject jsonObj = null;
                    Date dateDB = null;
                    Date ultmaConex = null;
                    Calendar cal = new GregorianCalendar();
                    final Date dateDISPO = cal.getTime();
                    boolean descargaActiva = false;
                    if (!jsonObjConsulta1.getString("resultado").equals("false")) {
                        String dateDatabase = jsonObjConsulta1.getString("fechaExpiracion");
                        String ultimaConexion = jsonObjConsulta1.getString("ultimaConexion");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        ultmaConex = simpleDateFormat.parse(ultimaConexion);
                        try {
                            if (!dateDatabase.equals("0000-00-00 00:00:00")) {
                                dateDB = simpleDateFormat.parse(dateDatabase);
                                ///comienza la descarga.
                                ArrayList<String[]> lista = new ArrayList<String[]>();
                                String[] arreglo = new String[2];
                                String loc = Constants.EXTERNAL_STORAGE.concat("/");
                                descargaActiva = false;
                                try {
                                    String rutaDescargar = jsonObjConsulta1.getString("tipoTerminal");
                                    rutaDescargar = rutaDescargar.replace("_", "/");
                                    File archivo = new File(loc + "htdocs/admin.php");
                                    if (!archivo.exists()) {
                                        descargaActiva = true;
                                        arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/" + rutaDescargar + "/htdocs.zip";
                                        arreglo[1] = "htdocs.zip";
                                        lista.add(arreglo);
                                        DescargarArchivo descarga = new DescargarArchivo(getApplicationContext(), lista, rutaDescargar, actividad, dialogoEspere, jsonObjConsulta1.getString("idCliente"));
                                        descarga.execute();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (ParseException ex) {
                            System.out.println("Exception " + ex);
                        }
                    }

                    if (jsonObjConsulta1.getString("resultado").equals("false")) {
                        //Conexion
                        httpClient = new DefaultHttpClient();
                        localContext = new BasicHttpContext();
                        httpPost = new HttpPost("http://neosepel.ferozo.net/neoinnovaciones/clasesFuncionesWebService/android_webservice.php");
                        //Asignacion de Parametros en POST
                        ArrayList<NameValuePair> params1 = new ArrayList<NameValuePair>();
                        params1.add(new BasicNameValuePair("accion", "verificarDispositivoExiste"));
                        params1.add(new BasicNameValuePair("servidor", "localhost"));
                        params1.add(new BasicNameValuePair("user", "neosepel_neo")); //neosepel_neo
                        params1.add(new BasicNameValuePair("pwd", "Atenas32")); //Atenas32
                        params1.add(new BasicNameValuePair("database", "neosepel_ni_licenciamiento"));
                        params1.add(new BasicNameValuePair("fingerprint", android_id));
                        //Ejecucion de consulta en Apache
                        httpPost.setEntity(new UrlEncodedFormEntity(params1, "UTF-8"));
                        response = httpClient.execute(httpPost, localContext);
                        //Manejo de resultado de consulta
                        String resultado1;
                        entity = response.getEntity();
                        resultado1 = EntityUtils.toString(entity, "UTF-8");
                        jsonObj = new JSONObject(resultado1);
                        if (jsonObj.getString("resultado").equals("false")) {
                            myWebView.setVisibility(View.VISIBLE);
                            String postData = "fingerprint=" + android_id + "&login_password=myPassword";
                            myWebView.postUrl("http://neosepel.ferozo.net/neoinnovaciones/RegistroClientes/view/registro.php", EncodingUtils.getBytes(postData, "utf-8"));
                            return;
                        }
                        ////////////////////////////////////////////////////////////////////////////////////
                        httpPost = new HttpPost("http://localhost:8080/clasesFuncionesWebService/android_webservice.php");
                        //Asignacion de Parametros en POST
                        ArrayList<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("accion", "InsertDatosClienteLocal"));
                        params2.add(new BasicNameValuePair("servidor", "localhost"));
                        params2.add(new BasicNameValuePair("user", "root"));
                        params2.add(new BasicNameValuePair("pwd", "5Du1WaFGfkhS2qmYAmE3"));
                        params2.add(new BasicNameValuePair("database", "db_licenciamiento"));
                        params2.add(new BasicNameValuePair("idCliente", jsonObj.getString("idCliente")));
                        emailClient = jsonObj.getString("email");
                        SharedPreferencesUtils.setEmail(FullscreenActivity.this, emailClient);
                        params2.add(new BasicNameValuePair("email", emailClient));
                        params2.add(new BasicNameValuePair("password", jsonObj.getString("password")));
                        params2.add(new BasicNameValuePair("nomEmpresa", jsonObj.getString("nombreEmpresa")));
                        params2.add(new BasicNameValuePair("fingerprint", jsonObj.getString("fingerprint")));
                        params2.add(new BasicNameValuePair("tipoTerminal", jsonObj.getString("tipoTerminal")));
                        params2.add(new BasicNameValuePair("nombreTerminal", jsonObj.getString("nombreTerminal")));
                        params2.add(new BasicNameValuePair("musicaFuncional", jsonObj.getString("musicaFuncional")));
                        params2.add(new BasicNameValuePair("fechaActivacion", jsonObj.getString("fechaActivacion")));
                        fechaExpiracion = jsonObj.getString("fechaExpiracion");
                        params2.add(new BasicNameValuePair("fechaExpiracion", fechaExpiracion));
                        params2.add(new BasicNameValuePair("activo", jsonObj.getString("activo")));

                        //Ejecucion de consulta en Apache
                        HttpContext localContext2 = new BasicHttpContext();
                        httpPost.setEntity(new UrlEncodedFormEntity(params2, "UTF-8"));
                        response = httpClient.execute(httpPost, localContext2);
                        //Manejo de resultado de consulta
                        String resultado2, res;
                        entity = response.getEntity();
                        resultado2 = EntityUtils.toString(entity, "UTF-8");
                        res = resultado2;
                    } else if (jsonObjConsulta1.getString("fechaExpiracion").equals("0000-00-00 00:00:00") || dateDB.compareTo(dateDISPO) < 0 || dateDISPO.compareTo(ultmaConex) < 0) {
                        if (jsonObjConsulta1.getString("activo").equals("0")) {
                            if (dateDISPO.compareTo(ultmaConex) < 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(FullscreenActivity.this)
                                                .setTitle("Error en Fecha y Hora")
                                                .setMessage("Por favor, corrija la fecha y hora de su dispositivo.")
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                        noti.cancel(143);
                                                        finishAffinity();
                                                    }
                                                })
                                                .show();
                                    }
                                });
                            }
                        }
                        if (isOnline()) {
                            //Conexion
                            httpClient = new DefaultHttpClient();
                            localContext = new BasicHttpContext();
                            httpPost = new HttpPost("http://neosepel.ferozo.net/neoinnovaciones/clasesFuncionesWebService/android_webservice.php");
                            //Asignacion de Parametros en POST
                            ArrayList<NameValuePair> params1 = new ArrayList<NameValuePair>();
                            params1.add(new BasicNameValuePair("accion", "verificarDispositivoExiste"));
                            params1.add(new BasicNameValuePair("servidor", "localhost"));
                            params1.add(new BasicNameValuePair("user", "neosepel_neo")); //neosepel_neo
                            params1.add(new BasicNameValuePair("pwd", "Atenas32")); //Atenas32
                            params1.add(new BasicNameValuePair("database", "neosepel_ni_licenciamiento"));
                            params1.add(new BasicNameValuePair("fingerprint", android_id));
                            //Ejecucion de consulta en Apache
                            httpPost.setEntity(new UrlEncodedFormEntity(params1, "UTF-8"));
                            response = httpClient.execute(httpPost, localContext);
                            //Manejo de resultado de consulta
                            String resultado1;
                            entity = response.getEntity();
                            resultado1 = EntityUtils.toString(entity, "UTF-8");
                            jsonObj = new JSONObject(resultado1);

                            ////////////////////////////////////////////////////////////////////////////////////
                            httpPost = new HttpPost("http://localhost:8080/clasesFuncionesWebService/android_webservice.php");
                            //Asignacion de Parametros en POST
                            ArrayList<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("accion", "UpdateDatosClienteLocal"));
                            params2.add(new BasicNameValuePair("servidor", "localhost"));
                            params2.add(new BasicNameValuePair("user", "root"));
                            params2.add(new BasicNameValuePair("pwd", "5Du1WaFGfkhS2qmYAmE3"));
                            params2.add(new BasicNameValuePair("database", "db_licenciamiento"));

                            params2.add(new BasicNameValuePair("idCliente", jsonObj.getString("idCliente")));
                            emailClient = jsonObj.getString("email");
                            SharedPreferencesUtils.setEmail(FullscreenActivity.this, emailClient);
                            params2.add(new BasicNameValuePair("email", emailClient));
                            params2.add(new BasicNameValuePair("password", jsonObj.getString("password")));
                            params2.add(new BasicNameValuePair("nomEmpresa", jsonObj.getString("nombreEmpresa")));
                            params2.add(new BasicNameValuePair("fingerprint", jsonObj.getString("fingerprint")));
                            params2.add(new BasicNameValuePair("tipoTerminal", jsonObj.getString("tipoTerminal")));
                            params2.add(new BasicNameValuePair("nombreTerminal", jsonObj.getString("nombreTerminal")));
                            params2.add(new BasicNameValuePair("musicaFuncional", jsonObj.getString("musicaFuncional")));
                            params2.add(new BasicNameValuePair("fechaActivacion", jsonObj.getString("fechaActivacion")));
                            fechaExpiracion = jsonObj.getString("fechaExpiracion");
                            params2.add(new BasicNameValuePair("fechaExpiracion", fechaExpiracion));
                            params2.add(new BasicNameValuePair("activo", jsonObj.getString("activo")));

                            //Ejecucion de consulta en Apache
                            HttpContext localContext2 = new BasicHttpContext();
                            httpPost.setEntity(new UrlEncodedFormEntity(params2, "UTF-8"));
                            response = httpClient.execute(httpPost, localContext2);
                            //Manejo de resultado de consulta
                            String resultado2;
                            entity = response.getEntity();
                            resultado2 = EntityUtils.toString(entity, "UTF-8");
                            String res = resultado2;
                        } else {
                            descargaActiva = false;
                        }
                    }
                    if (!isOnline()) {
                        descargaActiva = false;
                    }
                    final String TipoTerminal, activo;
                    if (!jsonObjConsulta1.getString("resultado").equals("false")) {
                        TipoTerminal = jsonObjConsulta1.getString("tipoTerminal");
                        idClient = jsonObjConsulta1.getString("idCliente");
                        activo = jsonObjConsulta1.getString("activo");
                    } else {
                        TipoTerminal = "";
                        idClient = "";
                        activo = "";
                    }
                    final Date dateDBFinal = dateDB;
                    final boolean[] descargaActivaF = {descargaActiva};
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ((dateDBFinal != null && dateDBFinal.compareTo(dateDISPO) > 0) || activo.equals("1")) {
                                if (!descargaActivaF[0]) {
                                    dialogoEspere.dismiss();
                                    proyectoActivo = TipoTerminal;
                                    String[] parts = proyectoActivo.split("_");
                                    proyectoActivo = parts[0];
                                    if (proyectoActivo.equals("ST")) {
                                        proyectoActivo = proyectoActivo + parts[1];
                                    }
                                    if (!(proyectoActivo.equals("PV") || proyectoActivo.equals("PC") || proyectoActivo.equals("CH") || proyectoActivo.equals("STTAB"))) {
                                        delayedHide(0);
                                    }
                                    myWebView.setVisibility(View.VISIBLE);
                                    myWebView.setWebChromeClient(new WebChromeClient());
                                    postDataStr = "idCliente=" + idClient + "&key=mC5GPxx2JSXmVXy9jm5j&ipServer=" + getIpAddress();
                                    myWebView.postUrl("http://localhost:8080/index.php", EncodingUtils.getBytes(postDataStr, "utf-8"));
                                    myWebView.getSettings().setJavaScriptEnabled(true);
                                    final LoadListener lListener = new LoadListener();
                                    myWebView.addJavascriptInterface(lListener, "HTMLOUT");
                                    lListener.setWebView(myWebView);
                                    lListener.setCliente(idClient);

                                    myWebView.setWebViewClient(new WebViewClient() {
                                        public void onPageFinished(WebView view, String url) {
                                            view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                                        }
                                    });

                                    //TODO//////////////////////////////////////////////////////////////////////////////////
                                    //Verificar si la app vencio o no
                                    try {
                                        if (!SharedPreferencesUtils.getDeviceUnlock(FullscreenActivity.this)) {
                                            if (appExpired() && !numPadLaunched) {
                                                numPadLaunched = true;
                                                Intent intent = new Intent(FullscreenActivity.this, NumpadTool.class);
                                                intent.putExtra(NumpadTool.SCREEN_FOR_EXPIRED, true);
                                                startActivity(intent);
                                            }
                                        }
                                    } catch (Exception ignored) {

                                    }

                                    //Activar el WebSocket
                                    WebSocket();
                                    //TODO////////////////////////////////////////////////////////////////////////////////
                                }
                            } else {
                                dialogoEspere.dismiss();
                                dialogoRetry();
                            }
                        }
                    });
                } catch (IOException e) {
                    Log.e("MYAPP", "exception: " + e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        hiloConsulta.start();
    }

    private boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress();
                        Log.e("IP address", "" + ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Socket exception in GetIP Address of Utilities", ex.toString());
        }
        return null;
    }

    private Context context;
    private SharedPreferences preferences;
    private String queSeInstalo = null;
    public static boolean continuar;
    ArrayList<String[]> lista = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        preventStatusBarExpansion(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        viewBlackScreen = findViewById(R.id.viewBlackScreen);
        Timer();

        //Verificar si existen los archivos, si no existe crearlos.
        if (!FileUtils.checkIfExecutableExists()) {
            String[] arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/data1.zip";
            arreglo[1] = "data1.zip";
            lista.add(arreglo);
            arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/data2.zip";
            arreglo[1] = "data2.zip";
            lista.add(arreglo);
            arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/data3.zip";
            arreglo[1] = "data3.zip";
            lista.add(arreglo);
            arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/data4.zip";
            arreglo[1] = "data4.zip";
            lista.add(arreglo);
            arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/mysql1.zip";
            arreglo[1] = "mysql1.zip";
            lista.add(arreglo);
            arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/mysql2.zip";
            arreglo[1] = "mysql2.zip";
            lista.add(arreglo);
            arreglo = new String[2];
            arreglo[0] = "http://neosepel.ferozo.net/neoinnovaciones/zipProjects/htdocs.zip";
            arreglo[1] = "htdocs.zip";
            lista.add(arreglo);
            queSeInstalo = "core";
        } else {
            queSeInstalo = "noCore";
            iniciarServicios();
            refreshServerRunnable.run();
        }
    }

    public void iniciarServicios() {
        new ConnectionListenerTask().execute();

        boolean enableSU = preferences.getBoolean("run_as_root", false);
        String execName = preferences.getString("use_server_httpd", "lighttpd");
        String bindPort = preferences.getString("server_port", "8080");

        this.startService(new Intent(this, ServerService.class));
        CommandTask task = CommandTask.createForConnect(this, execName, bindPort);
        task.enableSU(enableSU);
        task.execute();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPause() {
        super.onPause();
        timerCustom.stop();
//        finishAffinity();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //delayedHide(0);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                if (!(proyectoActivo.equals("PV") || proyectoActivo.equals("PC") || proyectoActivo.equals("CF") || proyectoActivo.equals("CH")) || proyectoActivo.equals("STTAB")) {
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    Handler refreshServerHandler = new Handler();
    Runnable refreshServerRunnable = new Runnable() {
        @Override
        public void run() {
// todo CLASE DESCONOCIDA           new iniciarServicios2(preferences.getString("use_server_httpd", "lighttpd"), preferences.getString("server_port", "8080")).execute();
            refreshServerHandler.postDelayed(refreshServerRunnable, 2000);
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    public void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public Context getContext() {
        return context;
    }


    //TODO/////////////////////////////////////////////////////////////////////////////////////////
    private void scanHtml() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                myWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                if (myWebView.getUrl() != null) {
                    Utilities.log("URL-->" + myWebView.getUrl());
                    if (!myWebView.getUrl().equals("http://localhost:8080/demo/?protocolo=http&recurso=localhost%3A8080%2Findex1.php&w=1920&h=1080&titulo=&nota=")) {
                        myWebView.loadUrl("http://localhost:8080/index.php");
                    }
                }
            }
        });
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            Utilities.log("PAGINA--> " + html);
            if (html.contains("var proporcionAncho = 1;")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FullscreenActivity.this,
                                "DETECTO FRAGMENTO DE CODIGO DE EJEMPLO --> var proporcionAncho = 1;",
                                Toast.LENGTH_SHORT).show();
                        Settings.System.putInt(getContentResolver(),
                                Settings.System.SCREEN_BRIGHTNESS, 20);

                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.screenBrightness = 0f;// 100 / 100.0f;
                        getWindow().setAttributes(lp);
                    }
                });
            }
        }
    }

    private void sendGetScreenStatus() {
        JsonObject params = new JsonObject();
        params.addProperty("fingerprint", Utilities.getFingerPrint(this));

        NetUtilities netUtilities = new NetUtilities(this, new NetUtilities.OnNetUtilsActions() {
            @Override
            public void onInitRequest(String url) {
            }

            @Override
            public void onFinishRequest(String url, Exception e, String response, int status) {
                Utilities.log("RESPONSE--> " + response);
                JsonParser parser = new JsonParser();
                JsonElement obj = parser.parse(response);
                try {
                    int screen = obj.getAsJsonObject().get("screen").getAsInt();
                    if (screen == 0) {
                        screenOffAction();
                    } else {
                        screenOnAction();
                    }
                } catch (Exception ignored) {

                }
            }
        });
        netUtilities.getRequest(Utilities.urlBase + "pantalla.php", params);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerCustom.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCustom.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!timerCustom.isRunning()){
            Timer();
        }
    }

    private void screenOnAction() {
        if(viewBlackScreen.getVisibility() == View.VISIBLE){
            AnimationUtilities.animarAlpha(viewBlackScreen, 300, false, true);
            TimeHandler timeHandler = new TimeHandler(300, new TimeHandler.OnTimeComplete() {
                @Override
                public void onFinishTime() {
                    viewBlackScreen.setVisibility(View.GONE);
                }
            });
            timeHandler.start();
        }
    }

    private void screenOffAction() {
        if(viewBlackScreen.getVisibility() == View.GONE){
            viewBlackScreen.setVisibility(View.VISIBLE);
            AnimationUtilities.animarAlpha(viewBlackScreen, 300, true, true);
        }
    }

    //TODO/////////////////////////////////////////////////////////////////////////////////

    private SocketUtils socketUtils;

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
                        TimeHandler timeHandler = new TimeHandler(500, new TimeHandler.OnTimeComplete() {
                            @Override
                            public void onFinishTime() {
                                json.set("idClient", idClient);
                                json.set("image", ImageUtils.captureScreen(FullscreenActivity.this));
                                socketUtils.sendMessage(json.toString());
                            }
                        });
                        timeHandler.start();
                    } else if (json.at("type").asString().equals("screen")) {
                        screenOnOffAction();
                        TimeHandler timeHandler = new TimeHandler(500, new TimeHandler.OnTimeComplete() {
                            @Override
                            public void onFinishTime() {
                                json.set("idClient", idClient);
                                json.set("screenStatus", 2);
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

    private void screenOnOffAction() {
        if (viewBlackScreen.getVisibility() == View.GONE) {
            viewBlackScreen.setVisibility(View.VISIBLE);
            AnimationUtilities.animarAlpha(viewBlackScreen, 300, true, true);
        } else {
            AnimationUtilities.animarAlpha(viewBlackScreen, 300, false, true);
            TimeHandler timeHandler = new TimeHandler(300, new TimeHandler.OnTimeComplete() {
                @Override
                public void onFinishTime() {
                    viewBlackScreen.setVisibility(View.GONE);
                }
            });
            timeHandler.start();
        }
    }

    private void Timer(){
        timerCustom = new TimerCustom(10000,10000);
        timerCustom.setOnTimeRun(new TimerCustom.OnTimeRun() {
            @Override
            public void onTime() {
                sendGetScreenStatus();
            }
        });
    }
    //TODO////////////////////////////////////////////////////////////////////////////////

    //TODO////////////////////////////////////////////////////////////////////////////////
    private boolean appExpired() {
        return DateUtils.stringToDate(fechaExpiracion).before(DateUtils.getToday());
    }
    //TODO////////////////////////////////////////////////////////////////////////////////
}

//Clases Agregadas
class ConnectionListenerTask extends AsyncTask<Void, String, Void> {

    @Override
    protected Void doInBackground(Void... voids) {

        String FIND_PROCESS = String.format(
                "%s ps | %s grep \"components\"",
                Constants.BUSYBOX_SBIN_LOCATION,
                Constants.BUSYBOX_SBIN_LOCATION);

        List<String> rc = Shell.SH.run(FIND_PROCESS);

        boolean serverListing;
        boolean phpListing;
        boolean mysqlListing;

        String shellOutput = "";
        for (String buf : rc.toArray(new String[rc.size()])) {
            shellOutput += buf;
        }

        serverListing = (shellOutput.contains("lighttpd") || shellOutput.contains("nginx"));
        phpListing = shellOutput.contains("php-cgi");
        mysqlListing = shellOutput.contains("mysqld");

        if (serverListing && phpListing && mysqlListing) {
            publishProgress("OK");
        } else {
            publishProgress("ERROR");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

}

class LoadListener {
    private WebView objectWebView;
    private Handler handler = new Handler();
    private String idCliente;

    public void processHTML(String html) {
        boolean containsTotal = false;
        String errores[] = {"<h1>404 - Not Found</h1>", "<h1>500 - Internal Server Error</h1>", "<h1>400 - Bad request</h1>",
                "<h1>401 - Unauthorized</h1>", "<h1>503 Service unavailable</h1>", "<h1>Página web no disponible</h1>"};
        for (int x = 0; x < errores.length; x++) {
            if (html.contains(errores[x])) {
                containsTotal = true;
            }
        }
        if (containsTotal) {
            objectWebView.loadUrl("http://localhost:8080/aguarde.html");
        } else {
            if (objectWebView.getUrl().equals("http://localhost:8080/aguarde.html")) {

                final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                exec.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpContext localContext = new BasicHttpContext();
                            HttpPost httpPost = new HttpPost("http://localhost:8080/index.php");
                            //Asignacion de Parametros en POST
                            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("idCliente", idCliente));
                            //Ejecucion de consulta en Apache
                            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                            HttpResponse response = httpClient.execute(httpPost, localContext);

                            //Manejo de resultado de consulta
                            String resultado;
                            HttpEntity entity = response.getEntity();
                            resultado = EntityUtils.toString(entity, "UTF-8");

                            boolean containsTotal = false;
                            String errores[] = {"<h1>404 - Not Found</h1>", "<h1>500 - Internal Server Error</h1>", "<h1>400 - Bad request</h1>",
                                    "<h1>401 - Unauthorized</h1>", "<h1>503 Service unavailable</h1>"};
                            for (int x = 0; x < errores.length; x++) {
                                if (resultado.contains(errores[x])) {
                                    containsTotal = true;
                                }
                            }
                            if (containsTotal) {
                                Log.d("RES:", String.valueOf(containsTotal));
                            } else {
                                exec.shutdown();
                                String postDataStr = "idCliente=" + idCliente;
                                objectWebView.postUrl("http://localhost:8080/index.php", EncodingUtils.getBytes(postDataStr, "utf-8"));
                                Log.d("RES:", String.valueOf(containsTotal));
                            }
                        } catch (IOException e) {
                            exec.execute(this);
                            e.printStackTrace();
                        }
                    }
                }, 0, 5, TimeUnit.SECONDS);
            }
        }
        Log.e("result load html:", html);
    }

    public void setCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setWebView(WebView wv) {
        this.objectWebView = wv;
    }

}











