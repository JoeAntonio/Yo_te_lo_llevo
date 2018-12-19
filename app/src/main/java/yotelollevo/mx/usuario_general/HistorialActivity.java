package yotelollevo.mx.usuario_general;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.adapter.HistorialCardAdapterRecyclerView;
import yotelollevo.mx.usuario_general.model.Historial;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistorial;
    private HistorialCardAdapterRecyclerView historialCardAdapterRecyclerView;
    private ArrayList<Historial> aHistorial;

    private RequestQueue conexionServidor;
    private StringRequest historial;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_activity);
        showToolbar(getResources().getString(R.string.toolabar_historial), true);

        recyclerViewHistorial = (RecyclerView) findViewById(R.id.historialRecycler);

        recyclerViewHistorial.setHasFixedSize(true);
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setMax(10);

        aHistorial = new ArrayList<>();

        conexionServidor = Volley.newRequestQueue(HistorialActivity.this);
        obtenerHistorial();
    }

    private void obtenerHistorial() {
        String url = "http://yotelollevo.mx/webservices/Controller/service.php?f=getServices";

        historial = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject objetoRespuesta = new JSONObject(response);

                    if (objetoRespuesta.getInt("code") == 404) {

                        Toast.makeText(HistorialActivity.this, "Aún no has realizado ningún pedido", Toast.LENGTH_SHORT).show();

                    } else if (objetoRespuesta.getInt("code") == 200) {

                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(0);

                        JSONArray jsonArray = objetoRespuesta.getJSONArray("services");

                        int id = jsonArray.length();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            progressBar.setVisibility(View.GONE);

                            JSONObject hit = jsonArray.getJSONObject(i);

                            String nombre = hit.getString("name");
                            String fecha = hit.getString("date");
                            String detalle = hit.getString("detail");

                            aHistorial.add(new Historial(nombre,fecha,detalle,String.valueOf(id)));
                            id--;
                        }

                        historialCardAdapterRecyclerView = new HistorialCardAdapterRecyclerView(HistorialActivity.this, aHistorial);
                        recyclerViewHistorial.setAdapter(historialCardAdapterRecyclerView);

                        LinearLayoutManager linearPictures = new LinearLayoutManager(HistorialActivity.this);
                        linearPictures.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerViewHistorial.setLayoutManager(linearPictures);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(HistorialActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map variable = new HashMap();

                SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                String id = espacio.getString("id", null);

                variable.put("idPerson", id);

                return variable;
            }
        };

        conexionServidor.add(historial);
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
        Intent intent = new Intent(HistorialActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
