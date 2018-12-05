package yotelollevo.mx.usuario_general.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.LoginActivity;
import yotelollevo.mx.usuario_general.adapter.PictureCardAdapterRecyclerView;
import yotelollevo.mx.usuario_general.adapter.TopCardAdapterRecyclerView;
import yotelollevo.mx.usuario_general.model.Picture;
import yotelollevo.mx.usuario_general.model.Top;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerPictureCard, recyclerTopCard;

    private TopCardAdapterRecyclerView topCardAdapterRecyclerView;
    private ArrayList<Top> aTop;

    private ProgressBar progressBar;

    private RequestQueue mRequestQueue;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inicio_fragment, container, false);

        recyclerPictureCard = (RecyclerView) view.findViewById(R.id.cardRecycler);
        recyclerTopCard = (RecyclerView) view.findViewById(R.id.topRecycler);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setMax(10);

        recyclerTopCard.setHasFixedSize(true);
        recyclerTopCard.setLayoutManager(new LinearLayoutManager(getActivity()));

        aTop = new ArrayList<>();

        //Pictures.
        LinearLayoutManager linearPictures = new LinearLayoutManager(getActivity());
        linearPictures.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerPictureCard.setLayoutManager(linearPictures);

        PictureCardAdapterRecyclerView pictureCardAdapterRecyclerView =
                new PictureCardAdapterRecyclerView(buidPictures(), R.layout.picture_cardview, getActivity());
        recyclerPictureCard.setAdapter(pictureCardAdapterRecyclerView);

        mRequestQueue = Volley.newRequestQueue(getActivity());
        obtenerTops();

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    private void obtenerTops() {
        // Se comprueba la conexión a Internet.
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Si hay conexión a Internet la variable hayConexion es verdadera.
        boolean hayConexion = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

        // Si hay conexión a Internet se carga la url.
        if (hayConexion) {
            String url = "http://yotelollevo.mx/webservices/Controller/typeService.php?f=getTypeServices";

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(0);

                                JSONArray jsonArray = response.getJSONArray("typeServices");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    progressBar.setVisibility(View.GONE);

                                    JSONObject hit = jsonArray.getJSONObject(i);

                                    String imagen = hit.getString("img");

                                    aTop.add(new Top(imagen));
                                }

                                topCardAdapterRecyclerView = new TopCardAdapterRecyclerView(getActivity(), aTop);
                                recyclerTopCard.setAdapter(topCardAdapterRecyclerView);
                                recyclerTopCard.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            //Agregamos la petición al servidor.
            mRequestQueue.add(request);

        } else {
            //si no hay conexión a Internet se carga el mensaje de error.
            Toast.makeText(getActivity(),"No se puede conectar a Internet", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Picture> buidPictures() {
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture(R.drawable.tiendas_comerciales, "Ahorra tiempo....", "Yo te lo llevo"));
        pictures.add(new Picture(R.drawable.envios, "Novedades...", "Yo te lo llevo"));
        pictures.add(new Picture(R.drawable.alimentos, "Ahorra tiempo....", "Yo te lo llevo"));
        pictures.add(new Picture(R.drawable.mudanza, "Novedades...", "Yo te lo llevo"));
        return pictures;
    }

}