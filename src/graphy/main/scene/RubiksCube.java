/*
 * File:    RubiksCube.java
 * Package: graphy.main.scene
 * Author:  Zachary Gill
 */

package graphy.main.scene;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import commons.math.component.vector.Vector;
import graphy.camera.Camera;
import graphy.main.Environment;
import graphy.object.base.Object;
import graphy.object.base.Scene;
import graphy.object.base.group.RotationGroup;
import graphy.object.polyhedron.regular.platonic.Hexahedron;

/**
 * Defines a Rubik's Cube scene.
 */
public class RubiksCube extends Scene {
    
    //Constants
    
    /**
     * The index of the front face within the piece matrix.
     */
    private static final int FRONT = 0;
    
    /**
     * The index of the top face within the piece matrix.
     */
    private static final int TOP = 1;
    
    /**
     * The index of the right face within the piece matrix.
     */
    private static final int RIGHT = 2;
    
    /**
     * The index of the bottom face within the piece matrix.
     */
    private static final int BOTTOM = 3;
    
    /**
     * The index of the left face within the piece matrix.
     */
    private static final int LEFT = 4;
    
    /**
     * The index of the back face within the piece matrix.
     */
    private static final int BACK = 5;
    
    /**
     * The constant for the clockwise direction.
     */
    private static final int CLOCKWISE = 1;
    
    /**
     * The constant for the counterclockwise direction.
     */
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
    
    
    //Static Fields
    
