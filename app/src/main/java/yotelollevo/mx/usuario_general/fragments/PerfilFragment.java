package yotelollevo.mx.usuario_general.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;
import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.EditarPerfilActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    private Button editar;
    private TextView nom, usuario, nombres, apellidos, fecha, telefono, orden;

    private RequestQueue conexionServidor;
    private StringRequest peticion;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.perfil_fragment, container, false);

        editar = (Button) view.findViewById(R.id.butt_editar);
        nom = (TextView) view.findViewById(R.id.perf_nombre);
        usuario = (TextView) view.findViewById(R.id.perf_usuario);
        nombres = (TextView) view.findViewById(R.id.dato_nombre);
        apellidos = (TextView) view.findViewById(R.id.dato_apellido);
        fecha = (TextView) view.findViewById(R.id.dato_fecha);
        telefono = (TextView) view.findViewById(R.id.dato_telefono);
        orden = (TextView) view.findViewById(R.id.dato_ordenes);

        editar.setOnClickListener(this);

        conexionServidor = Volley.newRequestQueue(getActivity());

        ObtenerDatos();
        cargarDatos();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.butt_editar:
                Intent intent = new Intent(this.getActivity(), EditarPerfilActivity.class);
                startActivity(intent);
                break;
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
                        SharedPreferences espacio = getActivity().getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = espacio.edit();

                        editor.putString("id", datosUsuario.getString("id"));
                        editor.putString("nombres", datosUsuario.getString("name"));
                        editor.putString("apellidos", datosUsuario.getString("lastName"));
                        editor.putString("fecha", datosUsuario.getString("birthday"));
                        editor.putString("telefono", datosUsuario.getString("phone"));
                        editor.putString("email", datosUsuario.getString("email"));
                        editor.putString("servicio", datosUsuario.getString("numService"));
                        editor.commit();

                        cargarDatos();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    cargarDatos();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "no hay conexión a internet", Toast.LENGTH_SHORT).show();
                cargarDatos();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                SharedPreferences espacio = getActivity().getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
                String email = espacio.getString("email", null);
                map.put("email", email);
                return map;
            }
        };

        conexionServidor.add(peticion);
    }

    private void cargarDatos() {
        SharedPreferences preferences = getActivity().getSharedPreferences("yotelollevo", Context.MODE_PRIVATE);
        String stringN = preferences.getString("nombres", null);
        String stringA = preferences.getString("apellidos", null);
        String stringF = preferences.getString("fecha", null);
        String stringT = preferences.getString("telefono", null);
        String stringU = preferences.getString("email", null);
        String stringO = preferences.getString("servicio", null);

        nombres.setText(stringN);
        apellidos.setText(stringA);
        fecha.setText(stringF);
        telefono.setText(stringT);
        usuario.setText(stringU);
        nom.setText(stringN);
        orden.setText(stringO);
    }

}
