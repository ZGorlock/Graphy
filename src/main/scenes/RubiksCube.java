/*
 * File:    RubiksCube.java
 * Package: main.scenes
 * Author:  Zachary Gill
 */

package main.scenes;

import camera.Camera;
import main.Environment;
import math.vector.Vector;
import objects.base.Object;
import objects.base.Scene;
import objects.base.group.RotationGroup;
import objects.polyhedron.regular.platonic.Hexahedron;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a Rubik's Cube scene.
 */
public class RubiksCube extends Scene
{
    
    //Constants
    
    /**
     * The indices of faces within the piece matrix.
     */
    private static final int FRONT = 0;
    private static final int TOP = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 3;
    private static final int LEFT = 4;
    private static final int BACK = 5;
    
    /**
     * The speed of cube flips in milliseconds.
     */
    public static final int FLIP_SPEED = 250;
    
    /**
     * A flag indicating whether or not to print the cube state after transformations.
     */
    public static final boolean PRINT_CUBE_STATE = false;
    
    
    //Fields
    
    /**
     * The radius of the enclosing sphere of the Rubik's cube.
     */
    private double radius;
    
    /**
     * The Rubik's Cube.
     */
    private static RubiksCube rubiksCube;
    
    /**
     * The list of pieces that make up the cube.
     */
    private static final List<Hexahedron> pieces = new ArrayList<>();
    
    /**
     * The matrix of pieces that are positioned on each side.
     */
    private static int[][] matrix = {{0, 1, 2, 3, 4, 5, 6, 7, 8}, //front
                                     {18, 19, 20, 9, 10, 11, 0, 1, 2}, //top
                                     {2, 11, 20, 5, 14, 23, 8, 17, 26}, //right
                                     {6, 7, 8, 15, 16, 17, 24, 25, 26}, //bottom
                                     {18, 9, 0, 21, 12, 3, 24, 15, 6}, //left
                                     {20, 19, 18, 23, 22, 21, 26, 25, 24}}; //back
    
    
    //Main Method
    
