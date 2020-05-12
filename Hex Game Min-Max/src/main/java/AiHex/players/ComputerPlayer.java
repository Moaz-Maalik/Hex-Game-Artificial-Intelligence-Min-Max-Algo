package AiHex.players;

import AiHex.gameMechanics.Move;
import AiHex.gameMechanics.Runner;
import AiHex.hexBoards.Board;
import java.util.ArrayList;
import AiHex.hexBoards.GameBoard;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import AiHex.hexBoards.Board;
import java.util.Random;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.sqrt;

public class ComputerPlayer implements Player {
    protected ArrayList<Board> auxBoards = new ArrayList<Board>();
    protected Runner game;
    protected int player;
    protected int opponent;
    protected int size;
    ArrayList<node> openlist = new ArrayList<node>(); //frnge
    ArrayList<node> closelist = new ArrayList<node>();
    node start = new node();
    node end= new node();
    //int[][] board ;

    public ComputerPlayer(Runner game, int colour) {
        this.game = game;
        this.player = colour;
        if (this.player == Board.RED)
            this.opponent = Board.BLUE;
        else
            this.opponent = Board.RED;
        this.size = game.getBoard().getSize();
    }

    public ArrayList<Board> getAuxBoards() {
        return new ArrayList<Board>();
    }

    public Move getMove(){

        GameBoard gameboard = game.getBoard();
        int size = gameboard.getSize();
        int [][]board = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = gameboard.get(i, j);
            }
        }
        return minimax(board, size, 3);
    }
    // this is the evaluation function that calculates the shortest path between every element of first row with every element of the last
    // row ... This returns the cost of the path. The cost of blank box is 1 and the cost of red box in the path is 0 in case of red player and vice versa
    // the path with the minimum cost is selected and one of the x,y pair is found the return in the move object
    int Evaluationfunction(int[][] board, int n){
        int mincost = 1000000;
        int cost = 0;
        openlist = new ArrayList<node>();
        closelist = new ArrayList<node>();
        for(int i = 0; i < n; i++)
        {
            if(board[0][i] != this.opponent)
            {
                for(int j = 0; j < n; j++)
                {
                    if(board[n-1][j] != this.opponent)
                    {
                        start.current.x = 0;
                        start.current.y = i;
                        end.current.x = n-1;
                        end.current.y = j;
                        openlist = new ArrayList<node>();
                        closelist = new ArrayList<node>();
                        cost = findpath(start , end, board);
                        if(cost < mincost)
                            mincost = cost;
                        System.out.println("Cost :" + cost);
                    }

                }
            }

        }
        return cost;
    }
    // this is the driver minimax funtion which makes use of the other minimax function to return the move with the lowest cost
    Move minimax(int[][] board, int n, int depth){
        int best = -1000;
        int alpha = -1000, beta = 1000;
        int x = -1;
        int y = -1;
        for (int i = 0; i < n; i++)
        {
            for(int j = 0 ; j < n ; j++) {
                if (board[i ][j] == Board.BLANK) {
                    board[i ][j] = this.player;
                    int val = minimax_2(depth - 1, false, alpha, beta, board, n);
                    board[i ][j] = Board.BLANK;
                    if (best < val) {
                        best = val;
                        x = j;
                        y = i;
                    }
                    System.out.println("if statement val : " + val);
                    alpha = Math.max(alpha, best);
                }
                else{
                    System.out.println(i + ", " + j);
                }
            }

        }
        System.out.println("Hello");
        System.out.println("x and y : "+ x + ", " + y);
        if(this.player == Board.RED)
            return new Move(this.player, y, x);
        else
            return new Move(this.player, x, y);
    }
    // This is the minimax funtion that makes uses of evauluation function to find the minimum cost and
    // one of the x,y pair is returned of the path with lowest cost
    int minimax_2(int depth, Boolean maximizingPlayer, int alpha, int beta, int[][] board, int n)  {
        if (depth == 0)
            return Evaluationfunction(board, n);

        if (maximizingPlayer)
        {
            int best = -1000;

            for (int i = 0; i < n; i++)
            {
                for(int j = 0; j < n ;j++)
                if(board[i][j] == Board.BLANK){
                    board[i][j] = this.player;
                    int val = minimax_2(depth - 1, !maximizingPlayer, alpha, beta, board, n);
                    board[i][j] = Board.BLANK;
                    best = Math.max(best, val);
                    alpha = Math.max(alpha, best);

                    if (beta <= alpha)
                        return best;
                }
            }
            return best;
        }
        else
        {
            int best = 1000;

            for (int i = 0; i < n; i++)
            {
                for(int j = 0 ; j < n ; j++) {
                    if (board[i ][j] == Board.BLANK) {
                        board[i][j] = this.opponent;
                        int val = minimax_2(depth - 1, !maximizingPlayer, alpha, beta, board, n);
                        board[i][j] = Board.BLANK;
                        best = Math.min(best, val);
                        beta = Math.min(beta, best);

                        if (beta <= alpha)
                            return best;
                    }
                }
            }
            return best;
        }
    }
    // all of the following functions are used by the heuristic function to find the path with the minimum cost
    boolean contains(node a ){
        for(int i = 0 ; i < closelist.size(); i++){
            if(closelist.get(i).current.x == a.current.x && closelist.get(i).current.y == a.current.y ){
                return true;
            }
        }
        return false;
    }
    void sortfofn(){
        for (int i = 0; i < openlist.size()-1; i++){

            // Last i elements are already in place
            for (int j = 0; j < openlist.size()-i-1; j++)  {
                if (openlist.get(j).heuristic > openlist.get(j).heuristic){
                    node temp = new node();
                    temp = openlist.get(j);
                    openlist.set(j, openlist.get(j+1)) ;
                    openlist.set(j+1, temp) ;

                }

            }
        }

    }
    void addChildrenToopenlist(node p, int[][] board){
        int x=  p.current.x;
        int y = p.current.y;
        GameBoard gameboard = game.getBoard();
        int rowcols = gameboard.getSize();
        for(int i = -1 ; i <= 1 ; i++){
            for(int j = -1 ; j <= 1 ; j++){
                if(x+i >= 0 && y + j >= 0 && x+i < rowcols && y+j < rowcols  ){

                    if(i == j && i == 0)
                    {
                        //skip for same node
                    }
                    else{

                        node temp = new node();
                        temp.current.x = p.current.x+i;
                        temp.current.y = p.current.y+j;
                        //System.out.println(temp.current.x + "  " + temp.current.y);
                        if(board[temp.current.x][temp.current.y] != this.opponent  ){

                            if(!contains(temp)){
                                node childnode = new node();
                                point fg = new point();
                                fg.x=temp.current.x;
                                fg.y = temp.current.y;
                                childnode.parent = p.current;
                                childnode.current= fg;
                                childnode.heuristic = findheuristic(fg, end.current);
                                childnode.fOfn=childnode.heuristic+p.distancetravelled+1;
                                childnode.distancetravelled++;
                                openlist.add(childnode);
                                sortfofn();
                            }
                        }
                    }
                }
            }
        }
    }
    double  findheuristic(point current , point goal){
        double distx = (current.x - end.current.x)*(current.x - end.current.x);
        double disty = (current.y - end.current.y)*(current.y - end.current.y);
        return sqrt( distx + disty );
    }


    public int findpath(node start, node end, int[][] board ){
        openlist.add(start);
        int cost = findroute(board);
        return  cost;
    }

    public int printpath(node a, int[][] board){
        int cx = end.current.x;
        int cy = end.current.y;
        node ab = new node();
        ab = a;
        int cost = 0;
        if(board[cx][cy] == this.player)
        {
            ;
        }
        else
        {
            cost = cost + 1;
        }
        while(true){
            if(ab.current.x == start.current.x && ab.current.y == start.current.y) {
                if(board[ab.current.x][ab.current.y] == this.player)
                {
                    ;
                }
                else
                {
                    cost = cost + 1;
                }
                break;
            }

            int parentx = ab.parent.x;
            int parenty = ab.parent.y;
            if(board[parentx][parenty] == this.player)
            {
                ;
            }
            else
            {
                cost = cost + 1;
            }
            //System.out.println(parentx + " "+ parenty);
            for(int i =0 ; i < closelist.size() ; i++){
                if(closelist.get(i).current.x == parentx && closelist.get(i).current.y == parenty ){
                    ab = closelist.get(i);
                    break;
                }
            }
        }
        return cost;
    }
    public int findroute(int[][] board){
        if (openlist.isEmpty())
            return 0;
        else
        {
            node a = new node();
            a = openlist.remove(0);

            if (a.current.x == end.current.x && a.current.y == end.current.y){
                //System.out.println("Path found");
                int cost  = printpath(a, board);
                return cost;
            }
            else{
                if (!contains(a)){
                    closelist.add(a);
                    addChildrenToopenlist(a, board);
                }
                return findroute(board);
            }
        }
    }
    /*public ArrayList<Board> getAuxBoards() {
        return new ArrayList<Board>();
    }*/
}

class node{
    public point current = new point();
    public point parent= new point();
    public double fOfn;
    public double heuristic;
    public int distancetravelled; //no of cells;

    node(){

    }

}

class point {
    public int x,y;
    point(){

    }

}

