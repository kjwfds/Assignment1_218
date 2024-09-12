/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pong;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;




/**
 *
 * @author c3rea
 */
public class Score extends Rectangle {
    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    int player1;
    int player2;
    
    Score(int GAME_WIDTH,int GAME_HEIGHT){
    Score.GAME_WIDTH = GAME_WIDTH;
    Score.GAME_HEIGHT = GAME_HEIGHT;
    }
    
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("Consolas",Font.PLAIN,60));
        
       
          Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{25}, 30);
        g2d.setStroke(dashed);
        g2d.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
        
        g.drawString(String.valueOf(player1/10) + String.valueOf(player1%10), (GAME_WIDTH/2)-85, 50);
        g.drawString(String.valueOf(player2/10) + String.valueOf(player2%10), (GAME_WIDTH/2)+20, 50);
    }
}
