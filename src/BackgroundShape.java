import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.Random;

public class BackgroundShape {

    private final StackPane mathPane;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final Random random = new Random();
    private final BoxBlur blur = new BoxBlur(10, 10, 3);

    public BackgroundShape(StackPane mathPane) {
        this.mathPane = mathPane;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2), event -> {
                    Shape shape;
                    double size = random.nextInt(80) + 120;
                    double xPos = random.nextDouble() * WIDTH;
                    if (random.nextBoolean()) {
                        shape = new Circle(size);
                    } else if (random.nextBoolean()) {
                        shape = new Rectangle(size, size);
                    } else {
                        shape = new Polygon(
                                0, 0,
                                size, size / 2,
                                0, size
                        );
                    }
                    shape.setTranslateX(xPos);
                    shape.setTranslateY(HEIGHT + size + random.nextInt(HEIGHT));
                    shape.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.5));
                    shape.setEffect(blur);
                    mathPane.getChildren().add(shape);

                    Timeline shapeTimeline = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(shape.translateYProperty(), shape.getTranslateY())),
                            new KeyFrame(Duration.seconds(random.nextDouble() * 30 + 4),
                                    new KeyValue(shape.translateYProperty(), -size - random.nextInt(HEIGHT) * 1.5))
                    );
                    shapeTimeline.play();

                    // Add a ChangeListener to the translateYProperty of the shape
                    shape.translateYProperty().addListener((observableValue, oldValue, newValue) -> {
                        // Check if the shape has passed the stage
                        if (newValue.doubleValue() <= -size) {
                            mathPane.getChildren().remove(shape); // Remove the shape from the mathPane
                        }
                    });
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
