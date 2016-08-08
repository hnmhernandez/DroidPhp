package org.opendroidphp.app.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import org.opendroidphp.app.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Guille on 17/04/2015.
 */
public class DescargarArchivo extends ProgressDialogTask<String, String, String>{

    String loc;
    Context cont;
    String nombreProjecto;
    String urlDescarga;
    Activity actividad;
    ArrayList<String[]> listaADescargar;
    AlertDialog dialogo;
    String idCliente;
    String core = "";

    public DescargarArchivo(Context context,ArrayList<String[]> listaADescargar, String nombreProjecto,Activity actividad,String core) {
        //super(context, title, message);
        this.cont = context;
        this.actividad = actividad;
        this.listaADescargar = listaADescargar;
        this.nombreProjecto = nombreProjecto;
        this.core = core;
    }
    public DescargarArchivo(Context context,ArrayList<String[]> listaADescargar, String nombreProjecto,Activity actividad,AlertDialog dialogo,String idCliente) {
        //super(context);
        this.cont = context;
        this.actividad = actividad;
        this.idCliente = idCliente;
        this.nombreProjecto = nombreProjecto;
        this.listaADescargar = listaADescargar;
        this.dialogo = dialogo;
    }

    @Override
    protected String doInBackground(String... params) {
        loc = Constants.EXTERNAL_STORAGE.concat("/");
        if(this.core.equals("core")){
            borrarCarpetas();
        }
        try {
            for(int i = 0; i < listaADescargar.size(); i++)
            {
                String[] arrat;
                arrat =listaADescargar.get(i);
                Log.d("Variable String 1", arrat[1]);
                Log.d("Variable String 2", arrat[0]);
                //Statements

                File root = android.os.Environment.getExternalStorageDirectory();

                File dir = new File (root.getAbsolutePath());
                if(dir.exists()==false) {
                    dir.mkdirs();
                }

                URL url = new URL(arrat[0]); //you can write here any link
                File file = new File(dir, arrat[1]);

                long startTime = System.currentTimeMillis();
                Log.d("DownloadManager", "download begining");
                Log.d("DownloadManager", "download url:" + arrat[0]);
                Log.d("DownloadManager", "downloaded file name:" + arrat[1]);

                   /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

                   /*
                    * Define InputStreams to read from the URLConnection.
                    */
                /*InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                ByteArrayBuffer baf = new ByteArrayBuffer(10000);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }*/
                //
                final int BUFFER_SIZE = 23 * 1024;
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] baf = new byte[BUFFER_SIZE];
                int actual = 0;
                while (actual != -1) {
                    fos.write(baf, 0, actual);
                    actual = bis.read(baf, 0, BUFFER_SIZE);
                }

                fos.close();
                //

                   /* Convert the Bytes read to a String. */
                /*FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.flush();
                fos.close();*/
                Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
            }
        } catch (IOException e) {
            Log.d("DownloadManager", "Error: " + e);
        }
        return null;
    }

    public static void borrarCarpetas(){
        String loc = Constants.EXTERNAL_STORAGE.concat("/");
        final ArrayList<File> listaCarpetas = new ArrayList<File>();
        listaCarpetas.add(new File(loc + "clasesFuncionesWebService/"));
        listaCarpetas.add(new File(loc + "droidphp/"));
        listaCarpetas.add(new File(loc + "htdocs/"));
        listaCarpetas.add(new File(loc + "js/"));
        listaCarpetas.add(new File(loc + "libraries/"));
        listaCarpetas.add(new File(loc + "locale/"));
        listaCarpetas.add(new File(loc + "setup/"));
        listaCarpetas.add(new File(loc + "themes/"));
        listaCarpetas.add(new File(loc + "mnt/"));
        for (int i=0;i<listaCarpetas.size();i++){
            deleteDirectory(listaCarpetas.get(i));
        }
        new File(loc + "data1.zip").delete();
        new File(loc + "data2.zip").delete();
        new File(loc + "data3.zip").delete();
        new File(loc + "data4.zip").delete();
        new File(loc + "htdocs.zip").delete();
        new File(loc + "htdocs").delete();
        new File(loc + "mysql1.zip").delete();
        new File(loc + "mysql2.zip").delete();
        Log.d("Borrar Archivos","Archivos Borrados. Para nueva instalacion");
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        if(nombreProjecto != null){
            //WebView myWebView = (WebView) actividad.findViewById(R.id.webView);
            //myWebView.setVisibility(View.VISIBLE);
            //String android_id= Settings.Secure.getString(cont.getContentResolver(),
                   // Settings.Secure.ANDROID_ID);
            //String postData = "fingerprint=" + android_id + "&login_password=myPassword";
            //myWebView.postUrl("http://192.168.1.130/Projects/view/registro.php", EncodingUtils.getBytes(postData, "utf-8"));
            //myWebView.postUrl("http://localhost:8080/webService/prueba.php", EncodingUtils.getBytes(postData, "utf-8"));
            //myWebView.loadUrl("http://localhost:8080/");
            InstallerHtdocsProyecto asyncTask = new InstallerHtdocsProyecto(cont,actividad,dialogo,this.idCliente);
            asyncTask.execute();
        }else{
            RepoInstallerTask task = new RepoInstallerTask(this.cont,this.actividad);
            task.execute("", Constants.INTERNAL_LOCATION.concat("/"));
        }

    }
}
