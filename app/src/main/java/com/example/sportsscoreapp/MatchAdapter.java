package com.example.sportsscoreapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private Context context;
    private List<MatchModel> matchList;

    public MatchAdapter(Context context, List<MatchModel> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_match, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        MatchModel m = matchList.get(position);

        holder.team1.setText(m.team1);
        holder.team2.setText(m.team2);
        holder.score.setText(m.score);
        holder.status.setText(m.status);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView team1, team2, score, status;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);

            team1 = itemView.findViewById(R.id.text_team1);
            team2 = itemView.findViewById(R.id.text_team2);
            score = itemView.findViewById(R.id.text_score);
            status = itemView.findViewById(R.id.text_status);
        }
    }
}
