package com.example.sportsscoreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFragment extends Fragment {

    private RecyclerView recyclerUpcoming;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private ArrayList<MatchModel> upcomingList = new ArrayList<>();
    private MatchAdapter matchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        recyclerUpcoming = view.findViewById(R.id.recyclerUpcoming);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        recyclerUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));

        matchAdapter = new MatchAdapter(getContext(), upcomingList);
        recyclerUpcoming.setAdapter(matchAdapter);

        fetchMatches();
        return view;
    }

    public void fetchMatches() {
        progressBar.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);
        upcomingList.clear();
        matchAdapter.notifyDataSetChanged();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Map<String, Object>> call = apiService.getAllMatches(Constants.API_KEY, 0);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    parseUpcomingMatches(response.body());
                } else {
                    showEmptyMessage("Error loading matches.");
                    Log.e("UPCOMING_API_ERROR", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showEmptyMessage("Network error ⚠️");
            }
        });
    }

    private void parseUpcomingMatches(Map<String, Object> data) {

        upcomingList.clear();

        try {
            List<Map<String, Object>> matches = (List<Map<String, Object>>) data.get("data");

            if (matches != null) {

                for (Map<String, Object> match : matches) {

                    String rawStatus = match.get("status") != null ? match.get("status").toString() : "";
                    String status = rawStatus.toLowerCase();

                    // COMPLETED MATCH STATUS
                    boolean isCompleted =
                            status.contains("won") ||
                                    status.contains("finished") ||
                                    status.contains("result") ||
                                    status.contains("draw") ||
                                    status.contains("stumps");

                    // LIVE MATCH STATUS
                    boolean isLive =
                            status.contains("live") ||
                                    status.contains("in progress") ||
                                    status.contains("progress") ||
                                    status.contains("running");

                    // UPCOMING STATUS — extracted from real CricAPI data
                    boolean isUpcoming =
                            status.contains("not started") ||
                                    status.contains("scheduled") ||
                                    status.contains("fixture") ||
                                    status.contains("yet to") ||
                                    status.contains("starts") ||
                                    status.contains("to be played") ||
                                    status.contains("match not started") ||
                                    status.contains("match scheduled");

                    // APPLY FILTER
                    if (isUpcoming && !isLive && !isCompleted) {

                        String name = match.get("name") != null ? match.get("name").toString() : "Match";
                        String type = match.get("matchType") != null ? match.get("matchType").toString() : "Match";

                        String[] parts = name.split(" vs ");
                        String t1 = parts.length > 0 ? parts[0].trim() : "Team 1";
                        String t2 = parts.length > 1 ? parts[1].trim() : "Team 2";

                        upcomingList.add(new MatchModel(t1, t2, type, rawStatus));
                    }
                }
            }

        } catch (Exception e) {
            Log.e("UPCOMING_PARSE_ERROR", e.getMessage());
        }

        if (upcomingList.isEmpty()) {
            showEmptyMessage("No upcoming matches found 🔍");
        } else {
            txtEmpty.setVisibility(View.GONE);
            matchAdapter.notifyDataSetChanged();
        }
    }

    private void showEmptyMessage(String message) {
        txtEmpty.setVisibility(View.VISIBLE);
        txtEmpty.setText(message);
    }
}
