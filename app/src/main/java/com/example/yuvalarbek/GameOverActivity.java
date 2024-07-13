package com.example.yuvalarbek;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class GameOverActivity extends AppCompatActivity {

    private MaterialButton gameOver_BTN_restart;
    private MaterialButton gameOver_BTN_exit;
    private MaterialButton gameOver_BTN_main_menu;
    private SoundtrackManager soundtrackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        soundtrackManager = SoundtrackManager.getInstance(); // Get the singleton instance

        findViews();
        initViews();
    }

    private void findViews() {
        gameOver_BTN_restart = findViewById(R.id.gameOver_BTN_restart);
        gameOver_BTN_exit = findViewById(R.id.gameOver_BTN_exit);
        gameOver_BTN_main_menu = findViewById(R.id.gameOver_BTN_main_menu);
    }

    private void initViews() {
        gameOver_BTN_restart.setOnClickListener(v -> restartGame());
        gameOver_BTN_exit.setOnClickListener(v -> exitGame());
        gameOver_BTN_main_menu.setOnClickListener(v -> goToMainMenu());
    }

    private void restartGame() {
        Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }

    private void exitGame() {
        soundtrackManager.stopSoundtrack(); // Stop the soundtrack when exiting
        finishAffinity(); // Close all activities
    }

    private void goToMainMenu() {
        Intent intent = new Intent(GameOverActivity.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}
