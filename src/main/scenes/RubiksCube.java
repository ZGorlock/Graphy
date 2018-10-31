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
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Defines a Rubik's Cube scene.
 */
public class RubiksCube extends Scene {
    
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
     * The constants for directions.
     */
    private static final int CLOCKWISE = 1;
    
    private static final int COUNTERCLOCKWISE = -1;
    
    /**
     * The speed of cube flips in milliseconds.
     */
    public static final int FLIP_SPEED = 100;
    
    /**
     * The speed of cube rotations in milliseconds.
     */
    public static final int ROTATE_SPEED = 100;
    
    /**
     * The number of moves to perform during a shuffle.
     */
    public static final int SHUFFLE_MOVES = 25;
    
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
    private static int[][] matrix = {{26, 17, 8, 25, 16, 7, 24, 15, 6}, //front
                                     {20, 11, 2, 23, 14, 5, 26, 17, 8}, //up
                                     {8, 5, 2, 7, 4, 1, 6, 3, 0}, //right
                                     {0, 9, 18, 3, 12, 21, 6, 15, 24}, //down
                                     {20, 23, 26, 19, 22, 25, 18, 21, 24}, //left
                                     {2, 11, 20, 1, 10, 19, 0, 9, 18}}; //back
    
    /**
     * The list of moves that have been performed on the cube.
     */
    private static final Stack<String> moves = new Stack<>();
    
    /**
     * A flag indicating whether the cube is performing automatic movement or not.
     */
    private static final AtomicBoolean inAutoMovement = new AtomicBoolean(false);
    
    
    //Main Method
    
