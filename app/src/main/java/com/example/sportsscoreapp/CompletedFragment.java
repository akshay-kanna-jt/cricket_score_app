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

public class CompletedFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txtEmpty;
    ArrayList<MatchModel> completedList = new ArrayList<>();
    MatchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_matches);
        progressBar = view.findViewById(R.id.progress_bar);
        txtEmpty = view.findViewById(R.id.text_error);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MatchAdapter(getContext(), completedList);
        recyclerView.setAdapter(adapter);

        fetchMatches();
        return view;
    }

    public void fetchMatches() {

        progressBar.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<Map<String, Object>> call = service.getAllMatches(Constants.API_KEY, 0);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {

                progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful() || response.body() == null) {
                    txtEmpty.setText("Response failed: " + response.code());
                    txtEmpty.setVisibility(View.VISIBLE);
                    return;
                }

                List<Map<String, Object>> matches =
                        (List<Map<String, Object>>) response.body().get("data");

                completedList.clear();

                for (Map<String, Object> match : matches) {
                    String status = String.valueOf(match.get("status")).toLowerCase();

                    boolean isCompleted = status.contains("won")
                            || status.contains("finished")
                            || status.contains("result")
                            || status.contains("draw");

                    if (!isCompleted) continue;

                    String name = match.get("name") + "";
                    String[] parts = name.split(" vs ");

                    String team1 = parts.length > 0 ? parts[0] : "Team 1";
                    String team2 = parts.length > 1 ? parts[1] : "Team 2";

                    completedList.add(new MatchModel(team1, team2, status, "Completed"));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {

                progressBar.setVisibility(View.GONE);
                txtEmpty.setText("Network error");
                txtEmpty.setVisibility(View.VISIBLE);

                Log.e("CompletedFragment", t.getMessage());
            }
        });
    }
}
