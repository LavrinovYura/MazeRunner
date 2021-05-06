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

        MenuButton ng = new MenuButton("New Game", this::fireNewGame);
        MenuButton ex = new MenuButton("Exit", this::fireExit);

        ng.setTranslateX(getAppWidth() / 2.0 -50);
        ng.setTranslateY(getAppWidth() / 2.0);

        ex.setTranslateX(getAppHeight() / 2.0 - 50);
        ex.setTranslateY(getAppHeight() / 2.0 + 50);

        getContentRoot().getChildren().addAll(back, ng, ex);
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
