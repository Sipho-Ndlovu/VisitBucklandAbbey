package com.example.visitbucklandabbeyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class attractionsAdapter extends RecyclerView.Adapter<attractionsAdapter.ViewHolder> {
    private final AttractionsRecyclerViewInterface attractionsRecyclerViewInterface;

    LayoutInflater inflater;
    List<Attraction> Attractions;
    private Context ctx;

    public attractionsAdapter(Context ctx, List<Attraction> Attractions, AttractionsRecyclerViewInterface attractionsRecyclerViewInterface) {
        this.inflater = LayoutInflater.from(ctx);
        this.Attractions = Attractions;
        this.attractionsRecyclerViewInterface = attractionsRecyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.attractions_page_list_layout, parent, false);
        return new ViewHolder(view, attractionsRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(Attractions.get(position).getName());
    }

    @Override
    public int getItemCount() { return Attractions.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView, AttractionsRecyclerViewInterface attractionsRecyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.Name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (attractionsAdapter.this.attractionsRecyclerViewInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            attractionsAdapter.this.attractionsRecyclerViewInterface.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
