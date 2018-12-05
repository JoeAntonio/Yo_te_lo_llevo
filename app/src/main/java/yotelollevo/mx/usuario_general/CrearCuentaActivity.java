package yotelollevo.mx.usuario_general;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;

public class CrearCuentaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nombres, apellidos, fecha, telefono, usuario, pass, confpass;
    private Button guardar;

    private RequestQueue conexionServidor;
    private StringRequest insertar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearcuenta_activity);
        showToolbar(getResources().getString(R.string.toolbat_createaccount), true);

        nombres = (EditText) findViewById(R.id.idnombres);
        apellidos = (EditText) findViewById(R.id.idapellidos);
        fecha = (EditText) findViewById(R.id.idfecha);
        telefono = (EditText) findViewById(R.id.idtelefono);
        usuario = (EditText) findViewById(R.id.idusuario);
        pass = (EditText) findViewById(R.id.idpassword);
        confpass = (EditText) findViewById(R.id.idconfirmarpa);
        guardar = (Button) findViewById(R.id.butt_cuenta);

        guardar.setOnClickListener(this);

        fecha.setKeyListener(null);
        fecha.setOnClickListener(this);
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Calendario(fecha, CrearCuentaActivity.this, "CrearCuentaActivity");
                }
            }
        });

        conexionServidor = Volley.newRequestQueue(CrearCuentaActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.butt_cuenta:
                //Acciones del click del botón de crear cuenta.
                String url = "http://yotelollevo.mx/webservices/Controller/person.php?f=insert";

                //Tipo de envío (POST, GET), URL, En caso de respuesta, En caso de error.
                if (validacion()) {
                    insertar = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject objetoRespuesta = new JSONObject(response);

                                if(objetoRespuesta.getInt("code") == 404){
                                    //Error en la insersión de datos.
                                    Toast.makeText(CrearCuentaActivity.this, "este correo ya existe", Toast.LENGTH_SHORT).show();

                                } else if(objetoRespuesta.getInt("code") == 200){
                                    //Si es correcta.
                                    Toast.makeText(CrearCuentaActivity.this, "¡cuenta creada con exito!", Toast.LENGTH_SHORT).show();

                                    //Guardar correo.
                                    SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = espacio.edit();

                                    editor.putString("email", usuario.getText().toString());
                                    editor.commit();

                                    //Intent.
                                    Intent intent = new Intent(CrearCuentaActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    CrearCuentaActivity.this.finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //En caso de error de conexión.
                            Toast.makeText(CrearCuentaActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
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
                            variables.put("email", usuario.getText().toString());
                            variables.put("password", pass.getText().toString());
                            return variables;
                        }
                    };

                    //Agregamos la petición al servidor.
                    conexionServidor.add(insertar);
                }
                break;

            case R.id.idfecha:
                Calendario(fecha, CrearCuentaActivity.this, "CrearCuentaActivity");
                break;
        }
    }

    public boolean validacion() {
        if (nombres.getText().toString().isEmpty()) {
            nombres.setError("campo obligatorio");
            nombres.requestFocus();
            return false;

        }else if (apellidos.getText().toString().isEmpty()) {
            apellidos.setError("campo obligatorio");
            apellidos.requestFocus();
            return false;

        }else if (fecha.getText().toString().isEmpty()) {
            fecha.setError("campo obligatorio");
            fecha.requestFocus();
            return false;

        }else if (telefono.getText().toString().isEmpty()) {
            telefono.setError("campo obligatorio");
            telefono.requestFocus();
            return false;

        } else if (!validarCorreo(usuario.getText().toString())) {
            usuario.setError("correo no valido");
            return false;
        } else if (pass.getText().toString().isEmpty()) {
            pass.setError("campo obligatorio");
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

    //Se valida el correo.
    private boolean validarCorreo(String correo) {
        String EMAIL_PATTERN = "^[A-Za-z0-9\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(correo);
        return  matcher.matches();
    }

    public void Calendario(final EditText txt, final Context activity, final String nombreActividad) {
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}

