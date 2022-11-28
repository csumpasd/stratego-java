package helper;

import java.awt.*;

/**
 * Interface for a lambda function that can shift a coordinate pair by i
 */
public interface PointShiftFunction {

    /**
     * Shift a coordinate pair by i, using a given lambda function
     * @param x First coordinate
     * @param y Second coordinate
     * @param i Shift coordinate
     * @return The shifted point
     */
    Point shiftPoint(int x, int y, int i);


}
