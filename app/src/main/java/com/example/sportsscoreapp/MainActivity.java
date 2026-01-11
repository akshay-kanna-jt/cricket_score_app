package com.example.sportsscoreapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main Activity of the application, responsible for managing navigation
 * between Live, Upcoming, and Completed match fragments.
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ImageView btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Uses the activity_main.xml layout
        setContentView(R.layout.activity_main);

        // Initialize UI components
        bottomNav = findViewById(R.id.bottomNav);
        btnRefresh = findViewById(R.id.btnRefresh);

        // Set the listener for bottom navigation clicks
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_live) {
                loadFragment(new LiveFragment(), "live");
                return true;
            } else if (itemId == R.id.nav_upcoming) {
                loadFragment(new UpcomingFragment(), "upcoming");
                return true;
            } else if (itemId == R.id.nav_completed) {
                loadFragment(new CompletedFragment(), "completed");
                return true;
            }
            return false;
        });

        // Set the refresh button listener
        btnRefresh.setOnClickListener(v -> {
            refreshCurrentFragment();
            Toast.makeText(this, "Refreshing data...", Toast.LENGTH_SHORT).show();
        });

        // Load the default fragment (Live) on initial startup
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_live);
        }
    }

    /**
     * Replaces the Fragment container with the specified fragment.
     * @param fragment The fragment to load.
     * @param tag The tag used to identify the fragment (e.g., "live", "upcoming").
     */
    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Use replace for navigation to ensure proper lifecycle management
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, tag);
        fragmentTransaction.commit();
    }

    /**
     * Finds the currently active fragment and calls its refresh method.
     */
    private void refreshCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Determine the current fragment based on the selected item ID
        int selectedId = bottomNav.getSelectedItemId();
        String currentTag = null;

        if (selectedId == R.id.nav_live) {
            currentTag = "live";
        } else if (selectedId == R.id.nav_upcoming) {
            currentTag = "upcoming";
        } else if (selectedId == R.id.nav_completed) {
            currentTag = "completed";
        }

        // Find the fragment by its tag
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentTag);

        if (currentFragment instanceof LiveFragment) {
            ((LiveFragment) currentFragment).fetchLiveMatches();
        } else if (currentFragment instanceof UpcomingFragment) {
            ((UpcomingFragment) currentFragment).fetchMatches();
        } else if (currentFragment instanceof CompletedFragment) {
            ((CompletedFragment) currentFragment).fetchMatches();
        }
    }
}