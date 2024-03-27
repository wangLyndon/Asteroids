package Model;

import Screen.PlayerListener;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game implements ucd.comp2011j.engine.Game {
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 800;

    private static final int NO_LEVELS = 10; // Maximum number of levels

    private int playerLives;
    private int playerScore;
    private boolean pause = true;
    private Random random = new Random();
    private Player player;
    private List<Asteroid>[] asteroidLevel = new ArrayList[10]; // Set asteroids for each level
    private List<Asteroid> asteroidList;
    private List<Bullet> playerBullets;
    private List<EnemyShip> enemyShipList;
    private List<Bullet> enemyBullets;
    private Level[] levels;
    private int currentLevel = 0;
    private int canGetLiveScore = 10000; // Set points to earn extra lives
    private PlayerListener listener;
    private Timer timer;

    public Game(PlayerListener listener){
        this.listener = listener;
        startNewGame();
    }


    @Override
    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public int getLives() {
        return playerLives;
    }

    @Override
    public void updateGame() {
        if (!isPaused()){
            player.tick(); // Updates player and enemy bullet firing
            for (EnemyShip enemyShip : enemyShipList) {
                enemyShip.tick();
            }
            movePlayer();
            moveAsteroids();
            moveEnemy();
            testCrash();
            playerBullets();
            enemyBullets();
            addLive();
        }
    }

    public void playerBullets(){
        List<Bullet> remove = new ArrayList<>();// Remove bullets that alive = false
        for (int i = 0; i != playerBullets.size(); i++) {
            if (playerBullets.get(i).isAlive()){
                playerBullets.get(i).move();
                for (Asteroid asteroid : asteroidList) {
                    if (asteroid.isHit(playerBullets.get(i))){
                        playerScore = playerScore + asteroid.getPoints();
                        playerBullets.get(i).destroy();
                        divideAsteroid(asteroid);
                        break;
                    }
                }
                for (EnemyShip enemyShip : enemyShipList) {
                    if (enemyShip.isHit(playerBullets.get(i))){
                        playerScore = playerScore + enemyShip.getPoints();
                        playerBullets.get(i).destroy();
                        enemyShipList.remove(enemyShip);
                        break;
                    }
                }
                if (player.isHit(playerBullets.get(i))){
                    playerLives--;
                    playerBullets.get(i).destroy();
                    pause = true;
                }
                // The destory command is not needed to detect Bullet collisions,
                // because the same effect is already achieved in the isHit method of the bullet class
                for (Bullet eb : enemyBullets) {
                    eb.isHit(playerBullets.get(i));
                }
                for (Bullet pb : playerBullets) {
                    if (pb != playerBullets.get(i)){
                        pb.isHit(playerBullets.get(i));
                    }
                }
            }else{
                remove.add(playerBullets.get(i));
            }
        }
        playerBullets.removeAll(remove);
    }

    public void enemyBullets(){
        List<Bullet> remove = new ArrayList<>(); // Remove bullets that live = false
        for (int i = 0; i != enemyBullets.size(); i++) {
            if (enemyBullets.get(i).isAlive()){
                enemyBullets.get(i).move();
                for (Asteroid asteroid : asteroidList) {
                    if (asteroid.isHit(enemyBullets.get(i))){
                        enemyBullets.get(i).destroy();
                        divideAsteroid(asteroid);
                        break;
                    }
                }
                if (player.isHit(enemyBullets.get(i))){
                    playerLives--;
                    enemyBullets.get(i).destroy();
                    pause = true;
                }
                // The destory command is not needed to detect Bullet collisions,
                // because the same effect is already achieved in the isHit method of the bullet class
                for (Bullet pb : playerBullets) {
                    pb.isHit(enemyBullets.get(i));
                }
                for (Bullet eb : enemyBullets) {
                    if (eb != enemyBullets.get(i)){
                        eb.isHit(enemyBullets.get(i));
                    }
                }
            }else{
                remove.add(enemyBullets.get(i));
            }
        }
        enemyBullets.removeAll(remove);
    }

    public void movePlayer(){
        if (listener.hasPressedFire()){
            Bullet b = player.fire();
            if (b != null){
                playerBullets.add(b);
                listener.resetFire();
            }
        }
        if (listener.isPressingForward()){
            player.speedUp();
        }else{
            player.resetFlames(); // Make sure there is no fire coming out when the ship is not moving
        }
        if (listener.isPressingLeft()){
            player.rotate(true);
        } else if (listener.isPressingRight()) {
            player.rotate(false);
        }
        player.move();
    }

    public void moveAsteroids(){
        for (Asteroid asteroid : asteroidList) {
            asteroid.move();
        }
    }

    public void moveEnemy(){
        List<EnemyShip> remove = new ArrayList<>();
        for (EnemyShip enemyShip : enemyShipList) {
            enemyShip.move();
            if (enemyShip.isAlive()) {
                Bullet b = enemyShip.fire();
                if (b != null) {
                    enemyBullets.add(b);
                }
            }else {
                remove.add(enemyShip);
            }
        }
        enemyShipList.removeAll(remove);
    }

    public void testCrash(){
        for (Asteroid asteroid : asteroidList) {
            if (player.isCrash(asteroid)){
                playerLives--;
                pause = true;
                divideAsteroid(asteroid);
                break;
            }
        }
        List<EnemyShip> remove = new ArrayList<>();
        for (EnemyShip enemyShip : enemyShipList) {
            for (Asteroid asteroid : asteroidList) {
                if (enemyShip.isCrash(asteroid)){
                    remove.add(enemyShip);
                    divideAsteroid(asteroid);
                    break;
                }
            }
        }
        enemyShipList.removeAll(remove);
    }

    public void divideAsteroid(Asteroid asteroid){
        double angle;
        if (asteroid.getType() == AsteroidType.Large){
            angle = asteroid.getAngle(); // Get the Angle of motion of the original asteroid
            // The plus of 5 to x and 5 to y is intended to prevent the asteroids from overlapping and affecting the player's perception
            // Additionally, the speed setting is related to the current level. The higher the level, the faster the speed, but the change is not very big
            asteroidList.add(new Asteroid(asteroid.getX() + 5, asteroid.getY() + 5, AsteroidType.Medium, angle + random.nextDouble(Math.PI / 4.5) - (Math.PI / 9), AsteroidType.Medium.getSpeed() * (1 + 0.03 * currentLevel)));
            asteroidList.add(new Asteroid(asteroid.getX(), asteroid.getY(), AsteroidType.Medium, angle + random.nextDouble(Math.PI / 4.5) - (Math.PI / 9), AsteroidType.Medium.getSpeed() * (1 + 0.03 * currentLevel)));
        } else if (asteroid.getType() == AsteroidType.Medium) {
            angle = asteroid.getAngle();
            asteroidList.add(new Asteroid(asteroid.getX() + 5, asteroid.getY() + 5, AsteroidType.Small, angle + random.nextDouble(Math.PI / 4.5) - (Math.PI / 9), AsteroidType.Small.getSpeed() * (1 + 0.03 * currentLevel)));
            asteroidList.add(new Asteroid(asteroid.getX(), asteroid.getY(), AsteroidType.Small, angle + random.nextDouble(Math.PI / 4.5) - (Math.PI / 9), AsteroidType.Small.getSpeed() * (1 + 0.03 * currentLevel)));
        }
        asteroidList.remove(asteroid);
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public void checkForPause() {
        if (listener.hasPressedPause()){
            pause = !pause;
            listener.resetPause();
        }
    }

    @Override
    public void startNewGame() {
        playerLives = 3;
        playerScore = 0;
        player = new Player();
        player.setMaxWeaponCountdown(30); // Set the firing interval for the bullets
        playerBullets = new ArrayList<>();
        enemyShipList = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        levels = new Level[NO_LEVELS];
        for (int i = 0; i != NO_LEVELS; i++) {
            levels[i] = new Level(this, (i + 1), i);
        }
        asteroidList = asteroidLevel[currentLevel];
        createEnemy();
    }

    @Override
    public boolean isLevelFinished() {
        if (currentLevel < NO_LEVELS){
            return asteroidList.isEmpty();
        }else{
            return true;
        }
    }

    @Override
    public boolean isPlayerAlive() {
        return player.isAlive();
    }

    @Override
    public void resetDestroyedPlayer() {
        player.resetDestroyed();
        playerBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
    }

    @Override
    public void moveToNextLevel() {
        pause = true;
        currentLevel++;
        player.resetDestroyed();
        player.setMaxWeaponCountdown(30 - (2 * currentLevel)); // The firing interval of the bullet decreases as the level increases
        asteroidList = asteroidLevel[currentLevel];
        playerBullets = new ArrayList<>();
        enemyShipList = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        createEnemy();
    }

    public void addLive(){
        if (playerScore >= canGetLiveScore){
            playerLives++;
            canGetLiveScore = canGetLiveScore + 10000;
        }
    }

    public void createEnemy(){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int x = 0;
                int y = 0;
                double angle = Math.PI / 2;
                int option = random.nextInt(4);
                // Set up a create enemy situation on four sides and fly at an Angle of 120.
                switch (option){
                    case 0:
                        y = 1;
                        x = random.nextInt(Game.SCREEN_WIDTH);
                        angle = random.nextDouble(2 * Math.PI / 3) + (2 * Math.PI / 3);
                        break;
                    case 1:
                        x = Game.SCREEN_WIDTH - 5;
                        y = random.nextInt(Game.SCREEN_WIDTH);
                        angle = random.nextDouble(2 * Math.PI / 3) + (7 * Math.PI / 6);
                        break;
                    case 2:
                        y = Game.SCREEN_HEIGHT - 15;
                        x = random.nextInt(Game.SCREEN_WIDTH);
                        angle = random.nextDouble(2 * Math.PI / 3) - Math.PI / 3;
                        break;
                    case 3:
                        x = 1;
                        y = random.nextInt(Game.SCREEN_WIDTH);
                        angle = random.nextDouble(2 * Math.PI / 3) + Math.PI / 6;
                        break;
                }
                if (currentLevel < 6) {
                    enemyShipList.add(new EnemyShip(EnemyType.Large, x, y, angle, player));
                }else{
                    enemyShipList.add(new EnemyShip(EnemyType.Small, x, y, angle, player));
                }
            }
        }, 30000, 30000); // Every 30s to create a new one
    }

    @Override
    public boolean isGameOver() {
        return !(playerLives > 0 && currentLevel <= NO_LEVELS);
    }

    @Override
    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    @Override
    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public Player getPlayer(){
        return player;
    }

    public void setAsteroidList(int index, List<Asteroid> asteroidList) {
        asteroidLevel[index] = asteroidList;
    }

    public List<Asteroid> getAsteroidList() {
        return asteroidList;
    }

    public List<Bullet> getPlayerBullets() {
        return playerBullets;
    }

    public List<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    public List<EnemyShip> getEnemyShipList() {
        return enemyShipList;
    }
}
