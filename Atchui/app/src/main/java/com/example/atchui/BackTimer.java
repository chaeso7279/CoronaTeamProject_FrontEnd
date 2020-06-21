package com.example.atchui;

import java.util.Timer;
import java.util.TimerTask;

public class BackTimer {
    private static Timer timer = null;

    public static void createTimer(TimerTask task, long delay, long period) {
        timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, period);
    }

    public static void stopTimer() {
        timer.cancel();
        timer = null;
    }

    public static boolean isRunning() {
        return timer != null;
    }
}