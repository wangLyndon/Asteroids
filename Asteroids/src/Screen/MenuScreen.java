package Screen;

import Model.Game;

import javax.swing.*;
import java.awt.*;

public class MenuScreen extends JPanel {
    public void paintComponent(Graphics g){
        // Set color background and size of window
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        // Set the font
        g.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, 32);
        g.setFont(font);
        g.drawString("Welcome to Asteroids !!!", 300, 200);
        font = new Font("Arial", Font.BOLD, 26);
        g.setFont(font);
        g.drawString("To start a game press N", 340, 300);
        g.drawString("To see the controls press A", 320, 400);
        g.drawString("To see the High scores press H", 310, 500);
        g.drawString("To exit press X", 380, 600);
    }
}
