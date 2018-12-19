package yotelollevo.mx.usuario_general.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import yotelollevo.mx.R;
import yotelollevo.mx.usuario_general.model.Historial;

public class HistorialCardAdapterRecyclerView extends RecyclerView.Adapter<HistorialCardAdapterRecyclerView.HistorialViewHolder> {

    private ArrayList<Historial> aHistorial;
    private Context aContext;

    public HistorialCardAdapterRecyclerView(Context context, ArrayList<Historial> historialArrayList) {
        aHistorial = historialArrayList;
        aContext = context;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(aContext).inflate(R.layout.cardview_historial, parent, false);
        return new HistorialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        Historial historial = aHistorial.get(position);

        String stringNom = historial.getNombres();
        String stringFec = historial.getFecha();
        String stringDet = historial.getDetalle();
        String stringOrd = historial.getOrdenSer();

        holder.Nombres.setText(stringNom);
        holder.Fecha.setText(stringFec);
        holder.Detalle.setText(stringDet);
        holder.Orden.setText(stringOrd);
    }

    @Override
    public int getItemCount() {
        return aHistorial.size();
    }

    class HistorialViewHolder extends RecyclerView.ViewHolder {

        TextView Nombres, Fecha, Detalle, Orden;

        HistorialViewHolder(@NonNull View itemView) {
            super(itemView);

            Nombres = (TextView) itemView.findViewById(R.id.historial_nombre);
            Fecha = (TextView) itemView.findViewById(R.id.historial_fecha);
            Detalle = (TextView) itemView.findViewById(R.id.historial_detalle);
            Orden = (TextView) itemView.findViewById(R.id.historial_ordenServicio);
        }
    }
}