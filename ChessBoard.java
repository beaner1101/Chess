package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math.*;

public class ChessBoard {

    private String[][] cBoard = new String[8][8];
    private Boolean validMove = false;
    private int turn = 0;
    private String turnColor = "W";

    public static void main(String[] args) throws IOException {
        ChessBoard cB = new ChessBoard();
        cB.fillBoard();
        cB.displayBoard();
        while (true) {
            cB.movePiece();
            cB.turnChecker();
            cB.validMove = false;
        }
    }

    public void fillBoard() {
        //fill board -> White
        cBoard[0][0] = "WC";
        cBoard[0][7] = "WC";
        cBoard[0][1] = "WR";
        cBoard[0][6] = "WR";
        cBoard[0][2] = "WN";
        cBoard[0][5] = "WN";
        cBoard[0][3] = "WK";
        cBoard[0][4] = "WQ";
        for (int a = 0; a < 8; a++) {
            cBoard[1][a] = "WP";
        }

        //fill board -> Black
        cBoard[7][0] = "BC";
        cBoard[7][7] = "BC";
        cBoard[7][1] = "BR";
        cBoard[7][6] = "BR";
        cBoard[7][2] = "BN";
        cBoard[7][5] = "BN";
        cBoard[7][3] = "BK";
        cBoard[7][4] = "BQ";
        for (int a = 0; a < 8; a++) {
            cBoard[6][a] = "BP";
        }

        //fill board -> blank space
        for (int b = 2; b < 6; b++) {
            for (int c = 0; c < 8; c++) {
                cBoard[b][c] = "* ";
            }
        }
    }

    public void displayBoard() {
        //Display board
        for (int x = 7; x >= 0; x--) {
            System.out.print(x+" ");
            for (int y = 0; y < 8; y++) {
                System.out.print(cBoard[x][y] + "  ");
            }
            System.out.println();
        }
        System.out.print("  ");
        for(int i = 0; i < 8; i++){
            System.out.print(i+"   ");
        }
        System.out.println();
    }

    public void movePiece() throws IOException {
        //get peice from location and to location
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String directionPiece = userInput.readLine();
        directionPiece = directionPiece.replaceAll(" ", "");
        System.out.println("==============================");

        //grab from and to from the user input
        int fromX = Integer.parseInt(directionPiece.substring(0, 1));
        int fromY = Integer.parseInt(directionPiece.substring(1, 2));
        int toX = Integer.parseInt(directionPiece.substring(2, 3));
        int toY = Integer.parseInt(directionPiece.substring(3, 4));

        //determine the content of the from location
        if (cBoard[fromY][fromX].equals("* ")) {
            System.out.println("No Piece at:" + fromX + fromY);
        } else {
            //determine if from piece color is shared with to piece color
            String pieceFrom = cBoard[fromY][fromX];
            if (pieceFrom.substring(0, 1).equals(turnColor)) {
                String pieceTo = cBoard[toY][toX];
                if (pieceFrom.substring(0, 1).equals(pieceTo.substring(0, 1))) {
                    System.out.println("Traitorous move detected. They trusted you!");
                } else {
                    //move checker
                    if (pieceFrom.substring(1, 2).equals("P")) {
                        possiblePawn(fromX, fromY, toX, toY, pieceFrom.substring(0, 1), pieceTo);
                    }
                    if (pieceFrom.substring(1, 2).equals("K")) {
                        possibleKing(fromX, fromY, toX, toY);
                    }
                    if (pieceFrom.substring(1, 2).equals("R")) {
                        possibleRook(fromX, fromY, toX, toY, pieceFrom.substring(0, 1), pieceTo);
                    }
                    if (pieceFrom.substring(1, 2).equals("B")) {
                        possibleBishop(fromX, fromY, toX, toY, pieceFrom.substring(0, 1), pieceTo);
                    }
                    if (pieceFrom.substring(1, 2).equals("N")){
                        possibleKnight(fromX, fromY, toX, toY);
                    }
                    if (validMove == true) {
                        cBoard[fromY][fromX] = "* ";
                        cBoard[toY][toX] = pieceFrom;
                        displayBoard();
                    }
                }
            } else {
                System.out.println("Not your turn");
            }

        }
    }

