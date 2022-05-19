package com.example.game2d;

public class PlayerScore {

    private String player;
    private  String score;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public PlayerScore(String player, String score) {
        this.player = player;
        this.score = score;
    }
}
