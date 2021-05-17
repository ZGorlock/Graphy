/*
 * File:    DrawUtility.java
 * Package: commons.graphics
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.List;

import commons.math.component.vector.VectorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles 2D Graphics drawing.
 */
public class DrawUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DrawUtility.class);
    
    
    //Functions
    
    /**
     * Draws a line.
     *
     * @param graphics The 2D Graphics context.
     * @param point    The Vector of the point.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawPoint(Graphics2D graphics, VectorInterface<?, ?> point) throws ArrayIndexOutOfBoundsException {
        graphics.drawRect(
                point.getRawComponents()[0].intValue(), point.getRawComponents()[1].intValue(),
                1, 1);
    }
    
    /**
     * Draws a line.
     *
     * @param graphics The 2D Graphics context.
     * @param from     The Vector to start the line at.
     * @param to       The Vector to end the line at.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawLine(Graphics2D graphics, VectorInterface<?, ?> from, VectorInterface<?, ?> to) throws ArrayIndexOutOfBoundsException {
        graphics.drawLine(
                from.getRawComponents()[0].intValue(), from.getRawComponents()[1].intValue(),
                to.getRawComponents()[0].intValue(), to.getRawComponents()[1].intValue());
    }
    
    /**
     * Draws a rectangle.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the rectangle.
     * @param lowerRight The Vector of the lower right point of the rectangle.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.drawRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Draws a rectangle.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the rectangle.
     * @param width     The width of the rectangle.
     * @param height    The height of the rectangle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height) throws ArrayIndexOutOfBoundsException {
        graphics.drawRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height);
    }
    
    /**
     * Draws a circle.
     *
     * @param graphics The 2D Graphics context.
     * @param center   The Vector of the center of the circle.
     * @param radius   The radius of the circle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawCircle(Graphics2D graphics, VectorInterface<?, ?> center, int radius) throws ArrayIndexOutOfBoundsException {
        graphics.drawOval(
                center.getRawComponents()[0].intValue() - radius, center.getRawComponents()[1].intValue() - radius,
                (radius * 2), (radius * 2));
    }
    
    /**
     * Draws an oval.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the oval.
     * @param lowerRight The Vector of the lower right point of the oval.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawOval(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.drawOval(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Draws an oval.
     *
     * @param graphics The 2D Graphics context.
     * @param center   The Vector of the center of the oval.
     * @param width    The width of the oval.
     * @param height   The height of the oval.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawOval(Graphics2D graphics, VectorInterface<?, ?> center, int width, int height) throws ArrayIndexOutOfBoundsException {
        graphics.drawOval(
                center.getRawComponents()[0].intValue() - (width / 2), center.getRawComponents()[1].intValue() - (height / 2),
                width, height);
    }
    
    /**
     * Draws a polygon.
     *
     * @param graphics The 2D Graphics context.
     * @param polygon  The polygon.
     */
    public static void drawPolygon(Graphics2D graphics, Polygon polygon) {
        graphics.drawPolygon(polygon);
    }
    
    /**
     * Draws a polygon.
     *
     * @param graphics The 2D Graphics context.
     * @param points   The Vectors of the points of the polygon.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawPolygon(Graphics2D graphics, List<? extends VectorInterface<?, ?>> points) throws ArrayIndexOutOfBoundsException {
        graphics.drawPolygon(
                points.stream().mapToInt(e -> e.getRawComponents()[0].intValue()).toArray(),
                points.stream().mapToInt(e -> e.getRawComponents()[1].intValue()).toArray(),
                points.size());
    }
    
    /**
     * Draws a shape.
     *
     * @param graphics The 2D Graphics context.
     * @param shape    The shape.
     */
    public static void drawShape(Graphics2D graphics, Shape shape) {
        graphics.draw(shape);
    }
    
    /**
     * Draws an arc.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the arc.
     * @param lowerRight The Vector of the lower right point of the arc.
     * @param startAngle The starting angle of the arc, in radians.
     * @param endAngle   The ending angle of the arc, in radians.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawArc(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight, double startAngle, double endAngle) throws ArrayIndexOutOfBoundsException {
        graphics.drawArc(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue(),
                (int) Math.toDegrees(startAngle), (int) Math.toDegrees(endAngle - startAngle));
    }
    
    /**
     * Draws an arc.
     *
     * @param graphics   The 2D Graphics context.
     * @param center     The Vector of the center of the arc.
     * @param width      The width of the arc.
     * @param height     The height of the arc.
     * @param startAngle The starting angle of the arc, in radians.
     * @param endAngle   The ending angle of the arc, in radians.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawArc(Graphics2D graphics, VectorInterface<?, ?> center, int width, int height, double startAngle, double endAngle) throws ArrayIndexOutOfBoundsException {
        graphics.drawArc(
                center.getRawComponents()[0].intValue() - (width / 2), center.getRawComponents()[1].intValue() - (height / 2),
                width, height,
                (int) Math.toDegrees(startAngle), (int) Math.toDegrees(endAngle - startAngle));
    }
    
    /**
     * Draws a 3D rectangle.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the 3D rectangle.
     * @param lowerRight The Vector of the lower right point of the 3D rectangle.
     * @param raised     Whether or not to raise the 3D rectangle.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void draw3DRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight, boolean raised) throws ArrayIndexOutOfBoundsException {
        graphics.draw3DRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue(),
                raised);
    }
    
    /**
     * Draws a 3D rectangle.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the 3D rectangle.
     * @param width     The width of the 3D rectangle.
     * @param height    The height of the 3D rectangle.
     * @param raised    Whether or not to raise the 3D rectangle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void draw3DRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height, boolean raised) throws ArrayIndexOutOfBoundsException {
        graphics.draw3DRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height,
                raised);
    }
    
    /**
     * Draws a rounded rectangle.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the rounded rectangle.
     * @param lowerRight The Vector of the lower right point of the rounded rectangle.
     * @param arcWidth   The width of the arc for the rounded rectangle.
     * @param arcHeight  The height of the arc for the rounded rectangle.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawRoundRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight, int arcWidth, int arcHeight) throws ArrayIndexOutOfBoundsException {
        graphics.drawRoundRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue(),
                arcWidth, arcHeight);
    }
    
    /**
     * Draws a rounded rectangle.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the rounded rectangle.
     * @param width     The width of the rounded rectangle.
     * @param height    The height of the rounded rectangle.
     * @param arcWidth  The width of the arc for the rounded rectangle.
     * @param arcHeight The height of the arc for the rounded rectangle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawRoundRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height, int arcWidth, int arcHeight) throws ArrayIndexOutOfBoundsException {
        graphics.drawRoundRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height,
                arcWidth, arcHeight);
    }
    
    /**
     * Fills a rectangle.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the rectangle.
     * @param lowerRight The Vector of the lower right point of the rectangle.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void fillRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.fillRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Fills a rectangle.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the rectangle.
     * @param width     The width of the rectangle.
     * @param height    The height of the rectangle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void fillRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height) throws ArrayIndexOutOfBoundsException {
        graphics.fillRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height);
    }
    
    /**
     * Fills a circle.
     *
     * @param graphics The 2D Graphics context.
     * @param center   The Vector of the center of the circle.
     * @param radius   The radius of the circle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void fillCircle(Graphics2D graphics, VectorInterface<?, ?> center, int radius) throws ArrayIndexOutOfBoundsException {
        graphics.fillOval(
                center.getRawComponents()[0].intValue() - radius, center.getRawComponents()[1].intValue() - radius,
                (radius * 2), (radius * 2));
    }
    
    /**
     * Fills an oval.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the oval.
     * @param lowerRight The Vector of the lower right point of the oval.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void fillOval(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.fillOval(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Fills an oval.
     *
     * @param graphics The 2D Graphics context.
     * @param center   The Vector of the center of the oval.
     * @param width    The width of the oval.
     * @param height   The height of the oval.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void fillOval(Graphics2D graphics, VectorInterface<?, ?> center, int width, int height) throws ArrayIndexOutOfBoundsException {
        graphics.fillOval(
                center.getRawComponents()[0].intValue() - (width / 2), center.getRawComponents()[1].intValue() - (height / 2),
                width, height);
    }
    
    /**
     * Fills a polygon.
     *
     * @param graphics The 2D Graphics context.
     * @param polygon  The polygon.
     */
    public static void fillPolygon(Graphics2D graphics, Polygon polygon) {
        graphics.fillPolygon(polygon);
    }
    
    /**
     * Fills a polygon.
     *
     * @param graphics The 2D Graphics context.
     * @param points   The Vectors of the points of the polygon.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void fillPolygon(Graphics2D graphics, List<? extends VectorInterface<?, ?>> points) throws ArrayIndexOutOfBoundsException {
        graphics.fillPolygon(
                points.stream().mapToInt(e -> e.getRawComponents()[0].intValue()).toArray(),
                points.stream().mapToInt(e -> e.getRawComponents()[1].intValue()).toArray(),
                points.size());
    }
    
    /**
     * Fills a shape.
     *
     * @param graphics The 2D Graphics context.
     * @param shape    The shape.
     */
    public static void fillShape(Graphics2D graphics, Shape shape) {
        graphics.fill(shape);
    }
    
    /**
     * Fills an arc.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the arc.
     * @param lowerRight The Vector of the lower right point of the arc.
     * @param startAngle The starting angle of the arc, in radians.
     * @param endAngle   The ending angle of the arc, in radians.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void fillArc(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight, double startAngle, double endAngle) throws ArrayIndexOutOfBoundsException {
        graphics.fillArc(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue(),
                (int) Math.toDegrees(startAngle), (int) Math.toDegrees(endAngle - startAngle));
    }
    
    /**
     * Fills an arc.
     *
     * @param graphics   The 2D Graphics context.
     * @param center     The Vector of the center of the arc.
     * @param width      The width of the arc.
     * @param height     The height of the arc.
     * @param startAngle The starting angle of the arc, in radians.
     * @param endAngle   The ending angle of the arc, in radians.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void fillArc(Graphics2D graphics, VectorInterface<?, ?> center, int width, int height, double startAngle, double endAngle) throws ArrayIndexOutOfBoundsException {
        graphics.fillArc(
                center.getRawComponents()[0].intValue() - (width / 2), center.getRawComponents()[1].intValue() - (height / 2),
                width, height,
                (int) Math.toDegrees(startAngle), (int) Math.toDegrees(endAngle - startAngle));
    }
    
    /**
     * Fills a 3D rectangle.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the 3D rectangle.
     * @param lowerRight The Vector of the lower right point of the 3D rectangle.
     * @param raised     Whether or not to raise the 3D rectangle.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void fill3DRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight, boolean raised) throws ArrayIndexOutOfBoundsException {
        graphics.fill3DRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue(),
                raised);
    }
    
    /**
     * Fills a 3D rectangle.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the 3D rectangle.
     * @param width     The width of the 3D rectangle.
     * @param height    The height of the 3D rectangle.
     * @param raised    Whether or not to raise the 3D rectangle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void fill3DRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height, boolean raised) throws ArrayIndexOutOfBoundsException {
        graphics.fill3DRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height,
                raised);
    }
    
    /**
     * Fills a rounded rectangle.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the rounded rectangle.
     * @param lowerRight The Vector of the lower right point of the rounded rectangle.
     * @param arcWidth   The width of the arc for the rounded rectangle.
     * @param arcHeight  The height of the arc for the rounded rectangle.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void fillRoundRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight, int arcWidth, int arcHeight) throws ArrayIndexOutOfBoundsException {
        graphics.fillRoundRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue(),
                arcWidth, arcHeight);
    }
    
    /**
     * Fills a rounded rectangle.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the rounded rectangle.
     * @param width     The width of the rounded rectangle.
     * @param height    The height of the rounded rectangle.
     * @param arcWidth  The width of the arc for the rounded rectangle.
     * @param arcHeight The height of the arc for the rounded rectangle.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void fillRoundRect(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height, int arcWidth, int arcHeight) throws ArrayIndexOutOfBoundsException {
        graphics.fillRoundRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height,
                arcWidth, arcHeight);
    }
    
    /**
     * Draws an image.
     *
     * @param graphics         The 2D Graphics context.
     * @param image            The image.
     * @param sourceUpperLeft  The Vector of the upper left point of the source area.
     * @param sourceLowerRight The Vector of the lower right point of the source area.
     * @param destUpperLeft    The Vector of the upper left point of the destination area.
     * @param destLowerRight   The Vector of the lower right point of the destination area.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawImage(Graphics2D graphics, BufferedImage image, VectorInterface<?, ?> sourceUpperLeft, VectorInterface<?, ?> sourceLowerRight, VectorInterface<?, ?> destUpperLeft, VectorInterface<?, ?> destLowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.drawImage(image,
                destUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue(),
                destLowerRight.getRawComponents()[0].intValue(), destLowerRight.getRawComponents()[1].intValue(),
                sourceUpperLeft.getRawComponents()[0].intValue(), sourceUpperLeft.getRawComponents()[1].intValue(),
                sourceLowerRight.getRawComponents()[0].intValue(), sourceLowerRight.getRawComponents()[1].intValue(),
                null);
    }
    
    /**
     * Draws an image.
     *
     * @param graphics        The 2D Graphics context.
     * @param image           The image.
     * @param sourceUpperLeft The Vector of the upper left point of the source area.
     * @param sourceWidth     The width of the source area.
     * @param sourceHeight    The height of the source area.
     * @param destUpperLeft   The Vector of the upper left point of the destination area.
     * @param destWidth       The width of the destination area.
     * @param destHeight      The height of the destination area.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawImage(Graphics2D graphics, BufferedImage image, VectorInterface<?, ?> sourceUpperLeft, int sourceWidth, int sourceHeight, VectorInterface<?, ?> destUpperLeft, int destWidth, int destHeight) throws ArrayIndexOutOfBoundsException {
        graphics.drawImage(image,
                destUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue(),
                destUpperLeft.getRawComponents()[0].intValue() + destWidth, destUpperLeft.getRawComponents()[1].intValue() + destHeight,
                sourceUpperLeft.getRawComponents()[0].intValue(), sourceUpperLeft.getRawComponents()[1].intValue(),
                sourceUpperLeft.getRawComponents()[0].intValue() + sourceWidth, sourceUpperLeft.getRawComponents()[1].intValue() + sourceHeight,
                null);
    }
    
    /**
     * Draws an image.
     *
     * @param graphics       The 2D Graphics context.
     * @param image          The image.
     * @param destUpperLeft  The Vector of the upper left point of the destination area.
     * @param destLowerRight The Vector of the lower right point of the destination area.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void drawImage(Graphics2D graphics, BufferedImage image, VectorInterface<?, ?> destUpperLeft, VectorInterface<?, ?> destLowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.drawImage(image,
                destUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue(),
                destLowerRight.getRawComponents()[0].intValue() - destUpperLeft.getRawComponents()[0].intValue(), destLowerRight.getRawComponents()[1].intValue() - destUpperLeft.getRawComponents()[1].intValue(),
                null);
    }
    
    /**
     * Draws an image.
     *
     * @param graphics      The 2D Graphics context.
     * @param image         The image.
     * @param destUpperLeft The Vector of the upper left point of the destination area.
     * @param destWidth     The width of the destination area.
     * @param destHeight    The height of the destination area.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawImage(Graphics2D graphics, BufferedImage image, VectorInterface<?, ?> destUpperLeft, int destWidth, int destHeight) throws ArrayIndexOutOfBoundsException {
        graphics.drawImage(image,
                destUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue(),
                destWidth, destHeight,
                null);
    }
    
    /**
     * Draws an image.
     *
     * @param graphics      The 2D Graphics context.
     * @param image         The image.
     * @param destUpperLeft The Vector of the upper left point of the destination area.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawImage(Graphics2D graphics, BufferedImage image, VectorInterface<?, ?> destUpperLeft) throws ArrayIndexOutOfBoundsException {
        graphics.drawImage(image,
                destUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue(),
                null);
    }
    
    /**
     * Draws an image.
     *
     * @param graphics The 2D Graphics context.
     * @param image    The image.
     */
    public static void drawImage(Graphics2D graphics, BufferedImage image) {
        graphics.drawImage(image,
                0, 0,
                null);
    }
    
    /**
     * Draws a string.
     *
     * @param graphics  The 2D Graphics context.
     * @param string    The string.
     * @param lowerLeft The Vector of the lower left point of the area to draw the string.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void drawString(Graphics2D graphics, String string, VectorInterface<?, ?> lowerLeft) throws ArrayIndexOutOfBoundsException {
        graphics.drawString(string,
                lowerLeft.getRawComponents()[0].intValue(), lowerLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Copies an area to another location.
     *
     * @param graphics         The 2D Graphics context.
     * @param sourceUpperLeft  The Vector of the upper left point of the area to copy.
     * @param sourceLowerRight The Vector of the lower right point of the area to copy.
     * @param destUpperLeft    The Vector of the upper left point of the area to paste.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void copyArea(Graphics2D graphics, VectorInterface<?, ?> sourceUpperLeft, VectorInterface<?, ?> sourceLowerRight, VectorInterface<?, ?> destUpperLeft) throws ArrayIndexOutOfBoundsException {
        graphics.copyArea(
                sourceUpperLeft.getRawComponents()[0].intValue(), sourceUpperLeft.getRawComponents()[1].intValue(),
                sourceLowerRight.getRawComponents()[0].intValue() - sourceUpperLeft.getRawComponents()[0].intValue(), sourceLowerRight.getRawComponents()[1].intValue() - sourceUpperLeft.getRawComponents()[1].intValue(),
                destUpperLeft.getRawComponents()[0].intValue() - sourceUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue() - sourceUpperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Copies an area to another location.
     *
     * @param graphics        The 2D Graphics context.
     * @param sourceUpperLeft The Vector of the upper left point of the area to copy.
     * @param sourceWidth     The width of the area to copy.
     * @param sourceHeight    The height of the area to copy.
     * @param destUpperLeft   The Vector of the upper left point of the area to copy to.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void copyArea(Graphics2D graphics, VectorInterface<?, ?> sourceUpperLeft, int sourceWidth, int sourceHeight, VectorInterface<?, ?> destUpperLeft) throws ArrayIndexOutOfBoundsException {
        graphics.copyArea(
                sourceUpperLeft.getRawComponents()[0].intValue(), sourceUpperLeft.getRawComponents()[1].intValue(),
                sourceWidth, sourceHeight,
                destUpperLeft.getRawComponents()[0].intValue() - sourceUpperLeft.getRawComponents()[0].intValue(), destUpperLeft.getRawComponents()[1].intValue() - sourceUpperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Clears an area.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the area to clear.
     * @param lowerRight The Vector of the lower right point of the area to clear.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void clearArea(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.clearRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Clears an area.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the area to clear.
     * @param width     The width of the area to clear.
     * @param height    The height of the area to clear.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void clearArea(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height) throws ArrayIndexOutOfBoundsException {
        graphics.clearRect(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height);
    }
    
    /**
     * Sets the drawing color.
     *
     * @param graphics The 2D Graphics context.
     * @param color    The color.
     */
    public static void setColor(Graphics2D graphics, Color color) {
        graphics.setColor(color);
    }
    
    /**
     * Sets the background color.
     *
     * @param graphics The 2D Graphics context.
     * @param color    The background color.
     */
    public static void setBackground(Graphics2D graphics, Color color) {
        graphics.setBackground(color);
    }
    
    /**
     * Sets the drawing clip.
     *
     * @param graphics The 2D Graphics context.
     * @param font     The font.
     */
    public static void setFont(Graphics2D graphics, Font font) {
        graphics.setFont(font);
    }
    
    /**
     * Sets the drawing clip.
     *
     * @param graphics The 2D Graphics context.
     * @param clip     The clip.
     */
    public static void setClip(Graphics2D graphics, Shape clip) {
        graphics.setClip(clip);
    }
    
    /**
     * Sets the drawing clip.
     *
     * @param graphics The 2D Graphics context.
     * @param points   The Vectors of the points of the clip.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void setClip(Graphics2D graphics, List<? extends VectorInterface<?, ?>> points) throws ArrayIndexOutOfBoundsException {
        graphics.setClip(new Polygon(
                points.stream().mapToInt(e -> e.getRawComponents()[0].intValue()).toArray(),
                points.stream().mapToInt(e -> e.getRawComponents()[1].intValue()).toArray(),
                points.size()));
    }
    
    /**
     * Sets the drawing clip.
     *
     * @param graphics   The 2D Graphics context.
     * @param upperLeft  The Vector of the upper left point of the clip.
     * @param lowerRight The Vector of the lower right point of the clip.
     * @throws ArrayIndexOutOfBoundsException If any of the Vectors do not have at least a dimensionality of 2.
     */
    public static void setClip(Graphics2D graphics, VectorInterface<?, ?> upperLeft, VectorInterface<?, ?> lowerRight) throws ArrayIndexOutOfBoundsException {
        graphics.setClip(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                lowerRight.getRawComponents()[0].intValue() - upperLeft.getRawComponents()[0].intValue(), lowerRight.getRawComponents()[1].intValue() - upperLeft.getRawComponents()[1].intValue());
    }
    
    /**
     * Sets the drawing clip.
     *
     * @param graphics  The 2D Graphics context.
     * @param upperLeft The Vector of the upper left point of the clip.
     * @param width     The width of the clip.
     * @param height    The height of the clip.
     * @throws ArrayIndexOutOfBoundsException If the Vector does not have at least a dimensionality of 2.
     */
    public static void setClip(Graphics2D graphics, VectorInterface<?, ?> upperLeft, int width, int height) throws ArrayIndexOutOfBoundsException {
        graphics.setClip(
                upperLeft.getRawComponents()[0].intValue(), upperLeft.getRawComponents()[1].intValue(),
                width, height);
    }
    
    /**
     * Returns the drawing color.
     *
     * @param graphics The 2D Graphics context.
     * @return The drawing color.
     */
    public static Color getColor(Graphics2D graphics) {
        return graphics.getColor();
    }
    
    /**
     * Returns the background color.
     *
     * @param graphics The 2D Graphics context.
     * @return The background color.
     */
    public static Color getBackground(Graphics2D graphics) {
        return graphics.getBackground();
    }
    
    /**
     * Returns the font.
     *
     * @param graphics The 2D Graphics context.
     * @return The font.
     */
    public static Font getFont(Graphics2D graphics) {
        return graphics.getFont();
    }
    
    /**
     * Returns the clip.
     *
     * @param graphics The 2D Graphics context.
     * @return The clip.
     */
    public static Shape getClip(Graphics2D graphics) {
        return graphics.getClip();
    }
    
    /**
     * Disposes of the 2D Graphics context.
     *
     * @param graphics The 2D Graphics context.
     */
    public static void dispose(Graphics2D graphics) {
        graphics.dispose();
    }
    
}
