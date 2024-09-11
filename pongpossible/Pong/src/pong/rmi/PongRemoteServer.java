package pong.rmi;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import pong.rmi.gameobjects.GameState;

public class PongRemoteServer extends UnicastRemoteObject implements IServer {

    private static final long serialVersionUID = 1L;

    private GameState gameState;
    private Paddle player1, player2;
    private Ball ball;

    private final int pongWidth = 700, pongHeight = 700;
    private int scoreLimit = 11;
    private int playerWon;

    private int controllingPlayer = 1; // Default to player 1

    private boolean w, s, up, down;

    public PongRemoteServer() throws RemoteException {
        this.gameState = GameState.Menu;
    }

    @Override
    public GameState gameState() throws RemoteException {
        return this.gameState;
    }

    @Override
    public void updateGameState(GameState state) throws RemoteException {
        this.gameState = state;
    }

    @Override
    public void start() throws RemoteException {
        gameState = GameState.Playing;
        player1 = new Paddle(pongWidth, pongHeight, 1);
        player2 = new Paddle(pongWidth, pongHeight, 2);
        ball = new Ball(pongWidth, pongHeight);
        playerWon = 0; // Reset the winner
    }

    @Override
    public Dimension getPongSize() throws RemoteException {
        return new Dimension(pongWidth, pongHeight);
    }

    @Override
    public void update(int playerId) throws RemoteException {
        if (gameState != GameState.Playing) {
            return;
        }

        // Check for win conditions
        if (player1.score >= scoreLimit) {
            playerWon = 1;
            gameState = GameState.Over;
        }

        if (player2.score >= scoreLimit) {
            playerWon = 2;
            gameState = GameState.Over;
        }

        // Update player positions based on control ID
        if (playerId == 1) {
            if (w) {
                player1.move(true);
            }
            if (s) {
                player1.move(false);
            }
        } else if (playerId == 2) {
            if (up) {
                player2.move(true);
            }
            if (down) {
                player2.move(false);
            }
        }

        // Update ball position
        ball.update(player1, player2);
    }

    @Override
    public void keyPressed(int id) throws RemoteException {
        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) {
            up = true;
        } else if (id == KeyEvent.VK_DOWN) {
            down = true;
        }

        switch (id) {
            case KeyEvent.VK_RIGHT:
                if (gameState == GameState.Menu) {
                    scoreLimit++;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (gameState == GameState.Menu && scoreLimit > 1) {
                    scoreLimit--;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (gameState == GameState.Playing || gameState == GameState.Over) {
                    gameState = GameState.Menu;
                }
                break;
            case KeyEvent.VK_SPACE:
                if (gameState == GameState.Menu || gameState == GameState.Over) {
                    start();
                } else if (gameState == GameState.Paused) {
                    gameState = GameState.Playing;
                } else if (gameState == GameState.Playing) {
                    gameState = GameState.Paused;
                }
                break;
        }
    }

    @Override
    public void keyReleased(int id) throws RemoteException {
        if (id == KeyEvent.VK_W) {
            w = false;
        } else if (id == KeyEvent.VK_S) {
            s = false;
        } else if (id == KeyEvent.VK_UP) {
            up = false;
        } else if (id == KeyEvent.VK_DOWN) {
            down = false;
        }
    }

    @Override
    public void setScoreLimit(int score) throws RemoteException {
        scoreLimit = score;
    }

    @Override
    public int getScoreLimit() throws RemoteException {
        return scoreLimit;
    }

    @Override
    public Paddle getPlayer(int number) throws RemoteException {
        if (number == 1) {
            return player1;
        } else if (number == 2) {
            return player2;
        }
        return null;
    }

    @Override
    public Ball getBall() throws RemoteException {
        return ball;
    }

    @Override
    public int getPlayerWon() throws RemoteException {
        return playerWon;
    }

    @Override
    public void setPlayerControlling(int playerId) throws RemoteException {
        this.controllingPlayer = playerId;
    }
}
