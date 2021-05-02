package MazeRunner;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;


import java.util.Map;

import static MazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGL.*;


public class MazeRunnerMain extends GameApplication {
    public static final int BLOCK_SIZE = 40;

    public static final int MAP_SIZE = 40;

    private AStarGrid grid;

    private Entity player;

    private PlayerComponent playerComponent;

    public AStarGrid getGrid() {
        return grid;
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("lvl", 1);
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
                playerComponent.moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                playerComponent.moveDown();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                playerComponent.moveRight();
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
                playerComponent.rotateRight();
            }
        }, KeyCode.E);

        getInput().addAction(new UserAction("Rotate Left") {
            @Override
            protected void onAction() {
                playerComponent.rotateLeft();
            }
        }, KeyCode.Q);

    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());

        setLevel(1);

    }
    private void nextLevel() {
        inc("lvl", +1);
        var levelNum = geti("lvl");

        setLevel(levelNum);
    }

    public void setLevel(int levelNum) {
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        Level level = getAssetLoader().loadLevel("lvl" + levelNum + ".txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);
        grid = AStarGrid.fromWorld(getGameWorld(), 20, 20, 40, 40, type -> {
            if (type.equals(WALL))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });
        set("grid", grid);

        player = spawn("P",100,100);
        playerComponent = player.getComponent(PlayerComponent.class);

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
                nextLevel();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
