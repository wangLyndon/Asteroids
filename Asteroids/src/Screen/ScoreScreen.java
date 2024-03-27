package Screen;

import Model.Game;
import ucd.comp2011j.engine.Score;
import ucd.comp2011j.engine.ScoreKeeper;

import javax.swing.*;
import java.awt.*;

public class ScoreScreen extends JPanel {
    private ScoreKeeper sk;
    public ScoreScreen(ScoreKeeper sk){
        this.sk = sk;
    }

    public void paintComponent(Graphics g){
        // Set color background and size of window
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        // Set the font
        g.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, 32);
        g.setFont(font);
        g.drawString("Asteroids Hall of Fame", 330, 200);
        font = new Font("Arial", Font.BOLD, 18);
        g.setFont(font);
        Score[] scores = sk.getScores();
        for (int i = 0; i != scores.length; i++) {
            g.drawString(scores[i].getName() + ": " + scores[i].getScore(), 420, 260 + (40 * i));
        }
        font = new Font("Arial", Font.BOLD, 28);
        g.setFont(font);
        g.drawString("Press 'M' to return to the Main Menu", 280, 700);
    }
}
