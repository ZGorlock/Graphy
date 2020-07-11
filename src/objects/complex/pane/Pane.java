/*
 * File:    Pane.java
 * Package: objects.complex.pane
 * Author:  Zachary Gill
 */

package objects.complex.pane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import main.Environment;
import math.matrix.Matrix3;
import math.vector.Vector;
import math.vector.Vector3;
import objects.base.AbstractObject;
import objects.base.BaseObject;
import objects.base.polygon.Rectangle;
import utility.BoundUtility;
import utility.ColorUtility;

/**
 * Defines a Pane.
 */
public abstract class Pane extends BaseObject {
    
    //Constants
    
    /**
     * The color used to identify the pixels touched by the Pane.
     */
    private static final int TOUCH_COLOR = new Color(0, 255, 0).getRGB();
    
    
    //Fields
    
    /**
     * The bounds of the Pane.
     */
    public Rectangle bounds;
    
    /**
     * The unit normal vector of the Pane.
     */
    public Vector normal;
    
    /**
     * The image of the Pane.
     */
    public BufferedImage image;
    
    /**
     * A flag indicating whether or not to invert the colors of the Pane.
     */
    public boolean invert;
    
    /**
     * The projective matrix for the Pane.
     */
    private Matrix3 projectiveMatrix;
    
    /**
     * The prepared vectors from the last rendering, per perspective.
     */
    private Map<UUID, List<Vector>> lastPrepared = new ConcurrentHashMap<>();
    
    /**
     * The image dimensions from the last rendering.
     */
    private Vector lastImageDimensions;
    
    
    //Constructors
    
    /**
     * The constructor for a Pane.
     *
     * @param parent The parent of the Pane.
     * @param color  The color of the Pane.
     * @param bounds The bounds of the Pane.
     * @param invert Whether or not to invert the colors of the Pane.
     */
    public Pane(AbstractObject parent, Color color, Rectangle bounds, boolean invert) {
        super(parent, color, bounds.getCenter(), bounds.getP1(), bounds.getP2(), bounds.getP3(), bounds.getP4());
        
        this.bounds = bounds;
        this.invert = invert;
        this.projectiveMatrix = null;
        
        visible = true;
    }
    
    /**
     * The constructor for a Pane.
     *
     * @param parent The parent of the Pane.
     * @param color  The color of the Pane.
     * @param bounds The bounds of the Pane.
     */
    public Pane(AbstractObject parent, Color color, Rectangle bounds) {
        this(parent, color, bounds, false);
    }
    
    
    //Methods
    
    /**
     * Prepares the Pane to be rendered.
     *
     * @param perspective The perspective to prepare the Pane for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public java.util.List<BaseObject> prepare(UUID perspective) {
        List<BaseObject> preparedBases = new ArrayList<>();
        
        prepared.get(perspective).clear();
        for (Vector vertex : vertices) {
            prepared.get(perspective).add(vertex.clone().justify());
        }
        
        performRotationTransformation(prepared.get(perspective));
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Pane on the screen.
     *
     * @param perspective The perspective to render the Pane for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        calculateNormal();
        g2.setColor(getColor());
        switch (displayMode) {
            case VERTEX:
                for (Vector v : prepared.get(perspective)) {
                    g2.drawRect((int) v.getX(), (int) v.getY(), 1, 1);
                }
                break;
            
            case EDGE:
                for (int i = 1; i < 4; i++) {
                    g2.drawLine((int) prepared.get(perspective).get(i - 1).getX(), (int) prepared.get(perspective).get(i - 1).getY(), (int) prepared.get(perspective).get(i).getX(), (int) prepared.get(perspective).get(i).getY());
                }
                g2.drawLine((int) prepared.get(perspective).get(3).getX(), (int) prepared.get(perspective).get(3).getY(), (int) prepared.get(perspective).get(0).getX(), (int) prepared.get(perspective).get(0).getY());
                break;
            
            case FACE:
                BufferedImage imgTmp = new BufferedImage(Environment.screenX, Environment.screenY, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2Tmp = imgTmp.createGraphics();
                g2Tmp.setColor(new Color(TOUCH_COLOR));
                g2Tmp.fillRect(0, 0, imgTmp.getWidth(), imgTmp.getHeight());
                
                int[] xPoints = new int[4];
                int[] yPoints = new int[4];
                for (int i = 0; i < 4; i++) {
                    xPoints[i] = (int) prepared.get(perspective).get(i).getX();
                    yPoints[i] = (int) prepared.get(perspective).get(i).getY();
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
                    g2Tmp.drawLine((int) prepared.get(perspective).get(i - 1).getX(), (int) prepared.get(perspective).get(i - 1).getY(), (int) prepared.get(perspective).get(i).getX(), (int) prepared.get(perspective).get(i).getY());
                }
                g2Tmp.drawLine((int) prepared.get(perspective).get(3).getX(), (int) prepared.get(perspective).get(3).getY(), (int) prepared.get(perspective).get(0).getX(), (int) prepared.get(perspective).get(0).getY());
                
                draw(g2, perspective, imgTmp);
                
                g2.setColor(getColor());
                for (int i = 1; i < 4; i++) {
                    g2.drawLine((int) prepared.get(perspective).get(i - 1).getX(), (int) prepared.get(perspective).get(i - 1).getY(), (int) prepared.get(perspective).get(i).getX(), (int) prepared.get(perspective).get(i).getY());
                }
                g2.drawLine((int) prepared.get(perspective).get(3).getX(), (int) prepared.get(perspective).get(3).getY(), (int) prepared.get(perspective).get(0).getX(), (int) prepared.get(perspective).get(0).getY());
                
                break;
        }
    }
    
    /**
     * Calculates the unit normal vector of the Pane.
     *
     * @return The unit normal vector of the Pane.
     */
    public Vector calculateNormal() {
        normal = new Vector3(bounds.getP2().minus(bounds.getP1())).cross(new Vector3(bounds.getP4().minus(bounds.getP1()))).normalize();
        return normal;
    }
    
