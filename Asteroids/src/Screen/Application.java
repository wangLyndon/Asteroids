package Screen;

import Model.Game;
import ucd.comp2011j.engine.GameManager;
import ucd.comp2011j.engine.ScoreKeeper;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        JFrame mainWindow = new JFrame();
        mainWindow.setSize(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("Asteroids Game");
        mainWindow.setLocationRelativeTo(null);

        PlayerListener playerListener = new PlayerListener();
        mainWindow.addKeyListener(playerListener);
        MenuListener menuListener = new MenuListener();
        mainWindow.addKeyListener(menuListener);
        Game game = new Game(playerListener);
        GameScreen gameScreen = new GameScreen(game);
        MenuScreen menuScreen = new MenuScreen();
        ScoreKeeper scoreKeeper = new ScoreKeeper("scores.txt");
        GameManager gameManager = new GameManager(game, mainWindow, menuListener, menuScreen, new AboutScreen(), new ScoreScreen(scoreKeeper), gameScreen, scoreKeeper);
        mainWindow.setVisible(true);
        gameManager.run();
    }
}
