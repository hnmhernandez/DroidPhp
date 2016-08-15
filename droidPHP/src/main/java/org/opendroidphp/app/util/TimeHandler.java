package org.opendroidphp.app.util;

/**
 * Created by Harold Montenegro on 5/5/2016.
 */

import android.os.Handler;

/**
        * Esta clase es una especie de "delay", lo que hace es que ejecuta algo, cualquier cosa luego del tiempo que se le asigne que
        * espere... en milisegundos.
        * <p/>
        * Luego de crear la instancia, para que se ejecute hay que llamar la función "iniciar".
        */
public class TimeHandler extends Handler {
    private Integer despuesDe;
    private OnTimeComplete onTimeComplete;
    private Runnable runnable;

    public TimeHandler(Integer despuesDe, OnTimeComplete onTimeComplete) {
        this.despuesDe = despuesDe;
        this.onTimeComplete = onTimeComplete;
    }

    public void start() {
        runnable = new Runnable() {
            @Override
            public void run() {
                onTimeComplete.onFinishTime();
                runnable = null;
            }
        };
        postDelayed(runnable, despuesDe);
    }

    public void cancelTimeHandler() {
        if (runnable != null) {
            removeCallbacks(runnable);
            runnable = null;
        }
    }

    public interface OnTimeComplete {
        public void onFinishTime();
    }



}