    /**
     * The main method for the Rubik's Cube scene.
     *
     * @param args The arguments to the main method.
     */
    public static void main(String[] args) {
        String[] environmentArgs = new String[] {};
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
    public static List<Object> createObjects() {
        List<Object> objects = new ArrayList<>();
        
        rubiksCube = new RubiksCube(Environment.origin, 3);
        objects.add(rubiksCube);
        
        return objects;
    }
    
    /**
     * Sets up cameras for the scene.
     */
    public static void setupCameras() {
        Camera camera = new Camera(false, false);
        camera.setLocation(7 * Math.PI / 12, 7 * Math.PI / 6, 22);
        Camera.setActiveCamera(0);
    }
    
    /**
     * Sets up controls for the scene.
     */
    public static void setupControls() {
        Environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int dir = e.isControlDown() ? COUNTERCLOCKWISE : CLOCKWISE;
                
                if (!rubiksCube.inRotationTransformation() && !inAutoMovement.get()) {
                    if (key == KeyEvent.VK_NUMPAD0) {
                        front(dir);
                        moves.push("F" + (dir == CLOCKWISE ? "'" : ""));
                    }
                    if (key == KeyEvent.VK_NUMPAD5) {
                        back(dir);
                        moves.push("B" + (dir == CLOCKWISE ? "'" : ""));
                    }
                    if (key == KeyEvent.VK_NUMPAD4) {
                        left(dir);
                        moves.push("L" + (dir == CLOCKWISE ? "'" : ""));
                    }
                    if (key == KeyEvent.VK_NUMPAD6) {
                        right(dir);
                        moves.push("R" + (dir == CLOCKWISE ? "'" : ""));
                    }
                    if (key == KeyEvent.VK_NUMPAD8) {
                        up(dir);
                        moves.push("U" + (dir == CLOCKWISE ? "'" : ""));
                    }
                    if (key == KeyEvent.VK_NUMPAD2) {
                        down(dir);
                        moves.push("D" + (dir == CLOCKWISE ? "'" : ""));
                    }
                    
                    if (key == KeyEvent.VK_Q) {
                        shuffle();
                    }
                    if (key == KeyEvent.VK_W) {
                        solve();
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                
                if (!rubiksCube.inRotationTransformation() && !inAutoMovement.get()) {
                    if (key == KeyEvent.VK_UP) {
                        flipUp();
                        moves.push("XD");
                    }
                    if (key == KeyEvent.VK_DOWN) {
                        flipDown();
                        moves.push("XU");
                    }
                    if (key == KeyEvent.VK_LEFT) {
                        flipLeft();
                        moves.push("XR");
                    }
                    if (key == KeyEvent.VK_RIGHT) {
                        flipRight();
                        moves.push("XL");
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
    public RubiksCube(Vector center, double radius) {
        super(center);
        
        this.radius = radius;
        calculate();
    }
    
    
    //Methods
    
    /**
     * Calculates the entities of the Rubik's Cube scene.
     */
    @Override
    public void calculate() {
        double pieceRadius = radius / 3.0;
        Object o = new Object(this, Environment.origin, Color.BLACK);
        
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
    private void colorPiece(Hexahedron piece, int index) {
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
    private static List<Object> retrieveFace(int face) {
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
    private static int[][] remapMatrix(int[][] matrix, Map<Integer, Integer> map) {
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
    private static int[][] clockwise(int[][] matrix, int face) {
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
    private static int[][] counterclockwise(int[][] matrix, int face) {
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
     * Performs an operation on a face.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to rotate.
     * @param dir    The direction to rotate the face.
     * @return The matrix after the rotation.
     */
    private static int[][] rotate(int[][] matrix, int face, int dir) {
        if (dir == CLOCKWISE) {
            return clockwise(matrix, face);
        } else if (dir == COUNTERCLOCKWISE) {
            return counterclockwise(matrix, face);
        } else {
            return null;
        }
    }
    
    /**
     * Adjusts a face for a clockwise turn.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to adjust.
     * @return The matrix after the adjustment.
     */
    private static int[][] adjustFaceClockwise(int[][] matrix, int face) {
        int[] original = matrix[face];
        int[] adjusted = new int[] {original[6], original[3], original[0], original[7], original[4], original[1], original[8], original[5], original[2]};
        matrix[face] = adjusted;
        return matrix;
    }
    
    /**
     * Adjusts a face for a counterclockwise turn.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to adjust.
     * @return The matrix after the adjustment.
     */
    private static int[][] adjustFaceCounterclockwise(int[][] matrix, int face) {
        int[] original = matrix[face];
        int[] adjusted = new int[] {original[2], original[5], original[8], original[1], original[4], original[7], original[0], original[3], original[6]};
        matrix[face] = adjusted;
        return matrix;
    }
    
    /**
     * Adjusts a face for a turn.
     *
     * @param matrix The matrix of the cube.
     * @param face   The face to adjust.
     * @param dir    The direction to adjust the face.
     * @return The matrix after the adjustment.
     */
    private static int[][] adjust(int[][] matrix, int face, int dir) {
        if (dir == CLOCKWISE) {
            return adjustFaceClockwise(matrix, face);
        } else if (dir == COUNTERCLOCKWISE) {
            return adjustFaceCounterclockwise(matrix, face);
        } else {
            return null;
        }
    }
    
    /**
     * Rotates a face.
     *
     * @param face     The face to rotate.
     * @param dir      The direction to rotate the face.
     * @param rotation The vector of rotation for the face.
     */
    private static void rotateFace(int face, int dir, Vector rotation) {
        List<Object> f = retrieveFace(face);
        RotationGroup frontGroup = new RotationGroup(rubiksCube, f.get(4), f);
        
        frontGroup.rotateGroup(rotation.getX() * dir, rotation.getY() * dir, rotation.getZ() * dir, FLIP_SPEED);
        matrix = rotate(matrix, face, dir);
        
        printCube();
    }
    
    /**
     * Rotates the front face.
     *
     * @param dir The direction to turn the face.
     */
    private static void front(int dir) {
        rotateFace(FRONT, dir, new Vector(0, 0, -Math.PI / 2));
    }
    
    /**
     * Rotates the back face.
     *
     * @param dir The direction to turn the face.
     */
    private static void back(int dir) {
        rotateFace(BACK, dir, new Vector(0, 0, Math.PI / 2));
    }
    
    /**
     * Rotates the left face.
     *
     * @param dir The direction to turn the face.
     */
    private static void left(int dir) {
        rotateFace(LEFT, dir, new Vector(0, Math.PI / 2, 0));
    }
    
    /**
     * Rotates the right face.
     *
     * @param dir The direction to turn the face.
     */
    private static void right(int dir) {
        rotateFace(RIGHT, dir, new Vector(0, -Math.PI / 2, 0));
    }
    
    /**
     * Rotates the top face.
     *
     * @param dir The direction to turn the face.
     */
    private static void up(int dir) {
        rotateFace(TOP, dir, new Vector(-Math.PI / 2, 0, 0));
    }
    
    /**
     * Rotates the bottom face.
     *
     * @param dir The direction to turn the face.
     */
    private static void down(int dir) {
        rotateFace(BOTTOM, dir, new Vector(Math.PI / 2, 0, 0));
    }
    
    /**
     * Flips the cube left.
     */
    private static void flipLeft() {
        rubiksCube.addRotationTransformation(-Math.PI / 2, 0, 0, ROTATE_SPEED);
        matrix = new int[][] {matrix[RIGHT], matrix[TOP], matrix[BACK], matrix[BOTTOM], matrix[FRONT], matrix[LEFT]};
        matrix = adjust(matrix, TOP, CLOCKWISE);
        matrix = adjust(matrix, BOTTOM, COUNTERCLOCKWISE);
        printCube();
    }
    
    /**
     * Flips the cube right.
     */
    private static void flipRight() {
        rubiksCube.addRotationTransformation(Math.PI / 2, 0, 0, ROTATE_SPEED);
        matrix = new int[][] {matrix[LEFT], matrix[TOP], matrix[FRONT], matrix[BOTTOM], matrix[BACK], matrix[RIGHT]};
        matrix = adjust(matrix, TOP, COUNTERCLOCKWISE);
        matrix = adjust(matrix, BOTTOM, CLOCKWISE);
        printCube();
    }
    
    /**
     * Flips the cube up.
     */
    private static void flipUp() {
        rubiksCube.addRotationTransformation(0, -Math.PI / 2, 0, ROTATE_SPEED);
        matrix = new int[][] {matrix[BOTTOM], matrix[FRONT], matrix[RIGHT], matrix[BACK], matrix[LEFT], matrix[TOP]};
        matrix = adjust(matrix, LEFT, COUNTERCLOCKWISE);
        matrix = adjust(matrix, RIGHT, CLOCKWISE);
        printCube();
    }
    
    /**
     * Flips the cube down.
     */
    private static void flipDown() {
        rubiksCube.addRotationTransformation(0, Math.PI / 2, 0, ROTATE_SPEED);
        matrix = new int[][] {matrix[TOP], matrix[BACK], matrix[RIGHT], matrix[FRONT], matrix[LEFT], matrix[BOTTOM]};
        matrix = adjust(matrix, LEFT, CLOCKWISE);
        matrix = adjust(matrix, RIGHT, COUNTERCLOCKWISE);
        printCube();
    }
    
    /**
     * Shuffles the cube.
     *
     * @param count The number of moves to make.
     */
    private static void shuffle(int count) {
        if (inAutoMovement.compareAndSet(false, true)) {
            Timer shuffleTimer = new Timer();
            TimerTask shuffleTask = new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        int move = (int) (Math.random() * 6);
                        int dir = (int) (Math.random() * 2);
                        if (dir == 0) {
                            dir = COUNTERCLOCKWISE;
                        }
                        
                        switch (move) {
                            case 0:
                                front(dir);
                                moves.push("F" + (dir == CLOCKWISE ? "'" : ""));
                                break;
                            case 1:
                                back(dir);
                                moves.push("B" + (dir == CLOCKWISE ? "'" : ""));
                                break;
                            case 2:
                                left(dir);
                                moves.push("L" + (dir == CLOCKWISE ? "'" : ""));
                                break;
                            case 3:
                                right(dir);
                                moves.push("R" + (dir == CLOCKWISE ? "'" : ""));
                                break;
                            case 4:
                                up(dir);
                                moves.push("U" + (dir == CLOCKWISE ? "'" : ""));
                                break;
                            case 5:
                                down(dir);
                                moves.push("D" + (dir == CLOCKWISE ? "'" : ""));
                                break;
                        }
                        
                        waitForAnimation();
                    }
                    inAutoMovement.set(false);
                }
            };
            shuffleTimer.schedule(shuffleTask, 0);
        }
    }
    
    /**
     * Shuffles the cube.
     */
    private static void shuffle() {
        shuffle(SHUFFLE_MOVES);
    }
    
    /**
     * Waits for the cube to finish its animation.
     */
    private static void waitForAnimation() {
        do {
            try {
                Thread.sleep(FLIP_SPEED / 5);
            } catch (InterruptedException ignored) {
            }
        } while (rubiksCube.inRotationTransformation());
    }
    
    /**
     * Solves the cube.
     */
    private static void solve() {
        if (inAutoMovement.compareAndSet(false, true)) {
            Timer solveTimer = new Timer();
            TimerTask solveTask = new TimerTask() {
                @Override
                public void run() {
                    while (!moves.empty()) {
                        String move = moves.pop();
                        
                        int dir = move.endsWith("'") ? COUNTERCLOCKWISE : CLOCKWISE;
                        move = move.endsWith("'") ? move.substring(0, 1) : move;
                        
                        switch (move) {
                            case "F":
                                front(dir);
                                break;
                            case "B":
                                back(dir);
                                break;
                            case "L":
                                left(dir);
                                break;
                            case "R":
                                right(dir);
                                break;
                            case "U":
                                up(dir);
                                break;
                            case "D":
                                down(dir);
                                break;
                            case "XD":
                                flipDown();
                                break;
                            case "XU":
                                flipUp();
                                break;
                            case "XR":
                                flipRight();
                                break;
                            case "XL":
                                flipLeft();
                                break;
                        }
                        
                        waitForAnimation();
                    }
                    inAutoMovement.set(false);
                }
            };
            solveTimer.schedule(solveTask, 0);
            
            inAutoMovement.set(false);
        }
    }
    
    /**
     * Prints the state of the cube.
     */
    private static void printCube() {
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
