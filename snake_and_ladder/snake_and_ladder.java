package com.snake_and_ladder;
import java.util.*;

class Player{
    private String name;
    private int position;

    Player(String name,int position) {
        this.name = name;
        this.position = position;
    }
    String getName(){
        return this.name;
    }
    int getPosition(){
        return this.position;
    }
    void setName(String name){
        this.name = name;
    }
    void setPosition(int position){
        this.position = position;
    }
}

class Dice{
    private final int minimumValue=1;
    private final int maximumValue=6;
    private int range = maximumValue - minimumValue;
    public int numGenerator(){
        return (int)(Math.random() * range) + minimumValue;
    }
}

class Board{
    private List<Integer>board = new ArrayList<>();
    private TreeMap<Integer,Integer>stairs = new TreeMap<>();
    private TreeMap<Integer,Integer> snakes = new TreeMap<>();

    public void setStairs(Integer start,Integer end){
        this.stairs.put(start,end);
    }
    public void setSnakes(Integer start,Integer end){
        this.snakes.put(start,end);
    }
    public void addToBoard(Integer num){
        this.board.add(num);
    }
    public boolean checkStairs(Integer start){
        return this.stairs.containsKey(start);
    }
    public boolean checkSnakes(Integer start){
        return this.snakes.containsKey(start);
    }
    public Integer getStairValue(Integer start){
        return this.stairs.get(start);
    }
    public Integer getSnakesValue(Integer start){
        return this.snakes.get(start);
    }
}

class Game{
    private Player player1 = new Player("Jay",0);
    private Player player2 = new Player("Jiya",0);
    private List<Player>playerList = new ArrayList<>();
    private Board board = new Board();
    Dice dice = new Dice();

    void startGame(){
        playerList.add(player1);
        playerList.add(player2);
        board.setStairs(3, 22);
        board.setStairs(5, 8);
        board.setStairs(11, 26);
        board.setStairs(20, 29);
        board.setStairs(27, 53);
        board.setStairs(35, 59);
        board.setStairs(44, 58);
        board.setStairs(52, 72);
        board.setStairs(56, 74);
        board.setStairs(61, 99);

        board.setSnakes(25, 10);
        board.setSnakes(34, 6);
        board.setSnakes(47, 15);
        board.setSnakes(65, 52);
        board.setSnakes(87, 57);
        board.setSnakes(91, 61);
        board.setSnakes(99, 69);
        int counter=-1;
        while (true)
        {
            counter++;
            counter = counter%2;
            Player currentPlayer = playerList.get(counter);
            int position = currentPlayer.getPosition();
            int value = dice.numGenerator();
            int nextPosition =position + value;
            if(nextPosition > 100){
                System.out.println(currentPlayer.getName() + " got value of " + value + " on dice " + " and got value " + nextPosition + " so ignored");
                continue;
            }
            if(board.checkSnakes(nextPosition)){
                int snakeNextPosition = board.getSnakesValue(nextPosition);
                System.out.println(" The dice roll for " + currentPlayer.getName() + " was " + value);
                System.out.println(currentPlayer.getName() + " got bitten by snake at " + nextPosition + " and was sent to " + snakeNextPosition);
                currentPlayer.setPosition(snakeNextPosition);
            }
            else if(board.checkStairs(nextPosition)){
                int stairNextPosition = board.getStairValue(nextPosition);
                System.out.println(" The dice roll for " + currentPlayer.getName() + " was " + value);
                System.out.println(currentPlayer.getName() + " found stairs from " + nextPosition + " to " + stairNextPosition);
                currentPlayer.setPosition(stairNextPosition);
            }
            else {
                System.out.println(" The dice roll for " + currentPlayer.getName() + " was " + value);
                System.out.println(currentPlayer.getName() + " was sent to " + nextPosition);
                currentPlayer.setPosition(nextPosition);
            }

            if(player1.getPosition() == 100 ){
                System.out.println(player1.getName() + " has won the series");
                break;
            }
            if(player2.getPosition() == 100){
                System.out.println(player2.getName() + " has won the series");
                break;
            }

        }

    }

}


public class snake_and_ladder {

    public static void main(String []args)
    {
       Game game = new Game();
       game.startGame();
    }
}
