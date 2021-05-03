package MazeRunner;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;


import static MazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGL.*;


public class MazeRunnerMain extends GameApplication {

    private AStarGrid grid;

    public Entity player;

    public PlayerComponent playerComponent;

    public AStarGrid getGrid() {
        return grid;
    }


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Maze Runner");
        settings.setVersion("0.1");
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new Menu();
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {

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
                player.getComponent(PlayerComponent.class).shoot();
            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction("Rotate Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).rotateRight();
            }
        }, KeyCode.E);

        getInput().addAction(new UserAction("Rotate Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).rotateLeft();
            }
        }, KeyCode.Q);

    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());
        setLevel();

    }

    public void setLevel() {
        int[][] ar;

        ar = RandomLvl.drawRandom();

        int cellX = 0;
        int cellY = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                switch (ar[i][j]) {
                    case 0:
                        break;
                    case 1: {
                        spawn("1", cellX, cellY);
                        break;
                    }
                    case 2: {
                       spawn("2", cellX, cellY);
                        break;
                    }
                    case 3: {
                        spawn("3", cellX, cellY);
                        break;
                    }
                    case 8: {
                        player = spawn("8", cellX, cellY);
                        playerComponent = player.getComponent(PlayerComponent.class);
                        break;
                    }
                }
                cellX += 40;
            }
            cellY += 40;
            cellX = 0;
        }

        grid = AStarGrid.fromWorld(getGameWorld(), 20, 20, 40, 40, type -> {
            if (type.equals(WALL))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });
        set("grid", grid);

    }


    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(ENEMY, BULLET) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity enemy) {
                enemy.removeFromWorld();
                bullet.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BULLET, WALL) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity wall) {
                bullet.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(PLAYER, EXIT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity exit) {
                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                setLevel();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
