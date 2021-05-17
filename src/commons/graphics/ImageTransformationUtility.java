/*
 * File:    ImageTransformationUtility.java
 * Package: commons.graphics
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import commons.math.BoundUtility;
import commons.math.component.matrix.Matrix;
import commons.math.component.matrix.Matrix3;
import commons.math.component.vector.IntVector;
import commons.math.component.vector.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles image transformation operations.
 */
public class ImageTransformationUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ImageTransformationUtility.class);
    
    
    //Functions
    
    /**
     * Performs a quad to quad image transformation.
     *
     * @param src        The source image.
     * @param srcBounds  The bounds from the source image of the quad to transform.
     * @param dest       The destination image.
     * @param destBounds The bounds from the destination image of the quad to place the result of the transformation.
     */
    public static void transformImage(BufferedImage src, List<Vector> srcBounds, BufferedImage dest, List<Vector> destBounds) {
        Graphics2D destGraphics = dest.createGraphics();
        transformImage(src, srcBounds, destGraphics, dest.getWidth(), dest.getHeight(), destBounds);
        DrawUtility.dispose(destGraphics);
    }
    
    /**
     * Performs a quad to quad image transformation.
     *
     * @param src        The source image.
     * @param srcBounds  The bounds from the source image of the quad to transform.
     * @param dest       The destination graphics.
     * @param destWidth  The width of the destination graphics.
     * @param destHeight The height of the destination graphics.
     * @param destBounds The bounds from the destination graphics of the quad to place the result of the transformation.
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public static void transformImage(BufferedImage src, List<Vector> srcBounds, Graphics2D dest, int destWidth, int destHeight, List<Vector> destBounds) {
        if ((src == null) || (srcBounds == null) || (dest == null) || (destBounds == null) ||
                (srcBounds.size() != 4) || (destBounds.size() != 4)) {
            return;
        }
        
        Matrix projectiveMatrix = calculateProjectiveMatrix(srcBounds, destBounds);
        if (projectiveMatrix == null) {
            return;
        }
        
        final int filterColor = new Color(0, 255, 0).getRGB();
        
        BufferedImage maskImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        IntVector maskCenter = new IntVector(Vector.averageVector(destBounds));
        
        Graphics2D maskGraphics = maskImage.createGraphics();
        DrawUtility.setColor(maskGraphics, new Color(filterColor));
        DrawUtility.fillRect(maskGraphics, new IntVector(0, 0), maskImage.getWidth(), maskImage.getHeight());
        DrawUtility.setColor(maskGraphics, new Color(0, 0, 0));
        DrawUtility.fillPolygon(maskGraphics, destBounds);
        DrawUtility.dispose(maskGraphics);
        
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int maskWidth = maskImage.getWidth();
        int maskHeight = maskImage.getHeight();
        
        int[] srcData = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
        int[] maskData = ((DataBufferInt) maskImage.getRaster().getDataBuffer()).getData();
        
        Set<Integer> visited = new HashSet<>();
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(maskCenter.getX(), maskCenter.getY()));
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int x = (int) p.getX();
            int y = (int) p.getY();
            int index = (y * maskImage.getWidth()) + x;
            
            if ((x < 0) || (x >= maskWidth) || (y < 0) || (y >= maskHeight) ||
                    visited.contains(index) || (maskData[y * maskWidth + x] == filterColor)) {
                continue;
            }
            visited.add(index);
            
            stack.push(new Point(x + 1, y));
            stack.push(new Point(x - 1, y));
            stack.push(new Point(x, y + 1));
            stack.push(new Point(x, y - 1));
        }
        
        visited.parallelStream().forEach(p -> {
            Vector homogeneousSourcePoint = projectiveMatrix.times(new Vector(p % maskWidth, p / maskWidth, 1.0));
            int sX = BoundUtility.truncateNum(homogeneousSourcePoint.getRawX() / homogeneousSourcePoint.getRawZ(), 0, srcWidth - 1).intValue();
            int sY = BoundUtility.truncateNum(homogeneousSourcePoint.getRawY() / homogeneousSourcePoint.getRawZ(), 0, srcHeight - 1).intValue();
            maskData[p] = srcData[sY * srcWidth + sX];
        });
        visited.clear();
        
        Shape saveClip = DrawUtility.getClip(dest);
        DrawUtility.setClip(dest, destBounds);
        DrawUtility.drawImage(dest, maskImage);
        DrawUtility.setClip(dest, saveClip);
    }
    
    /**
     * Calculates the projective matrix for a quad to quad image transformation.
     *
     * @param src  The bounds of the quad in the source.
     * @param dest The bounds of the quad in the destination.
     * @return The projective matrix.
     */
    public static Matrix calculateProjectiveMatrix(List<Vector> src, List<Vector> dest) {
        Matrix projectiveMatrixSrc = new Matrix3(
                src.get(0).getRawX(), src.get(1).getRawX(), src.get(3).getRawX(),
                src.get(0).getRawY(), src.get(1).getRawY(), src.get(3).getRawY(),
                1.0, 1.0, 1.0);
        Vector solutionSrc = new Vector(src.get(2).getRawX(), src.get(2).getRawY(), 1.0);
        Vector coordinateSystemSrc = projectiveMatrixSrc.solveSystem(solutionSrc);
        Matrix coordinateMatrixSrc = new Matrix3(
                coordinateSystemSrc.getRawX(), coordinateSystemSrc.getRawY(), coordinateSystemSrc.getRawZ(),
                coordinateSystemSrc.getRawX(), coordinateSystemSrc.getRawY(), coordinateSystemSrc.getRawZ(),
                coordinateSystemSrc.getRawX(), coordinateSystemSrc.getRawY(), coordinateSystemSrc.getRawZ()
        );
        projectiveMatrixSrc = projectiveMatrixSrc.scale(coordinateMatrixSrc);
        
        Matrix projectiveMatrixDest = new Matrix3(
                dest.get(0).getRawX(), dest.get(1).getRawX(), dest.get(3).getRawX(),
                dest.get(0).getRawY(), dest.get(1).getRawY(), dest.get(3).getRawY(),
                1.0, 1.0, 1.0);
        Vector solutionDest = new Vector(dest.get(2).getRawX(), dest.get(2).getRawY(), 1.0);
        Vector coordinateSystemDest = projectiveMatrixDest.solveSystem(solutionDest);
        Matrix coordinateMatrixDest = new Matrix3(
                coordinateSystemDest.getRawX(), coordinateSystemDest.getRawY(), coordinateSystemDest.getRawZ(),
                coordinateSystemDest.getRawX(), coordinateSystemDest.getRawY(), coordinateSystemDest.getRawZ(),
                coordinateSystemDest.getRawX(), coordinateSystemDest.getRawY(), coordinateSystemDest.getRawZ()
        );
        projectiveMatrixDest = projectiveMatrixDest.scale(coordinateMatrixDest);
        
        try {
            projectiveMatrixDest = projectiveMatrixDest.inverse();
        } catch (ArithmeticException ignored) {
            return null;
        }
        return projectiveMatrixSrc.times(projectiveMatrixDest);
    }
    
    /**
     * Creates the default bounds for an image.
     *
     * @param image The image.
     * @return The default bounds for the image.
     */
    public static List<Vector> getBoundsForImage(BufferedImage image) {
        List<Vector> bounds = new ArrayList<>();
        bounds.add(new Vector(0, 0));
        bounds.add(new Vector(image.getWidth() - 1, 0));
        bounds.add(new Vector(image.getWidth() - 1, image.getHeight() - 1));
        bounds.add(new Vector(0, image.getHeight() - 1));
        return bounds;
    }
    
}
