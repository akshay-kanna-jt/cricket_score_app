package com.example.sportsscoreapp;

public class MatchModel {

    public String team1;
    public String team2;
    public String score;
    public String status;

    public MatchModel(String team1, String team2, String score, String status) {
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
        this.status = status;
    }
}
