import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PaneOrganizer {

    private BorderPane root = new BorderPane();

    public PaneOrganizer() {
        UIController controller = new UIController();
        this.root.setCenter(controller.getRoot());
    }

    public Pane getRoot() {
        return this.root;
    }



}
