package com.tictactoe.models;

public class Main {
    public static void main(String args[]) {
        int size=3;
        Board b = new Board(size);
        Game g = new Game(b);
        String player1="pp";
        String player2="kk";
        g.addPlayer(new Player(player1,new PieceTypeO()));
        g.addPlayer(new Player(player2,new PieceTypeX()));
        g.startGame();



    }
}