    /**
     * The main method for the Rubik's Cube scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args)
    {
        String[] environmentArgs = new String[]{};
        Environment.main(environmentArgs);
        
        List<Object> objects = createObjects();
        for (Object object : objects) {
            Environment.addObject(object);
        }
        
        setupCameras();
        
        setupControls();
    }
    
    /**
     * Creates objects for the scene.
     *
     * @return A list of Objects that were created for the scene.
     */
    public static List<Object> createObjects()
    {
        List<Object> objects = new ArrayList<>();

        rubiksCube = new RubiksCube(Environment.origin, 3);
        objects.add(rubiksCube);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras()
    {
        Camera camera = new Camera(false, false);
        camera.setLocation(7 * Math.PI / 12, 13 * Math.PI / 12, 22);
        Camera.setActiveCamera(0);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls()
    {
        Environment.frame.addKeyListener(new KeyListener()
        {
            
            @Override
            public void keyTyped(KeyEvent e)
            {
            }
        
            @Override
            public void keyPressed(KeyEvent e)
            {
                int key = e.getKeyCode();
    
                if (!rubiksCube.inRotationTransformation()) {
                    if (key == KeyEvent.VK_NUMPAD0) {
                        List<Object> face = retrieveFace(FRONT);
                        RotationGroup frontGroup = new RotationGroup(rubiksCube, face.get(4), face);
                        if (e.isControlDown()) {
                            frontGroup.rotateGroup(0, 0, Math.PI / 2, FLIP_SPEED);
                            matrix = counterclockwise(matrix, FRONT);
                        } else {
                            frontGroup.rotateGroup(0, 0, -Math.PI / 2, FLIP_SPEED);
                            matrix = clockwise(matrix, FRONT);
                        }
                        printCube();
                    }
                    if (key == KeyEvent.VK_NUMPAD5) {
                        List<Object> face = retrieveFace(BACK);
                        RotationGroup frontGroup = new RotationGroup(rubiksCube, face.get(4), face);
                        if (e.isControlDown()) {
                            frontGroup.rotateGroup(0, 0, -Math.PI / 2, FLIP_SPEED);
                            matrix = counterclockwise(matrix, BACK);
                        } else {
                            frontGroup.rotateGroup(0, 0, Math.PI / 2, FLIP_SPEED);
                            matrix = clockwise(matrix, BACK);
                        }
                        printCube();
                    }
                    if (key == KeyEvent.VK_NUMPAD4) {
                        List<Object> face = retrieveFace(LEFT);
                        RotationGroup frontGroup = new RotationGroup(rubiksCube, face.get(4), face);
                        if (e.isControlDown()) {
                            frontGroup.rotateGroup(-Math.PI / 2, 0, 0, FLIP_SPEED);
                            matrix = counterclockwise(matrix, LEFT);
                        } else {
                            frontGroup.rotateGroup(Math.PI / 2, 0, 0, FLIP_SPEED);
                            matrix = clockwise(matrix, LEFT);
                        }
                        printCube();
                    }
                    if (key == KeyEvent.VK_NUMPAD6) {
                        List<Object> face = retrieveFace(RIGHT);
                        RotationGroup frontGroup = new RotationGroup(rubiksCube, face.get(4), face);
                        if (e.isControlDown()) {
                            frontGroup.rotateGroup(Math.PI / 2, 0, 0, FLIP_SPEED);
                            matrix = counterclockwise(matrix, RIGHT);
                        } else {
                            frontGroup.rotateGroup(-Math.PI / 2, 0, 0, FLIP_SPEED);
                            matrix = clockwise(matrix, RIGHT);
                        }
                        printCube();
                    }
                    if (key == KeyEvent.VK_NUMPAD8) {
                        List<Object> face = retrieveFace(TOP);
                        RotationGroup frontGroup = new RotationGroup(rubiksCube, face.get(4), face);
                        if (e.isControlDown()) {
                            frontGroup.rotateGroup(0, Math.PI / 2, 0, FLIP_SPEED);
                            matrix = counterclockwise(matrix, TOP);
                        } else {
                            frontGroup.rotateGroup(0, -Math.PI / 2, 0, FLIP_SPEED);
                            matrix = clockwise(matrix, TOP);
                        }
                        printCube();
                    }
                    if (key == KeyEvent.VK_NUMPAD2) {
                        List<Object> face = retrieveFace(BOTTOM);
                        RotationGroup frontGroup = new RotationGroup(rubiksCube, face.get(4), face);
                        if (e.isControlDown()) {
                            frontGroup.rotateGroup(0, -Math.PI / 2, 0, FLIP_SPEED);
                            matrix = counterclockwise(matrix, BOTTOM);
                        } else {
                            frontGroup.rotateGroup(0, Math.PI / 2, 0, FLIP_SPEED);
                            matrix = clockwise(matrix, BOTTOM);
                        }
                        printCube();
                    }
                }
            }
        
            @Override
            public void keyReleased(KeyEvent e)
            {
                int key = e.getKeyCode();
    
                if (!rubiksCube.inRotationTransformation()) {
                    if (key == KeyEvent.VK_UP) {
                        rubiksCube.addRotationTransformation(-Math.PI / 2, 0, 0, FLIP_SPEED);
                        matrix = new int[][]{matrix[BOTTOM], matrix[FRONT], matrix[RIGHT], matrix[BACK], matrix[LEFT], matrix[TOP]};
                        matrix = adjustFaceCounterclockwise(matrix, LEFT);
                        matrix = adjustFaceClockwise(matrix, RIGHT);
                        printCube();
                    }
                    if (key == KeyEvent.VK_DOWN) {
                        rubiksCube.addRotationTransformation(Math.PI / 2, 0, 0, FLIP_SPEED);
                        matrix = new int[][]{matrix[TOP], matrix[BACK], matrix[RIGHT], matrix[FRONT], matrix[LEFT], matrix[BOTTOM]};
                        matrix = adjustFaceClockwise(matrix, LEFT);
                        matrix = adjustFaceCounterclockwise(matrix, RIGHT);
                        printCube();
                    }
                    if (key == KeyEvent.VK_LEFT) {
                        rubiksCube.addRotationTransformation(0, -Math.PI / 2, 0, FLIP_SPEED);
                        matrix = new int[][]{matrix[RIGHT], matrix[TOP], matrix[BACK], matrix[BOTTOM], matrix[FRONT], matrix[LEFT]};
                        matrix = adjustFaceClockwise(matrix, TOP);
                        matrix = adjustFaceCounterclockwise(matrix, BOTTOM);
                        printCube();
                    }
                    if (key == KeyEvent.VK_RIGHT) {
                        rubiksCube.addRotationTransformation(0, Math.PI / 2, 0, FLIP_SPEED);
                        matrix = new int[][]{matrix[LEFT], matrix[TOP], matrix[FRONT], matrix[BOTTOM], matrix[BACK], matrix[RIGHT]};
                        matrix = adjustFaceCounterclockwise(matrix, TOP);
                        matrix = adjustFaceClockwise(matrix, BOTTOM);
                        printCube();
                    }
                }
            }
        });
    }
    
    
    //Constructors
    
    /**
     * Constructor for a Rubik's Cube scene.
     *
     * @param center The center of the scene.
     * @param radius The radius of the sphere enclosing the Rubik's cube.
     */
    public RubiksCube(Vector center, double radius)
    {
        super(center);
        
        this.radius = radius;
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the entities of the Rubik's Cube scene.
     */
    @Override
    public void calculate()
    {
        double pieceRadius = radius / 3.0;
        
        int index = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    Hexahedron piece = new Hexahedron(this, center.plus(new Vector(pieceRadius * i * 2, pieceRadius * j * 2, pieceRadius * k * 2)), Color.BLACK, pieceRadius);
                    piece.addFrame(Color.BLACK);
                    pieces.add(piece);
                    //Text pieceLabel = new Text(piece, piece.getCenter().plus(new Vector(pieceRadius * i * 1.2, pieceRadius * j * 1.2, pieceRadius * k * 1.2)), String.valueOf(index));
                    if (i == 0 && j == 0 && k == 0) {
                        index++;
                    } else {
                        colorPiece(piece, index++);
                    }
                }
            }
        }
        
        printCube();
    }
    
