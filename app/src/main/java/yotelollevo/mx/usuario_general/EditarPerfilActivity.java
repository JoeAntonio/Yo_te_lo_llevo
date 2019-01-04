package yotelollevo.mx.usuario_general;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.fragments.PerfilFragment;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nombres, apellidos, fecha, telefono;
    private TextView eliminar;
    private EditText pass, confpass;
    private Button guardar;
    private Button guardarCuenta;
    private AlertDialog.Builder alerta;

    private RequestQueue conexionServidor;
    private StringRequest update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editarperfil_activity);
        showToolbar(getResources().getString(R.string.toolbar_editar), true);

        nombres = (EditText) findViewById(R.id.editar_nombre);
        apellidos = (EditText) findViewById(R.id.editar_apellido);
        fecha = (EditText) findViewById(R.id.editar_fecha);
        telefono = (EditText) findViewById(R.id.editar_telefono);
        guardar = (Button) findViewById(R.id.butt_guardarcambios);
        pass = (EditText) findViewById(R.id.editar_pass);
        confpass = (EditText) findViewById(R.id.editar_confpass);
        guardarCuenta = (Button) findViewById(R.id.butt_guardarcuenta);
        eliminar = (TextView) findViewById(R.id.eliminar);

        alerta = new AlertDialog.Builder(this);

        CargarDatos();

        guardar.setOnClickListener(this);
        guardarCuenta.setOnClickListener(this);
        eliminar.setOnClickListener(this);

        fecha.setKeyListener(null);
        fecha.setOnClickListener(this);
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Calendario(fecha, EditarPerfilActivity.this, "EditarPerfilActivity");
                }
            }
        });

        conexionServidor = Volley.newRequestQueue(EditarPerfilActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.butt_guardarcambios:
                //Acciones del click del botón del update.
                String urlA = "http://yotelollevo.mx/webservices/Controller/person.php?f=update";

                //Tipo de envío (POST, GET), URL, En caso de respuesta, En caso de error.
                update = new StringRequest(Request.Method.POST, urlA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //En caso de conexión success.
                            JSONObject objetoRespuesta = new JSONObject(response);

                            if (objetoRespuesta.getInt("code") == 500) {
                                //error.
                                //En caso de error de servidor.
                                Toast.makeText(EditarPerfilActivity.this, "error en el servidor, intentelo más tarde", Toast.LENGTH_SHORT).show();

                            } else if (objetoRespuesta.getInt("code") == 200) {
                                //correcta.
                                SharedPreferences preferences = getSharedPreferences("yotelollevo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("nombres", nombres.getText().toString());
                                editor.putString("apellidos", apellidos.getText().toString());
                                editor.putString("fecha", fecha.getText().toString());
                                editor.putString("telefono", telefono.getText().toString());
                                editor.commit();

                                Toast.makeText(EditarPerfilActivity.this, "cambio realizado", Toast.LENGTH_SHORT).show();

                                //Intent.
                                Intent intent = new Intent(EditarPerfilActivity.this, MainActivity.class);
                                startActivity(intent);
                                EditarPerfilActivity.this.finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //En caso de error de conexión.
                        Toast.makeText(EditarPerfilActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                    }
                }){
                    //Variables a mandar.
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map variables = new HashMap();
                        variables.put("name", nombres.getText().toString());
                        variables.put("lastName", apellidos.getText().toString());
                        variables.put("birthday", fecha.getText().toString());
                        variables.put("phone", telefono.getText().toString());
                        SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                        variables.put("id", espacio.getString("id", null));
                        return variables;
                    }
                };
                //Agregamos la petición al servidor.
                conexionServidor.add(update);
                break;

            case R.id.butt_guardarcuenta:
                if (Validacion()) {
                    //Acciones del click del botón del update.
                    String urlB = "http://yotelollevo.mx/webservices/Controller/user.php?f=update";
                    //Tipo de envío (POST, GET), URL, En caso de respuesta, En caso de error.
                    update = new StringRequest(Request.Method.POST, urlB, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                //En caso de conexión success.
                                JSONObject objetoRespuesta = new JSONObject(response);

                                if (objetoRespuesta.getInt("code") == 500) {
                                    //error.
                                    //En caso de error de servidor.
                                    Toast.makeText(EditarPerfilActivity.this, "error en el servidor, intentelo más tarde", Toast.LENGTH_SHORT).show();

                                } else if (objetoRespuesta.getInt("code") == 200) {
                                    //correcta.
                                    Toast.makeText(EditarPerfilActivity.this, "cambio realizado", Toast.LENGTH_SHORT).show();
                                    //Intent.
                                    Intent intent = new Intent(EditarPerfilActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    EditarPerfilActivity.this.finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //En caso de error de conexión.
                            Toast.makeText(EditarPerfilActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        //Variables a mandar.
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map variables = new HashMap();
                            variables.put("password", pass.getText().toString());
                            SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                            variables.put("email", espacio.getString("email", null));
                            return variables;
                        }
                    };
                    //Agregamos la petición al servidor.
                    conexionServidor.add(update);
                }
                break;

            case R.id.editar_fecha:
                Calendario(fecha, EditarPerfilActivity.this, "EditarPerfilActivity");
                break;

            case R.id.eliminar:

                alerta.setTitle("Eliminar cuenta");
                alerta.setMessage("¿Desea eliminar su cuenta?");
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //En caso del click del botón cancelar.
                        dialog.dismiss();
                    }
                });
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //En caso del click del botón aceptar.
                        String urlC = "http://yotelollevo.mx/webservices/Controller/user.php?f=delete";
                        update = new StringRequest(Request.Method.POST, urlC, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //En caso de conexión success.
                                    JSONObject objetoRespuesta = new JSONObject(response);

                                    if (objetoRespuesta.getInt("code") == 500) {
                                        //error.
                                        Toast.makeText(EditarPerfilActivity.this, "error en el servidor, intentelo más tarde", Toast.LENGTH_SHORT).show();

                                    } else if (objetoRespuesta.getInt("code") == 200) {
                                        //correcta.
                                        Toast.makeText(EditarPerfilActivity.this, "¡cuenta eliminada!", Toast.LENGTH_SHORT).show();

                                        SharedPreferences preferences = getSharedPreferences("yotelollevo", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();

                                        editor.putString("email", null);
                                        editor.commit();

                                        Intent intent = new Intent(EditarPerfilActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        EditarPerfilActivity.this.finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //En caso de error de conexión.
                                Toast.makeText(EditarPerfilActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            //Variables a mandar.
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map variables = new HashMap();

                                SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                                variables.put("email", espacio.getString("email", null));
                                return variables;
                            }
                        };
                        //Agregamos la petición al servidor.
                        conexionServidor.add(update);
                    }
                });
                alerta.show();
                break;
        }
    }

    public void CargarDatos() {
        SharedPreferences espacio = this.getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
        String email = espacio.getString("email", null);
        String s_nombre = espacio.getString("nombres", null);
        String s_apellido = espacio.getString("apellidos", null);
        String s_fecha = espacio.getString("fecha", null);
        String s_telefono = espacio.getString("telefono", null);

        nombres.setText(s_nombre);
        apellidos.setText(s_apellido);
        fecha.setText(s_fecha);
        telefono.setText(s_telefono);
    }

    public boolean Validacion() {
        if (pass.getText().toString().isEmpty()) {
            pass.setError("correo no valido");
            pass.requestFocus();
            return false;
        } else if (confpass.getText().toString().isEmpty()) {
            confpass.setError("campo obligatrio");
            confpass.requestFocus();
            return false;
        } else if (!pass.getText().toString().equals(confpass.getText().toString())) {
            confpass.setError("las contraseñas no coinciden");
            confpass.requestFocus();
            return false;
        }
        return true;
    }

    public void Calendario(final EditText txt, final Context activity, final String nombreActividad) {

        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                activity,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        Log.d(nombreActividad, "onDateSet: AAAA-MM-DD: "+year+"/"+month+"/"+dayOfMonth);
                        String fecha = year+"-"+month+"-"+dayOfMonth;
                        txt.setText(fecha);
                    }
                }),
                anio,
                mes,
                dia
        );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

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
        Intent intent = new Intent(EditarPerfilActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}