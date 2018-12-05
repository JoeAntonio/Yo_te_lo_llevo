package yotelollevo.mx.usuario_general.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import yotelollevo.mx.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactoFragment extends Fragment implements View.OnClickListener {

    private EditText asunto, nombre, telefono, mensaje;
    Button enviar;

    public ContactoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.contacto_fragment, container, false);

        asunto = (EditText) view.findViewById(R.id.cont_asunto);
        nombre = (EditText) view.findViewById(R.id.cont_nombre);
        telefono = (EditText) view.findViewById(R.id.cont_telefono);
        mensaje = (EditText) view.findViewById(R.id.cont_mensaje);
        enviar = (Button) view.findViewById(R.id.butt_enviar);

        enviar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.butt_enviar:
                if (Validacion()) {
                    Intent enviarCorreo = new Intent(Intent.ACTION_SEND);

                    //Se llenan los datos en el formulario del correo.
                    enviarCorreo.setType("plain/text");
                    enviarCorreo.putExtra(Intent.EXTRA_EMAIL, new String[]{"aplicaciones@impactosdigitales.com"});
                    enviarCorreo.putExtra(Intent.EXTRA_SUBJECT, asunto.getText().toString());
                    enviarCorreo.putExtra(Intent.EXTRA_TEXT,
                            "Nombre completo: " +nombre.getText().toString()+ '\n'
                                    + "Asunto: " +asunto.getText().toString()+ '\n'
                                    + "Teléfono: " +telefono.getText().toString() + '\n'
                                    + "Mensaje: " +mensaje.getText().toString()
                    );

                    try {
                        startActivity(Intent.createChooser(enviarCorreo,"enviar correo."));
                        Log.i("Finish sending email...", "");

                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this.getActivity(), "no hay ningun cliente de correo instalado", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(this.getActivity(), "no hay conexión a internet", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }

    public boolean Validacion() {
        if (asunto.getText().toString().isEmpty()) {
            asunto.setError("campo obligatorio");
            asunto.requestFocus();
            return false;

        } else if (nombre.getText().toString().isEmpty()) {
            nombre.setError("campo obligatorio");
            nombre.requestFocus();
            return false;

        } else if (telefono.getText().toString().isEmpty()) {
            telefono.setError("campo obligatorio");
            telefono.requestFocus();
            return false;

        } else if (mensaje.getText().toString().isEmpty()) {
            mensaje.setError("campo obligatorio");
            mensaje.requestFocus();
            return false;
        }
        return true;
    }

    //Limpia los campos de texto al reanudar la aplicación.
    @Override
    public void onResume() {
        super.onResume();
        asunto.setText("");
        nombre.setText("");
        telefono.setText("");
        mensaje.setText("");
    }
}
