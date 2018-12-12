package yotelollevo.mx.usuario_general;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import yotelollevo.mx.R;

import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_DESCRIPCION;
import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_IMAGEN;
import static yotelollevo.mx.usuario_general.fragments.InicioFragment.EXTRA_RESUMEN;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private me.biubiubiu.justifytext.library.JustifyTextView infoR, infoD;
    private ImageView vector;
    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        showToolbar(getResources().getString(R.string.toolbar_servicio), true);

        Intent intent = getIntent();

        String imagen = intent.getStringExtra(EXTRA_IMAGEN);
        String resumen = intent.getStringExtra(EXTRA_RESUMEN);
        String descripcion = intent.getStringExtra(EXTRA_DESCRIPCION);

        infoR = (me.biubiubiu.justifytext.library.JustifyTextView) findViewById(R.id.info_resumen);
        infoD = (me.biubiubiu.justifytext.library.JustifyTextView) findViewById(R.id.info_detalle);
        vector = (ImageView) findViewById(R.id.vectorImage);
        boton = (Button) findViewById(R.id.butt_siguiente);

        boton.setOnClickListener(this);

        Picasso.get().load(imagen).fit().centerInside().into(vector);
        infoR.setText(resumen);
        infoD.setText(descripcion);
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
}
