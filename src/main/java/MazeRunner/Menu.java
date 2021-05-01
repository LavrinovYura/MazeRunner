package MazeRunner;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class Menu extends FXGLMenu {

    public Menu() {
        super(MenuType.MAIN_MENU);
        Node back = texture("back.jpg");
        String musicFile = "StayTheNight.mp3";     // For example
        var menuBox = new VBox(
                5,
                new MenuButton("New Game", this::fireNewGame),
                new MenuButton("Exit", this::fireExit)
        );
        back.setTranslateX(-150);
        menuBox.setTranslateX(getAppWidth() / 2.0 - 50);
        menuBox.setTranslateY(getAppHeight() / 2.0 - 50);
        getContentRoot().getChildren().addAll(back, menuBox);
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 36.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.BLUE)
                            .otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());

            getChildren().add(text);
        }
    }
}
