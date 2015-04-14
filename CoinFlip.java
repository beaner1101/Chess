/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;
import java.util.Random;
/**
 *
 * @author beaner
 */
public class CoinFlip {
    private Random rand;
    public CoinFlip(){
        rand = new Random();
    }
    public int flip(){
        return rand.nextInt(2);
    }
}
