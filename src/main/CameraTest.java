/*
 * File:    main.CameraTest.java
 * Package: main
 * Author:  Zachary Gill
 */

package main;

import math.vector.Vector;
import objects.base.simple.Edge;
import objects.base.simple.Vertex;
import math.matrix.Matrix3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class CameraTest {
    
    private static int screenDim = 1000;
    private static int screenBorder = 100;
    private static int minValue = -2;
    private static int maxValue = 2;
    private static double stepValue = .01;
    private static double maxError = .001;
    
    private static int screenX = screenDim;
    private static int screenY = screenDim;
    
    private static int xMin = minValue;
    private static int xMax = maxValue;
    private static double xStep = stepValue;
    
    private static int yMin = minValue;
    private static int yMax = maxValue;
    private static double yStep = stepValue;
    
    private static int zMin = minValue;
    private static int zMax = maxValue;
    private static double zStep = stepValue;
    
    
    private static double ox = 0;
    private static double oy = 0;
    private static double oz = 0;
    
    private static double rho = 1;
    private static double phi = Math.PI / 2;
    private static double theta = Math.PI;
    
    private static List<Object> objects = new ArrayList<>();
    
    
    
    
    private static List<Vertex> calculateGraph()
    {
        List<Vertex> vertices = new ArrayList<>();




//        for (double x = xMin; x <= xMax; x += xStep) {
//            for (double y = yMin; y <= yMax; y += yStep) {
//                for (double z = zMin; z <= zMax; z += zStep) {
//
//                    if (x > -.2 && x < .2 && y > -.2 && y < .2 && z > -.2 && z < .2) {
//                        vertices.add(new Vertex(x, y, z));
//                    }
//
//                }
//            }
//        }
        
        

        
        List<Vertex> graph = new ArrayList<>();
//        for (Vertex vertex : vertices) {
//            graph.add(scaleVectorToScreen(vertex));
//        }
        return graph;
    }
    
    private static List<Edge> calculateAxes()
    {
        List<Edge> edges = new ArrayList<>();
        
        //main axes
//        edges.add(new objects.base.simple.Edge(new Vertex(xMin, 0, 0),
//                new Vertex(xMax,0,0), Color.RED));
//        edges.add(new objects.base.simple.Edge(new Vertex(0, yMin, 0),
//                new Vertex(0,yMax,0), Color.GREEN));
//        edges.add(new objects.base.simple.Edge(new Vertex(0, 0, zMin),
//                new Vertex(0,0, zMax), Color.BLUE));
//
//
//
//
//        //sub axes
//        Color subAxis = new Color(128, 128, 128, 32);
//        for (int xg = xMin; xg <= xMax; xg += 1) {
//            for (int yg = yMin; yg <= yMax; yg += 1) {
//                if (xg != 0 || yg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xg, yg, zMin),
//                            new Vertex(xg, yg, zMax), subAxis));
//                }
//            }
//            for (int zg = zMin; zg <= zMax; zg += 1) {
//                if (xg != 0 || zg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xg, yMin, zg),
//                            new Vertex(xg, yMax, zg), subAxis));
//                }
//            }
//        }
//        for (int yg = yMin; yg <= yMax; yg += 1) {
//            for (int xg = xMin; xg <= xMax; xg += 1) {
//                if (yg != 0 || xg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xg, yg, zMin),
//                            new Vertex(xg, yg, zMax), subAxis));
//                }
//            }
//            for (int zg = zMin; zg <= zMax; zg += 1) {
//                if (yg != 0 || zg != 0) {
//                    edges.add(new objects.base.simple.Edge(new Vertex(xMin, yg, zg),
//                            new Vertex(xMax, yg, zg), subAxis));
//                }
//            }
//        }
        
        //scale axes to fit screen
        List<Edge> axes = new ArrayList<>();
//        for (Edge edge : edges) {
//            axes.add(scaleEdge(edge));
//        }
        return axes;
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        frame.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }
    
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    phi -= .05;
                    if (phi < 0) {
                        phi = 0;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    phi += .05;
                    if (phi > Math.PI) {
                        phi = Math.PI;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    theta -= .05;
                    if (theta < 0) {
                        theta = 0;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    theta += .05;
                    if (theta > 2 * Math.PI) {
                        theta = 2 * Math.PI;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    rho -= .2;
                    if (rho < .5) {
                        rho = .5;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    rho += .2;
                }
                

            }
    
            @Override
            public void keyReleased(KeyEvent e)
            {
        
            }
        });
        
        // slider to control horizontal rotation
        JSlider headingSlider = new JSlider(-180, 180, 0);
        pane.add(headingSlider, BorderLayout.SOUTH);
        
        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);
        
        //get set of points on graph
        List<Vertex> graph = calculateGraph();
        
        //coordinate axes
        List<Edge> axes = calculateAxes();
        
        // panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                double heading = Math.toRadians(headingSlider.getValue());
                Matrix3 headingTransform = new Matrix3(new double[] {
                        Math.cos(heading), 0, -Math.sin(heading),
                        0, 1, 0,
                        Math.sin(heading), 0, Math.cos(heading)
                });
                double pitch = Math.toRadians(pitchSlider.getValue());
                Matrix3 pitchTransform = new Matrix3(new double[] {
                        1, 0, 0,
                        0, Math.cos(pitch), Math.sin(pitch),
                        0, -Math.sin(pitch), Math.cos(pitch)
                });
                Matrix3 transform = headingTransform.multiply(pitchTransform);
                
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                
                
                //point of camera, c
                double cx = rho * Math.sin(phi) * Math.cos(theta);
                double cy = rho * Math.sin(phi) * Math.sin(theta);
                double cz = rho * Math.cos(phi);
                Vector c = new Vector(cx, cy, cz);
                
                //value of plane at c
                double pc = Math.pow(cx, 2) + Math.pow(cy, 2) + Math.pow(cz, 2);
                
                //point on plane, p
                double px = 0;
                double py = 0;
                double pz = pc / cz;
                Vector p = new Vector(px, py, pz);
    
//                double W = 800;
//                double H = 600;
//                Vertex S1 = new Vertex(400, 400, 400);
//                Vertex S2 = new Vertex(880, 1040, -200);
//                Vertex S3 = new Vertex(400, 400, -200);
//                Vertex S4 = new Vertex()
    
                //normal unit vector n
                double nx = cx - ox;
                double ny = cy - oy;
                double nz = cz - oz;
                double nl = Math.sqrt(Math.pow(nx, 2) + Math.pow(ny, 2) + Math.pow(nz, 2));
                nx /= nl;
                ny /= nl;
                nz /= nl;
                Vector n = new Vector(nx, ny, nz);
    
    
                
//                Hexahedron cube = new Hexahedron(new Vertex(0, 0, 0), 1, Color.BLUE);
//                objects.add(cube);
//
//
//                for (Edge axis : axes) {
//                    Vertex q1 = axis.getV1();
//                    Vertex q2 = axis.getV2();
//
//                    Vertex v1 = q1.minus(n.scale((q1.minus(c)).dot(n)));
//                    Vertex v2 = q2.minus(n.scale((q2.minus(c)).dot(n)));
//
//                    v1 = transform.transform(v1);
//                    v2 = transform.transform(v2);
//
//                    g2.setColor(cube.getColor());
//                    g2.drawLine((int)v1.getX() + (getWidth() / 2), (int)v1.getY() + (getHeight() / 2), (int)v2.getX() + (getWidth() / 2), (int)v2.getY() + (getHeight() / 2));
//                }
                
                
                //axes
//                for (Edge axis : axes) {
//                    Vertex q1 = axis.v1;
//                    Vertex q2 = axis.v2;
//
//                    Vertex v1 = q1.minus(n.scale((q1.minus(c)).dot(n)));
//                    Vertex v2 = q2.minus(n.scale((q2.minus(c)).dot(n)));
//
//                    v1 = transform.transform(v1);
//                    v2 = transform.transform(v2);
//
//                    g2.setColor(axis.color);
//                    g2.drawLine((int)v1.x + (getWidth() / 2), (int)v1.y + (getHeight() / 2), (int)v2.x + (getWidth() / 2), (int)v2.y + (getHeight() / 2));
//                }
                
                
                //graph
//                for (Vertex point : graph) {
//                    Vertex q = point;
//
//                    Vertex v = q.minus(n.scale((q.minus(c)).dot(n)));
//                    v = transform.transform(v);
//
//                    img.setRGB((int)v.point.getX() + (getWidth() / 2), (int)v.point.getY() + (getHeight() / 2), Color.PINK.getRGB());
//                }
                
                g2.drawImage(img, 0, 0, null);
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);
        
        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());
        
        frame.setSize(screenX, screenY);
        frame.setVisible(true);
        
        while (true) {
            renderPanel.repaint();
        }
    }

//    private static Edge scaleEdge(Edge e1)
//    {
//        Edge e2 = new Edge(e1.getV1(), e1.getV2(), e1.);
//        double scale = (screenDim - (2 * screenBorder)) / (maxValue - minValue);
//
//        e1
//
//        e2.v1.x *= scale;
//        e2.v1.y *= scale;
//        e2.v1.z *= scale;
//        e2.v2.x *= scale;
//        e2.v2.y *= scale;
//        e2.v2.z *= scale;
//        return e2;
//    }

//    private static Vertex scaleVectorToScreen(Vertex v1)
//    {
//        Vertex v2 = new Vertex(v1.x, v1.y, v1.z);
//        double scale = (screenDim - (2 * screenBorder)) / (maxValue - minValue);
//        v2.x *= scale;
//        v2.y *= scale;
//        v2.z *= scale;
//        return v2;
//    }
    
    
}