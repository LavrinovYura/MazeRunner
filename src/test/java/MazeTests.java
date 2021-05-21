
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import mazeRunner.*;
import mazeRunner.components.PlayerComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mazeRunner.MazeParameters.CELL_SIZE;
import static mazeRunner.Type.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(RunWithFX.class)
public class MazeTests {

    public static void init() throws InterruptedException {
        Thread t = new Thread(() -> GameApplication.launch(MazeRunnerMain.class, new String[0]));
        t.start();
        Thread.sleep(3300);
    }

    @Test
    public void playerInit() throws InterruptedException {

        init();

        assertEquals(40, getGameWorld().getSingleton(PLAYER).getX());
        assertEquals(40, getGameWorld().getSingleton(PLAYER).getY());

        FXGL.getExecutor().startAsyncFX(() -> getGameWorld().removeEntities(getGameWorld().getEntitiesByType(ENEMY, BOSS))).await();

        getGameWorld().getSingleton(PLAYER).getComponent(HealthIntComponent.class).setMaxValue(100000000);

        var component = getGameWorld().getSingleton(PLAYER).getComponent(PlayerComponent.class);

        component.moveRight();
        Thread.sleep(1000);
        assertEquals(80.5, component.getEntity().getX());

        var move = component.getEntity().getComponent(AStarMoveComponent.class);

        move.moveToCell(18, 2);
        Thread.sleep(5000);
        assertEquals(18, (int) component.getEntity().getX() / 40);

        move.moveToCell(2, 18);
        Thread.sleep(7000);
        assertEquals(2, (int) getGameWorld().getSingleton(PLAYER).getX() / 40);

        move.moveToCell(2, 2);
        Thread.sleep(10000);
        assertEquals(2, (int) component.getEntity().getX() / 40);

    }
}