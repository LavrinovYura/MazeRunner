package MazeRunner;


import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import static MazeRunner.Type.PLAYER;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;


@Required(AStarMoveComponent.class)
public class SensorComponent extends Component {
    private AStarMoveComponent astar;
    @Override
    public void onUpdate(double tpf) {
        Entity player = getGameWorld().getSingleton(PLAYER);
        if (getEntity().distance(player) < 200) {
            astar.moveToCell(player.call("getCellX"), player.call("getCellY"));
        }
    }
}