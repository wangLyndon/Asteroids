package Model;

public enum AsteroidType {
    Large(2, 3, 50), Medium(2.5, 2, 100), Small(3, 1, 200);

    private double speed;
    private int size;
    private int score;
    AsteroidType(double speed, int size, int score){
        this.speed = speed;
        this.size = size;
        this.score = score;
    }

    public double getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public int getSize() {
        return size;
    }
}
