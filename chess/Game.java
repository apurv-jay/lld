package com.chess;
import java.util.Scanner;

// Enums
enum Color { WHITE, BLACK }
enum PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }
enum GameStatus { ONGOING, WHITE_WIN, BLACK_WIN, DRAW }

// Position class
class Position {
    private int row, col;
    public Position(int row, int col) { this.row = row; this.col = col; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public static Position fromAlgebraic(String alg) {
        int col = alg.charAt(0) - 'a';
        int row = alg.charAt(1) - '1';
        return new Position(row, col);
    }
    public String toAlgebraic() { return "" + (char)('a' + col) + (char)('1' + row); }
}

// Piece interface
interface Piece {
    Color getColor();
    PieceType getType();
    boolean isValidMove(Board board, Position start, Position end);
}

// Concrete piece classes
class King implements Piece {
    private Color color;
    public King(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public PieceType getType() { return PieceType.KING; }
    public boolean isValidMove(Board board, Position start, Position end) {
        int rowDiff = Math.abs(start.getRow() - end.getRow());
        int colDiff = Math.abs(start.getCol() - end.getCol());
        if (rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0)) {
            Piece endPiece = board.getPieceAt(end);
            return endPiece == null || endPiece.getColor() != color;
        }
        return false;
    }
}

class Rook implements Piece {
    private Color color;
    public Rook(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public PieceType getType() { return PieceType.ROOK; }
    public boolean isValidMove(Board board, Position start, Position end) {
        if (start.getRow() == end.getRow()) {
            int step = start.getCol() < end.getCol() ? 1 : -1;
            for (int col = start.getCol() + step; col != end.getCol(); col += step) {
                if (board.getPieceAt(new Position(start.getRow(), col)) != null) return false;
            }
        } else if (start.getCol() == end.getCol()) {
            int step = start.getRow() < end.getRow() ? 1 : -1;
            for (int row = start.getRow() + step; row != end.getRow(); row += step) {
                if (board.getPieceAt(new Position(row, start.getCol())) != null) return false;
            }
        } else {
            return false;
        }
        Piece endPiece = board.getPieceAt(end);
        return endPiece == null || endPiece.getColor() != color;
    }
}

// Stubbed pieces (to save time)
class Queen implements Piece {
    private Color color;
    public Queen(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public PieceType getType() { return PieceType.QUEEN; }
    public boolean isValidMove(Board board, Position start, Position end) { return false; } // Stub
}

class Bishop implements Piece {
    private Color color;
    public Bishop(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public PieceType getType() { return PieceType.BISHOP; }
    public boolean isValidMove(Board board, Position start, Position end) { return false; } // Stub
}

class Knight implements Piece {
    private Color color;
    public Knight(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public PieceType getType() { return PieceType.KNIGHT; }
    public boolean isValidMove(Board board, Position start, Position end) { return false; } // Stub
}

class Pawn implements Piece {
    private Color color;
    public Pawn(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public PieceType getType() { return PieceType.PAWN; }
    public boolean isValidMove(Board board, Position start, Position end) { return false; } // Stub
}

// Move class
class Move {
    private Position start, end;
    public Move(Position start, Position end) { this.start = start; this.end = end; }
    public Position getStart() { return start; }
    public Position getEnd() { return end; }
}

// Player class
class Player {
    private String name;
    private Color color;
    public Player(String name, Color color) { this.name = name; this.color = color; }
    public String getName() { return name; }
    public Color getColor() { return color; }
}

// Board class
class Board {
    private Piece[][] grid = new Piece[8][8];
    private Position whiteKingPos, blackKingPos;

    public Board() { setupInitialPosition(); }

    public void setupInitialPosition() {
        for (int col = 0; col < 8; col++) {
            grid[1][col] = new Pawn(Color.BLACK);
            grid[6][col] = new Pawn(Color.WHITE);
        }
        grid[0][0] = new Rook(Color.BLACK); grid[0][7] = new Rook(Color.BLACK);
        grid[0][4] = new King(Color.BLACK); blackKingPos = new Position(0, 4);
        grid[7][0] = new Rook(Color.WHITE); grid[7][7] = new Rook(Color.WHITE);
        grid[7][4] = new King(Color.WHITE); whiteKingPos = new Position(7, 4);
        // Add other pieces as time permits
    }

