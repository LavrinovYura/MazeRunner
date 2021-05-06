package mazeRunner.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

@Required(AStarMoveComponent.class)
public class AnimationComponent extends Component {

    private double lastX = 0;

    private double lastY = 0;

    private final AnimatedTexture texture;

    private final AnimationChannel animIdle, animUP, animDown, animRight, animLeft;

    public AnimationComponent(
            Image imageUp,
            Image imageDown,
            Image imageLeft,
            Image imageRight,
            int frameWidth,
            int frameHeight,
            int framesPerRow,
            int endFrame
    ) {

        animIdle = new AnimationChannel(imageDown, framesPerRow, frameWidth, frameHeight,  Duration.seconds(1), 1, 1);
        animUP = new AnimationChannel(imageUp, framesPerRow, frameWidth, frameHeight,  Duration.seconds(0.66), 0, endFrame);
        animDown = new AnimationChannel(imageDown, framesPerRow, frameWidth, frameHeight,  Duration.seconds(0.66), 0, endFrame);
        animLeft = new AnimationChannel(imageLeft, framesPerRow, frameWidth, frameHeight,  Duration.seconds(0.66), 0, endFrame);
        animRight = new AnimationChannel(imageRight, framesPerRow, frameWidth, frameHeight,  Duration.seconds(0.66), 0, endFrame);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

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