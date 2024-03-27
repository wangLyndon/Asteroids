package Model;

import java.awt.*;

public class Bullet implements Hittable{
    private int x;
    private int y;
    private double angle;
    private int speed = 8;
    private int width = 3;
    private int height = 3;
    private double speedX;
    private double speedY;
    private int distance;
    private int maxDistance = 600; // Set move limit
    private boolean aLive = true;
    private Rectangle hitbox;

    public Bullet(int x, int y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
        speedX = Math.sin(angle) * speed;
        speedY = Math.cos(angle) * speed;
        hitbox = new Rectangle(x, y, width, height);
    }

    public void move(){
        x = x + (int) speedX;
        y = y - (int) speedY;

        distance = distance + speed;
        // Over the limit will die
        if (distance > maxDistance){
            aLive = false;
        }

        checkOutOfArea();
        hitbox = new Rectangle(x, y, width, height);
    }

    public void checkOutOfArea(){
        // // Reappear from the other side of the screen
        if (x < 0){
            x = x + Game.SCREEN_WIDTH;
        } else if (x > Game.SCREEN_WIDTH) {
            x = x - Game.SCREEN_WIDTH;
        } else if (y < 0) {
            y = y + Game.SCREEN_HEIGHT;
        } else if (y > Game.SCREEN_HEIGHT) {
            y = y - Game.SCREEN_HEIGHT;
        }
    }

    @Override
    public boolean isAlive() {
        return aLive;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isHit(Bullet b) {
        // Reappear from the other side of the screen
        if (hitbox.intersects(b.hitbox)) {
            aLive = false;
            b.aLive = false;
        }
        return hitbox.intersects(b.hitbox);
    }

    @Override
    public boolean isCrash(Hittable h) {
        return false;
    }

    public void destroy(){
        aLive = false;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
