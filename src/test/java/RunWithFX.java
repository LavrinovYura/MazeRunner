import javafx.application.Platform;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * For tests that require JavaFX platform to be initialized.
 * Usage: @ExtendWith(RunWithFX.class) before the test class.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public final class RunWithFX implements BeforeAllCallback {

    private static boolean jfxStarted = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (jfxStarted)
            return;

        try {
            // this immediately throws IllegalStateException if javafx is not initialized
            Platform.runLater(() -> {});
        } catch (IllegalStateException e) {
            jfxStarted = true;
            Platform.startup(() -> {});
        }
    }
}