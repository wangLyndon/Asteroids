package Model;

import java.awt.*;
import java.util.Random;

public class Asteroid implements Hittable{

    private int x;
    private int y;
    private double angle;
    private double speed;
    private double speedX;
    private double speedY;
    private Polygon shape;
    // The shape of the asteroid
    private int[] xPoints = {25, 13, 9, -2, -13, -14, -25, -13, -3, 7, 21};
    private int[] yPoints = {0, 8, 21, 16, 15, 4, -7, -15, -25, -17, -13};
    private boolean alive;
    private AsteroidType type;
    private Random random = new Random();
    private Rectangle hitbox;

    public Asteroid(int x, int y, AsteroidType type, double angle, double speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
        alive = true;
        this.angle = angle;
        this.type = type;
        speedX = Math.sin(angle) * speed;
        speedY = Math.cos(angle) * speed;
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = xPoints[i] * type.getSize() + x;
            yPoints[i] = yPoints[i] * type.getSize() + y;
        }
        shape = new Polygon(xPoints, yPoints, xPoints.length);
        hitbox = shape.getBounds();
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
        if (s.intersects(hitbox.getBounds())){
            alive = false;
        }
        return s.intersects(hitbox.getBounds());
    }

    @Override
    public boolean isCrash(Hittable h) {
        return false;
    }

    @Override
    public Rectangle getHitBox() {
        return hitbox;
    }

    public void move(){
        x = x + (int) speedX;
        y = y - (int) speedY;
        for (int i = 0; i != xPoints.length; i++) {
            xPoints[i] = xPoints[i] + (int) speedX;
            yPoints[i] = yPoints[i] - (int) speedY;
        }
        this.shape = new Polygon(xPoints, yPoints, 11);
        hitbox = shape.getBounds();

        // Set asteroids collision rebound and also need to change the angle of motion
        if ((x - 25 * type.getSize()) < 0 || (x + 25 * type.getSize()) > Game.SCREEN_WIDTH) {
            speedX = -speedX;
            angle = Math.PI * 2 - angle;
        }
        if ((y - 25 * type.getSize()) < 0 || (y + 21 * type.getSize()) > Game.SCREEN_HEIGHT) {
            speedY = -speedY;
            angle = Math.PI - angle;
        }
    }

    public Polygon getShape() {
        return shape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public AsteroidType getType() {
        return type;
    }
}
