package org.opendroidphp.app.ui;


import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.IOUtils;
import org.opendroidphp.R;
import org.opendroidphp.app.AppController;
import org.opendroidphp.app.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Process;

public class Query{

    private static InputStream stdout;
    private static OutputStream stdin;
    private Button mBtnQuery;
    private EditText mQuery;
    private Process process;

    public Query() {
        final String username = "root";
        final String password = "";

        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeShell(username, password);
            }
        }).start();
        new AsyncCommandTask().execute();
    }

    protected Process initializeShell(final String username, final String password) {

        String[] baseShell = new String[]{
                Constants.MYSQL_MONITOR_SBIN_LOCATION, "-h",
                "127.0.0.1", "-T", "-f", "-r", "-t", "-E", "--disable-pager",
                "-n", "--user=" + username, "--password=" + password,
                "--default-character-set=utf8", "-L"};
        try {
            process = new ProcessBuilder(baseShell).
                    redirectErrorStream(true).
                    start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;
    }

    public void cambiarPassword(String password) {
        if (process == null) return;
        String command =
                "mysql -u root;use mysql;update user set password=PASSWORD('Atenas32') where user='root';flush privileges;";
        try {
            stdin.write((command + "\r\n").getBytes());
            stdin.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class AsyncCommandTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if (process == null) return null;
            BufferedReader buff = new BufferedReader(
                    new InputStreamReader(stdout));
            try {
                while (true) {
                    String stream = buff.readLine();
                    if (stream == null) break;
                    publishProgress(stream);
                }
                IOUtils.closeQuietly(buff);
            } catch (Exception e) {
                e.printStackTrace();
                IOUtils.closeQuietly(buff);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mQuery.append(values[0]);
        }
    }
}