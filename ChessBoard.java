package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math.*;

public class ChessBoard {

    private ChessPiece[][] cBoard = new ChessPiece[8][8];
    private Boolean validMove = false;
    private int turn = 0;
    private String turnColor = "B";

    public static void main(String[] args) throws IOException {
        ChessBoard cB = new ChessBoard();
        cB.fillBoard();
        cB.displayBoard();
        while (true) {
            cB.movePiece();
            cB.turnChecker();
            cB.displayBoard();
        }
    }

    public void fillBoard() {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                cBoard[i][j] = null;
            }
        }
        //fill board -> White
        for(int i = 0; i < 8; i++){
            cBoard[1][i] = new Pawn('W');
        }
        cBoard[0][0] = new Rook('W');
        cBoard[0][7] = new Rook('W');
        cBoard[0][1] = new Knight('W');
        cBoard[0][6] = new Knight('W');
        cBoard[0][2] = new Bishop('W');
        cBoard[0][5] = new Bishop('W');
        cBoard[0][3] = new Queen('W');
        cBoard[0][4] = new King('W');
        //fill board -> Black
        for(int i = 0; i < 8; i++){
            cBoard[6][i] = new Pawn('B');
        }
        cBoard[7][0] = new Rook('B');
        cBoard[7][7] = new Rook('B');
        cBoard[7][1] = new Knight('B');
        cBoard[7][6] = new Knight('B');
        cBoard[7][2] = new Bishop('B');
        cBoard[7][5] = new Bishop('B');
        cBoard[7][3] = new King('B');
        cBoard[7][4] = new Queen('B');
    }

    public void displayBoard() {
        //Display board
        for (int i = 0; i < 8; i++) {
            System.out.print(i+" ");
            for (int j = 0; j < 8; j++) {
                if(cBoard[i][j]!=null){
                    System.out.print("|"+cBoard[i][j].getPiece());
                }
                else{
                    System.out.print("|  ");
                }
                
            }
            System.out.println("|");
        }
        System.out.print("  ");
        for(int i = 0; i < 8; i++){
            System.out.print(" "+i+" ");
        }
        System.out.println();
    }

    public void movePiece() throws IOException {
        //get peice from location and to location
        validMove = false;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String directionPiece = userInput.readLine();
        directionPiece = directionPiece.replaceAll(" ", "");
        System.out.println("==============================");

        //grab from and to from the user input
        int fromY = Integer.parseInt(directionPiece.substring(0, 1));
        int fromX = Integer.parseInt(directionPiece.substring(1, 2));
        int toY = Integer.parseInt(directionPiece.substring(2, 3));
        int toX = Integer.parseInt(directionPiece.substring(3, 4));
        
        //Check coordinates for validity
        if(fromX > 7 || fromX < 0 || toX > 7 || toX < 0 || fromY > 7 || fromY < 0 || toY > 7 || toY < 0){
            System.out.println("Invalid coordinates, please try again");
            return;
        }
        
        //determine the content of the from location
        if (cBoard[fromX][fromY]==null) {
            System.out.println("No Piece at:" + fromX + fromY);
        } 
        
        //Space was not null, check to see if move is valid
        else {
            ChessPiece pieceFrom = cBoard[fromX][fromY];
            //Pawn
            if (pieceFrom.getPiece().substring(1, 2).equals("P")) {
                if(cBoard[toX][toY] == null && ((Pawn)cBoard[fromX][fromY]).validNKMove(fromX, fromY, toX, toY) && Math.abs(toY-fromY)==0){
                    if(isObstacleAt(fromX, fromY, toX, toY)){
                        validMove = false;
                        if(pieceFrom.getPiece().substring(1, 2).equals("B")){
                            
                        }
                    }
                    else{
                        validMove = true;
                    }
                }
                else if(((Pawn)cBoard[fromX][fromY]).validKillMove(cBoard[toX][toY]!=null, fromX, fromY, toX, toY) && !cBoard[toX][toY].getPiece().substring(1, 2).equals(turnColor)){
                    validMove = true;
                }
            }
            //King
            else if (pieceFrom.getPiece().substring(1, 2).equals("K")) {
                if(((King)cBoard[fromX][fromY]).validMove(fromX, fromY, toX, toY)){
                   validMove = true;
                }
            }
            //Rooke
            else if (pieceFrom.getPiece().substring(1, 2).equals("R")) {
                if(((Rook)cBoard[fromX][fromY]).validMove(fromX, fromY, toX, toY)){
                    validMove = true;
                }
            }
            //Bishop
            else if (pieceFrom.getPiece().substring(1, 2).equals("B")) {
                if(((Bishop)cBoard[fromX][fromY]).validMove(fromX, fromY, toX, toY)){
                    validMove = true;
                }
            }
            //Knight
            else if (pieceFrom.getPiece().substring(1, 2).equals("N")){
                if(((Knight)cBoard[fromX][fromY]).validMove(fromX, fromY, toX, toY)){
                    validMove = true;
                }
            }
            //Invalid move
            else{
                System.out.println("This is not a valid move, try again.");
            }
            //If move is valid, make the move
            if (validMove == true) {
                cBoard[toX][toY] = pieceFrom;
                cBoard[fromX][fromY] = null;
            }
        }
    }
 
    //Checks to see if there was an obstacle between the from point and to point
    public boolean isObstacleInbetween(int fromX, int fromY, int toX, int toY) {
        boolean obstacle = false;
        if (fromY == toY) { //up and down
            int xspace = Math.abs(fromX - toX) - 1;
            if (fromX - toX >= 0) { //Coming from black side
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (cBoard[fromX - i][fromY]!=null) {
                        obstacle = true;
                    }
                }
            } 
            else { //Coming from white side
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (cBoard[fromX + i][fromY]!=null) {
                        obstacle = true;
                    }
                }
            }
        } 
        else if (fromX == toX) { //left and right
            int yspace = Math.abs(fromY - toY) - 1;
            if (fromY - toY >= 0) { //Coming from right side
                for (int i = 1; i <= yspace && obstacle == false; i++) {
                    if (cBoard[fromX][fromY - i]!=null) {
                        obstacle = true;
                    }
                }
            } 
            else { //Coming from left side
                for (int i = 1; i <= yspace && obstacle == false; i++) {
                    if (cBoard[fromX][fromY + i]!=null) {
                        obstacle = true;
                    }
                }
            }
        } 
        else if ((toX > fromX && toY > fromX) || (toX < fromX && toY < fromX)) { //diagonal ie /
            int xspace = Math.abs(fromX - toX) - 1;
            int yspace = Math.abs(fromY - toY) - 1;
            if (fromX - toX >= 0) { //Going down-left 
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (cBoard[fromX - i][fromY - i]!=null) {
                        obstacle = true;
                    }
                }
            } 
            else { //Going up-right
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (cBoard[fromX + i][fromY + i]!=null) {
                        obstacle = true;
                    }
                }
            }
        } 
        else if ((toX > fromX && toY < fromX) || (toX < fromX && toY > fromX)) { //diagonal ie \
            int xspace = Math.abs(fromX - toX) - 1;
            int yspace = Math.abs(fromY - toY) - 1;
            if (fromX - toX >= 0) { //Going down-right 
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (cBoard[fromX - i][fromY + i]!=null) {
                        obstacle = true;
                    }
                }
            } 
            else { //Going up-right
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (cBoard[fromX + i][fromY - i]!=null) {
                        obstacle = true;
                    }
                }
            }
        }
        return obstacle;
    }
    
    //Check to see if there is an obstacle at the given point
    public boolean isObstacleAt(int fromX, int fromY, int toX, int toY){
        if(cBoard[toX][toY]!=null){
            return true;
        }
        return false;
    }
    
    //Check to see if piece at point is a King
    public boolean isKing(int x, int y){
        if(cBoard[x][y].getPiece().substring(1, 2).equals("K")){
            return true;
        }
            return false;
    }
    
    //Checks the current turn
    public void turnChecker() {
        if (validMove == true) {
            turn++;
        } else {
            //Invalid move so the turn does not change
        }
        if (turn % 2 == 0) {
            turnColor = "B";
        } else {
            turnColor = "W";
        }
        System.out.println(turnColor);
    }
}

