package org.opendroidphp.app.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import org.opendroidphp.R;
import org.opendroidphp.app.NumpadTool;
import org.opendroidphp.app.ui.FullscreenActivity;

/**
 * Created by Harold Montenegro on 29/07/16.
 */
public class DroidPhpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    int number_of_clicks = 0;
    boolean thread_started = false;
    final int DELAY_BETWEEN_CLICKS_IN_MILLISECONDS = 600;

    @Override
    public void onBackPressed() {
        if (this instanceof FullscreenActivity) {
            ++number_of_clicks;
            if (!thread_started) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thread_started = true;
                        try {
                            Thread.sleep(DELAY_BETWEEN_CLICKS_IN_MILLISECONDS);
                            if (number_of_clicks == 1) {
                                Log.d("onBackPressed1", String.valueOf(number_of_clicks));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DroidPhpActivity.this, R.string.message_exit,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (number_of_clicks == 5) {
                                Log.d("onBackPressed5", String.valueOf(number_of_clicks));
                                runOnUiThread(new Runnable() {
                                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                                    @Override
                                    public void run() {
                                        //todo ACCION DE MOSTRAR NUMPAD
                                        Intent intent = new Intent(DroidPhpActivity.this, NumpadTool.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            number_of_clicks = 0;
                            thread_started = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else {
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
        NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.cancel(143);
    }

    /*Funcion para prevenir que el StatusBar se pueda abrir*/
    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity) context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        customViewGroup view = new customViewGroup(context);

        manager.addView(view, localLayoutParams);
    }

    /*Clase usada para preventStatusBarExpansion*/
    public static class customViewGroup extends ViewGroup {

        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
    }
}
