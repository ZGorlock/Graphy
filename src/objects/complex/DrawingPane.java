/*
 * File:    DrawingPane.java
 * Package: objects.complex
 * Author:  Zachary Gill
 */

package objects.complex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.Environment;
import math.matrix.Matrix3;
import math.vector.Vector;
import objects.base.AbstractObject;
import objects.base.BaseObject;
import objects.base.polygon.Rectangle;
import utility.ColorUtility;

/**
 * Defines a DrawingPane.
 */
public class DrawingPane extends BaseObject {
    
    //Constants
    
    /**
     * The color used to identify the pixels touched by the DrawingPane.
     */
    private static final int TOUCH_COLOR = new Color(0, 255, 0).getRGB();
    
    
    //Fields
    
    /**
     * The bounds of the DrawingPane.
     */
    public Rectangle bounds;
    
    /**
     * The image of the DrawingPane.
     */
    public BufferedImage image;
    
    /**
     * A flag indicating whether or not to invert the colors of the DrawingPane.
     */
    public boolean invert;
    
    /**
     * The projective matrix for the DrawingPane.
     */
    private Matrix3 projectiveMatrix;
    
    /**
     * The prepared vectors from the last rendering.
     */
    private List<Vector> lastPrepared;
    
    /**
     * The image dimensions from the last rendering.
     */
    private Vector lastImageDimensions;
    
    
    //Constructors
    
    /**
     * The constructor for a DrawingPane.
     *
     * @param parent The parent of the DrawingPane.
     * @param color  The color of the DrawingPane.
     * @param bounds The bounds of the DrawingPane.
     * @param image  The image of the DrawingPane.
     * @param invert Whether or not to invert the colors of the DrawingPane.
     */
    public DrawingPane(AbstractObject parent, Color color, Rectangle bounds, BufferedImage image, boolean invert) {
        super(parent, color, bounds.getCenter(), bounds.getP1(), bounds.getP2(), bounds.getP3(), bounds.getP4());
        
        this.bounds = bounds;
        this.image = image;
        this.invert = invert;
        this.projectiveMatrix = null;
        
        visible = true;
    }
    
    /**
     * The constructor for a DrawingPane.
     *
     * @param parent The parent of the DrawingPane.
     * @param color  The color of the DrawingPane.
     * @param bounds The bounds of the DrawingPane.
     * @param image  The image of the DrawingPane.
     */
    public DrawingPane(AbstractObject parent, Color color, Rectangle bounds, BufferedImage image) {
        this(parent, color, bounds, image, false);
    }
    
    /**
     * The constructor for a DrawingPane.
     *
     * @param parent The parent of the DrawingPane.
     * @param color  The color of the DrawingPane.
     * @param bounds The bounds of the plane.
     */
    public DrawingPane(AbstractObject parent, Color color, Rectangle bounds) {
        this(parent, color, bounds, null);
    }
    
    
    //Methods
    
