package mazeRunner;

public enum Type {
    PLAYER("P"), ENEMY("E"), BULLET("Bullet"), WALL("W"),
    EXIT("EX"), BOSS("B"), ENEMY_BOSS("EB");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}