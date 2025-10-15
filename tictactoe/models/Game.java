package com.tictactoe.models;
import java.util.*;


public class Game {
    Queue<Player> playerQueue = new LinkedList<>();
    Board board;

    public Game(Board board) {
        this.board = board;
    }
    void addPlayer(Player p)
    {
        playerQueue.add(p);
    }
    private Player findPlayerByPiece(Piece piece) {
        for (Player p : playerQueue) {
            if (p.getPlayingPiece() == piece) {
                return p;
            }
        }
        return null;
    }

    private Player checkWinner(Piece[][] b) {
        int n = b.length;
        // Check rows & columns
        for (int i = 0; i < n; i++) {
            Piece rowFirst = b[i][0];
            if (rowFirst != null) {
                boolean rowWin = true;
                for (int j = 1; j < n; j++) {
                    if (b[i][j] != rowFirst) { rowWin = false; break; }
                }
                if (rowWin) return findPlayerByPiece(rowFirst);
            }
            Piece colFirst = b[0][i];
            if (colFirst != null) {
                boolean colWin = true;
                for (int j = 1; j < n; j++) {
                    if (b[j][i] != colFirst) { colWin = false; break; }
                }
                if (colWin) return findPlayerByPiece(colFirst);
            }
        }
        // Check diagonals
        Piece diag = b[0][0];
        if (diag != null) {
            boolean dwin = true;
            for (int i = 1; i < n; i++) if (b[i][i] != diag) { dwin = false; break; }
            if (dwin) return findPlayerByPiece(diag);
        }
        Piece anti = b[0][n - 1];
        if (anti != null) {
            boolean awin = true;
            for (int i = 1; i < n; i++) if (b[i][n - 1 - i] != anti) { awin = false; break; }
            if (awin) return findPlayerByPiece(anti);
        }
        return null;
    }
    private boolean isDraw(Piece[][] b) {
        // No winner and no empty cells
        if (checkWinner(b) != null) return false;
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                if (b[i][j] == null) return false;
            }
        }
        return true;
    }

    void startGame()
    {
        if(playerQueue.isEmpty())
        {
            System.out.println("please add player");
            return;
        }
        while(true)
        {
            Scanner scanner = new Scanner(System.in);
            Player curPlayer = playerQueue.poll();
            String name=curPlayer.getName();
            Piece value = curPlayer.getPlayingPiece();
            System.out.println("Name " + name);
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            board.board[x][y]=value;
            playerQueue.add(curPlayer);
            if(isDraw(board.board))
            {
                System.out.println("match has been drawn");
                return;
            }
            Player winner = checkWinner(board.board);
            if(winner!=null)
            {
                System.out.println("The winner is " + winner.getName() );
                return;
            }



        }
    }

}



