    /**
     * Prepares the Polygon to be rendered.
     *
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public java.util.List<BaseObject> prepare() {
        List<BaseObject> preparedBases = new ArrayList<>();
        
        prepared.clear();
        for (Vector vertex : vertices) {
            prepared.add(vertex.clone().justify());
        }
        
        performRotationTransformation(prepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Polygon on the screen.
     *
     * @param g2 The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2) {
        if (!preRender(prepared, vertices, -1)) {
            return;
        }
        
        g2.setColor(getColor());
        switch (displayMode) {
            case VERTEX:
                for (Vector v : prepared) {
                    g2.drawRect((int) v.getX(), (int) v.getY(), 1, 1);
                }
                break;
            
            case EDGE:
                for (int i = 1; i < 4; i++) {
                    g2.drawLine((int) prepared.get(i - 1).getX(), (int) prepared.get(i - 1).getY(), (int) prepared.get(i).getX(), (int) prepared.get(i).getY());
                }
                g2.drawLine((int) prepared.get(3).getX(), (int) prepared.get(3).getY(), (int) prepared.get(0).getX(), (int) prepared.get(0).getY());
                break;
            
            case FACE:
                BufferedImage imgTmp = new BufferedImage(Environment.screenX, Environment.screenY, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2Tmp = imgTmp.createGraphics();
                g2Tmp.setColor(new Color(TOUCH_COLOR));
                g2Tmp.fillRect(0, 0, imgTmp.getWidth(), imgTmp.getHeight());
                
                int[] xPoints = new int[4];
                int[] yPoints = new int[4];
                for (int i = 0; i < 4; i++) {
                    xPoints[i] = (int) prepared.get(i).getX();
                    yPoints[i] = (int) prepared.get(i).getY();
                }
                java.awt.Polygon face = new java.awt.Polygon(
                        xPoints,
                        yPoints,
                        4
                );
                g2Tmp.setColor(new Color(0, 0, 0));
                g2Tmp.fillPolygon(face);
                
                g2Tmp.setColor(new Color(TOUCH_COLOR));
                for (int i = 1; i < 4; i++) {
                    g2Tmp.drawLine((int) prepared.get(i - 1).getX(), (int) prepared.get(i - 1).getY(), (int) prepared.get(i).getX(), (int) prepared.get(i).getY());
                }
                g2Tmp.drawLine((int) prepared.get(3).getX(), (int) prepared.get(3).getY(), (int) prepared.get(0).getX(), (int) prepared.get(0).getY());
                
                draw(g2, imgTmp);
                
                g2.setColor(getColor());
                for (int i = 1; i < 4; i++) {
                    g2.drawLine((int) prepared.get(i - 1).getX(), (int) prepared.get(i - 1).getY(), (int) prepared.get(i).getX(), (int) prepared.get(i).getY());
                }
                g2.drawLine((int) prepared.get(3).getX(), (int) prepared.get(3).getY(), (int) prepared.get(0).getX(), (int) prepared.get(0).getY());
                
                break;
        }
        
        renderFrame(g2);
    }
    
    /**
     * Draws the drawing to the pane.
     *
     * @param g2     The 2D Graphics entity.
     * @param imgTmp The temporary image used to render the pane.
     */
    private synchronized void draw(Graphics2D g2, BufferedImage imgTmp) {
        if (image == null) {
            return;
        }
        
        boolean needsUpdate = false;
        if ((lastImageDimensions == null) || (lastPrepared == null) ||
                (lastImageDimensions.getX() != (image.getWidth() - 1)) || (lastImageDimensions.getY() != (image.getHeight() - 1))) {
            needsUpdate = true;
        } else {
            for (int i = 0; i < 4; i++) {
                if ((prepared.get(i).getX() != lastPrepared.get(i).getX()) || (prepared.get(i).getY() != lastPrepared.get(i).getY())) {
                    needsUpdate = true;
                    break;
                }
            }
        }
        
        Matrix3 projectiveMatrix = this.projectiveMatrix;
        if (needsUpdate || (projectiveMatrix == null)) {
            lastPrepared = new ArrayList<>();
            for (Vector v : prepared) {
                lastPrepared.add(v.clone());
            }
            lastImageDimensions = new Vector(image.getWidth() - 1, image.getHeight() - 1);
            
            Matrix3 projectiveMatrixSrc = new Matrix3(new double[] {
                    prepared.get(0).getX(), prepared.get(1).getX(), prepared.get(3).getX(),
                    prepared.get(0).getY(), prepared.get(1).getY(), prepared.get(3).getY(),
                    1.0, 1.0, 1.0});
            Vector solutionSrc = new Vector(prepared.get(2).getX(), prepared.get(2).getY(), 1.0);
            
            Vector coordinateSystemSrc = projectiveMatrixSrc.solveSystem(solutionSrc);
            Matrix3 coordinateMatrixSrc = new Matrix3(new double[] {
                    coordinateSystemSrc.getX(), coordinateSystemSrc.getY(), coordinateSystemSrc.getZ(),
                    coordinateSystemSrc.getX(), coordinateSystemSrc.getY(), coordinateSystemSrc.getZ(),
                    coordinateSystemSrc.getX(), coordinateSystemSrc.getY(), coordinateSystemSrc.getZ()
            });
            projectiveMatrixSrc = projectiveMatrixSrc.scale(coordinateMatrixSrc);
            
            Matrix3 projectiveMatrixDest = new Matrix3(new double[] {
                    0, image.getWidth() - 1, 0,
                    0, 0, image.getHeight() - 1,
                    -1.0, 1.0, 1.0});
            
            try {
                projectiveMatrixSrc = projectiveMatrixSrc.inverse();
            } catch (ArithmeticException ignored) {
                return;
            }
            projectiveMatrix = projectiveMatrixDest.multiply(projectiveMatrixSrc);
            this.projectiveMatrix = projectiveMatrix;
        }
        
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(((int) prepared.get(0).getX() + (int) prepared.get(2).getX()) / 2, ((int) prepared.get(0).getY() + (int) prepared.get(2).getY()) / 2));
        
        int width = imgTmp.getWidth() - 1;
        int height = imgTmp.getHeight() - 1;
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int x = (int) p.getX();
            int y = (int) p.getY();
            
            if ((x < 0) || (x > width) || (y < 0) || (y > height) ||
                    imgTmp.getRGB(x, y) == TOUCH_COLOR) {
                continue;
            }
            imgTmp.setRGB(x, y, TOUCH_COLOR);
            Vector homogeneousSourcePoint = projectiveMatrix.multiply(new Vector(x, y, 1.0));
            int gX = (int) (homogeneousSourcePoint.getX() / homogeneousSourcePoint.getZ());
            int gY = (int) (homogeneousSourcePoint.getY() / homogeneousSourcePoint.getZ());
            Color gC = new Color(image.getRGB(gX, gY));
            g2.setColor(invert ? ColorUtility.invertColor(gC) : gC);
            g2.drawRect(x, y, 1, 1);
            
            stack.push(new Point(x + 1, y));
            stack.push(new Point(x - 1, y));
            stack.push(new Point(x, y + 1));
            stack.push(new Point(x, y - 1));
        }
    }
    
    
    //Setters
    
    /**
     * Sets the image of the DrawingPane.
     *
     * @param image The image of the DrawingPane.
     */
    public synchronized void setImage(BufferedImage image) {
        this.image = image;
    }
    
}
