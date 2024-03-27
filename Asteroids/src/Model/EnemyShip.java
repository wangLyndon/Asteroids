package Model;

import java.awt.*;
import java.util.Random;

public class EnemyShip implements Hittable {
    private int x;
    private int y;
    private int width;
    private int height;
    private double angle;
    private double speedX;
    private double speedY;
    private int weaponCountdown;
    private boolean alive = true;
    private Rectangle hitbox;
    private EnemyType type;
    private Random random = new Random();
    private Player player;

    public EnemyShip(EnemyType type, int x, int y, double angle, Player player) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.type = type;
        this.player = player;
        speedX = Math.sin(angle) * type.getSpeed();
        speedY = Math.cos(angle) * type.getSpeed();
        width = 30 * type.getSize();
        height = 15 * type.getSize();
        hitbox = new Rectangle(x - 10 * type.getSize(), y, width, height);
    }

    public void tick() {
        // Set the firing interval of the bullet, and different for large and small types
        if (weaponCountdown > 0) {
            weaponCountdown--;
        } else {
            if (type == EnemyType.Large) {
                weaponCountdown = 80;
            } else if (type == EnemyType.Small) {
                weaponCountdown = 60;
            }
        }
    }

    public void move() {
        x = x + (int) speedX;
        y = y - (int) speedY;
        if (type == EnemyType.Large) {
            if (random.nextInt(1000) < 2) { // There is a 0.2% chance the trajectory will change
                angle = angle + Math.PI / 2;
                speedX = Math.sin(angle) * type.getSpeed();
                speedY = Math.cos(angle) * type.getSpeed();
            }
        } else if (type == EnemyType.Small) {
            int rand = random.nextInt(1000);
            // There is a 0.4% chance the trajectory will change, but in different directions
            if (rand < 2) {
                angle = angle + Math.PI / 2;
                speedX = Math.sin(angle) * type.getSpeed();
                speedY = Math.cos(angle) * type.getSpeed();
            } else if (rand < 4) {
                angle = angle - Math.PI / 2;
                speedX = Math.sin(angle) * type.getSpeed();
                speedY = Math.cos(angle) * type.getSpeed();
            }
        }
        hitbox = new Rectangle(x - 10 * type.getSize(), y, width, height); // Update hitbox
        checkOutOfArea();
    }

    public void checkOutOfArea() {
        if ((x + 12 * type.getSize()) < 0 || (x - 10 * type.getSize()) > Game.SCREEN_WIDTH || y > Game.SCREEN_HEIGHT || y + 15 * type.getSize() < 0) {
            alive = false; // If fly outside will die.
        }
    }

    public Bullet fire() {
        Bullet b = null;
        if (weaponCountdown == 0) {
            if (type == EnemyType.Large) {
                b = new Bullet(x + 10 * type.getSize(), y, random.nextDouble(Math.PI * 2)); // large ship bullets are fired at random
            } else if (type == EnemyType.Small) {
                // The Angle of a small ship bullet is calculated based on the slope of the ship's coordinates
                // and player's coordinates, which makes it more likely that the bullet will hit the player
                int playerX = player.getX();
                int playerY = player.getY();
                double angle = (Math.PI / 2 - Math.atan((double) (y - playerY) / (playerX - x)));
                if (x > playerX) {
                    angle = -Math.PI + angle;
                }
                b = new Bullet(x + 10 * type.getSize(), y, angle + random.nextDouble(Math.PI / 15) - Math.PI / 30);
            }
        }
        return b;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public int getPoints() {
        return type.getScore();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isHit(Bullet b) {
        Rectangle s = b.getHitBox();
        if (s.intersects(hitbox.getBounds())) {
            alive = false;
        }
        return s.intersects(hitbox.getBounds());
    }

    @Override
    public boolean isCrash(Hittable h) {
        Rectangle s = h.getHitBox();
        if (s.intersects(hitbox.getBounds())) {
            alive = false;
        }
        return s.intersects(hitbox.getBounds());
    }

    @Override
    public Rectangle getHitBox() {
        return hitbox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public EnemyType getType() {
        return type;
    }
}
