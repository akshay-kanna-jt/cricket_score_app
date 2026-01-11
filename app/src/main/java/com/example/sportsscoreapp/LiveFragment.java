package com.example.sportsscoreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Fragment to display the list of live cricket matches.
 * Fetches data from the "currentMatches" endpoint.
 */
public class LiveFragment extends Fragment {

    private RecyclerView recyclerLive;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private ArrayList<MatchModel> liveList = new ArrayList<>();
    private MatchAdapter matchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment (fragment_live.xml)
        View view = inflater.inflate(R.layout.fragment_live, container, false);

        recyclerLive = view.findViewById(R.id.recyclerLive);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        // Setup RecyclerView
        recyclerLive.setLayoutManager(new LinearLayoutManager(getContext()));
        matchAdapter = new MatchAdapter(getContext(), liveList);
        recyclerLive.setAdapter(matchAdapter);

        // Initiate data fetch on creation
        fetchLiveMatches();
        return view;
    }

    /**
     * Public method callable from MainActivity to refresh data on button click.
     */
    public void fetchLiveMatches() {
        progressBar.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);
        liveList.clear();
        matchAdapter.notifyDataSetChanged();

        // Use the ApiService and ApiClient to create the network call
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Map<String, Object>> call = apiService.getLiveMatches(Constants.API_KEY);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    parseLiveMatches(response.body());
                } else {
                    showEmptyMessage("Error loading matches: Response failed or was empty.");
                    Log.e("LIVE_API_ERROR", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showEmptyMessage("Network error ⚠️ Check your internet and API key.");
                Log.e("LIVE_API_ERROR", "Network failure: " + t.getMessage());
            }
        });
    }

    /**
     * Parses the Map response structure for live matches.
     * @param data The API response body as a Map.
     */
    private void parseLiveMatches(Map<String, Object> data) {
        liveList.clear();
        try {
            // The live matches endpoint returns data directly under the "data" key as a List
            List<Map<String, Object>> matches = (List<Map<String, Object>>) data.get("data");

            if (matches != null) {
                for (Map<String, Object> match : matches) {
                    // Extract the match name and status
                    String name = match.get("name") != null ? match.get("name").toString() : "Match vs Unknown";
                    String status = match.get("status") != null ? match.get("status").toString() : "Match in Progress";

                    // The live match API often includes score/details in 'score' array (optional)
                    String scoreSummary = "Tap for details"; // Default display for score

                    // Basic team name extraction from the 'name' field (e.g., "India vs Australia")
                    String[] teams = name.split(" vs ", 2);
                    String team1 = teams.length > 0 ? teams[0].trim() : "Team 1";
                    String team2 = teams.length > 1 ? teams[1].trim() : "Team 2";

                    // Check for score array presence and format it
                    if (match.containsKey("score")) {
                        List<Map<String, Object>> scores = (List<Map<String, Object>>) match.get("score");
                        if (scores != null && !scores.isEmpty()) {
                            // Simple summary: take the score of the first two innings if available
                            StringBuilder scoreBuilder = new StringBuilder();
                            for (int i = 0; i < Math.min(scores.size(), 2); i++) {
                                Map<String, Object> s = scores.get(i);
                                if (s.containsKey("r") && s.containsKey("w")) {
                                    scoreBuilder.append(s.get("r").toString()).append("/").append(s.get("w").toString());
                                    if (i == 0 && scores.size() > 1) {
                                        scoreBuilder.append(" | ");
                                    }
                                }
                            }
                            scoreSummary = scoreBuilder.length() > 0 ? scoreBuilder.toString() : "Live Score Available";
                        } else {
                            // Use the match type as a score placeholder if no score array
                            scoreSummary = match.get("matchType") != null ? match.get("matchType").toString() : "Live Match";
                        }
                    } else {
                        // Use the match type as a score placeholder if no score field exists
                        scoreSummary = match.get("matchType") != null ? match.get("matchType").toString() : "Live Match";
                    }

                    // Add to the list
                    liveList.add(new MatchModel(team1, team2, scoreSummary, status));
                }
            }
        } catch (Exception e) {
            Log.e("LIVE_PARSE_ERROR", "Failed to parse live match data: " + e.getMessage());
            showEmptyMessage("Error processing match data.");
        }

        if (liveList.isEmpty()) {
            showEmptyMessage("No live matches right now 😴");
        } else {
            txtEmpty.setVisibility(View.GONE);
            matchAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Helper to show the empty message view.
     * @param message The message to display.
     */
    private void showEmptyMessage(String message) {
        txtEmpty.setVisibility(View.VISIBLE);
        txtEmpty.setText(message);
    }
}