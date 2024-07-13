package com.example.yuvalarbek;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements GameManager.ScoreListener, GameManager.LifeListener, SensorEventListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private MaterialTextView main_LBL_score;
    private MaterialButton main_BTN_right;
    private MaterialButton main_BTN_left;

    private int[] mortyImages;
    private int[][] rickImages;

    private AppCompatImageView[] lifeImages;

    private GameManager gameManager;
    private int life = 3;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isTiltControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();

        boolean isDoubleSpeed = getIntent().getBooleanExtra("DOUBLE_SPEED", false);
        isTiltControl = getIntent().getBooleanExtra("TILT_CONTROL", false);
        int updateInterval = isDoubleSpeed ? GameManager.UPDATE_INTERVAL / 2 : GameManager.UPDATE_INTERVAL;
        gameManager = new GameManager(this, mortyImages, rickImages, updateInterval);
        gameManager.setScoreListener(this);
        gameManager.setLifeListener(this);

        if (isTiltControl) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            hideButtons(); // Hide the buttons if tilt control is enabled
        }

        // Check if location permissions are granted
        getLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTiltControl) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTiltControl) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameManager.stopTimer(); // Stop the timer when the activity is destroyed
    }

    private void initViews() {
        int score = 0;
        main_LBL_score.setText(String.valueOf(score));
        main_BTN_right.setOnClickListener(v -> changeMortyPosition(1));
        main_BTN_left.setOnClickListener(v -> changeMortyPosition(-1));
    }

    private void findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);

        mortyImages = new int[] {
                R.id.Main_Img_Morty_0_3,
                R.id.Main_Img_Morty_1_3,
                R.id.Main_Img_Morty_2_3,
                R.id.Main_Img_Morty_3_3
        };

        rickImages = new int[][] {
                { R.id.Main_IMG_Rick_0_0, R.id.Main_IMG_Rick_0_1, R.id.Main_IMG_Rick_0_2, R.id.Main_IMG_Rick_0_3 },
                { R.id.Main_IMG_Rick_1_0, R.id.Main_IMG_Rick_1_1, R.id.Main_IMG_Rick_1_2, R.id.Main_IMG_Rick_1_3 },
                { R.id.Main_IMG_Rick_2_0, R.id.Main_IMG_Rick_2_1, R.id.Main_IMG_Rick_2_2, R.id.Main_IMG_Rick_2_3 },
                { R.id.Main_IMG_Rick_3_0, R.id.Main_IMG_Rick_3_1, R.id.Main_IMG_Rick_3_2, R.id.Main_IMG_Rick_3_3 }
        };

        lifeImages = new AppCompatImageView[] {
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
    }

    private void hideButtons() {
        main_BTN_right.setVisibility(View.GONE);
        main_BTN_left.setVisibility(View.GONE);
    }

    private void changeMortyPosition(int direction) {
        if (direction == 1) {
            gameManager.moveMortyRight(this);
        } else if (direction == -1) {
            gameManager.moveMortyLeft(this);
        }
        updateMortyVisibility();
    }

    private void updateMortyVisibility() {
        for (int i = 0; i < mortyImages.length; i++) {
            AppCompatImageView imageView = findViewById(mortyImages[i]);
            imageView.setVisibility(gameManager.getMortyPosition() == i ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void onScoreUpdated(int newScore) {
        main_LBL_score.setText(String.valueOf(newScore));
    }

    @Override
    public void onLifeUpdated(int newLife) {
        life = newLife;
        updateLifeUI();
        if (life <= 0) {
            gameManager.saveScore(this);
            startGameOverActivity();
        }
    }

    private void updateLifeUI() {
        for (int i = 0; i < lifeImages.length; i++) {
            lifeImages[i].setVisibility(i < life ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void startGameOverActivity() {
        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }

    public void refreshRickVisibility(int[][] rickMatrix) {
        for (int row = 0; row < GameManager.ROW_COUNT; row++) {
            for (int col = 0; col < GameManager.COL_COUNT; col++) {
                AppCompatImageView imageView = findViewById(rickImages[row][col]);
                imageView.setVisibility(rickMatrix[row][col] == 1 ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isTiltControl && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            if (x > 3) { // Tilt right
                changeMortyPosition(1);
            } else if (x < -3) { // Tilt left
                changeMortyPosition(-1);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                Toast.makeText(this, "Location permission required for this app.", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }
}
