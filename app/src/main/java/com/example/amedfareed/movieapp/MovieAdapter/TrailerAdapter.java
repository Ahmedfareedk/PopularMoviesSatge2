package com.example.amedfareed.movieapp.MovieAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.model.MovieTrailer;
import com.example.amedfareed.movieapp.model.PopularMovie;

import java.util.List;

/**
 * Created by amedfareed on 09/05/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    List<MovieTrailer> trailerList;
    Context context;

    public TrailerAdapter(Context context, List<MovieTrailer> trailerList) {
        this.trailerList = trailerList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.trailer_card_view, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.trailerTitleTV.setText(trailerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        TextView trailerTitleTV;
        ImageView trailerMotto;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerMotto = itemView.findViewById(R.id.trailer_thumbnail);
            trailerTitleTV = itemView.findViewById(R.id.trailer_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String trailerId = trailerList.get(position).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerId));
                        intent.putExtra("trailer_id", trailerId);
                        context.startActivity(intent);
                    }
                }
            });
        }
}

}