    /**
     * Draws the drawing to the pane.
     *
     * @param g2          The 2D Graphics entity.
     * @param perspective The perspective from which to draw the drawing to the pane.
     * @param imgTmp      The temporary image used to render the pane.
     */
    private synchronized void draw(Graphics2D g2, UUID perspective, BufferedImage imgTmp) {
        if (image == null) {
            return;
        }
        
        boolean needsUpdate = false;
        if ((lastImageDimensions == null) || !lastPrepared.containsKey(perspective) ||
                (lastImageDimensions.getX() != (image.getWidth() - 1)) || (lastImageDimensions.getY() != (image.getHeight() - 1))) {
            needsUpdate = true;
        } else {
            for (int i = 0; i < 4; i++) {
                if ((prepared.get(perspective).get(i).getX() != lastPrepared.get(perspective).get(i).getX()) || (prepared.get(perspective).get(i).getY() != lastPrepared.get(perspective).get(i).getY())) {
                    needsUpdate = true;
                    break;
                }
            }
        }
        
        Matrix3 projectiveMatrix = this.projectiveMatrix;
        if (needsUpdate || (projectiveMatrix == null)) {
            lastPrepared.put(perspective, new ArrayList<>());
            for (Vector v : prepared.get(perspective)) {
                lastPrepared.get(perspective).add(v.clone());
            }
            lastImageDimensions = new Vector(image.getWidth() - 1, image.getHeight() - 1);
            
            Matrix3 projectiveMatrixSrc = new Matrix3(new double[] {
                    prepared.get(perspective).get(0).getX(), prepared.get(perspective).get(1).getX(), prepared.get(perspective).get(3).getX(),
                    prepared.get(perspective).get(0).getY(), prepared.get(perspective).get(1).getY(), prepared.get(perspective).get(3).getY(),
                    1.0, 1.0, 1.0});
            Vector solutionSrc = new Vector(prepared.get(perspective).get(2).getX(), prepared.get(perspective).get(2).getY(), 1.0);
            
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
        stack.push(new Point(((int) prepared.get(perspective).get(0).getX() + (int) prepared.get(perspective).get(2).getX()) / 2, ((int) prepared.get(perspective).get(0).getY() + (int) prepared.get(perspective).get(2).getY()) / 2));
        
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
            int gX = BoundUtility.truncateNum(homogeneousSourcePoint.getX() / homogeneousSourcePoint.getZ(), 0, image.getWidth() - 1).intValue();
            int gY = BoundUtility.truncateNum(homogeneousSourcePoint.getY() / homogeneousSourcePoint.getZ(), 0, image.getHeight() - 1).intValue();
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
     * Sets the image of the Pane.
     *
     * @param image The image of the Pane.
     */
    public synchronized void setImage(BufferedImage image) {
        this.image = image;
    }
    
}
