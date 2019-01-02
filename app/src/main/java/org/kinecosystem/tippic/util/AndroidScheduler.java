package org.kinecosystem.tippic.util;


import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AndroidScheduler implements Scheduler {

    private Handler handler = new Handler(Looper.getMainLooper());
    private ExecutorService executorService = Executors.newCachedThreadPool();


    @Override
    public void scheduleOnMain(Runnable runnable, long delayMillis) {
        if (runnable != null) {
            handler.postDelayed(runnable, delayMillis);
        }
    }

    public void post(Runnable runnable) {
        if (runnable != null) {
            handler.post(runnable);
        }
    }

    @Override
    public void cancel(Runnable runnable) {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public void executeOnBackground(Runnable runnable) {
        executorService.execute(runnable);
    }
}
