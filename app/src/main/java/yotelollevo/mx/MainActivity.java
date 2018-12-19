package yotelollevo.mx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import yotelollevo.mx.usuario_general.LoginActivity;
import yotelollevo.mx.usuario_general.HistorialActivity;
import yotelollevo.mx.usuario_general.adapter.ViewPagerAdapter;
import yotelollevo.mx.usuario_general.fragments.ContactoFragment;
import yotelollevo.mx.usuario_general.fragments.InicioFragment;
import yotelollevo.mx.usuario_general.fragments.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int[]tabIcons={R.drawable.ic_inicio,R.drawable.ic_perfil, R.drawable.ic_mensaje};

    final String TAG = this.getClass().getName();

    boolean twice;

    private RequestQueue conexionServidor;
    private StringRequest peticion;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToolbar(getResources().getString(R.string.toolbar_inicio), false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        loadViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        TabIcons();
        iconColor(Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition())), "#FFA000");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                iconColor(tab, "#FFA000");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                iconColor(tab, "#000000");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        conexionServidor = Volley.newRequestQueue(this);
        ObtenerDatos();
    }

    private void iconColor(TabLayout.Tab tab, String color) {
        tab.getIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
    }

    private void loadViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InicioFragment());
        adapter.addFragment(new PerfilFragment());
        adapter.addFragment(new ContactoFragment());
        viewPager.setAdapter(adapter);
    }

    private void TabIcons() {
        for (int i=0; i<3; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    private InicioFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        InicioFragment fragment = new InicioFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_menu_toolbar, menu);
        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_historial:
                Intent historial = new Intent(MainActivity.this, HistorialActivity.class);
                startActivity(historial);
                return true;

            case R.id.action_settings_cerrarsesion:
                SharedPreferences preferences = getSharedPreferences("yotelollevo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", null);
                editor.putString("id", null);
                editor.putString("nombres", null);
                editor.putString("apellidos", null);
                editor.putString("fecha", null);
                editor.putString("telefono", null);
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        {
            Log.d(TAG, "click");
            if (twice == true) {
                Intent salir = new Intent(Intent.ACTION_MAIN);
                salir.addCategory(Intent.CATEGORY_HOME);
                salir.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(salir);
                finish();
                System.exit(0);
            }

            twice = true;
            Log.d(TAG, "twice: " + twice);

            Toast.makeText(MainActivity.this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    twice = false;
                    Log.d(TAG, "twice: " + twice);
                }
            }, 3000);

        }
    }

    public void ObtenerDatos() {
        String url = "http://yotelollevo.mx/webservices/Controller/person.php?f=getPerson";

        peticion = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objetoRespuesta = new JSONObject(response);
                    if(objetoRespuesta.getInt("code") == 404){
                        //error.

                    }else if(objetoRespuesta.getInt("code") == 200){
                        //correcta.
                        JSONObject datosUsuario = objetoRespuesta.getJSONObject("dataPerson");
                        SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = espacio.edit();

                        editor.putString("id", datosUsuario.getString("id"));
                        editor.putString("nombres", datosUsuario.getString("name"));
                        editor.putString("apellidos", datosUsuario.getString("lastName"));
                        editor.putString("fecha", datosUsuario.getString("birthday"));
                        editor.putString("telefono", datosUsuario.getString("phone"));
                        editor.putString("email", datosUsuario.getString("email"));
                        editor.commit();
                    }

                } catch (JSONException e){
                    e.printStackTrace();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                String email = espacio.getString("email", null);
                map.put("email", email);
                return map;
            }
        };

        conexionServidor.add(peticion);
    }
}
