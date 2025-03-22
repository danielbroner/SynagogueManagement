package com.synagoguemanagement.synagoguemanagement.data;

public class Seat {
    private final int position;
    private final int userId;

    public Seat(int position, int userId) {
        this.position = position;
        this.userId = userId;
    }

    public int getPosition() {
        return position;
    }

    public int getUserId() {
        return userId;
    }
}
