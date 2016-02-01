package Models.Graphing;

import java.awt.*;

/**
 * Created by Rokas on 30/01/2016.
 */
public class Marker {
    private Paint color;
    private Shape shape;

    public Marker(Paint color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }

    public Paint getColor() {
        return color;
    }
}