////////////////////////////////////////////////////////////////////////////////

class ChessPiece{
    public ChessPiece(){
        
    }
    public String getPiece(){
        return null;
    } 
}
    
class Pawn extends ChessPiece{
    int fromX, toX, fromY, toY;
    char color;
    boolean firstMove = true;
    
    public Pawn(char color){
        this.color = color;
    }
    
    public boolean validNKMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        if(firstMove && this.color == 'W' && (this.toX - this.fromX ==2 || this.toX - this.fromX ==1) && this.toY - this.fromY == 0){
            firstMove = false;
            return true;
        }
        else if(this.color =='W' && this.toX - this.fromX ==1 && this.toY - this.fromY == 0){
            return true;
        }
        else if(firstMove && this.color == 'B' && (this.toX - this.fromX == -2 || this.toX - this.fromX == -1) && this.toY - this.fromY == 0){
            firstMove = false;
            return true;
        }
        else if(this.color == 'B' && this.toX - this.fromX == -1 && this.toY - this.fromY == 0){
            return true;
        }
        return false; 
    }
    
    public boolean validKillMove(boolean in, int fromX, int fromY, int toX, int toY){
        if(this.color == 'B' && in && Math.abs(toX - fromX) == Math.abs(toY - fromY) && toY - fromY == 1){
            return true;
        }
        else if(this.color == 'W' && in && Math.abs(toX - fromX) == Math.abs(toY - fromY) && toY - fromY == -1){
            return true;
        }
        return false;
    }
    
    @Override
    public String getPiece(){
        return this.color+"P";
    }
}

