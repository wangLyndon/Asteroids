package Model;

import java.awt.*;

public interface Hittable {
    boolean isAlive();
    int getPoints();
    boolean isPlayer();
    boolean isHit(Bullet b);
    boolean isCrash(Hittable h);
    Rectangle getHitBox();
}
