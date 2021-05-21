package mazeRunner.components;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;


public class PlayerComponent extends Component {

    private static AStarMoveComponent astar;

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


    public void shoot(Point2D shootPoint) {
        Point2D position = entity.getCenter();
        Point2D vectorToMouse = shootPoint.subtract(position);
        spawnBullet(position, vectorToMouse);
    }

    private void spawnBullet(Point2D position, Point2D direction) {
        play("gun.wav");
        var data = new SpawnData(position.getX(), position.getY())
                .put("direction", direction);

        spawn("Bullet", data);
    }
}
