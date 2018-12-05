package yotelollevo.mx.usuario_general;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.util.Timer;
import java.util.TimerTask;

import yotelollevo.mx.R;

public class InfoActivity extends AppCompatActivity {

    private me.biubiubiu.justifytext.library.JustifyTextView texto;
    private ImageView imagen;
    private ProgressBar progressBar;
    private Handler handler;
    private Runnable runnable;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        //texto = (me.biubiubiu.justifytext.library.JustifyTextView) findViewById(R.id.infoText);
        //imagen = (ImageView) findViewById(R.id.vectorImage);

        progressBar = (ProgressBar) findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                timer.cancel();
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 10000, 1000);
    }
}
