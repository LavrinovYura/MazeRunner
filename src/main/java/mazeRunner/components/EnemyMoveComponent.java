package mazeRunner.components;

import mazeRunner.Type;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import static mazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

@Required(AStarMoveComponent.class)
public class EnemyMoveComponent extends Component {

    private AStarMoveComponent astar;

    private final Type type;

    public EnemyMoveComponent(Type type) {
        this.type = type;
    }

    @Override
    public void onUpdate(double tpf) {

        switch (type) {
            case ENEMY -> {
                Entity enemy = getGameWorld().getSingleton(ENEMY);
                Entity player = getGameWorld().getSingleton(PLAYER);
                if (getEntity().distance(player) < 300) {
                    astar.moveToCell(player.call("getCellX"), player.call("getCellY"));
                }
            }
            case BOSS -> {
                Entity player = getGameWorld().getSingleton(PLAYER);
                astar.moveToCell(player.call("getCellX"), player.call("getCellY"));
            }

        }

    }

}