    /**
     * Colors the sides of a piece.
     *
     * @param piece The cube piece.
     * @param index The index of the piece.
     */
    private void colorPiece(Hexahedron piece, int index)
    {
        if (index <= 8) {
            piece.setFaceColor(1, new Color(255, 255, 255));
        }
        if (index % 3 == 2) {
            piece.setFaceColor(2, new Color(0, 128, 0));
        }
        if (index >= 18 && index <= 26) {
            piece.setFaceColor(3, new Color(255, 255, 0));
        }
        if (index % 3 == 0) {
            piece.setFaceColor(4, new Color(0, 0, 255));
        }
        if (index % 9 < 3) {
            piece.setFaceColor(5, new Color(255, 0, 0));
        }
        if (index % 9 > 5) {
            piece.setFaceColor(6, new Color(255, 165, 0));
        }
    }
    
    /**
     * Retrieves the list of pieces that make up a face.
     *
     * @param face The index of the face to retrieve.
     * @return The list of pieces that make up the face.
     */
    private static List<Object> retrieveFace(int face)
    {
        List<Object> facePieces = new ArrayList<>();
        for (int piece : matrix[face]) {
            facePieces.add(pieces.get(piece));
        }
        return facePieces;
    }
    
    /**
     * Remaps the matrix of the cube following the transformations specified in a map.
     *
     * @param matrix The matrix of the cube.
     * @param map    The transformation map.
     * @return The matrix after the transformation has been applied.
     */
    public static int[][] remapMatrix(int[][] matrix, Map<Integer, Integer> map) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (map.containsKey(matrix[i][j])) {
                    matrix[i][j] = map.get(matrix[i][j]);
                }
            }
        }
        return matrix;
    }
    
    /**
     * Performs a clockwise operation on a face.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to rotate.
     * @return The matrix after the rotation.
     */
    public static int[][] clockwise(int[][] matrix, int face)
    {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(matrix[face][0], matrix[face][6]);
        map.put(matrix[face][1], matrix[face][3]);
        map.put(matrix[face][2], matrix[face][0]);
        map.put(matrix[face][3], matrix[face][7]);
        map.put(matrix[face][5], matrix[face][1]);
        map.put(matrix[face][6], matrix[face][8]);
        map.put(matrix[face][7], matrix[face][5]);
        map.put(matrix[face][8], matrix[face][2]);
        
        return remapMatrix(matrix, map);
    }
    
    /**
     * Performs a counterclockwise operation on a face.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to rotate.
     * @return The matrix after the rotation.
     */
    public static int[][] counterclockwise(int[][] matrix, int face)
    {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(matrix[face][0], matrix[face][2]);
        map.put(matrix[face][1], matrix[face][5]);
        map.put(matrix[face][2], matrix[face][8]);
        map.put(matrix[face][3], matrix[face][1]);
        map.put(matrix[face][5], matrix[face][7]);
        map.put(matrix[face][6], matrix[face][0]);
        map.put(matrix[face][7], matrix[face][3]);
        map.put(matrix[face][8], matrix[face][6]);
    
        return remapMatrix(matrix, map);
    }
    
    /**
     * Adjusts a face for a clockwise turn.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to rotate.
     * @return The matrix after the rotation.
     */
    public static int[][] adjustFaceClockwise(int[][] matrix, int face)
    {
        int[] original = matrix[face];
        int[] adjusted = new int[]{original[6], original[3], original[0], original[7], original[4], original[1], original[8], original[5], original[2]};
        matrix[face] = adjusted;
        return matrix;
    }
    
    /**
     * Adjusts a face for a counterclockwise turn.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to rotate.
     * @return The matrix after the rotation.
     */
    public static int[][] adjustFaceCounterclockwise(int[][] matrix, int face)
    {
        int[] original = matrix[face];
        int[] adjusted = new int[]{original[2], original[5], original[8], original[1], original[4], original[7], original[0], original[3], original[6]};
        matrix[face] = adjusted;
        return matrix;
    }
    
    /**
     * Prints the state of the cube.
     */
    private static void printCube()
    {
        if (!PRINT_CUBE_STATE) {
            return;
        }
        
        StringBuilder state = new StringBuilder();
        state.append("           ").append("_________").append(System.lineSeparator());
        state.append("          |").append(String.format("%2d %2d %2d", matrix[TOP][0], matrix[TOP][1], matrix[TOP][2])).append(" |").append(System.lineSeparator());
        state.append("          |").append(String.format("%2d %2d %2d", matrix[TOP][3], matrix[TOP][4], matrix[TOP][5])).append(" |").append(System.lineSeparator());
        state.append("          |").append(String.format("%2d %2d %2d", matrix[TOP][6], matrix[TOP][7], matrix[TOP][8])).append(" |").append(System.lineSeparator());
        state.append(" _________|").append("_________|").append("_________ ").append("_________").append(System.lineSeparator());
        state.append("|").append(String.format("%2d %2d %2d", matrix[LEFT][0], matrix[LEFT][1], matrix[LEFT][2])).append(" |").append(String.format("%2d %2d %2d", matrix[FRONT][0], matrix[FRONT][1], matrix[FRONT][2])).append(" |").append(String.format("%2d %2d %2d", matrix[RIGHT][0], matrix[RIGHT][1], matrix[RIGHT][2])).append(" |").append(String.format("%2d %2d %2d", matrix[BACK][0], matrix[BACK][1], matrix[BACK][2])).append(" |").append(System.lineSeparator());
        state.append("|").append(String.format("%2d %2d %2d", matrix[LEFT][3], matrix[LEFT][4], matrix[LEFT][5])).append(" |").append(String.format("%2d %2d %2d", matrix[FRONT][3], matrix[FRONT][4], matrix[FRONT][5])).append(" |").append(String.format("%2d %2d %2d", matrix[RIGHT][3], matrix[RIGHT][4], matrix[RIGHT][5])).append(" |").append(String.format("%2d %2d %2d", matrix[BACK][3], matrix[BACK][4], matrix[BACK][5])).append(" |").append(System.lineSeparator());
        state.append("|").append(String.format("%2d %2d %2d", matrix[LEFT][6], matrix[LEFT][7], matrix[LEFT][8])).append(" |").append(String.format("%2d %2d %2d", matrix[FRONT][6], matrix[FRONT][7], matrix[FRONT][8])).append(" |").append(String.format("%2d %2d %2d", matrix[RIGHT][6], matrix[RIGHT][7], matrix[RIGHT][8])).append(" |").append(String.format("%2d %2d %2d", matrix[BACK][6], matrix[BACK][7], matrix[BACK][8])).append(" |").append(System.lineSeparator());
        state.append("|_________|").append("_________|").append("_________|").append("_________|").append(System.lineSeparator());
        state.append("          |").append(String.format("%2d %2d %2d", matrix[BOTTOM][0], matrix[BOTTOM][1], matrix[BOTTOM][2])).append(" |").append(System.lineSeparator());
        state.append("          |").append(String.format("%2d %2d %2d", matrix[BOTTOM][3], matrix[BOTTOM][4], matrix[BOTTOM][5])).append(" |").append(System.lineSeparator());
        state.append("          |").append(String.format("%2d %2d %2d", matrix[BOTTOM][6], matrix[BOTTOM][7], matrix[BOTTOM][8])).append(" |").append(System.lineSeparator());
        state.append("          |").append("_________|").append(System.lineSeparator());
        
        System.out.println(state.toString());
    }
    
}
