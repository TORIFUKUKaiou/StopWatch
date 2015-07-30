package jp.torifuku.stopwatch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final AndroidWatchModel watchModel;
    private final Observer observer;
    private TextView textView;
    private Button button;
    private boolean running;

    public MainActivityFragment() {
        super();
        watchModel = new AndroidWatchModel();
        observer = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                if (data instanceof Long) {
                    Long time = (Long) data;
                    showTime(time);
                }
            }
        };
        watchModel.addObserver(observer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) getView().findViewById(R.id.textView);
        showTime(0);
        button = (Button) getView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = watchModel.startOrStop();
                button.setText(running ? R.string.stop : R.string.start);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        watchModel.deleteObserver(observer);
        if (running) {
            watchModel.startOrStop();
        }
    }

    private void showTime(long time) {
        if (textView == null) {
            return;
        }
        textView.setText(represent(time));
    }

    private String represent(long time) {
        return String.format("%03d.%03d", (time / 1000), (time % 1000));
    }
}
