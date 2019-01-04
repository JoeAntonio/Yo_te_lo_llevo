package yotelollevo.mx.usuario_general;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;

import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_DESCRIPCION;
import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_FONDO;
import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_IMAGEN;
import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_NOMBRE;
import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_RESUMEN;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView infoR, infoD;
    private ImageView imagen_vector;
    private Button boton;
    private LinearLayout fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        Intent intent = getIntent();

        String imagenV = intent.getStringExtra(EXTRA_IMAGEN);
        String resumen = intent.getStringExtra(EXTRA_RESUMEN);
        String descripcion = intent.getStringExtra(EXTRA_DESCRIPCION);
        String nombreI = intent.getStringExtra(EXTRA_NOMBRE);
        String back = intent.getStringExtra(EXTRA_FONDO);

        infoR = (TextView) findViewById(R.id.info_resumen);
        infoD = (TextView) findViewById(R.id.info_detalle);
        imagen_vector = (ImageView) findViewById(R.id.vectorImage);
        boton = (Button) findViewById(R.id.butt_siguiente);
        fondo = (LinearLayout) findViewById(R.id.info_fondo);


        showToolbar(nombreI, true);

        boton.setOnClickListener(this);

        Picasso.get().load(imagenV).fit().centerInside().into(imagen_vector);
        infoR.setText(resumen);
        infoD.setText(descripcion);
        fondo.setBackgroundColor(Color.parseColor(back));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butt_siguiente:
                Intent intent = new Intent(InfoActivity.this, OrdenActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //codigo adicional.
        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
