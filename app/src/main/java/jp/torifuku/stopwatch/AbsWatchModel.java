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
                        time += now - last;
                        last = now;
                        observable.changed();
                        notifyObservers();
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

    protected void notifyObservers() {
        observable.notifyObservers(time);
    }

    protected long now() {
        return System.currentTimeMillis();
    }

    private static class StopWatchModelObservable extends Observable {
        private void changed() {
            setChanged();
        }
    }
}
