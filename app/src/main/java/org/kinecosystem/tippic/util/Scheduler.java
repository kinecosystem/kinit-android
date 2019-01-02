package org.kinecosystem.tippic.util;

public interface Scheduler {

    void scheduleOnMain(Runnable runnable, long delayMillis);

    void post(Runnable runnable);

    void cancel(Runnable runnable);

    long currentTimeMillis();

    void executeOnBackground(Runnable runnable);
}
