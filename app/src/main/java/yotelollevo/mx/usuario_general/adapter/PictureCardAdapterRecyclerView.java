package yotelollevo.mx.usuario_general.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.model.Picture;

public class PictureCardAdapterRecyclerView extends RecyclerView.Adapter<PictureCardAdapterRecyclerView.PictureCardViewHolder> {

    private ArrayList<Picture> aPicture;
    private Context aContext;

    public PictureCardAdapterRecyclerView(Context context, ArrayList<Picture> pictureArrayList) {
        aPicture = pictureArrayList;
        aContext = context;
    }

    @NonNull
    @Override
    public PictureCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(aContext).inflate(R.layout.picture_publicidad, parent, false);
        return new PictureCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureCardViewHolder holder, int position) {
        Picture picture = aPicture.get(position);

        String stringIma = picture.getImagen();
        String stringTit = picture.getTitulo();
        String stringSub = picture.getSubtitulo();

        holder.titulo.setText(stringTit);
        holder.subtitulo.setText(stringSub);
        Picasso.get().load(stringIma).fit().centerInside().into(holder.imagenCard);

    }

    @Override
    public int getItemCount() {
        return aPicture.size();
    }

    class PictureCardViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenCard;
        TextView titulo, subtitulo;

        PictureCardViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenCard = (ImageView) itemView.findViewById(R.id.idCarrusel);
            titulo = (TextView) itemView.findViewById(R.id.idTitulo);
            subtitulo = (TextView) itemView.findViewById(R.id.idDescripcion);
        }
    }
}
