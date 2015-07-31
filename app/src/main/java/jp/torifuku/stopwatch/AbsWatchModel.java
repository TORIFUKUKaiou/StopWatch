package jp.torifuku.stopwatch;

import java.util.Observable;
import java.util.Observer;

/**
 * AbsWatchModel
 */
public abstract class AbsWatchModel {

    private final StopWatchModelObservable observable;

    private long time;
    private long last;
    private boolean running;

    public AbsWatchModel() {
        super();
        observable = new StopWatchModelObservable();
    }

    public boolean startOrStop() {
        running = !running;
        if (running) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    last = now();
                    while (running) {
                        final long now = now();
                        setTimeAndNotifyObservers(getTime() + now - last);
                        last = now;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        return running;
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    public void deleteObserver(Observer observer) {
        observable.deleteObserver(observer);
    }

    public long getTime() {
        return time;
    }

    public void reset() {
        setTimeAndNotifyObservers(0);
    }

    protected void notifyObservers() {
        observable.changed();
        observable.notifyObservers(time);
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private void setTime(long arg) {
        time = arg;
    }

    private void setTimeAndNotifyObservers(long arg) {
        setTime(arg);
        notifyObservers();
    }

    private static class StopWatchModelObservable extends Observable {
        private void changed() {
            setChanged();
        }
    }
}
