package MazeRunner.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static MazeRunner.Type.PLAYER;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;


@Required(AStarMoveComponent.class)
public class BossComponent extends Component {

    private AStarMoveComponent astar;

    private double lastX = 0;

    private double lastY = 0;

    private final AnimatedTexture texture;

    private final AnimationChannel animIdle, animUP, animDown, animRight, animLeft;

    public BossComponent() {
        getSettings().setGlobalMusicVolume(0.1);
        getSettings().setGlobalSoundVolume(0.1);
        loopBGM("bossMusic.mp3");
        Image imageUp = image("bossU.png");
        Image imageDown = image("bossD.png");
        Image imageLeft = image("bossL.png");
        Image imageRight = image("bossR.png");

        animIdle = new AnimationChannel(imageDown, 3, 72, 96, Duration.seconds(1), 1, 1);
        animUP = new AnimationChannel(imageUp, 3, 72, 96, Duration.seconds(0.66), 0, 2);
        animDown = new AnimationChannel(imageDown, 3, 72, 96, Duration.seconds(0.66), 0, 2);
        animLeft = new AnimationChannel(imageLeft, 3, 72, 96, Duration.seconds(0.66), 0, 2);
        animRight = new AnimationChannel(imageRight, 3, 72, 96, Duration.seconds(0.66), 0, 2);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onUpdate(double tpf) {
        Entity player = getGameWorld().getSingleton(PLAYER);
        astar.moveToCell(player.call("getCellX"), player.call("getCellY"));

        double dx = entity.getX() - lastX;
        double dy = entity.getY() - lastY;

        lastX = entity.getX();
        lastY = entity.getY();

        if (dx == 0.0 && dy == 0.0) {
            texture.loopAnimationChannel(animIdle);
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            // move was horizontal
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
            // move was vertical
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

    @Override
    public void onAdded() {

        entity.getViewComponent().addChild(texture);
    }
}