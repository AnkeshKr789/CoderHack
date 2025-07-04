package com.crio.coderhack.dto.rest_input_format;

public class ScoreUpdate {
    private String userId;
    private String score;

    public ScoreUpdate(String userId, String score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public String getScore() {
        return score;
    }
}
