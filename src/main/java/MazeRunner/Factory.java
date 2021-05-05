package MazeRunner;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
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
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static MazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGL.geto;
import static com.almasb.fxgl.dsl.FXGL.texture;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;


public class Factory implements EntityFactory {
    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder()
                .at(-200, -200)
                .view("bg2.jpg")
                .zIndex(-1)
                .build();
    }

    @Spawns("W")
    public Entity newWall(SpawnData data) {

        return FXGL.entityBuilder(data)
                .type(WALL)
                .with(new CollidableComponent(true))
                .viewWithBBox(texture("wall.png", 40, 40))
                .build();
    }

    @Spawns("E")
    public Entity newEnemy(SpawnData data) {
        var hp =new HealthIntComponent(2);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.GREEN);
        hpView.setMaxValue(2);
        hpView.setWidth(35);
        hpView.setTranslateY(-15);
        hpView.setTranslateX(10);
        hpView.currentValueProperty().bind(hp.valueProperty());

        var e = FXGL.entityBuilder(data)
                .type(ENEMY)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(40, 40)))
                .with(new CellMoveComponent(40, 40, 125))
                .with(hp)
                .view(hpView)
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new EnemyComponent())
                .with(new CollidableComponent(true))
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("B")
    public Entity newBoss(SpawnData data) {
        var hp =new HealthIntComponent(30);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.GREEN);
        hpView.setMaxValue(30);
        hpView.setWidth(40);
        hpView.currentValueProperty().bind(hp.valueProperty());

        var e = FXGL.entityBuilder(data)
                .type(BOSS)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(72, 96)))
                .with(new CellMoveComponent(40, 40, 250))
                .with(hp)
                .view(hpView)
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new BossComponent())
                .with(new CollidableComponent(true))
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("P")
    public Entity newPlayer(SpawnData data) {
        var e =  entityBuilder(data)
                .type(Type.PLAYER)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(39, 30)))
                .with(new CellMoveComponent(40, 40, 250))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }
    @Spawns("Bullet")
    public Entity spawnBullet(SpawnData data) {

        var e = FXGL.entityBuilder(data)
                .at(data.getX(), data.getY() - 6.5)
                .type(BULLET)
                .viewWithBBox("bullet.png")
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(data.get("direction"), 1200))
                .rotationOrigin(0, 1)
                .build();

        return e;
    }

    @Spawns("EX")
    public Entity newExit(SpawnData data) {
        return entityBuilder(data)
                .type(EXIT)
                .viewWithBBox("exit.jpg")
                .scaleOrigin(40,40)
                .collidable()
                .build();
    }
}
