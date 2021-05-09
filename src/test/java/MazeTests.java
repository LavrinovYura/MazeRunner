import com.almasb.fxgl.app.GameApplication;
import mazeRunner.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mazeRunner.MazeRunnerMain.player;
import static mazeRunner.MazeRunnerMain.playerComponent;
import static mazeRunner.Type.PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(RunWithFX.class)
public class MazeTests {

    @BeforeAll
    public static void init() {
        GameApplication.launch(MazeRunnerMain.class, new String[0]);

    }

    @Test
    public void playerInit() {
        assertEquals(40, getGameWorld().getSingleton(PLAYER).getX());
        assertEquals(40, getGameWorld().getSingleton(PLAYER).getY());
        playerComponent.moveRight();
        assertEquals(44, player.getX());

    }
}
