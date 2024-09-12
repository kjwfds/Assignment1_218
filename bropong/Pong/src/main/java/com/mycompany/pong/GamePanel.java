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
public class GamePanel extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.555)); //recreate ping ptanle ration(5:9)
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100; 
    static final int FPS = 60;
    
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball ;
    Score score; 
    
    
    GamePanel(){
    newPaddles();
    newBall();
    score = new Score( GAME_WIDTH, GAME_HEIGHT);
    this.setFocusable(true);
    this.addKeyListener(new actionListener());
    this.setPreferredSize(SCREEN_SIZE);
    
    gameThread = new Thread(this);
    gameThread.start();
    
    }
    public void newBall(){
    random  = new Random();
    ball = new Ball((GAME_WIDTH/2) - (BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT  - BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }   
    
    public void newPaddles(){
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }
    
    public void paint(Graphics g){
    image = createImage(getWidth(),getHeight());
    graphics = image.getGraphics();
    draw(graphics);
    
    g.drawImage(image, 0, 0, this);
    }
    
    public void draw(Graphics g){
    paddle1.draw(g);
    paddle2.draw(g);
    ball.draw(g);
    score.draw(g);
    }
    
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    
    public void checkCollision(){
    //stop paddles at window edges
        if(paddle1.y<= 0 ){
            paddle1.y = 0;
        }
        if(paddle1.y>= GAME_HEIGHT - PADDLE_HEIGHT){
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        if(paddle2.y<= 0 ){
            paddle2.y = 0;
        }
        if(paddle2.y>= GAME_HEIGHT - PADDLE_HEIGHT){
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
     //bounce ball of edges
     if(ball.y<=0){
       ball.setYDirection(-ball.yVelocity);
     }
     if(ball.y >= GAME_HEIGHT - BALL_DIAMETER){
       ball.setYDirection(-ball.yVelocity);
     }
    
     //bounce ball of paddles
     if(ball.intersects(paddle1)){
         ball.xVelocity =  (-1 * ball.xVelocity);
         ball.xVelocity++;
         
         if(ball.yVelocity > 0){
         ball.yVelocity++;
            }
         else{
             ball.yVelocity--;
         }
        ball.setXDirection(ball.xVelocity);    
        ball.setYDirection(ball.yVelocity);
     }
    if(ball.intersects(paddle2)){
         ball.xVelocity =  (Math.abs(ball.xVelocity));
         ball.xVelocity++;
         
         if(ball.yVelocity > 0){
         ball.yVelocity++;
            }
         else{
             ball.yVelocity--;
         }
        ball.setXDirection(- ball.xVelocity);    
        ball.setYDirection(ball.yVelocity);
     }     
    
    //set score
    if(ball.x >= GAME_WIDTH-BALL_DIAMETER ){
        score.player1++;
        newPaddles();
        newBall();
    }
    if(ball.x <= 0){
        newPaddles();
        newBall();
        score.player2++;
     
    }
    }
    
    
    public void run(){
        //game loop
        double drawInterval = 1000000000/FPS; // 1 second/60 =  (0.01666 seconds)
        double nextDrawTime = System.nanoTime() + drawInterval; //draw  at current time +interval
		
	while(gameThread != null) { //repeats as long as game thread exists
			
					

          
		
		repaint();//call paintComponent
		checkCollision();
                move();

		
		try {
                    double remainingTime = nextDrawTime - System.nanoTime();  //check how much time is left from the next draw interval after calling paint and update		
                    remainingTime = remainingTime/1000000;//convert nano to milli seconds

                        if(remainingTime < 0) { // if the draw interval has been passed
                                remainingTime = 0; //set remaining time to 0
                        }

                        Thread.sleep((long) remainingTime ); // thread stops till the draw interval

                        nextDrawTime = System.nanoTime() + drawInterval; // set nextDrawTime once thread is active
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	   	}
    }
    
    public class actionListener extends KeyAdapter{
    
        public void keyPressed(KeyEvent e){
        paddle1.keyPressed(e);
        paddle2.keyPressed(e);
        
        }
        
        public void keyReleased(KeyEvent e){
        paddle1.keyReleased(e);
        paddle2.keyReleased(e);
        }
    }
    
}
