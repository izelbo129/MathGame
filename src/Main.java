import javafx.event.Event;
import org.mariuszgromada.math.mxparser.License;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage stage) {
        License.iConfirmNonCommercialUse("IanZelbo");
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Zero In");
        stage.show();


    }


    public static void main(String[] argv) {
        launch(argv);
    }
}