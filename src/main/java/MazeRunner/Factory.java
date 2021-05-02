package MazeRunner;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static MazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGL.geto;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;


public class Factory implements EntityFactory {

    @Spawns("1")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(WALL)
                .with(new CollidableComponent(true))
                .viewWithBBox(new Rectangle(40, 40, Color.BLACK))
                .build();
    }


    @Spawns("E")
    public Entity newEnemy(SpawnData data) {
        var e = FXGL.entityBuilder(data)
                .type(ENEMY)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(40, 40)))
                .viewWithBBox(new Rectangle(40, 40, Color.BLACK))
                .with(new CellMoveComponent(40, 40, 125))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new SensorComponent())
                .collidable()
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("P")
    public Entity spawnPlayer(SpawnData data) {
        var e =  entityBuilder(data)
                .type(PLAYER)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(40, 40)))
                .viewWithBBox(new Rectangle(40, 40, Color.BLUE))
                .with(new CellMoveComponent(40, 40, 300))
                .with(new AStarMoveComponent(FXGL.<MazeRunnerMain>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .collidable()
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data) {
        Point2D dir = data.get("dir");

        return entityBuilder(data)
                .type(BULLET)
                .viewWithBBox("bullet.png")
                .with(new ProjectileComponent(dir, 2000))
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }

    @Spawns("R")
    public Entity spawnExit(SpawnData data) {
        return entityBuilder(data)
                .type(EXIT)
                .viewWithBBox(new Rectangle(40, 40, Color.RED))
                .collidable()
                .build();
    }
}
