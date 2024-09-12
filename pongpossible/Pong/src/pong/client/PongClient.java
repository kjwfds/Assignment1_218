package pong.client;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;
import javax.swing.*;
import pong.rmi.Ball;
import pong.rmi.IServer;
import pong.rmi.Paddle;
import pong.rmi.gameobjects.GameState;

public class PongClient implements KeyListener {
    private int playerID; // Add this to track the player ID
    private IServer remote;
    public Renderer renderer;
    public int width, height;
    public Random random;
    public JFrame jframe;
    private Thread gameThread;
    private boolean running = false;
    private final long drawInterval = 1000000000 / 60; // 60 FPS
    private long nextDrawTime = System.nanoTime();

    public PongClient(IServer remote, int playerID) throws RemoteException {
        this.remote = remote;
        this.playerID = playerID; // Set the player ID

        remote.setPlayerControlling(playerID); // Notify server of the controlling player ID

        Dimension pongSize = remote.getPongSize();
        width = pongSize.width;
        height = pongSize.height;

        random = new Random();
        renderer = new Renderer(this);

        jframe = new JFrame("Pong");
        jframe.setSize(width + 15, height + 35);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderer);
        jframe.addKeyListener(this);

        JMenuBar jmb = new JMenuBar();
        jframe.setJMenuBar(jmb);

        JMenu file = new JMenu("File");
        jmb.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);

        exit.addActionListener(e -> System.exit(0));

        startGameLoop();
    }

    private void startGameLoop() {
        running = true;
        gameThread = new Thread(() -> {
            while (running) {
                long startTime = System.nanoTime();
                updateGame();
                repaint();
                long elapsedTime = System.nanoTime() - startTime;
                long remainingTime = drawInterval - elapsedTime;
                if (remainingTime > 0) {
                    try {
                        Thread.sleep(remainingTime / 1000000); // Convert nano to milli
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                nextDrawTime = System.nanoTime() + drawInterval;
            }
        });
        gameThread.start();
    }

    private void updateGame() {
        try {
            remote.update(playerID); // Pass player ID to update method
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            remote.keyPressed(e.getKeyCode());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            remote.keyReleased(e.getKeyCode());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void repaint() {
        renderer.repaint();
    }

    public void render(Graphics g0) throws RemoteException {
        Graphics2D g = (Graphics2D) g0;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GameState gameState = remote.gameState();

        if (gameState == GameState.Menu) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("PONG", width / 2 - 75, 50);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Press Space to Play", width / 2 - 150, height / 2 - 25);
            g.drawString("<< Score Limit: " + remote.getScoreLimit() + " >>", width / 2 - 150, height / 2 + 75);
        } else if (gameState == GameState.Paused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("PAUSED", width / 2 - 103, height / 2 - 25);
        } else if (gameState == GameState.Playing || gameState == GameState.Paused) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(5f));
            g.drawLine(width / 2, 0, width / 2, height);
            g.setStroke(new BasicStroke(2f));
            g.drawOval(width / 2 - 150, height / 2 - 150, 300, 300);
            g.setFont(new Font("Arial", Font.BOLD, 50));

            Paddle p1 = remote.getPlayer(1);
            Paddle p2 = remote.getPlayer(2);
            g.drawString(String.valueOf(p1.score), width / 2 - 90, 50);
            g.drawString(String.valueOf(p2.score), width / 2 + 65, 50);
            g.fillRect(p1.x, p1.y, p1.width, p1.height);
            g.fillRect(p2.x, p2.y, p2.width, p2.height);

            Ball ball = remote.getBall();
            g.fillOval(ball.x, ball.y, ball.width, ball.height);
        } else if (gameState == GameState.Over) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("PONG", width / 2 - 75, 50);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
            g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);
        }
    }

    public static void main(String[] args) {
        try {
            IServer remoteServer = (IServer) Naming.lookup("rmi://192.168.108.220:1099/RegisterInterface");
            int playerID = 2; // Set this to 1 or 2 based on which player this client is
            new PongClient(remoteServer, playerID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
