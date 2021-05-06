package mazeRunner;

import mazeRunner.components.*;
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

import static mazeRunner.MazeParameters.CELL_SIZE;
import static mazeRunner.Type.*;
import static com.almasb.fxgl.dsl.FXGL.geto;
import static com.almasb.fxgl.dsl.FXGL.texture;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import  static mazeRunner.Textures.*;

public class Factory implements EntityFactory {

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder()
                .at(-200, -200)
                .view(BG_TEXTURE)
                .zIndex(-1)
                .build();
    }

    @Spawns("W")
    public Entity newWall(SpawnData data) {

        return FXGL.entityBuilder(data)
                .type(WALL)
                .with(new CollidableComponent(true))
                .viewWithBBox(texture(WALL_TEXTURE, CELL_SIZE, CELL_SIZE))
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
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(CELL_SIZE, CELL_SIZE)))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 300))
                .with(hp)
                .view(hpView)
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new EnemyMoveComponent(ENEMY))
                .with(new AnimationComponent(ENEMY_UP,ENEMY_DOWN,ENEMY_LEFT,ENEMY_RIGHT,54, 40,3,2))
                .with(new CollidableComponent(true))
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }
    @Spawns("EB")
    public Entity newEnemyBoss(SpawnData data) {
        var hp =new HealthIntComponent(15);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.GREEN);
        hpView.setMaxValue(15);
        hpView.setWidth(35);
        hpView.setTranslateY(-15);
        hpView.setTranslateX(10);
        hpView.currentValueProperty().bind(hp.valueProperty());

        var e = FXGL.entityBuilder(data)
                .type(ENEMY_BOSS)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(CELL_SIZE, CELL_SIZE)))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 300))
                .with(hp)
                .view(hpView)
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new EnemyMoveComponent(BOSS))
                .with(new AnimationComponent(ENEMY_UP,ENEMY_DOWN,ENEMY_LEFT,ENEMY_RIGHT,54, 40,3,2))
                .with(new CollidableComponent(true))
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("B")
    public Entity newBoss(SpawnData data) {
        var hp =new HealthIntComponent(100);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.GREEN);
        hpView.setMaxValue(100);
        hpView.setWidth(60);
        hpView.setTranslateY(-15);
        hpView.setTranslateX(3);
        hpView.currentValueProperty().bind(hp.valueProperty());

        var e = FXGL.entityBuilder(data)
                .type(BOSS)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(72, 96)))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 150))
                .with(hp)
                .view(hpView)
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new EnemyMoveComponent(BOSS))
                .with(new AnimationComponent(BOSS_UP,BOSS_DOWN,BOSS_LEFT,BOSS_RIGHT,72, 96,3,2))
                .with(new CollidableComponent(true))
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("P")
    public Entity newPlayer(SpawnData data) {
        var hp =new HealthIntComponent(100);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.GREEN);
        hpView.setMaxValue(100);
        hpView.setWidth(40);
        hpView.setTranslateY(-15);

        hpView.currentValueProperty().bind(hp.valueProperty());

        var e =  entityBuilder(data)
                .type(Type.PLAYER)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(39, 30)))
                .with(hp)
                .view(hpView)
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 250))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .with(new AnimationComponent(PLAYER_UP,PLAYER_DOWN,PLAYER_LEFT,PLAYER_RIGHT,CELL_SIZE,CELL_SIZE,3,2))
                .build();
        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("Bullet")
    public Entity spawnBullet(SpawnData data) {

       return FXGL.entityBuilder(data)
                .at(data.getX(), data.getY() - 6.5)
                .type(BULLET)
                .viewWithBBox(texture(BULLET_TEXTURE,10,10))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(data.get("direction"), 1200))
                .rotationOrigin(0, 1)
                .build();
    }

    @Spawns("EX")
    public Entity newExit(SpawnData data) {
        return entityBuilder(data)
                .type(EXIT)
                .viewWithBBox(EXIT_TEXTURE)
                .scaleOrigin(CELL_SIZE,CELL_SIZE)
                .collidable()
                .build();
    }
}
