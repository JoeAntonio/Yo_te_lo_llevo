package yotelollevo.mx.usuario_general.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import yotelollevo.mx.MainActivity;
import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.InfoActivity;
import yotelollevo.mx.usuario_general.LoginActivity;
import yotelollevo.mx.usuario_general.adapter.PictureCardAdapterRecyclerView;
import yotelollevo.mx.usuario_general.adapter.TopCardAdapterRecyclerView;
import yotelollevo.mx.usuario_general.model.Picture;
import yotelollevo.mx.usuario_general.model.Top;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment implements TopCardAdapterRecyclerView.OnItemClickListener {

    private RecyclerView recyclerPictureCard, recyclerTopCard;
    private TopCardAdapterRecyclerView topCardAdapterRecyclerView;
    private ArrayList<Top> aTop;
    private PictureCardAdapterRecyclerView pictureCardAdapterRecyclerView;
    private ArrayList<Picture> aPicture;

    public static final String EXTRA_IMAGEN = "imagen";
    public static final String EXTRA_RESUMEN = "resumen";
    public static final String EXTRA_DESCRIPCION = "descripcion";

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

        recyclerPictureCard.setHasFixedSize(true);
        recyclerPictureCard.setLayoutManager(new LinearLayoutManager(getActivity()));

        aTop = new ArrayList<>();

        aPicture = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());

        obtenerTop();
        obtenerPicture();

        return view;
    }

    private void obtenerPicture() {
        // Se comprueba la conexión a Internet.
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Si hay conexión a Internet la variable hayConexion es verdadera.
        boolean hayConexion = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

        // Si hay conexión a Internet se carga la url.
        if (hayConexion) {
            String url = "http://yotelollevo.mx/webservices/Controller/advert.php?f=getAdverts";

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(0);

                                JSONArray jsonArray = response.getJSONArray("adverts");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    progressBar.setVisibility(View.GONE);

                                    JSONObject hit = jsonArray.getJSONObject(i);

                                    String imagen = hit.getString("img");
                                    String titulo = hit.getString("title");
                                    String subtitulo = hit.getString("desc");

                                    aPicture.add(new Picture(imagen,titulo,subtitulo));
                                }

                                pictureCardAdapterRecyclerView = new PictureCardAdapterRecyclerView(getActivity(), aPicture);
                                recyclerPictureCard.setAdapter(pictureCardAdapterRecyclerView);

                                LinearLayoutManager linearPictures = new LinearLayoutManager(getActivity());
                                linearPictures.setOrientation(LinearLayoutManager.HORIZONTAL);
                                recyclerPictureCard.setLayoutManager(linearPictures);

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

    private void obtenerTop() {
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
                                    String resumen = hit.getString("name");
                                    String descripcion = hit.getString("desc");

                                    aTop.add(new Top(imagen, resumen, descripcion));
                                }

                                topCardAdapterRecyclerView = new TopCardAdapterRecyclerView(getActivity(), aTop);
                                recyclerTopCard.setAdapter(topCardAdapterRecyclerView);
                                topCardAdapterRecyclerView.setOnItemClickListener(InicioFragment.this);

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

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), InfoActivity.class);

        Top clickedItem = aTop.get(position);

        detailIntent.putExtra(EXTRA_IMAGEN, clickedItem.getTop());
        detailIntent.putExtra(EXTRA_RESUMEN, clickedItem.getResumen());
        detailIntent.putExtra(EXTRA_DESCRIPCION, clickedItem.getDescripcion());

        startActivity(detailIntent);
    }

}