package org.opendroidphp.app.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opendroidphp.R;
import org.opendroidphp.app.Constants;
import org.opendroidphp.app.common.utils.FileUtils;
import org.opendroidphp.app.tasks.RepoInstallerTask;

import java.io.File;
import java.io.IOException;
import java.lang.Process;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * Created by Guille on 31/03/2015.
 */
public class Registro extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        if (!FileUtils.checkIfExecutableExists()) {
            RepoInstallerTask task = new RepoInstallerTask(this,this);
            task.execute("", Constants.INTERNAL_LOCATION.concat("/"));
        }
    }
    String spinnerText;
    @Override
    protected void onStart() {
        super.onStart();
        Bundle bund = getIntent().getExtras();
        final TextView txtEmpresa = (TextView) findViewById(R.id.txtEmpresa);
        final TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        final TextView txtPassword = (TextView) findViewById(R.id.txtPass);
        final TextView txtPais = (TextView) findViewById(R.id.txtPais);
        final TextView txtProvincia = (TextView) findViewById(R.id.txtProvincia);
        final TextView txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        final CheckBox checkMusica = (CheckBox) findViewById(R.id.checkBoxMusica);
        final Button btnAceptar = (Button) findViewById(R.id.btnRegistrar);

        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        Spinner spinnerList = (Spinner) findViewById(R.id.cbxTerminal);
        ArrayAdapter<String> adapter;
        ArrayList<String> list = new ArrayList<String>();
        list.add("Cartelera para Velatorios");
        list.add("Cartelera para Sala Velatoria");
        list.add("Cartelera para Coche Fúnebre");
        list.add("Cartelera de Condolencias");
        list.add("Carteleras de Homenajes");
        list.add("Puesto de Informes para Velatorios");
        list.add("Puesto de Informes para Cementerios");
        list.add("Música Funcional");
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.setAdapter(adapter);
        //Si selecciona musica funcional esconde el check
        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Música Funcional")){
                    checkMusica.setVisibility(View.GONE);
                }else{
                    checkMusica.setVisibility(View.VISIBLE);
                }
                spinnerText = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerText = "Cartelera para Velatorios";
            }
        });

        if (bund != null) {
            txtEmpresa.setText(bund.getString("nombreEmpresa"));
            txtEmpresa.setEnabled(false);
            txtEmail.setText(bund.getString("email"));
            txtEmail.setEnabled(false);
            txtPassword.setText(bund.getString("password"));
            txtPassword.setEnabled(false);
            txtPais.setText(bund.getString("pais"));
            txtPais.setEnabled(false);
            txtProvincia.setText(bund.getString("provincia"));
            txtProvincia.setEnabled(false);
            txtTelefono.setText(bund.getString("telefono"));
            txtTelefono.setEnabled(false);
            String s = bund.getString("tipoTerminal");
            if (s.equals("CV")) {
                spinnerList.setSelection(0);

            } else if (s.equals("CI")) {
                spinnerList.setSelection(1);

            } else if (s.equals("CF")) {
                spinnerList.setSelection(2);

            } else if (s.equals("CC")) {
                spinnerList.setSelection(3);

            } else if (s.equals("CH")) {
                spinnerList.setSelection(4);

            } else if (s.equals("PV")) {
                spinnerList.setSelection(5);

            } else if (s.equals("PC")) {
                spinnerList.setSelection(6);

            } else if (s.equals("MF")) {
                spinnerList.setSelection(7);

            }
            spinnerList.setEnabled(false);
            if (bund.getString("musicaFuncional").equals("1")){
                checkMusica.setChecked(true);
            }else{
                checkMusica.setChecked(false);
            }
            checkMusica.setEnabled(false);
            btnAceptar.setText("Continuar");

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Se procederá a cerrar la aplicación para continuar la instalación.", Toast.LENGTH_LONG).show();
                    Handler closeApk = new Handler();
                    closeApk.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            noti.cancel(143);
                            finishAffinity();
                        }
                    },5000);
                }
            });
            btnCancelar.setText("Salir");
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    noti.cancel(143);
                    finishAffinity();
                }
            });
        }else{
            btnAceptar.setText("Registrar");
            //controlgogogo.start();
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String musicaFuncional;
                        if(checkMusica.isChecked() || spinnerText.equals("Música Funcional")){
                            musicaFuncional = "true";
                        }else{
                            musicaFuncional = "false";
                        }
                        if (txtEmpresa.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Nombre de empresa no puede estar vacio.",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (txtEmail.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Email no puede estar vacio.",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (txtPassword.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Password no puede estar vacio.",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (txtTelefono.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Telefono no puede estar vacio.",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (ingresarNuevoUser(txtEmpresa.getText().toString(), txtEmail.getText().toString(), txtPassword.getText().toString(), txtPais.getText().toString(), txtProvincia.getText().toString(),
                                txtTelefono.getText().toString(), idDispositivo(), spinnerText, musicaFuncional) == true) {
                            Toast.makeText(getApplicationContext(), "Registrado Correctamente. Se procederá a cerrar la aplicación para continuar la instalación.", Toast.LENGTH_LONG).show();
                            Handler closeApk = new Handler();
                            closeApk.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    noti.cancel(143);
                                    finishAffinity();
                                }
                            },5000);

                        }else{
                            Toast.makeText(getApplicationContext(),"No se pudo registrar, el dispositivo ya existe, llame a la asistencia tecnica para mayor informacion.",Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            btnCancelar.setText("Salir");
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    noti.cancel(143);
                    finishAffinity();
                }
            });

        }

    }

    private boolean ingresarNuevoUser(final String NombreEmpresa, final String email, final String password, final String Pais, final String Provincia, final String Telefono,
                                   final String idDispositivo, final String tipoTerminal, final String musicaFuncional) throws InterruptedException {
        final boolean[] respuesta = new boolean[1];
        Thread hiloConsulta = new Thread(new Runnable() {

            Context appCont = getApplicationContext();
            public void run() {
                try {
                    //Conexion
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpContext localContext = new BasicHttpContext();
                    HttpPost httpPost = new HttpPost("http://neosepel.ferozo.net/neoinnovaciones/clasesFuncionesWebService/android_webservice.php");
                    //HttpPost httpPost = new HttpPost("http://192.168.1.130/cv/acciones/android_webservice.php");
                    //Asignacion de Parametros en POST
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("accion", "registrarUsuario"));
                    params.add(new BasicNameValuePair("servidor", "localhost"));
                    params.add(new BasicNameValuePair("user", "neosepel_neo"));
                    params.add(new BasicNameValuePair("pwd", "Atenas32"));
                    params.add(new BasicNameValuePair("database", "neosepel_ni_licenciamiento"));
                    params.add(new BasicNameValuePair("email", email));
                    params.add(new BasicNameValuePair("password", password));
                    params.add(new BasicNameValuePair("nomEmpresa", NombreEmpresa));
                    params.add(new BasicNameValuePair("provincia", Provincia));
                    params.add(new BasicNameValuePair("pais", Pais));
                    params.add(new BasicNameValuePair("telefono", Telefono));
                    params.add(new BasicNameValuePair("fingerprint", idDispositivo));
                    params.add(new BasicNameValuePair("tipoTerminal", tipoTerminal));
                    params.add(new BasicNameValuePair("musicaFuncional", musicaFuncional));

                    //Ejecucion de consulta en Apache
                    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                    HttpResponse response = httpClient.execute(httpPost, localContext);
                    //Manejo de resultado de consulta
                    String resultado;
                    HttpEntity entity = response.getEntity();
                    resultado = EntityUtils.toString(entity, "UTF-8");

                    if (resultado.equals("OK")) {
                        respuesta[0] = true;
                    }else{
                        respuesta[0] = false;
                    }

                }catch (IOException e) {
                    Log.e("MYAPP", "exception: " + e.getMessage());
                }
            }
        });
        hiloConsulta.start();
        hiloConsulta.join();
        return respuesta[0];
    }

    private String idDispositivo(){
        return Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
