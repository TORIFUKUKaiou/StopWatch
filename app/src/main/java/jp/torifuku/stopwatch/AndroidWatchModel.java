package jp.torifuku.stopwatch;

import android.os.Handler;


/**
 * AndroidWatchModel
 */
public class AndroidWatchModel extends AbsWatchModel {
    private final Handler handler;

    public AndroidWatchModel() {
        super();
        handler = new Handler();
    }

    @Override
    protected void notifyObservers() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                AndroidWatchModel.super.notifyObservers();
            }
        });
    }
}
