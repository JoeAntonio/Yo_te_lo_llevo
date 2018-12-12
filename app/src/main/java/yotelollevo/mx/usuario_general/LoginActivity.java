package yotelollevo.mx.usuario_general;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usuario, clave;
    private TextView subrayado;
    private Button acceder;

    private RequestQueue conexionServidor;
    private StringRequest login;

    final String TAG = this.getClass().getName();

    boolean twice;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
        String email = espacio.getString("email", null);
        if(email != null){
            //Intent
            LoginActivity.this.finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        usuario = (EditText) findViewById(R.id.login_usuario);
        clave = (EditText) findViewById(R.id.login_pass);
        acceder = findViewById(R.id.butt_sesion);
        subrayado = (TextView) findViewById(R.id.textView_crear);

        //Manda a llamar al texto subrayado.
        subrayado.setText(Html.fromHtml(getResources().getString(R.string.crear)));

        dialog = new ProgressDialog(this);

        acceder.setOnClickListener(this);

        conexionServidor = Volley.newRequestQueue(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.butt_sesion:
                if (validacion()) {
                    dialog.setTitle("Cargando");
                    dialog.setMessage("Por favor espere");
                    dialog.setCancelable(false);
                    dialog.show();
                    //Acciones del click del botón de iniciar sesión.
                    String url = "http://yotelollevo.mx/webservices/Controller/user.php?f=login";
                    //Tipo de envío (POST, GET), URL, En caso de respuesta, En caso de error.
                    login = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //En caso de conexión success.
                            try {
                                JSONObject objetoRespuesta = new JSONObject(response);
                                if(objetoRespuesta.getInt("code") == 404){
                                    //Error en el login.
                                    Toast.makeText(LoginActivity.this, "usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }else if(objetoRespuesta.getInt("code") == 200){
                                    //Si es correcta.
                                    JSONObject datosUsuario = objetoRespuesta.getJSONObject("dataUser");
                                    SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = espacio.edit();

                                    editor.putString("email", usuario.getText().toString());
                                    editor.commit();
                                    //Intent.
                                    dialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            } catch (JSONException e) {
                                dialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //En caso de error de conexión.
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }){
                        //Variables a mandar.
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map variables = new HashMap();
                            variables.put("email_phone", usuario.getText().toString());
                            variables.put("password", clave.getText().toString());
                            return variables;
                        }
                    };
                    //Agregamos la petición al servidor.
                    conexionServidor.add(login);
                }
                break;

            case R.id.textView_crear:
                Intent intent = new Intent(LoginActivity.this, CrearCuentaActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean validacion() {
        if  (usuario.getText().toString().isEmpty()) {
            usuario.setError("campo obligatorio");
            usuario.requestFocus();
            return false;
        } else if (clave.getText().toString().isEmpty()) {
            clave.setError("campo obligatorio");
            clave.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
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

        Toast.makeText(LoginActivity.this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
                Log.d(TAG, "twice: " + twice);
            }
        }, 3000);

    }
}
