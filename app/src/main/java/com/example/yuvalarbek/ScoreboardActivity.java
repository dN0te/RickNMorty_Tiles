package com.example.yuvalarbek;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView scoreboardRecyclerView;
    private ScoreboardAdapter scoreboardAdapter;
    private List<ScoreEntry> topScores;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreboardRecyclerView = findViewById(R.id.scoreboardRecyclerView);
        scoreboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        topScores = getTopScores();
        scoreboardAdapter = new ScoreboardAdapter(topScores);
        scoreboardRecyclerView.setAdapter(scoreboardAdapter);

        scoreboardAdapter.setOnItemClickListener(new ScoreboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScoreEntry scoreEntry) {
                if (mMap != null) {
                    LatLng location = new LatLng(scoreEntry.getLatitude(), scoreEntry.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(location).title("Score: " + scoreEntry.getScore()));
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private List<ScoreEntry> getTopScores() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        List<ScoreEntry> scores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int score = sharedPreferences.getInt("SCORE_" + i, -1);
            double latitude = Double.longBitsToDouble(sharedPreferences.getLong("LAT_" + i, Double.doubleToLongBits(0.0)));
            double longitude = Double.longBitsToDouble(sharedPreferences.getLong("LON_" + i, Double.doubleToLongBits(0.0)));
            if (score != -1) {
                scores.add(new ScoreEntry(score, latitude, longitude));
            }
        }
        Collections.sort(scores, (s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        return scores;
    }
}
