package com.example.visitbucklandabbeyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class adminAdapter extends RecyclerView.Adapter<adminAdapter.ViewHolder> {
    private final AdminRecyclerViewInterface adminRecyclerViewInterface;

    LayoutInflater inflater;
    List<Attraction> Attractions;
    private Context context;

    public adminAdapter(Context ctx, List<Attraction> Attractions, AdminRecyclerViewInterface adminRecyclerViewInterface) {
        this.inflater = LayoutInflater.from(ctx);
        this.Attractions = Attractions;
        this.adminRecyclerViewInterface = adminRecyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.attractions_list_layout, parent, false);
        return new ViewHolder(view, adminRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setText("ID: " + (Attractions.get(position).getId()));
        holder.facilities.setText("Facilities: " + Attractions.get(position).getFacilities());
        holder.name.setText(Attractions.get(position).getName());
        holder.openingTime.setText("Opens: " + Attractions.get(position).getOpeningTime());
        holder.ticketPrices.setText("Price: " + Attractions.get(position).getTicketPrices());
    }

    @Override
    public int getItemCount() {
        return Attractions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  id, name, facilities, openingTime, ticketPrices;
        Button btnDelete, btnCreate;

        public ViewHolder(@NonNull View itemView, AdminRecyclerViewInterface adminRecyclerViewInterface) {
            super(itemView);

            id = itemView.findViewById(R.id.Id);
            name = itemView.findViewById(R.id.Name);
            facilities = itemView.findViewById(R.id.Facilities);
            openingTime = itemView.findViewById(R.id.OpeningTime);
            ticketPrices = itemView.findViewById(R.id.TicketPrices);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (adminAdapter.this.adminRecyclerViewInterface != null){
                        int position = getAdapterPosition();
                        int id = Attractions.get(position).getId();

                        if (position != RecyclerView.NO_POSITION) {
                            adminAdapter.this.adminRecyclerViewInterface.onDeleteLongClick(position, id);
                        }
                    }
                    return true;
                }
            });
        }
    }

}
