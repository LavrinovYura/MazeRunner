package MazeRunner;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.geometry.Point2D;

public class PlayerComponent extends Component {
    private CellMoveComponent cell;
    private AStarMoveComponent astar;

    public void moveUp() {
        astar.moveToUpCell();
    }

    public void moveDown() {
        astar.moveToDownCell();
    }

    public void moveRight() {
        astar.moveToRightCell();
    }

    public void moveLeft() {
        astar.moveToLeftCell();
    }

    public void rotateRight(){
        entity.rotateBy(5);
    }

    public void rotateLeft(){
        entity.rotateBy(-5);
    }

    public void shoot() {
        Point2D center = entity.getCenter();
        Vec2 dir = Vec2.fromAngle(entity.getRotation()-90);
        FXGL.spawn("Bullet", new SpawnData(center.getX()-5, center.getY()-5).put("dir", dir.toPoint2D()));
    }
}

