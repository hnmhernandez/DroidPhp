package org.opendroidphp.app.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harold Montenegro on 17/08/16.
 */
public class TimerCustom {
    int time, delay = 0;
    private OnTimeRun onTimeRun;
    boolean isRunning = true;

    public void setOnTimeRun(OnTimeRun onTimeRun) {
        this.onTimeRun = onTimeRun;
    }

    public interface OnTimeRun {
        void onTime();
    }

    // Clase en la que está el código a ejecutar
    TimerTask timerTask = new TimerTask() {
        public void run() {
            if (onTimeRun != null) {
                onTimeRun.onTime();
            }
        }
    };

    Timer timer = new Timer();

    public TimerCustom(int time) {
        this.time = time;
        timer.scheduleAtFixedRate(timerTask, 0, time);
    }

    public TimerCustom(int time, int delay) {
        this.time = time;
        this.delay = delay;
        timer.scheduleAtFixedRate(timerTask, delay, time);
    }

    public void stop() {
        isRunning = false;
        timer.cancel();
    }

    public void start() {
        isRunning = true;
        timer.scheduleAtFixedRate(timerTask, delay, time);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