    /**
     * The radius of the enclosing sphere of the Rubik's cube.
     */
    private static double radius = 3;
    
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
     * @throws Exception When the Scene cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runScene(RubiksCube.class);
    }
    
    
    //Constructors
    
    /**
     * Constructor for the Rubik's Cube scene.
     *
     * @param environment The Environment to render the Rubik's Cube in.
     */
    public RubiksCube(Environment environment) {
        super(environment);
    }
    
    
    //Methods
    
    /**
     * Calculates the components that compose the Rubik's Cube.
     */
    @Override
    public void calculate() {
        double pieceRadius = radius / 3.0;
        Object o = new Object(this, Environment.ORIGIN, Color.BLACK);
        
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
     * Sets up components for the Rubik's Cube scene.
     */
    @Override
    public void initComponents() {
    }
    
    /**
     * Sets up cameras for the Rubik's Cube scene.
     */
    @Override
    public void setupCameras() {
        Camera camera = new Camera(this, environment.perspective, false, false);
        camera.setLocation(15, 4 * Math.PI / 3, 5 * Math.PI / 12);
    }
    
    /**
     * Sets up controls for the Rubik's Cube scene.
     */
    @Override
    public void setupControls() {
        environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int dir = e.isControlDown() ? COUNTERCLOCKWISE : CLOCKWISE;
                
                if (!inRotationTransformation() && !inAutoMovement.get()) {
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
                
                if (!inRotationTransformation() && !inAutoMovement.get()) {
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
    private List<Object> retrieveFace(int face) {
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
    private int[][] remapMatrix(int[][] matrix, Map<Integer, Integer> map) {
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
    private int[][] clockwise(int[][] matrix, int face) {
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
    private int[][] counterclockwise(int[][] matrix, int face) {
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
    private int[][] rotate(int[][] matrix, int face, int dir) {
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
    private int[][] adjustFaceClockwise(int[][] matrix, int face) {
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
    private int[][] adjustFaceCounterclockwise(int[][] matrix, int face) {
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
    private int[][] adjust(int[][] matrix, int face, int dir) {
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
    private void rotateFace(int face, int dir, Vector rotation) {
        List<Object> f = retrieveFace(face);
        RotationGroup frontGroup = new RotationGroup(this, f.get(4), f);
        
        frontGroup.rotateGroup(rotation.getRawX() * dir, rotation.getRawY() * dir, rotation.getRawZ() * dir, FLIP_SPEED);
        matrix = rotate(matrix, face, dir);
        
        printCube();
    }
    
    /**
     * Rotates the front face.
     *
     * @param dir The direction to turn the face.
     */
    private void front(int dir) {
        rotateFace(FRONT, dir, new Vector(0, Math.PI / 2, 0));
    }
    
    /**
     * Rotates the back face.
     *
     * @param dir The direction to turn the face.
     */
    private void back(int dir) {
        rotateFace(BACK, dir, new Vector(0, -Math.PI / 2, 0));
    }
    
    /**
     * Rotates the left face.
     *
     * @param dir The direction to turn the face.
     */
    private void left(int dir) {
        rotateFace(LEFT, dir, new Vector(Math.PI / 2, 0, 0));
    }
    
    /**
     * Rotates the right face.
     *
     * @param dir The direction to turn the face.
     */
    private void right(int dir) {
        rotateFace(RIGHT, dir, new Vector(-Math.PI / 2, 0, 0));
    }
    
    /**
     * Rotates the top face.
     *
     * @param dir The direction to turn the face.
     */
    private void up(int dir) {
        rotateFace(TOP, dir, new Vector(0, 0, Math.PI / 2));
    }
    
    /**
     * Rotates the bottom face.
     *
     * @param dir The direction to turn the face.
     */
    private void down(int dir) {
        rotateFace(BOTTOM, dir, new Vector(0, 0, -Math.PI / 2));
    }
    
    /**
     * Flips the cube up.
     */
    private void flipUp() {
        addRotationTransformation(Math.PI / 2, 0, 0, ROTATE_SPEED);
        matrix = new int[][] {matrix[TOP], matrix[BACK], matrix[RIGHT], matrix[FRONT], matrix[LEFT], matrix[BOTTOM]};
        matrix = adjust(matrix, LEFT, CLOCKWISE);
        matrix = adjust(matrix, RIGHT, COUNTERCLOCKWISE);
        printCube();
    }
    
    /**
     * Flips the cube down.
     */
    private void flipDown() {
        addRotationTransformation(-Math.PI / 2, 0, 0, ROTATE_SPEED);
        matrix = new int[][] {matrix[BOTTOM], matrix[FRONT], matrix[RIGHT], matrix[BACK], matrix[LEFT], matrix[TOP]};
        matrix = adjust(matrix, LEFT, COUNTERCLOCKWISE);
        matrix = adjust(matrix, RIGHT, CLOCKWISE);
        printCube();
    }
    
    /**
     * Flips the cube left.
     */
    private void flipLeft() {
        addRotationTransformation(0, 0, -Math.PI / 2, ROTATE_SPEED);
        matrix = new int[][] {matrix[LEFT], matrix[TOP], matrix[FRONT], matrix[BOTTOM], matrix[BACK], matrix[RIGHT]};
        matrix = adjust(matrix, TOP, COUNTERCLOCKWISE);
        matrix = adjust(matrix, BOTTOM, CLOCKWISE);
        printCube();
    }
    
    /**
     * Flips the cube right.
     */
    private void flipRight() {
        addRotationTransformation(0, 0, Math.PI / 2, ROTATE_SPEED);
        matrix = new int[][] {matrix[RIGHT], matrix[TOP], matrix[BACK], matrix[BOTTOM], matrix[FRONT], matrix[LEFT]};
        matrix = adjust(matrix, TOP, CLOCKWISE);
        matrix = adjust(matrix, BOTTOM, COUNTERCLOCKWISE);
        printCube();
    }
    
    /**
     * Shuffles the cube.
     *
     * @param count The number of moves to make.
     */
    private void shuffle(int count) {
        if (inAutoMovement.compareAndSet(false, true)) {
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
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
            }, 0, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Shuffles the cube.
     */
    private void shuffle() {
        shuffle(SHUFFLE_MOVES);
    }
    
    /**
     * Waits for the cube to finish its animation.
     */
    private void waitForAnimation() {
        do {
            try {
                Thread.sleep(FLIP_SPEED / 5);
            } catch (InterruptedException ignored) {
            }
        } while (inRotationTransformation());
    }
    
    /**
     * Solves the cube.
     */
    private void solve() {
        if (inAutoMovement.compareAndSet(false, true)) {
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
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
            }, 0, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Prints the state of the cube.
     */
    private void printCube() {
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
