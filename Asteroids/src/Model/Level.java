package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private Game game;
    private int numAsteroids;
    private int currentLevel;
    private Random random = new Random();
    public Level(Game game, int num, int currentLevel){
        this.game = game;
        this.numAsteroids = num;
        this.currentLevel = currentLevel;
        reset();
    }

    public void reset(){
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i != numAsteroids; i++) {
            int x;
            int y;
            while (true){
                int xp = random.nextInt(Game.SCREEN_WIDTH - 160) + 80; // Prevent asteroids from being created outside
                int yp = random.nextInt(Game.SCREEN_HEIGHT - 160) + 80;
                if ((xp < (Game.SCREEN_WIDTH / 2 - 100) || xp > (Game.SCREEN_WIDTH / 2 + 100)) && (yp < (Game.SCREEN_HEIGHT / 2 - 100) || yp > (Game.SCREEN_HEIGHT / 2 + 100))){
                    x = xp;
                    y = yp;
                    break;
                }
            }
            asteroids.add(new Asteroid(x, y, AsteroidType.Large, random.nextDouble(Math.PI * 2), AsteroidType.Large.getSpeed() * (1 + 0.03 * currentLevel)));
        }
        game.setAsteroidList(currentLevel, asteroids); // Create the number of asteroids corresponding to the level
    }
}
