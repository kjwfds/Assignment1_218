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
public class GameFrame extends JFrame {
    GamePanel gamePanel;
    
    
    GameFrame(){
    gamePanel = new GamePanel();
   
    this.add(gamePanel);
    this.setTitle("Ping Pong Game");
    this.setBackground(Color.BLACK);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    }
    
    
   
}
