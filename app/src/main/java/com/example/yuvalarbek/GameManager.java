package com.example.yuvalarbek;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer; // Added import for MediaPlayer
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameManager {
    private final int[] mortyImages;
    private final int[][] rickMatrix;
    private int mortyPosition;
    private int score;
    private int life;
    private Handler handler;
    private Runnable obstacleRunnable;
    public static final int UPDATE_INTERVAL = 1000; // 1 second
    private final int updateInterval;
    private final Random randomGenerator;
    private ScoreListener scoreListener;
    private LifeListener lifeListener;
    private final Vibrator vibrator;
    private final FusedLocationProviderClient fusedLocationClient;
    private boolean scoreSaved; // To prevent saving the score multiple times
    private final MediaPlayer crashSound; // MediaPlayer for crash sound

    public static final int ROW_COUNT = 4;
    public static final int COL_COUNT = 4;

    public GameManager(Context context, int[] mortyImages, int[][] rickImages, int updateInterval) {
        this.mortyImages = mortyImages;
        this.rickMatrix = new int[ROW_COUNT][COL_COUNT];
        this.mortyPosition = 1; // Start in the middle position
        this.score = 0;
        this.life = 3; // Start with 3 lives
        this.randomGenerator = new Random();
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.updateInterval = updateInterval;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.scoreSaved = false; // Initialize to false

        // Initialize MediaPlayer for crash sound
        crashSound = MediaPlayer.create(context, R.raw.rickandmortyscreaming);
        crashSound.setVolume(0.8f,0.8f);
        updateMortyVisibility(context);
        startObstacleTimer(context);
    }

    public void moveMortyRight(Context context) {
        if (mortyPosition < mortyImages.length - 1) {
            mortyPosition++;
            updateMortyVisibility(context);
        }
    }

    public void moveMortyLeft(Context context) {
        if (mortyPosition > 0) {
            mortyPosition--;
            updateMortyVisibility(context);
        }
    }

    private void updateMortyVisibility(Context context) {
        for (int i = 0; i < mortyImages.length; i++) {
            AppCompatImageView imageView = ((AppCompatImageView) ((MainActivity) context).findViewById(mortyImages[i]));
            imageView.setVisibility(i == mortyPosition ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public int getMortyPosition() {
        return mortyPosition;
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    public void setScoreListener(ScoreListener listener) {
        this.scoreListener = listener;
    }

    public void setLifeListener(LifeListener listener) {
        this.lifeListener = listener;
    }

    private void startObstacleTimer(Context context) {
        handler = new Handler(Looper.getMainLooper());
        obstacleRunnable = new Runnable() {
            @Override
            public void run() {
                increaseScore();
                updateRickMatrix(context);
                checkCollision(context);
                refreshRickVisibility(context);
                if (life > 0) {
                    handler.postDelayed(this, updateInterval);
                } else {
                    saveScore(context);
                }
            }
        };
        handler.postDelayed(obstacleRunnable, updateInterval);
    }

    private void increaseScore() {
        score += 10;
        if (scoreListener != null) {
            scoreListener.onScoreUpdated(score);
        }
    }

    private void updateRickMatrix(Context context) {
        for (int col = COL_COUNT - 1; col > 0; col--) {
            for (int row = 0; row < ROW_COUNT; row++) {
                rickMatrix[row][col] = rickMatrix[row][col - 1];
            }
        }

        for (int row = 0; row < ROW_COUNT; row++) {
            rickMatrix[row][0] = 0;
        }

        int randomRow = randomGenerator.nextInt(ROW_COUNT);
        rickMatrix[randomRow][0] = 1;
    }

    private void refreshRickVisibility(Context context) {
        ((MainActivity) context).refreshRickVisibility(rickMatrix);
    }

    private void checkCollision(Context context) {
        for (int row = 0; row < ROW_COUNT; row++) {
            if (rickMatrix[row][COL_COUNT - 1] == 1 && row == mortyPosition) {
                AppCompatImageView mortyImageView = ((AppCompatImageView) ((MainActivity) context).findViewById(mortyImages[mortyPosition]));
                if (mortyImageView.getVisibility() == View.VISIBLE) {
                    playCrashSound();
                    reduceLife(context);
                    break;
                }
            }
        }
    }

    private void playCrashSound() {
        if (crashSound != null && !crashSound.isPlaying()) {
            crashSound.start();
        }
    }

    private void reduceLife(Context context) {
        life--;
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        if (lifeListener != null) {
            lifeListener.onLifeUpdated(life);
        }
        if (life <= 0 && !scoreSaved) {
            stopTimer();
            saveScore(context);
        }
    }

    public void stopTimer() {
        if (handler != null && obstacleRunnable != null) {
            handler.removeCallbacks(obstacleRunnable);
        }
    }

    public void saveScore(Context context) {
        if (scoreSaved) return; // Prevent saving the score multiple times
        scoreSaved = true;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((MainActivity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                List<ScoreEntry> scores = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    int score = sharedPreferences.getInt("SCORE_" + i, -1);
                                    double latitude = Double.longBitsToDouble(sharedPreferences.getLong("LAT_" + i, Double.doubleToLongBits(0.0)));
                                    double longitude = Double.longBitsToDouble(sharedPreferences.getLong("LON_" + i, Double.doubleToLongBits(0.0)));
                                    if (score != -1) {
                                        scores.add(new ScoreEntry(score, latitude, longitude));
                                    }
                                }

                                scores.add(new ScoreEntry(score, location.getLatitude(), location.getLongitude()));
                                Collections.sort(scores, (s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));

                                scores = scores.subList(0, Math.min(scores.size(), 10));

                                for (int i = 0; i < scores.size(); i++) {
                                    editor.putInt("SCORE_" + i, scores.get(i).getScore());
                                    editor.putLong("LAT_" + i, Double.doubleToLongBits(scores.get(i).getLatitude()));
                                    editor.putLong("LON_" + i, Double.doubleToLongBits(scores.get(i).getLongitude()));
                                }
                                editor.apply();
                            }
                        }
                    });
        }
    }

    public interface ScoreListener {
        void onScoreUpdated(int newScore);
    }

    public interface LifeListener {
        void onLifeUpdated(int newLife);
    }
}