    public boolean isObstacleInbetween(int fromX, int fromY, int toX, int toY) {
        boolean obstacle = false;
        if (fromY == toY) { //up and down
            int xspace = Math.abs(fromX - toX) - 1;
            if (fromX - toX >= 0) { //Coming from black side
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (!cBoard[fromX - i][fromY].contains("*")) {
                        obstacle = true;
                    }
                }
            } else { //Coming from white side
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (!cBoard[fromX + i][fromY].contains("*")) {
                        obstacle = true;
                    }
                }
            }
        } else if (fromX == toX) { //left and right
            int yspace = Math.abs(fromY - toY) - 1;
            if (fromY - toY >= 0) { //Coming from right side
                for (int i = 1; i <= yspace && obstacle == false; i++) {
                    if (!cBoard[fromX][fromY - i].contains("*")) {
                        obstacle = true;
                    }
                }
            } else { //Coming from left side
                for (int i = 1; i <= yspace && obstacle == false; i++) {
                    if (!cBoard[fromX][fromY + i].contains("*")) {
                        obstacle = true;
                    }
                }
            }
        } else if ((toX > fromX && toY > fromX) || (toX < fromX && toY < fromX)) { //diagonal ie /
            int xspace = Math.abs(fromX - toX) - 1;
            int yspace = Math.abs(fromY - toY) - 1;
            if (fromX - toX >= 0) { //Going down-left 
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (!cBoard[fromX - i][fromY - i].contains("*")) {
                        obstacle = true;
                    }
                }
            } else { //Going up-right
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (!cBoard[fromX + i][fromY + i].contains("*")) {
                        obstacle = true;
                    }
                }
            }
        } else if ((toX > fromX && toY < fromX) || (toX < fromX && toY > fromX)) { //diagonal ie \
            int xspace = Math.abs(fromX - toX) - 1;
            int yspace = Math.abs(fromY - toY) - 1;
            if (fromX - toX >= 0) { //Going down-right 
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (!cBoard[fromX - i][fromY + i].contains("*")) {
                        obstacle = true;
                    }
                }
            } else { //Going up-right
                for (int i = 1; i <= xspace && obstacle == false; i++) {
                    if (!cBoard[fromX + i][fromY - i].contains("*")) {
                        obstacle = true;
                    }
                }
            }
        }
        return obstacle;
    }

    public void possibleBishop(int fromX, int fromY, int toX, int toY, String color, String pieceTo) {
        if (fromX == toX || fromY == toY) {
            validMove = false;
            System.out.println("Invalid Bishop Move: Cannot move up/down/left/right.");
        } else if (isObstacleInbetween(fromX, fromY, toX, toY)) {
            validMove = false;
            System.out.println("Invalid Bishop Move: Obstacle in the way.");
        } else {
            // Move without taking a piece
            if (pieceTo.contains("*")) {
                validMove = true;
                System.out.println("Valid Bishop Move: Non Homicidal");
            } else { //Taking piece 
                validMove = true;
                System.out.println("Valid Bishop Move: Homicidal");
            }
        }
    }

    public void possibleRook(int fromX, int fromY, int toX, int toY, String color, String pieceTo) {
        if (fromX != toX && fromY != toY) {
            validMove = false;
            System.out.println("Invalid Rook Move: Cannot move diagonal.");
        } else if (isObstacleInbetween(fromX, fromY, toX, toY)) {
            validMove = false;
            System.out.println("Invalid Rook Move: Obstacle in the way.");
        } else {
            // Move without taking a piece
            if (pieceTo.contains("*")) {
                validMove = true;
                System.out.println("Valid Rook Move: Non Homicidal");
            } else { //Taking piece 
                validMove = true;
                System.out.println("Valid Rook Move: Homicidal");
            }
        }
    }

    public void possibleKnight(int fromX, int fromY, int toX, int toY) {
        if ((Math.abs(fromX - toX) == 1 && Math.abs(fromY - toY) == 3) || (Math.abs(fromY - toY) == 1 && Math.abs(fromX - toX) == 3)) {
            System.out.println("Valid move.");
            validMove = true;
            if(isKing(toX+3, toY+1) || isKing(toX+3, toY-1) || isKing(toX-3, toY+1) || isKing(toX-3, toY-1)){
                if(turnColor.equals('W')){
                System.out.println("Black King in check");
            }
            else{
                System.out.println("White King in check");
            }
            }
        }
        else{
            System.out.println("Invalid move.");
            validMove = false;
        }
    }
    //Chad

    public void possibleKing(int fromX, int fromY, int toX, int toY) {
        if (Math.abs(toY - fromY) == 1 && toX == fromX) {
            System.out.println("Valid King Move: 1 Vertical");
            validMove = true;
        } else if (Math.abs(toX - fromX) == 1 && toY == fromY) {
            System.out.println("Valid King Move: 1 Horizontal");
            validMove = true;
        } else if (Math.abs(toY - fromY) == 1 && Math.abs(toX - fromX) == 1) {
            System.out.println("Valid King Move: 1 Diagonal");
            validMove = true;
        } else {
            System.out.println("King at " + fromX + fromY + " to " + toX + toY + " is an invalid move.");
        }

    }
    //Curtis

    public void possiblePawn(int fromX, int fromY, int toX, int toY, String color, String pieceTo) {
        int row;
        int move;

        //determine direction of possible moves
        if (color.equals("W")) {
            row = 1;
            move = 1;
        } else {
            row = 6;
            move = -1;
        }

        //pawn moves if not taking another piece
        if (pieceTo.equals("* ")) {
            if (fromX == toX) {
                if (toY == (fromY + (1 * move))) {
                    System.out.println("Valid Pawn Move: 1 Space Non Homocidal");
                    validMove = true;
                    if(isKing(toX+1, toY+1) || isKing(toX-1, toY+1)){
                        if(turnColor.equals('W')){
                            System.out.println("Black King in check");
                        }
                        else{
                            System.out.println("White King in check");
                        }
                    }
                } else if (fromY == row && toY == (fromY + (2 * move))) {
                    if (cBoard[fromY + (1 * move)][fromX].equals("* ")) {
                        System.out.println("Valid Pawn Move: Did not jump piece");
                        System.out.println("Valid Pawn Move: 2 Spaces Non Homocidal");
                        validMove = true;
                        if(isKing(toX+1, toY+1) || isKing(toX-1, toY+1)){
                            if(turnColor.equals('W')){
                                System.out.println("Black King in check");
                            }
                            else{
                                System.out.println("White King in check");
                            }
                        }
                    } else {
                        System.out.println("Pawn at " + fromX + fromY + " to " + toX + toY + " is an invalid move.");
                    }
                } else {
                    System.out.println("Pawn at " + fromX + fromY + " to " + toX + toY + " is an invalid move.");
                }
            } else {
                System.out.println("Pawn at " + fromX + fromY + " to " + toX + toY + " is an invalid move.");
            }
        } //pawn move if taking another piece
        else {
            if (fromX == toX + 1 || fromX == toX - 1) {
                if (toY == (fromY + (1 * move))) {
                    System.out.println("Valid Pawn Move: 1 Space Homocidal");
                    validMove = true;
                    if(isKing(toX+1, toY+1) || isKing(toX-1, toY+1)){
                        if(turnColor.equals('W')){
                            System.out.println("Black King in check");
                        }
                        else{
                            System.out.println("White King in check");
                        }
                    }
                } else {
                    System.out.println("Pawn at " + fromX + fromY + " to " + toX + toY + " is an invalid move.");
                }
            } else {
                System.out.println("Pawn at " + fromX + fromY + " to " + toX + toY + " is an invalid move.");
            }
        }
    }
    //Curtis

    public void turnChecker() {
        if (validMove == true) {
            turn++;
        } else {
            //Invalid move so the turn does not change
        }
        if (turn % 2 == 0) {
            turnColor = "W";
        } else {
            turnColor = "B";
        }
    }

    public boolean isKing(int x, int y){
        if(x > 7 || y > 7 || x < 0 || y < 0){
            return false;
        }
        if(cBoard[x][y].substring(1, 2).equals("K") && !cBoard[x][y].substring(0, 1).equals(turnColor)){
            return true;
        }
        return false;
    }
}

class Pawn{
    int fromX, toX, fromY, toY;
    char color;
    public Pawn(char color){
        this.color = color;
    }
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        return true;
    }
}

class Knight{
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
        return true;
    }
    
}

class Bishop{
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
        return true;
    }
}

class Rook{
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
        return true;
    }
}

class King{
    int fromX, toX, fromY, toY;
    char color;
    public King(char color){
        this.color = color;
    }
    public boolean validMove(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        return true;
    }
}

class Queen{
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
        return true;
    }
}