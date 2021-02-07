/*
 * File:    ImageTransformationUtility.java
 * Package: commons.graphics
 * Author:  Zachary Gill
 */

package commons.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import commons.math.BoundUtility;
import commons.math.matrix.Matrix3;
import commons.math.vector.Vector;
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
        destGraphics.dispose();
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
        
        Matrix3 projectiveMatrix = calculateProjectiveMatrix(srcBounds, destBounds);
        if (projectiveMatrix == null) {
            return;
        }
        
        final int filterColor = new Color(0, 255, 0).getRGB();
        
        BufferedImage maskImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D maskGraphics = maskImage.createGraphics();
        maskGraphics.setColor(new Color(filterColor));
        maskGraphics.fillRect(0, 0, maskImage.getWidth(), maskImage.getHeight());
        Polygon mask = new Polygon(
                destBounds.stream().map(e -> (int) e.getX()).mapToInt(Integer::valueOf).toArray(),
                destBounds.stream().map(e -> (int) e.getY()).mapToInt(Integer::valueOf).toArray(),
                4
        );
        Vector maskCenter = Vector.averageVector(destBounds);
        maskGraphics.setColor(new Color(0, 0, 0));
        maskGraphics.fillPolygon(mask);
        maskGraphics.dispose();
        
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int maskWidth = maskImage.getWidth();
        int maskHeight = maskImage.getHeight();
        
        int[] srcData = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
        int[] maskData = ((DataBufferInt) maskImage.getRaster().getDataBuffer()).getData();
        
        Set<Integer> visited = new HashSet<>();
        Stack<Point> stack = new Stack<>();
        stack.push(new Point((int) maskCenter.getX(), (int) maskCenter.getY()));
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
            Vector homogeneousSourcePoint = projectiveMatrix.multiply(new Vector(p % maskWidth, p / maskWidth, 1.0));
            int sX = BoundUtility.truncateNum(homogeneousSourcePoint.getX() / homogeneousSourcePoint.getZ(), 0, srcWidth - 1).intValue();
            int sY = BoundUtility.truncateNum(homogeneousSourcePoint.getY() / homogeneousSourcePoint.getZ(), 0, srcHeight - 1).intValue();
            maskData[p] = srcData[sY * srcWidth + sX];
        });
        visited.clear();
        
        Shape saveClip = dest.getClip();
        dest.setClip(mask);
        dest.drawImage(maskImage, 0, 0, maskWidth, maskHeight, null);
        dest.setClip(saveClip);
    }
    
    /**
     * Calculates the projective matrix for a quad to quad image transformation.
     *
     * @param src  The bounds of the quad in the source.
     * @param dest The bounds of the quad in the destination.
     * @return The projective matrix.
     */
    private static Matrix3 calculateProjectiveMatrix(List<Vector> src, List<Vector> dest) {
        Matrix3 projectiveMatrixSrc = new Matrix3(new double[] {
                src.get(0).getX(), src.get(1).getX(), src.get(3).getX(),
                src.get(0).getY(), src.get(1).getY(), src.get(3).getY(),
                1.0, 1.0, 1.0});
        Vector solutionSrc = new Vector(src.get(2).getX(), src.get(2).getY(), 1.0);
        Vector coordinateSystemSrc = projectiveMatrixSrc.solveSystem(solutionSrc);
        Matrix3 coordinateMatrixSrc = new Matrix3(new double[] {
                coordinateSystemSrc.getX(), coordinateSystemSrc.getY(), coordinateSystemSrc.getZ(),
                coordinateSystemSrc.getX(), coordinateSystemSrc.getY(), coordinateSystemSrc.getZ(),
                coordinateSystemSrc.getX(), coordinateSystemSrc.getY(), coordinateSystemSrc.getZ()
        });
        projectiveMatrixSrc = projectiveMatrixSrc.scale(coordinateMatrixSrc);
        
        Matrix3 projectiveMatrixDest = new Matrix3(new double[] {
                dest.get(0).getX(), dest.get(1).getX(), dest.get(3).getX(),
                dest.get(0).getY(), dest.get(1).getY(), dest.get(3).getY(),
                1.0, 1.0, 1.0});
        Vector solutionDest = new Vector(dest.get(2).getX(), dest.get(2).getY(), 1.0);
        Vector coordinateSystemDest = projectiveMatrixDest.solveSystem(solutionDest);
        Matrix3 coordinateMatrixDest = new Matrix3(new double[] {
                coordinateSystemDest.getX(), coordinateSystemDest.getY(), coordinateSystemDest.getZ(),
                coordinateSystemDest.getX(), coordinateSystemDest.getY(), coordinateSystemDest.getZ(),
                coordinateSystemDest.getX(), coordinateSystemDest.getY(), coordinateSystemDest.getZ()
        });
        projectiveMatrixDest = projectiveMatrixDest.scale(coordinateMatrixDest);
        
        try {
            projectiveMatrixDest = projectiveMatrixDest.inverse();
        } catch (ArithmeticException ignored) {
            return null;
        }
        return projectiveMatrixSrc.multiply(projectiveMatrixDest);
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
