package Screen;

import Model.Game;

import javax.swing.*;
import java.awt.*;

public class AboutScreen extends JPanel {
    public void paintComponent(Graphics g){
        // Set color background and size of window
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        // Set the font
        g.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, 32);
        g.setFont(font);
        g.drawString("Asteroids Controls", 360, 200);
        font = new Font("Arial", Font.BOLD, 18);
        g.setFont(font);
        g.drawString("Move forward", 200, 300);
        g.drawString("Up arrow", 700, 300);
        g.drawString("Counterclockwise Rotation", 200, 400);
        g.drawString("Left arrow", 700, 400);
        g.drawString("Clockwise Rotation", 200, 500);
        g.drawString("right arrow", 700, 500);
        g.drawString("Fire", 200, 600);
        g.drawString("Space", 700, 600);
        font = new Font("Arial", Font.BOLD, 28);
        g.setFont(font);
        g.drawString("Press 'M' to return to the Main Menu", 280, 700);
    }
}
