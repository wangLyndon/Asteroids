package Model;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements Hittable {
    private int x;
    private int y;
    private int width;
    private int height;
    private double angle;
    private double speedX;
    private double speedY;
    private int weaponCountdown;
    private double acceleration;
    private int maxSpeed;
    private double frictionCoefficient; // To make the ship slow down
    private boolean alive = true;
    private int flames;
    private int maxWeaponCountdown;
    private boolean invincible;
    private Timer timer;
    private Rectangle hitbox;


    public Player(){
        x = Game.SCREEN_WIDTH / 2;
        y = Game.SCREEN_HEIGHT / 2;
        angle = 0;
        speedX = 0;
        speedY = 0;
        acceleration = 0.2;
        maxSpeed = 6;
        frictionCoefficient = 0.99;
        invincible = true;
        // Get the ship roughly width and height
        width = Math.abs((x + (int) (Math.sin(angle + 5 * Math.PI / 6) * 15)) - (x + (int) (Math.sin(angle + 7 * Math.PI / 6) * 15)));
        height = Math.abs((y + (int) (Math.cos(angle + 5 * Math.PI / 6) * 15)) - (y - (int) (Math.cos(angle + 5 * Math.PI / 6) * 15)));
        hitbox = new Rectangle(x + (int) (Math.sin(7 * Math.PI / 6) * 15), y + (int) (Math.cos(7 * Math.PI / 6) * 15) - 10, width, height + 10);
        changeInvincible();
    }

    public void tick() {
        // Set the firing interval of the bullet
        if (weaponCountdown > 0) {
            weaponCountdown--;
        } else {
            weaponCountdown = maxWeaponCountdown;
        }
    }

    @Override
    public boolean isAlive(){
        return alive;
    }

    @Override
    public int getPoints() {
        return -100;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isHit(Bullet b) {
        if (invincible){
            return false;
        }
        Rectangle s = b.getHitBox();
        if (s.intersects(hitbox.getBounds())){
            alive = false;
        }
        return s.intersects(hitbox.getBounds());
    }

    public Bullet fire(){
        Bullet b = null;
        if (weaponCountdown == 0){
            // The bullet is fired from the head of the ship
            b = new Bullet(x + (int) (Math.sin(angle) * 25), y - (int) (Math.cos(angle) * 25), angle);
        }
        return b;
    }

    @Override
    public boolean isCrash(Hittable h) {
        if (invincible){
            return false;
        }
        Rectangle s = h.getHitBox();
        if (s.intersects(hitbox.getBounds())){
            alive = false;
        }
        return s.intersects(hitbox.getBounds());
    }

    public void resetDestroyed() {
        alive = true;
        invincible = true;
        x = Game.SCREEN_WIDTH / 2;
        y = Game.SCREEN_HEIGHT / 2;
        angle = 0;
        speedX = 0;
        speedY = 0;
        width = Math.abs((x + (int) (Math.sin(angle + 5 * Math.PI / 6) * 15)) - (x + (int) (Math.sin(angle + 7 * Math.PI / 6) * 15)));
        height = Math.abs((y + (int) (Math.cos(angle + 5 * Math.PI / 6) * 15)) - (y - (int) (Math.cos(angle + 5 * Math.PI / 6) * 15)));
        hitbox = new Rectangle(x + (int) (Math.sin(7 * Math.PI / 6) * 15), y + (int) (Math.cos(7 * Math.PI / 6) * 15) - 10, width, height + 10);
        changeInvincible();
    }

    @Override
    public Rectangle getHitBox() {
        return hitbox;
    }

    public void speedUp(){
        speedX = speedX + Math.sin(angle) * acceleration;
        speedY = speedY + Math.cos(angle) * acceleration;
        double shipSpeed = Math.sqrt((speedX * speedX) + (speedY * speedY));
        if (shipSpeed > maxSpeed){
            // If the speed bigger than maximum, the speed in the x and y directions will be decreased in equal proportions
            speedX = speedX * maxSpeed / shipSpeed;
            speedY = speedY * maxSpeed / shipSpeed;
        }
        flames++;
    }

    public void rotate(boolean direction){
        if (direction){ // True for left, false for right
            angle = angle - Math.PI / 30;
        }else{
            angle = angle + Math.PI / 30;
        }
    }

    public void move(){
        x = x + (int) speedX;
        y = y - (int) speedY;

        speedX = speedX * frictionCoefficient; // The speed will gradually decrease
        speedY = speedY * frictionCoefficient;

        checkOutOfArea();
        // Update hit box
        // The change of ship direction can only be estimated approximately, and the collision area of the spacecraft can be roughly determined by the horizontal and vertical rectangles
        if ((angle % (2 * Math.PI)) < Math.PI / 4 || (angle % (2 * Math.PI) > (3 * Math.PI / 4) && angle % (2 * Math.PI) < (5 * Math.PI / 4)) || (angle % (2 * Math.PI)) > (7 * Math.PI / 4)) {
            hitbox = new Rectangle(x + (int) (Math.sin(7 * Math.PI / 6) * 15), y + (int) (Math.cos(7 * Math.PI / 6) * 15) - 10, width, height + 10);
        } else {
            hitbox = new Rectangle(x + (int) (Math.sin(Math.PI / 2 + 7 * Math.PI / 6) * 15) - 5, y - (int) (Math.cos(Math.PI / 2 + 7 * Math.PI / 6) * 15) - 5, width + 20, height - 5);
        }
    }

    public void changeInvincible(){
        if (timer != null){
            timer.cancel(); // To release memory
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                invincible = false;
            }
        }, 4000);
    }

    public void checkOutOfArea(){
        // Reappear from the other side of the screen
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public int getFlames() {
        return flames;
    }

    public void resetFlames(){
        flames = 0;
    }

    public void setMaxWeaponCountdown(int maxWeaponCountdown) {
        this.maxWeaponCountdown = maxWeaponCountdown;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
