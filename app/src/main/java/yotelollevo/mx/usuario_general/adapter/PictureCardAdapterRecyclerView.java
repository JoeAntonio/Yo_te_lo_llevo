package yotelollevo.mx.usuario_general.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.model.Picture;

public class PictureCardAdapterRecyclerView extends RecyclerView.Adapter<PictureCardAdapterRecyclerView.PictureCardViewHolder> {

    private ArrayList<Picture> pictureArrayList;
    private int resource;
    private Activity activity;

    public PictureCardAdapterRecyclerView(ArrayList<Picture> pictureArrayList, int resource, Activity activity) {
        this.pictureArrayList = pictureArrayList;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PictureCardAdapterRecyclerView.PictureCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new PictureCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureCardAdapterRecyclerView.PictureCardViewHolder pictureCardViewHolder, int i) {
        final Picture picture = pictureArrayList.get(i);
        pictureCardViewHolder.titulo.setText(picture.getTitulo());
        pictureCardViewHolder.subtitulo.setText(picture.getSubtitulo());
        pictureCardViewHolder.imagenCard.setImageResource(picture.getImagen());

        //Se ejecuta la imagen para ingresar a un nuevo Activity.
    }

    @Override
    public int getItemCount() {
        return pictureArrayList.size();
    }

    public class PictureCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagenCard;
        private TextView titulo, subtitulo;

        public PictureCardViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenCard = (ImageView) itemView.findViewById(R.id.idCarrusel);
            titulo = (TextView) itemView.findViewById(R.id.idTitulo);
            subtitulo = (TextView) itemView.findViewById(R.id.idDescripcion);
        }
    }
}
