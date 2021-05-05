package MazeRunner.Components;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static MazeRunner.MazeRunnerMain.CellSize;
import static com.almasb.fxgl.dsl.FXGL.*;


public class PlayerComponent extends Component {

    private CellMoveComponent cell;

    private AStarMoveComponent astar;

    private final AnimatedTexture texture;

    private final AnimationChannel animIdle, animUP, animDown, animRight, animLeft;

    public PlayerComponent() {
        Image imageUp = image("playerUp.png");
        Image imageDown = image("playerDown.png");
        Image imageLeft = image("playerLeft.png");
        Image imageRight = image("playerRight.png");

        animIdle = new AnimationChannel(imageDown, 4, CellSize, CellSize, Duration.seconds(1), 1, 1);
        animUP = new AnimationChannel(imageUp, 4, CellSize, CellSize, Duration.seconds(0.66), 0, 3);
        animDown = new AnimationChannel(imageDown, 4, CellSize, CellSize, Duration.seconds(0.66), 0, 3);
        animLeft = new AnimationChannel(imageLeft, 4, CellSize, CellSize, Duration.seconds(0.66), 0, 3);
        animRight = new AnimationChannel(imageRight, 4, CellSize, CellSize, Duration.seconds(0.66), 0, 3);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }


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


    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    private double lastX = 0;
    private double lastY = 0;

    @Override
    public void onUpdate(double tpf) {

        double dx = entity.getX() - lastX;
        double dy = entity.getY() - lastY;

        lastX = entity.getX();
        lastY = entity.getY();

        if (dx == 0.0 && dy == 0.0) {
            texture.loopAnimationChannel(animIdle);
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                if (texture.getAnimationChannel() != animRight) {
                    texture.loopAnimationChannel(animRight);
                }
            } else {
                if (texture.getAnimationChannel() != animLeft) {
                    texture.loopAnimationChannel(animLeft);
                }
            }
        } else {
            if (dy > 0) {
                if (texture.getAnimationChannel() != animDown) {
                    texture.loopAnimationChannel(animDown);
                }
            } else {
                if (texture.getAnimationChannel() != animUP) {
                    texture.loopAnimationChannel(animUP);
                }
            }
        }
    }
}
