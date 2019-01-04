package yotelollevo.mx.usuario_general;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;

public class OrdenActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner servicios;
    private EditText nombre, mensaje, destinorigen;
    private TextView subrayado;
    private Button siguiente, llamada;
    private TextInputLayout textInputNombre, textInputMensaje, textInputDestino;
    private RelativeLayout relativeMapa;
    private TextView textoViaje;

    private String textoNombreRecibe;

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;

    private RequestQueue conexionServidor;
    private StringRequest insertar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orden_activity);
        showToolbar(getResources().getString(R.string.toolbar_orden), true);

        servicios = (Spinner) findViewById(R.id.spinner);
        nombre = (EditText) findViewById(R.id.orden_nombre);
        mensaje = (EditText) findViewById(R.id.orden_mensaje);
        siguiente = (Button) findViewById(R.id.orden_button);
        subrayado = (TextView) findViewById(R.id.orden_googleMaps);
        destinorigen = (EditText) findViewById(R.id.orden_indicaciones);
        textInputNombre = (TextInputLayout) findViewById(R.id.textInput_nombre);
        textInputMensaje = (TextInputLayout) findViewById(R.id.textInput_mensaje);
        textInputDestino = (TextInputLayout) findViewById(R.id.textInput_indicaciones);
        relativeMapa = (RelativeLayout) findViewById(R.id.relativeLayout);
        textoViaje = (TextView) findViewById(R.id.orden_textoLlamar);
        llamada = (Button) findViewById(R.id.orden_llamar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.combo_servicios, R.layout.support_simple_spinner_dropdown_item);

        servicios.setAdapter(adapter);

        servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        textInputNombre.setVisibility(View.VISIBLE);
                        textInputMensaje.setVisibility(View.VISIBLE);
                        textInputDestino.setVisibility(View.VISIBLE);
                        relativeMapa.setVisibility(View.VISIBLE);

                        textoViaje.setVisibility(View.GONE);
                        llamada.setVisibility(View.GONE);

                        textoNombreRecibe = null;
                        break;
                    case 1:
                        textInputNombre.setVisibility(View.VISIBLE);
                        textInputMensaje.setVisibility(View.VISIBLE);
                        textInputDestino.setVisibility(View.VISIBLE);
                        relativeMapa.setVisibility(View.VISIBLE);

                        textoViaje.setVisibility(View.GONE);
                        llamada.setVisibility(View.GONE);

                        textoNombreRecibe = null;
                        break;
                    case 2:
                        textInputNombre.setVisibility(View.VISIBLE);
                        textInputMensaje.setVisibility(View.VISIBLE);
                        textInputDestino.setVisibility(View.VISIBLE);
                        relativeMapa.setVisibility(View.VISIBLE);

                        textoViaje.setVisibility(View.GONE);
                        llamada.setVisibility(View.GONE);

                        textoNombreRecibe = null;
                        break;
                    case 3:
                        textInputNombre.setVisibility(View.VISIBLE);
                        textInputMensaje.setVisibility(View.VISIBLE);
                        textInputDestino.setVisibility(View.VISIBLE);
                        relativeMapa.setVisibility(View.VISIBLE);

                        textoViaje.setVisibility(View.GONE);
                        llamada.setVisibility(View.GONE);

                        textoNombreRecibe = null;
                        break;
                    case 4:
                        textInputNombre.setVisibility(View.GONE);
                        textInputMensaje.setVisibility(View.GONE);
                        textInputDestino.setVisibility(View.GONE);
                        relativeMapa.setVisibility(View.GONE);

                        textoViaje.setVisibility(View.VISIBLE);
                        llamada.setVisibility(View.VISIBLE);

                        siguiente.setVisibility(View.GONE);

                        textoNombreRecibe = null;
                        break;
                    case 5:
                        textInputNombre.setVisibility(View.VISIBLE);
                        textInputDestino.setVisibility(View.VISIBLE);
                        relativeMapa.setVisibility(View.VISIBLE);
                        siguiente.setVisibility(View.VISIBLE);

                        textInputMensaje.setVisibility(View.GONE);
                        textoViaje.setVisibility(View.GONE);
                        llamada.setVisibility(View.GONE);

                        textoNombreRecibe = "Servicio Ejecutivo";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        siguiente.setOnClickListener(this);
        subrayado.setOnClickListener(this);
        llamada.setOnClickListener(this);

        conexionServidor = Volley.newRequestQueue(OrdenActivity.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orden_button:
                //Acciones del click del botón de crear cuenta.
                String url = "http://yotelollevo.mx/webservices/Controller/service.php?f=insert";

                //valida los campos antes de enviar los parametros.
                if (validacion()) {
                    //Tipo de envío (POST, GET), URL, En caso de respuesta, En caso de error.
                    insertar = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject objetoRespuesta = new JSONObject(response);

                                if(objetoRespuesta.getInt("code") == 500){
                                    //error en el servidor.
                                    Toast.makeText(OrdenActivity.this, "error en el servidor, intentelo más tarde", Toast.LENGTH_SHORT).show();

                                } else if(objetoRespuesta.getInt("code") == 200){
                                    //parametros correctos.
                                    Toast.makeText(OrdenActivity.this, "¡orden realizada!", Toast.LENGTH_SHORT).show();

                                    //WhatsApp.
                                    openWhatsApp(
                                            "*Tipo de servicio:* " + servicios.getSelectedItem().toString() + '\n'  + '\n'
                                                    + "*A nombre de:* " + nombre.getText().toString() + '\n' + '\n'
                                                    + "*Detalle del pedido:* " + mensaje.getText().toString() + '\n' + '\n'
                                                    + "*Origen y destino:* " + destinorigen.getText().toString()
                                    );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //En caso de error de conexión.
                            Toast.makeText(OrdenActivity.this, "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        //Variables a mandar.
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map variables = new HashMap();

                            SharedPreferences espacio = getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                            String id = espacio.getString("id", null);

                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
                            Date todayDate = new Date();
                            String thisDate = currentDate.format(todayDate);

                            if(textoNombreRecibe == null)
                                variables.put("detail", mensaje.getText().toString());
                            else
                                variables.put("detail", textoNombreRecibe);

                            variables.put("namePerson", nombre.getText().toString());
                            //variables.put("detail", mensaje.getText().toString());
                            variables.put("nameTypeService", servicios.getSelectedItem().toString());
                            variables.put("date", thisDate);
                            variables.put("idPerson", id);
                            return variables;
                        }
                    };

                    //Agregamos la petición al servidor.
                    conexionServidor.add(insertar);
                }
                break;

            case R.id.orden_googleMaps:
                //Manda a llamar la app de Google Maps.
                PackageManager pm = getPackageManager();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this,"Puede ser que no tengas la aplicación instalada", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.orden_llamar:
                requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                Intent llamar = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4424790091"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED)
                    return;
                startActivity(llamar);
                break;
        }
    }

    public boolean validacion() {
        if (nombre.getText().toString().isEmpty()) {
            nombre.setError("campo obligatorio");
            nombre.requestFocus();
            return false;
        } else if (mensaje.getText().toString().isEmpty()) {
            mensaje.setError("campo obligatorio");
            mensaje.requestFocus();
            return false;
        } else if (destinorigen.getText().toString().isEmpty()) {
            destinorigen.setError("campo obligatorio");
            destinorigen.requestFocus();
            return false;
        }

        return true;
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
        Intent intent = new Intent(OrdenActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void openWhatsApp(String mensaje){
        try {
            String text = mensaje;// Replace with your message.

            String toNumber = "5214424790091"; // Replace with mobile phone number without +Sign or leading zeros.

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}