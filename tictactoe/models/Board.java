package com.tictactoe.models;
import java.io.*;

public class Board {
    int size;
    public Piece[][] board;

    public Board(int size) {
        this.size = size;
        board = new Piece[size][size];
    }
}