    public Piece getPieceAt(Position pos) { return grid[pos.getRow()][pos.getCol()]; }
    public void setPieceAt(Position pos, Piece piece) { grid[pos.getRow()][pos.getCol()] = piece; }

    public Piece movePiece(Position start, Position end) {
        Piece captured = getPieceAt(end);
        Piece moving = getPieceAt(start);
        setPieceAt(end, moving);
        setPieceAt(start, null);
        if (moving.getType() == PieceType.KING) {
            if (moving.getColor() == Color.WHITE) whiteKingPos = end;
            else blackKingPos = end;
        }
        return captured;
    }

    public void undoMove(Move move, Piece captured) {
        Piece moving = getPieceAt(move.getEnd());
        setPieceAt(move.getStart(), moving);
        setPieceAt(move.getEnd(), captured);
        if (moving.getType() == PieceType.KING) {
            if (moving.getColor() == Color.WHITE) whiteKingPos = move.getStart();
            else blackKingPos = move.getStart();
        }
    }

    public boolean isInCheck(Color color) {
        Position kingPos = color == Color.WHITE ? whiteKingPos : blackKingPos;
        Color oppColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor() == oppColor) {
                    if (piece.isValidMove(this, new Position(row, col), kingPos)) return true;
                }
            }
        }
        return false;
    }

    public void displayBoard() {
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < 8; col++) {
                Piece p = grid[row][col];
                if (p == null) System.out.print(". ");
                else System.out.print((p.getColor() == Color.WHITE ? "W" : "B") + p.getType().toString().charAt(0) + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }
}

// Game class
public class Game {
    private Board board;
    private Player[] players = new Player[2];
    private Player currentPlayer;
    private GameStatus gameStatus;
    private Scanner scanner = new Scanner(System.in);

    public Game(String player1Name, String player2Name) {
        board = new Board();
        players[0] = new Player(player1Name, Color.WHITE);
        players[1] = new Player(player2Name, Color.BLACK);
        currentPlayer = players[0]; // White starts
        gameStatus = GameStatus.ONGOING;
    }

    public void startGame() {
        while (gameStatus == GameStatus.ONGOING) {
            board.displayBoard();
            System.out.println(currentPlayer.getName() + "'s turn (" + currentPlayer.getColor() + "): ");
            String input = scanner.nextLine();
            Move move = parseMove(input);
            if (move != null && makeMove(move)) {
                Player opponent = currentPlayer == players[0] ? players[1] : players[0];
                if (board.isInCheck(opponent.getColor())) {
                    System.out.println("Check!");
                    // Checkmate detection can be added if time permits
                }
                currentPlayer = opponent;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    public boolean makeMove(Move move) {
        Piece piece = board.getPieceAt(move.getStart());
        if (piece == null || piece.getColor() != currentPlayer.getColor()) return false;
        if (!piece.isValidMove(board, move.getStart(), move.getEnd())) return false;

        Piece captured = board.movePiece(move.getStart(), move.getEnd());
        if (board.isInCheck(currentPlayer.getColor())) {
            board.undoMove(move, captured);
            return false;
        }
        return true;
    }

    public boolean isGameOver() { return gameStatus != GameStatus.ONGOING; }

    private Move parseMove(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length != 2) return null;
        try {
            Position start = Position.fromAlgebraic(parts[0]);
            Position end = Position.fromAlgebraic(parts[1]);
            if (start.getRow() < 0 || start.getRow() > 7 || start.getCol() < 0 || start.getCol() > 7 ||
                    end.getRow() < 0 || end.getRow() > 7 || end.getCol() < 0 || end.getCol() > 7) return null;
            return new Move(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        Game game = new Game("Player1", "Player2");
        game.startGame();
    }
}