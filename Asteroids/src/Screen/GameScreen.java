package Screen;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameScreen extends JPanel {
    private Game game;

    public GameScreen(Game game){
        this.game = game;
    }

    public void paintComponent(Graphics g){
        if (game != null){
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            g.setColor(Color.WHITE);
            Font font = new Font("Arial", Font.BOLD, 20);
            g.setFont(font);
            g.drawString("Score: " + game.getPlayerScore(), 10, 20);
            for (int i = 0; i != game.getLives(); i++) {
                g.drawLine(20 + (i * 50) + (int) (Math.sin(0) * 25), 50 - (int) (Math.cos(0) * 25), 20 + (i * 50) + (int) (Math.sin(0 + 5 * Math.PI / 6) * 25), 50 - (int) (Math.cos(0 + 5 * Math.PI / 6) * 25));
                g.drawLine(20 + (i * 50) + (int) (Math.sin(0) * 25), 50 - (int) (Math.cos(0) * 25), 20 + (i * 50) + (int) (Math.sin(0 + 7 * Math.PI / 6) * 25), 50 - (int) (Math.cos(0 + 7 * Math.PI / 6) * 25));
                g.drawLine(20 + (i * 50) + (int) (Math.sin(0 + 5 * Math.PI / 6) * 15), 50 - (int) (Math.cos(0 + 5 * Math.PI / 6) * 15), 20 + (i * 50) + (int) (Math.sin(0 + 7 * Math.PI / 6) * 15), 50 - (int) (Math.cos(0 + 7 * Math.PI / 6) * 15));

            }
            drawPlayerShip(g, game.getPlayer());
            for (Asteroid asteroid : game.getAsteroidList()) {
                drawAsteroids(g, asteroid);
            }
            for (Bullet bullet : game.getPlayerBullets()) {
                drawBullets(g, bullet);
            }
            for (Bullet bullet : game.getEnemyBullets()) {
                drawBullets(g, bullet);
            }
            for (EnemyShip enemyShip : game.getEnemyShipList()) {
                drawEnemyShip(g, enemyShip);
            }

            if (game.isPaused() && !game.isGameOver()) {
                g.setColor(Color.WHITE);
                g.drawString("Press 'p' to continue ", Game.SCREEN_WIDTH / 2 - 100, Game.SCREEN_HEIGHT / 2 - 100);
            } else if (game.getPlayer().isInvincible()) {
                g.setColor(Color.WHITE);
                g.drawString("In invincibility", Game.SCREEN_WIDTH / 2 - 100, Game.SCREEN_HEIGHT / 2 - 100);
            } else if (game.isGameOver()) {
                g.setColor(Color.WHITE);
                g.drawString("Game over ", Game.SCREEN_WIDTH / 2 - 100, Game.SCREEN_HEIGHT / 2 - 100);
            }
        }
    }

    public void drawPlayerShip(Graphics g, Player player){
        int x = player.getX();
        int y = player.getY();
        double angle = player.getAngle();
        g.setColor(Color.WHITE);
        g.drawLine(x + (int) (Math.sin(angle) * 25), y - (int) (Math.cos(angle) * 25), x + (int) (Math.sin(angle + 5 * Math.PI / 6) * 25), y - (int) (Math.cos(angle + 5 * Math.PI / 6) * 25));
        g.drawLine(x + (int) (Math.sin(angle) * 25), y - (int) (Math.cos(angle) * 25), x + (int) (Math.sin(angle + 7 * Math.PI / 6) * 25), y - (int) (Math.cos(angle + 7 * Math.PI / 6) * 25));
        g.drawLine(x + (int) (Math.sin(angle + 5 * Math.PI / 6) * 15), y - (int) (Math.cos(angle + 5 * Math.PI / 6) * 15), x + (int) (Math.sin(angle + 7 * Math.PI / 6) * 15), y - (int) (Math.cos(angle + 7 * Math.PI / 6) * 15));
        if (player.getFlames() % 4 == 1){ // Draw every 4 seconds to create a flicker effect
            g.setColor(Color.RED);
            g.drawLine(x + (int) (Math.sin(angle + Math.PI) * 20), y - (int) (Math.cos(angle + Math.PI) * 20), x + (int) (Math.sin(angle + 5 * Math.PI / 6) * 15), y - (int) (Math.cos(angle + 5 * Math.PI / 6) * 15));
            g.drawLine(x + (int) (Math.sin(angle + Math.PI) * 20), y - (int) (Math.cos(angle + Math.PI) * 20), x + (int) (Math.sin(angle + 7 * Math.PI / 6) * 15), y - (int) (Math.cos(angle + 7 * Math.PI / 6) * 15));
        }
    }

    public void drawEnemyShip(Graphics g, EnemyShip enemyShip){
        int x = enemyShip.getX();
        int y = enemyShip.getY();
        EnemyType type = enemyShip.getType();
        int size = type.getSize();
        g.setColor(Color.WHITE);
        g.drawLine(x, y, x + 10 * size, y);
        g.drawLine(x, y, x - 2 * size, y + 5 * size);
        g.drawLine(x + 10 * size, y, x + 12 * size, y + 5 * size);
        g.drawLine(x - 2 * size, y + 5 * size, x + 12 * size, y + 5 * size);
        g.drawLine(x - 2 * size, y + 5 * size, x - 10 * size, y + 10 * size);
        g.drawLine(x + 12 * size, y + 5 * size, x + 20 * size, y + 10 * size);
        g.drawLine(x - 10 * size, y + 10 * size, x + 20 * size, y + 10 * size);
        g.drawLine(x - 10 * size, y + 10 * size, x - 2 * size, y + 15 * size);
        g.drawLine(x + 20 * size, y + 10 * size, x + 12 * size, y + 15 * size);
        g.drawLine(x - 2 * size, y + 15 * size, x + 12 * size, y + 15 * size);
    }

    public void drawBullets(Graphics g, Bullet b){
        g.setColor(Color.WHITE);
        g.fillOval(b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }

    public void drawAsteroids(Graphics g, Asteroid asteroid){
        g.setColor(Color.WHITE);
        g.drawPolygon(asteroid.getShape());
    }
}
