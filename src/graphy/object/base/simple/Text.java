/*
 * File:    Text.java
 * Package: graphy.object.base.simple
 * Author:  Zachary Gill
 */

package graphy.object.base.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.math.vector.Vector;
import graphy.object.base.AbstractObject;
import graphy.object.base.BaseObject;

/**
 * Defines a Text object.
 */
public class Text extends BaseObject {
    
    //Fields
    
    /**
     * The text to display.
     */
    protected char[] text;
    
    
    //Constructor
    
    /**
     * The constructor for a Text.
     *
     * @param parent The parent of the Text.
     * @param color  The color of the Text.
     * @param v      The Vector defining the point of the Text.
     * @param text   The text to display.
     */
    public Text(AbstractObject parent, Color color, Vector v, String text) {
        super(parent, color, v, v);
        this.text = text.toCharArray();
    }
    
    /**
     * The constructor for a Text.
     *
     * @param parent The parent of the Text.
     * @param v      The Vector defining the point of the Text.
     * @param text   The text to display.
     */
    public Text(AbstractObject parent, Vector v, String text) {
        this(parent, Color.BLACK, v, text);
    }
    
    /**
     * The constructor for a Text.
     *
     * @param color The color of the Text.
     * @param v     The Vector defining the point of the Text.
     * @param text  The text to display.
     */
    public Text(Color color, Vector v, String text) {
        this(null, color, v, text);
    }
    
    /**
     * The constructor for a Text.
     *
     * @param v    The Vector defining the point of the Text.
     * @param text The text to display.
     */
    public Text(Vector v, String text) {
        this(null, Color.BLACK, v, text);
    }
    
    
    //Methods
    
    /**
     * Prepares the Text to be rendered.
     *
     * @param perspective The perspective to prepare the Text for.
     * @return The list of BaseObjects that were prepared.
     */
    @Override
    public List<BaseObject> prepare(UUID perspective) {
        List<BaseObject> preparedBases = new ArrayList<>();
        List<Vector> perspectivePrepared = prepared.get(perspective);
        
        perspectivePrepared.clear();
        perspectivePrepared.add(vertices[0].clone().justify());
        
        performRotationTransformation(perspectivePrepared);
        
        preparedBases.add(this);
        return preparedBases;
    }
    
    /**
     * Renders the Text on the screen.
     *
     * @param perspective The perspective to render the Text for.
     * @param g2          The 2D Graphics entity.
     */
    @Override
    public void render(Graphics2D g2, UUID perspective) {
        g2.setColor(color);
        g2.drawChars(text, 0, text.length, (int) prepared.get(perspective).get(0).getX(), (int) prepared.get(perspective).get(0).getY());
    }
    
    
    //Setters
    
    /**
     * Sets the text for the Text object.
     *
     * @param text The new text.
     */
    public void setText(String text) {
        this.text = text.toCharArray();
    }
    
}
