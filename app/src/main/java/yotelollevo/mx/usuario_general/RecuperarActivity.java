package yotelollevo.mx.usuario_general;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;

public class RecuperarActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText correo;
    private Button enviar;

    private RequestQueue conexionServidor;
    private StringRequest insertar;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperar_activity);

        correo = (EditText) findViewById(R.id.login_correo);
        enviar = (Button) findViewById(R.id.login_enviar);

        enviar.setOnClickListener(this);

        dialog = new ProgressDialog(this);

        conexionServidor = Volley.newRequestQueue(RecuperarActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_enviar:
                dialog.setTitle("Cargando");
                dialog.setMessage("Por favor espere");
                dialog.setCancelable(false);
                dialog.show();
                //Acciones del click del botón de crear cuenta.
                String url = "http://yotelollevo.mx/webservices/Controller/user.php?f=restartPassword";

                //Tipo de envío (POST, GET), URL, En caso de respuesta, En caso de error.
                insertar = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject objetoRespuesta = new JSONObject(response);

                            if(objetoRespuesta.getInt("code") == 404){
                                //Error en la insersión de datos.
                                Toast.makeText(RecuperarActivity.this, "este correo no esta registrado", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else if(objetoRespuesta.getInt("code") == 200){
                                //Si es correcta.
                                Toast.makeText(RecuperarActivity.this, "¡mensaje enviado!", Toast.LENGTH_SHORT).show();

                                //Intent.
                                dialog.dismiss();
                                Intent intent = new Intent(RecuperarActivity.this, LoginActivity.class);
                                startActivity(intent);
                                RecuperarActivity.this.finish();
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
                        Toast.makeText(RecuperarActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                    }
                }){

                    //Variables a mandar.
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map variables = new HashMap();
                        variables.put("email", correo.getText().toString());
                        return variables;
                    }
                };

                //Agregamos la petición al servidor.
                conexionServidor.add(insertar);
                break;
        }
    }

}
