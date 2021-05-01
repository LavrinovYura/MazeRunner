package MazeRunner;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static MazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;


public class Factory implements EntityFactory {

    @Spawns("1")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(WALL)
                .viewWithBBox(new Rectangle(40,40,Color.BLACK))
                .build();
    }


    @Spawns("P")
    public Entity spawnPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .type(PLAYER)
                .viewWithBBox(new Rectangle(40,40, Color.BLUE))
                .with(new CellMoveComponent(40, 40, 300))
                .with(new AStarMoveComponent(FXGL.<MazeRunnerMain>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data) {
        Point2D dir = data.get("dir");

        return entityBuilder(data)
                .type(BULLET)
                .viewWithBBox("bullet.png")
                .with(new ProjectileComponent(dir, 1000))
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }

}
