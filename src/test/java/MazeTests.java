
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import mazeRunner.*;
import mazeRunner.components.PlayerComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mazeRunner.MazeParameters.CELL_SIZE;
import static mazeRunner.Type.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//if you want to test you may
//set settings.setMainMenuEnabled(false!!!)
//or first sleep do in range 10-15s
//to start game in time

@ExtendWith(RunWithFX.class)
public class MazeTests {

    public static void init() throws InterruptedException {
        Thread t = new Thread(() -> GameApplication.launch(MazeRunnerMain.class, new String[0]));
        t.start();
        Thread.sleep(10000);
    }

    @Test
    public void playerInit() throws InterruptedException {

        init();

        assertEquals(40, getGameWorld().getSingleton(PLAYER).getX());
        assertEquals(40, getGameWorld().getSingleton(PLAYER).getY());

        FXGL.getExecutor().startAsyncFX(() -> getGameWorld().removeEntities(getGameWorld().getEntitiesByType(ENEMY, BOSS))).await();

        var component = getGameWorld().getSingleton(PLAYER).getComponent(PlayerComponent.class);

        component.moveRight();
        Thread.sleep(1000);
        assertEquals(80.5, component.getEntity().getX());

        var move = component.getEntity().getComponent(AStarMoveComponent.class);

        move.moveToCell(18, 2);
        Thread.sleep(5000);
        assertEquals(18, (int) component.getEntity().getX() /CELL_SIZE);

        move.moveToCell(2, 18);
        Thread.sleep(7000);
        assertEquals(2, (int) getGameWorld().getSingleton(PLAYER).getX() / CELL_SIZE);

        move.moveToCell(1, 1);
        Thread.sleep(10000);
        assertEquals(1, (int) component.getEntity().getX() / CELL_SIZE);

    }
}