class Knight extends ChessPiece{
    int fromX, toX, fromY, toY;
    char color;
    public Knight(char color){
        this.color = color;
    }
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        return (Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 2) || (Math.abs(toY - fromY) == 1 && Math.abs(toX - fromX) == 2);
    }
    
    public String getPiece(){
        return this.color+"N";
    }
}

class Bishop extends ChessPiece{
    int fromX, toX, fromY, toY;
    char color;
    
    public Bishop(char color){
        this.color = color;
    }
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        return(Math.abs(toX - fromX) == Math.abs(toY - fromY) && toY >=0 && toY <= 7 && toX >= 0 && fromX <= 7);
    }
    
    public String getPiece(){
        return this.color+"B";
    }
}

class Rook extends ChessPiece{
    int fromX, toX, fromY, toY;
    char color;
    public Rook(char color){
        this.color = color;
    }
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        
        return (Math.abs(fromX - toX) != 0 && toY - fromY ==0) || (Math.abs(fromY - toY) != 0 && toX - fromX ==0) && toX >=0 &&toX <= 7 && toY >= 0 &&toY <= 7;
    }
    
    public String getPiece(){
        return this.color+"R";
    }
}

class King extends ChessPiece{
    int fromX, toX, fromY, toY;
    char color;
    boolean check;
    
    public King(char color){
        this.color = color;
    }
    
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        return (Math.abs(toX - fromX) == 1 || Math.abs(toY - fromY) == 1) && toX >=0 && toY >= 0 && toX <= 7 && toY <= 7;
    }
    
    public String getPiece(){
        return this.color+"K";
    }
    
    public boolean isInCheck(){
        return check;
    }
    
    public void setCheck(boolean check){
        this.check = check;
    }
}

class Queen extends ChessPiece{
    int fromX, toX, fromY, toY;
    char color;
    
    public Queen(char color){
        this.color = color;
    }
    
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        return (Math.abs(toX - fromX)!= 0 && Math.abs(toY - fromY) == 0) || (Math.abs(toY - fromY)!= 0 && Math.abs(toX - fromX) == 0) || Math.abs(toX - fromX) == Math.abs(toY - fromY) && toX >= 0 && toX <= 7 && toY >= 0 && toY <= 7;
    }
    
    public String getPiece(){
        return this.color+"Q";
    }
}