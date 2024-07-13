package com.example.yuvalarbek;

public class ScoreEntry {
    private int score;
    private double latitude;
    private double longitude;

    public ScoreEntry(int score, double latitude, double longitude) {
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
