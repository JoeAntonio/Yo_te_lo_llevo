package yotelollevo.mx.usuario_general.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.model.Top;

public class TopCardAdapterRecyclerView extends RecyclerView.Adapter<TopCardAdapterRecyclerView.TopCardViewHolder> {

    private ArrayList<Top> aTop;
    private Context aContext;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public TopCardAdapterRecyclerView( Context context, ArrayList<Top> topArrayList) {
        aTop = topArrayList;
        aContext = context;
    }

    @NonNull
    @Override
    public TopCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(aContext).inflate(R.layout.picture_tops, parent, false);
        return new TopCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopCardViewHolder holder, int position) {
        Top top = aTop.get(position);

        String stringIma = top.getTop();

        Picasso.get().load(stringIma).fit().centerInside().into(holder.imagenCard);
    }

    @Override
    public int getItemCount() {
        return aTop.size();
    }

    class TopCardViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenCard;

        TopCardViewHolder(View itemView) {
            super(itemView);

            imagenCard = (ImageView) itemView.findViewById(R.id.id_top);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
