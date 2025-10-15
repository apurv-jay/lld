package com.tictactoe.models;

public class Player {
    private String name;
    private Piece playingPiece;

    public Player(String name, Piece playingPiece) {
        this.name = name;
        this.playingPiece = playingPiece;
    }

    public Piece getPlayingPiece() {
        return playingPiece;
    }

    public String getName() {
        return name;
    }
}
