package com.example.yuvalarbek;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Initialize SoundtrackManager and start the soundtrack if not already playing
        SoundtrackManager soundtrackManager = SoundtrackManager.getInstance();
        soundtrackManager.startSoundtrack(this);

        // Set up the Start Game button
        MaterialButton startGameButton = findViewById(R.id.startButton);
        startGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Set up the 2x Speed button
        MaterialButton doubleSpeedButton = findViewById(R.id.x2SpeedButton);
        doubleSpeedButton.setOnClickListener(v -> startGameWithExtras(true, false));

        // Set up the Tilt Mode button
        MaterialButton tiltModeButton = findViewById(R.id.tiltControlButton);
        tiltModeButton.setOnClickListener(v -> startGameWithExtras(false, true));

        // Set up the Scoreboard button
        MaterialButton scoreboardButton = findViewById(R.id.scoreboardButton);
        scoreboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, ScoreboardActivity.class);
            startActivity(intent);
        });
    }

    private void startGameWithExtras(boolean isDoubleSpeed, boolean isTiltControl) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra("DOUBLE_SPEED", isDoubleSpeed);
        intent.putExtra("TILT_CONTROL", isTiltControl);
        startActivity(intent);
    }
}
