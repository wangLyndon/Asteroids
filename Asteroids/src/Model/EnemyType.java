package Model;

public enum EnemyType {
    Large(2.5, 2, 500), Small(4, 1, 1000);

    private double speed;
    private int size;
    private int score;
    EnemyType(double speed, int size, int score){
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
