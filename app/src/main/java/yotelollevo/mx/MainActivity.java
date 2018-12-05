package yotelollevo.mx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.Objects;

import yotelollevo.mx.usuario_general.InfoActivity;
import yotelollevo.mx.usuario_general.LoginActivity;
import yotelollevo.mx.usuario_general.adapter.ViewPagerAdapter;
import yotelollevo.mx.usuario_general.fragments.ContactoFragment;
import yotelollevo.mx.usuario_general.fragments.InicioFragment;
import yotelollevo.mx.usuario_general.fragments.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int[]tabIcons={R.drawable.ic_inicio,R.drawable.ic_perfil, R.drawable.ic_mensaje};

    final String TAG = this.getClass().getName();

    boolean twice;

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
                iconColor(tab, "#E0E0E0");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                Intent i = new Intent(this, InfoActivity.class);
                startActivity(i);
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
}
