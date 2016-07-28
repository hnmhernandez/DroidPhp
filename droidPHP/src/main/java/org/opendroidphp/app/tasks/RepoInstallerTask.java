package org.opendroidphp.app.tasks;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;
import org.opendroidphp.R;
import org.opendroidphp.app.Constants;
import org.opendroidphp.app.ui.FullscreenActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.opendroidphp.app.ui.FullscreenActivity.NOTIFICATION_SERVICE;

public class RepoInstallerTask extends ProgressDialogTask<String, String, String> {

    public static final String INSTALL_DONE = "org.opendroidphp.repository.INSTALLED";
    public static final String INSTALL_ERROR = "org.opendroidphp.repository.INSTALL_ERROR";

    private String repositoryName;
    private String repositoryPath;
    private String loc = Constants.EXTERNAL_STORAGE.concat("/");
    private Context cont;
    private Activity actividad;


    public RepoInstallerTask(Context context,Activity actividad) {
        //super(context);
        this.cont = context;
        this.actividad = actividad;
    }

    public RepoInstallerTask(Context context, String title, String message) {
        //super(context, title, message);
    }

    @Override
    protected String doInBackground(String... file) {
        repositoryName = file[0];
        repositoryPath = file[1];
        boolean isSuccess = true;
        dismissProgress();
        ZipInputStream zipInputStream = null;
        createDirectory("");
        try {
            if (repositoryName == null || repositoryName.equals("")) {
                //zipInputStream = new ZipInputStream(getContext().getAssets().open("data.zip"));
                zipInputStream = new ZipInputStream(new FileInputStream(loc + "data1.zip"));
            } else if (new File(repositoryName).exists()) {
                zipInputStream = new ZipInputStream(new FileInputStream(repositoryName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZipEntry zipEntry;
        try {
            FileOutputStream fout;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    createDirectory(zipEntry.getName());
                } else {
                    if (!new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().exists()){
                        new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().mkdirs();
                    }
                    fout = new FileOutputStream(repositoryPath + "/" + zipEntry.getName());
                    //publishProgress(zipEntry.getName());
                    byte[] buffer = new byte[4096 * 10];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }
                    zipInputStream.closeEntry();
                    fout.close();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }

        //creacion archivo 2
        try {
            if (repositoryName == null || repositoryName.equals("")) {
                //zipInputStream = new ZipInputStream(getContext().getAssets().open("data.zip"));
                zipInputStream = new ZipInputStream(new FileInputStream(loc + "data2.zip"));
            } else if (new File(repositoryName).exists()) {
                zipInputStream = new ZipInputStream(new FileInputStream(repositoryName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fout;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    createDirectory(zipEntry.getName());
                } else {
                    if (!new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().exists()){
                        new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().mkdirs();
                    }
                    fout = new FileOutputStream(repositoryPath + "/" + zipEntry.getName());
                    //publishProgress(zipEntry.getName());
                    byte[] buffer = new byte[4096 * 10];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }
                    zipInputStream.closeEntry();
                    fout.close();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }

        //creacion archivo 3
        try {
            if (repositoryName == null || repositoryName.equals("")) {
                //zipInputStream = new ZipInputStream(getContext().getAssets().open("data.zip"));
                zipInputStream = new ZipInputStream(new FileInputStream(loc + "data3.zip"));
            } else if (new File(repositoryName).exists()) {
                zipInputStream = new ZipInputStream(new FileInputStream(repositoryName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fout;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    createDirectory(zipEntry.getName());
                } else {
                    if (!new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().exists()){
                        new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().mkdirs();
                    }
                    fout = new FileOutputStream(repositoryPath + "/" + zipEntry.getName());
                    //publishProgress(zipEntry.getName());
                    byte[] buffer = new byte[4096 * 10];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }
                    zipInputStream.closeEntry();
                    fout.close();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }

        //creacion archivo 4
        try {
            if (repositoryName == null || repositoryName.equals("")) {
                //zipInputStream = new ZipInputStream(getContext().getAssets().open("data.zip"));
                zipInputStream = new ZipInputStream(new FileInputStream(loc + "data4.zip"));
            } else if (new File(repositoryName).exists()) {
                zipInputStream = new ZipInputStream(new FileInputStream(repositoryName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fout;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    createDirectory(zipEntry.getName());
                } else {
                    if (!new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().exists()){
                        new File(repositoryPath + "/" + zipEntry.getName()).getParentFile().mkdirs();
                    }
                    fout = new FileOutputStream(repositoryPath + "/" + zipEntry.getName());
                    //publishProgress(zipEntry.getName());
                    byte[] buffer = new byte[4096 * 10];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }
                    zipInputStream.closeEntry();
                    fout.close();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }

        //publishProgress(isSuccess ? INSTALL_DONE : INSTALL_ERROR);
        //dismissProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        setMessage(values[0]);
        if (values[0].equals(INSTALL_DONE) || values[0].equals(INSTALL_ERROR)) {
            int resId = values[0].equals(INSTALL_DONE) ? R.string.core_apps_installed :
                    R.string.core_apps_not_installed;
            //AppController.toast(getContext(), getContext().getString(resId));
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        new File (loc + "data1.zip").delete();
        new File (loc + "data2.zip").delete();
        new File (loc + "data3.zip").delete();
        new File (loc + "data4.zip").delete();
        InstallerAsync3 asyncTask = new InstallerAsync3(cont,actividad);
        asyncTask.execute();
    }

    /**
     * Responsible for creating directory inside application's data directory
     *
     * @param dirName Directory to create during extracting
     */
    protected void createDirectory(String dirName) {
        File file = new File(repositoryPath + dirName);
        if (!file.isDirectory()) file.mkdirs();
    }
}

class InstallerAsync3 extends ProgressDialogTask<String, String, String>  {
    String loc;
    Context cont;
    Activity actividad;

    public InstallerAsync3(Context context,Activity actividad) {
        //super(context);
        this.cont =context;
        this.actividad = actividad;
    }

    ProgressDialog msgBox;

    @Override
    protected String doInBackground(String... params) {
        loc = Constants.EXTERNAL_STORAGE.concat("/");
        if (!new File(loc + "droidphp/phpMyAdmin").exists()) {
            try {
                createDirectory("droidphp/phpMyAdmin");
                ZipInputStream zin = new ZipInputStream(new FileInputStream(loc + "mysql1.zip"));
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    if (ze.isDirectory()) {
                        createDirectory(ze.getName());
                    } else {
                        if (!new File(loc + "droidphp/phpMyAdmin/" + ze.getName()).getParentFile().exists()){
                            new File(loc + "droidphp/phpMyAdmin/" + ze.getName()).getParentFile().mkdirs();
                        }
                        FileOutputStream fout = new FileOutputStream(loc + "droidphp/phpMyAdmin/" + ze.getName());
                        byte[] buffer = new byte[4096 * 10];
                        int length = 0;
                        while ((length = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, length);
                        }
                        zin.closeEntry();
                        fout.close();
                    }
                }
                zin.close();

                ZipInputStream zin2 = new ZipInputStream(new FileInputStream(loc + "mysql2.zip"));
                ZipEntry ze2 = null;
                while ((ze2 = zin2.getNextEntry()) != null) {
                    if (ze2.isDirectory()) {
                        createDirectory(ze2.getName());
                    } else {
                        if (!new File(loc + "droidphp/phpMyAdmin/" + ze2.getName()).getParentFile().exists()){
                            new File(loc + "droidphp/phpMyAdmin/" + ze2.getName()).getParentFile().mkdirs();
                        }
                        FileOutputStream fout2 = new FileOutputStream(loc + "droidphp/phpMyAdmin/" + ze2.getName());
                        byte[] buffer = new byte[4096 * 10];
                        int length = 0;
                        while ((length = zin2.read(buffer)) != -1) {
                            fout2.write(buffer, 0, length);
                        }
                        zin2.closeEntry();
                        fout2.close();
                    }
                }
                zin2.close();
                //crear htdocs en raiz y borrar la que esta droidphp
                new File(loc + "droidphp/htdocs").delete();
                new File (loc + "mysql1.zip").delete();
                new File (loc + "mysql2.zip").delete();
                //new File(loc + "/htdocs").mkdir();

            } catch (java.lang.Exception e) {
                //publishProgress("error");
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        //dismissProgress();
        InstallerHtdocs asyncTask = new InstallerHtdocs(cont,actividad);
        asyncTask.execute();
    }

    protected void createDirectory(String dirName) {
        File file2 = new File(Constants.EXTERNAL_STORAGE.concat("/") + dirName);
        if (!file2.isDirectory()) {
            file2.mkdirs();
        }
    }
}

class InstallerHtdocs extends ProgressDialogTask<String, String, String> {

    String loc;
    Context cont;
    Activity actividad;

    public InstallerHtdocs(Context context, Activity actividad) {
        //super(context);
        this.cont = context;
        this.actividad = actividad;
    }

    private SharedPreferences preferences;

    protected void createDirectory(String dirName) {
        File file2 = new File(Constants.EXTERNAL_STORAGE.concat("/") + dirName);
        if (!file2.isDirectory()) {
            file2.mkdirs();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        loc = Constants.EXTERNAL_STORAGE.concat("/");
        try {
            createDirectory("htdocs");
            ZipInputStream zin = new ZipInputStream(new FileInputStream(loc + "htdocs.zip"));
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    createDirectory(loc + "htdocs/" +ze.getName());
                } else {
                    if (!new File(loc + "htdocs/" + ze.getName()).getParentFile().exists()){
                        new File(loc + "htdocs/" + ze.getName()).getParentFile().mkdirs();
                    }
                    FileOutputStream fout = new FileOutputStream(loc + "htdocs/" + ze.getName());
                    byte[] buffer = new byte[4096 * 10];
                    int length = 0;
                    while ((length = zin.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }
                    zin.closeEntry();
                    fout.close();
                }
            }
            zin.close();
            copyDirectory(new File(loc + "htdocs/clasesFuncionesWebService"), new File(loc + "clasesFuncionesWebService"));
            new File (loc + "htdocs.zip").delete();
            new File (loc + "clasesFuncionesWebService/localhost.sql").delete();
        } catch (java.lang.Exception e) {
            //publishProgress("error");
        }
        return null;
    }

    public void copyDirectory(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        WebView myWebView = (WebView) actividad.findViewById(R.id.webView);
        String webUrl = myWebView.getUrl();
        //if (myWebView.getUrl().equals("http://192.168.1.130/Projects/view/successful.php")){
        if (myWebView.getUrl().equals("http://neosepel.ferozo.net/neoinnovaciones/RegistroClientes/view/successful.php")){
            NotificationManager noti = (NotificationManager) actividad.getSystemService(NOTIFICATION_SERVICE);
            noti.cancel(143);
            actividad.finishAffinity();
        }
        myWebView.setWebViewClient(new WebViewClient() {
            boolean as;
            public void onPageFinished(WebView view, String url) {
                //if (url.equals("http://192.168.1.130/Projects/view/successful.php")){
                if (url.equals("http://neosepel.ferozo.net/neoinnovaciones/RegistroClientes/view/successful.php")){
                    Handler myHandler = new Handler();
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NotificationManager noti = (NotificationManager) actividad.getSystemService(NOTIFICATION_SERVICE);
                            noti.cancel(143);
                            actividad.finishAffinity();
                        }
                    }, 6000);
                    as=true;
                }else{
                    as=false;
                }
            }
        });
         //dismissProgress();
        //Button btn = (Button) myactivity.findViewById(R.id.btnRegistrar);
        //Button btnSalir = (Button) myactivity.findViewById(R.id.btnCancelar);
        //btn.setEnabled(true);
        //btnSalir.setEnabled(true);
    }
}

class InstallerHtdocsProyecto extends ProgressDialogTask<String, String, String> {

    String loc;
    Context cont;
    Activity actividad;
    AlertDialog dialogo;
    String idCliente;

    public InstallerHtdocsProyecto(Context context, Activity actividad, AlertDialog dialogo,String idCliente) {
        //super(context);
        this.cont = context;
        this.actividad = actividad;
        this.dialogo = dialogo;
        this.idCliente = idCliente;
    }

    private SharedPreferences preferences;

    protected void createDirectory(String dirName) {
        File file2 = new File(Constants.EXTERNAL_STORAGE.concat("/") + dirName);
        if (!file2.isDirectory()) {
            file2.mkdirs();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        loc = Constants.EXTERNAL_STORAGE.concat("/");
            try {
                //createDirectory("htdocs");
                ZipInputStream zin = new ZipInputStream(new FileInputStream(loc + "htdocs.zip"));
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    if (ze.isDirectory()) {
                        createDirectory(loc + "htdocs/" + ze.getName());
                    } else {
                        if (!new File(loc + "htdocs/" + ze.getName()).getParentFile().exists()){
                            new File(loc + "htdocs/" + ze.getName()).getParentFile().mkdirs();
                        }
                        FileOutputStream fout = new FileOutputStream(loc + "htdocs/" + ze.getName());
                        byte[] buffer = new byte[4096 * 10];
                        int length = 0;
                        while ((length = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, length);
                        }
                        zin.closeEntry();
                        fout.close();
                    }
                }
                zin.close();
                new File (loc + "htdocs.zip").delete();
            } catch (java.lang.Exception e) {
                //publishProgress("error");
            }
        return null;
    }
    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        dialogo.dismiss();
        WebView myWebView = (WebView) actividad.findViewById(R.id.webView);
        myWebView.setVisibility(View.VISIBLE);
        String postData = "idCliente=" + idCliente + "&key=mC5GPxx2JSXmVXy9jm5j&ipServer="+ FullscreenActivity.getIpAddress();
        //myWebView.postUrl("http://192.168.1.130/Projects/view/registro.php", EncodingUtils.getBytes(postData, "utf-8"));
        myWebView.postUrl("http://localhost:8080/index.php", EncodingUtils.getBytes(postData, "utf-8"));
        //myWebView.loadUrl("http://localhost:8080/");
    }
}
