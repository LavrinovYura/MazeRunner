package mazeRunner;

import mazeRunner.components.PlayerComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

import static mazeRunner.MazeParameters.CELL_SIZE;
import static mazeRunner.MazeParameters.FIELD_SIZE;
import static mazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;


public class MazeRunnerMain extends GameApplication {

    public static AStarGrid grid;

    public Entity player;

    public PlayerComponent playerComponent;

    public Boolean secondLive = true;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Maze Runner");
        settings.setVersion("1.0");
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new Menu();
            }
        });
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.1);
        getSettings().setGlobalSoundVolume(0.1);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                playerComponent.moveUp();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveDown();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveRight();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                playerComponent.shoot(getInput().getMousePositionWorld());
            }
        }, MouseButton.PRIMARY);

    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());
        setLevel();
    }

    public void setLevel() {
        String[][] ar;

        ar = RandomLvl.drawRandom();

        int cellX = 0;
        int cellY = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                switch (ar[i][j]) {
                    case "0":
                        break;

                    case "W": {
                        spawn("W", cellX, cellY);
                        break;
                    }
                    case "EX": {
                        spawn("EX", cellX, cellY);
                        break;
                    }
                    case "B": {
                        spawn("B", cellX, cellY);
                        play("bossMusic.mp3");
                        break;
                    }
                    case "E": {
                        spawn("E", cellX, cellY);
                        break;
                    }
                    case "P": {
                        player = spawn("P", cellX, cellY);
                        playerComponent = player.getComponent(PlayerComponent.class);
                        spawn("BG");
                        break;
                    }

                }
                cellX += CELL_SIZE;
            }
            cellY += CELL_SIZE;
            cellX = 0;
        }

        grid = AStarGrid.fromWorld(getGameWorld(), FIELD_SIZE, FIELD_SIZE, CELL_SIZE, CELL_SIZE, type -> {
            if (type.equals(WALL))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });
        set("grid", grid);

        //getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
    }


    @Override
    protected void initPhysics() {

        onCollisionBegin(ENEMY, BULLET, (enemy, bullet) -> {
            var hp = enemy.getComponent(HealthIntComponent.class);
            if (hp.getValue() > 1) {
                bullet.removeFromWorld();
                hp.damage(1);
                return;
            }
            enemy.removeFromWorld();
            bullet.removeFromWorld();
        });

        onCollisionBegin(ENEMY_BOSS, BULLET, (enemy, bullet) -> {
            var hp = enemy.getComponent(HealthIntComponent.class);
            if (hp.getValue() > 1) {
                bullet.removeFromWorld();
                hp.damage(1);
                return;
            }
            enemy.removeFromWorld();
            bullet.removeFromWorld();
        });

        onCollisionBegin(BOSS, BULLET, (boss, bullet) -> {
            var hp = boss.getComponent(HealthIntComponent.class);

            if (hp.getValue() == 60 || hp.getValue() == 30) {
                    spawn("EB", boss.getX(), boss.getY());
            }

            if (hp.getValue() == 1 && secondLive) {
                hp.restoreFully();
                secondLive = false;
            }

            if (hp.getValue() > 1) {
                bullet.removeFromWorld();
                hp.damage(1);
                return;
            }

            secondLive = true;
            getAudioPlayer().stopAllMusic();
            boss.removeFromWorld();
            bullet.removeFromWorld();
            spawn("EX", 400, 400);

        });

        onCollisionBegin(WALL, BULLET, (wall, bullet) -> bullet.removeFromWorld());

        onCollisionBegin(PLAYER, EXIT, (player, exit) -> {
            getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
            setLevel();
        });

        onCollision(PLAYER, ENEMY, (player, enemy) -> {

            var hp = player.getComponent(HealthIntComponent.class);
            if (hp.getValue() == 0) {
                gameOver();
                return;
            }
            hp.damage(1);
        });

        onCollision(PLAYER, ENEMY_BOSS, (player, enemy) -> {

            var hp = player.getComponent(HealthIntComponent.class);
            if (hp.getValue() == 0) {
                gameOver();
                return;
            }
            hp.damage(1);
        });

        onCollision(PLAYER, BOSS, (player, enemy) -> {
            var hp = player.getComponent(HealthIntComponent.class);
            if (hp.getValue() == 0) {
                gameOver();
                return;
            }
            hp.damage(3);
        });

    }

    private void gameOver() {
        getAudioPlayer().stopAllMusic();
        getGameController().gotoMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
