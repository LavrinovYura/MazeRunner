package mazeRunner;

public enum Type {
    PLAYER, ENEMY, BULLET, WALL, EXIT, BOSS, ENEMY_BOSS;


    public String getWall() { return "W"; }
    public String getEnemy() { return "E"; }
    public String getExit() { return "EX"; }
    public String getBoss() { return "B"; }
    public String getPlayer() { return "P"; }
}