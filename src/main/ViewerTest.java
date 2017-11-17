///*
// * File:    main.ViewerTest.java
// * Package: main
// * Author:  Zachary Gill
// */
//
//package main;
//
//import objects.base.piece.Edge;
//import objects.base.piece.Vertex;
//import math.matrix.Matrix3;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.List;
//import java.util.ArrayList;
//import java.awt.image.BufferedImage;
//
//public class DemoViewer {
//
//    private static int screenDim = 1000;
//    private static int screenBorder = 100;
//    private static int minValue = -2;
//    private static int maxValue = 2;
//    private static double stepValue = .0759;
//    private static double maxError = .001;
//
//    private static int screenX = screenDim;
//    private static int screenY = screenDim;
//
//    private static int xMin = minValue;
//    private static int xMax = maxValue;
//    private static double xStep = stepValue;
//
//    private static int yMin = minValue;
//    private static int yMax = maxValue;
//    private static double yStep = stepValue;
//
//    private static int zMin = minValue;
//    private static int zMax = maxValue;
//    private static double zStep = stepValue;
//
//
//    private static double rho = 1;
//    private static double phi = 0;
//    private static double theta = 0;
//
//
//    private static List<Vertex> trace = new ArrayList<>();
//
//    private static List<Vertex> calculateGraph()
//    {
//        List<Vertex> vertices = new ArrayList<>(trace);
//
//        theta += .01543;
//        phi -= .00874311;
//
//
//        double cx = rho * Math.sin(phi) * Math.cos(theta);
//        double cy = rho * Math.sin(phi) * Math.sin(theta);
//        double cz = rho * Math.cos(phi);
//        trace.add(new Vertex(cx, cy, cz));
//        vertices.add(new Vertex(cx, cy, cz));
//
//
//
//
//
//
//
//
//
//        for (double x = xMin; x <= xMax; x += xStep) {
//            for (double y = yMin; y <= yMax; y += yStep) {
//                for (double z = zMin; z <= zMax; z += zStep) {
//
//                    //sphere r=1 center(0,0,0)
//                    //if (x*x + y*y + z*z - 1 < maxError) {
//
//                    //sphere r=2 center(1,1,1)
//                    //if ((x-1)*(x-1) + (y-1)*(y-1) + (z-1)*(z-1) - 4 < maxError) {
//
//                    //hyperboloid
//                    //if (((x*x)/4) - ((y*y)/9) + ((z*z)/1) - 1 < maxError && z >= 0) {
//
//
////                    //cylinder
////                    if ((x*x) + (z*z) - 3 < maxError) {
////
////
////
////                    //elptic hyperboloid
////                    //if (((x*x)/4) - (y) + ((z*z)/9) < maxError) {
////                    //if (((x*x)/4) + (y) + ((z*z)/4) < maxError) {
////
////
////
////                        vertices.add(new Vertex(x, y, z));
////                    }
//
//
//                }
//            }
//        }
//
//        List<Vertex> graph = new ArrayList<>();
//        for (Vertex vertex : vertices) {
//            graph.add(scaleVectorToScreen(vertex));
//        }
//        return graph;
//    }
//
//    private static List<Edge> calculateAxes()
//    {
//        List<Edge> edges = new ArrayList<>();
//
//        //main axes
//        edges.add(new Edge(new Vertex(xMin, 0, 0),
//                new Vertex(xMax,0,0), Color.RED));
//        edges.add(new Edge(new Vertex(0, yMin, 0),
//                new Vertex(0,yMax,0), Color.GREEN));
//        edges.add(new Edge(new Vertex(0, 0, zMin),
//                new Vertex(0,0, zMax), Color.BLUE));
//
//        //sub axes
//        Color subAxis = new Color(128, 128, 128, 32);
//        for (int xg = xMin; xg <= xMax; xg += 1) {
//            for (int yg = yMin; yg <= yMax; yg += 1) {
//                if (xg != 0 || yg != 0) {
//                    edges.add(new Edge(new Vertex(xg, yg, zMin),
//                            new Vertex(xg, yg, zMax), subAxis));
//                }
//            }
//            for (int zg = zMin; zg <= zMax; zg += 1) {
//                if (xg != 0 || zg != 0) {
//                    edges.add(new Edge(new Vertex(xg, yMin, zg),
//                            new Vertex(xg, yMax, zg), subAxis));
//                }
//            }
//        }
//        for (int yg = yMin; yg <= yMax; yg += 1) {
//            for (int xg = xMin; xg <= xMax; xg += 1) {
//                if (yg != 0 || xg != 0) {
//                    edges.add(new Edge(new Vertex(xg, yg, zMin),
//                            new Vertex(xg, yg, zMax), subAxis));
//                }
//            }
//            for (int zg = zMin; zg <= zMax; zg += 1) {
//                if (yg != 0 || zg != 0) {
//                    edges.add(new Edge(new Vertex(xMin, yg, zg),
//                            new Vertex(xMax, yg, zg), subAxis));
//                }
//            }
//        }
//
//        //scale axes to fit screen
//        List<Edge> axes = new ArrayList<>();
//        for (Edge edge : edges) {
//             axes.add(scaleEdge(edge));
//        }
//        return axes;
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        Container pane = frame.getContentPane();
//        pane.setLayout(new BorderLayout());
//
//        // slider to control horizontal rotation
//        JSlider headingSlider = new JSlider(-180, 180, 0);
//        pane.add(headingSlider, BorderLayout.SOUTH);
//
//        // slider to control vertical rotation
//        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
//        pane.add(pitchSlider, BorderLayout.EAST);
//
//        //get set of points on graph
//        List<Vertex> graph = calculateGraph();
//
//        //coordinate axes
//        List<Edge> axes = calculateAxes();
//
//        // panel to display render results
//        JPanel renderPanel = new JPanel() {
//            public void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g;
//                g2.setColor(Color.WHITE);
//                g2.fillRect(0, 0, getWidth(), getHeight());
//
//                double heading = Math.toRadians(headingSlider.getValue());
//                Matrix3 headingTransform = new Matrix3(new double[] {
//                        Math.cos(heading), 0, -Math.sin(heading),
//                        0, 1, 0,
//                        Math.sin(heading), 0, Math.cos(heading)
//                });
//                double pitch = Math.toRadians(pitchSlider.getValue());
//                Matrix3 pitchTransform = new Matrix3(new double[] {
//                        1, 0, 0,
//                        0, Math.cos(pitch), Math.sin(pitch),
//                        0, -Math.sin(pitch), Math.cos(pitch)
//                });
//                Matrix3 transform = headingTransform.multiply(pitchTransform);
//
//                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//                //axes
//                for (Edge axis : axes) {
//                    Vertex v1 = transform.transform(axis.v1);
//                    v1.point.setX(v1.point.setX() + getWidth() / 2;
//                    v1.y += getHeight() / 2;
//                    Vertex v2 = transform.transform(axis.v2);
//                    v2.x += getWidth() / 2;
//                    v2.y += getHeight() / 2;
//
//                    g2.setColor(axis.color);
//                    g2.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
//                }
//
//                List<Vertex> graph = calculateGraph();
//
//                //graph
//                for (Vertex point : graph) {
//                    Vertex v1 = transform.transform(point);
//                    v1.x += getWidth() / 2;
//                    v1.y += getHeight() / 2;
//
//                    img.setRGB((int)v1.x, (int)v1.y, Color.PINK.getRGB());
//                }
//
//                g2.drawImage(img, 0, 0, null);
//            }
//        };
//        pane.add(renderPanel, BorderLayout.CENTER);
//
//        headingSlider.addChangeListener(e -> renderPanel.repaint());
//        pitchSlider.addChangeListener(e -> renderPanel.repaint());
//
//        frame.setSize(screenX, screenY);
//        frame.setVisible(true);
//
//        while (true) {
//            renderPanel.repaint();
//        }
//    }
//
//    private static Edge scaleEdge(Edge e1)
//    {
//        Edge e2 = new Edge(e1.v1, e1.v2, e1.color);
//        double scale = (screenDim - (2 * screenBorder)) / (maxValue - minValue);
//        e2.v1.x *= scale;
//        e2.v1.y *= scale;
//        e2.v1.z *= scale;
//        e2.v2.x *= scale;
//        e2.v2.y *= scale;
//        e2.v2.z *= scale;
//        return e2;
//    }
//
//    private static Vertex scaleVectorToScreen(Vertex v1)
//    {
//        Vertex v2 = new Vertex(v1.x, v1.y, v1.z);
//        double scale = (screenDim - (2 * screenBorder)) / (maxValue - minValue);
//        v2.x *= scale;
//        v2.y *= scale;
//        v2.z *= scale;
//        return v2;
//    }
//
////                List<Triangle> tris = new ArrayList<>();
////                tris.add(new Triangle(new Vertex(100, 100, 100),
////                        new Vertex(-100, -100, 100),
////                        new Vertex(-100, 100, -100),
////                        Color.WHITE));
////                tris.add(new Triangle(new Vertex(100, 100, 100),
////                        new Vertex(-100, -100, 100),
////                        new Vertex(100, -100, -100),
////                        Color.RED));
////                tris.add(new Triangle(new Vertex(-100, 100, -100),
////                        new Vertex(100, -100, -100),
////                        new Vertex(100, 100, 100),
////                        Color.GREEN));
////                tris.add(new Triangle(new Vertex(-100, 100, -100),
////                        new Vertex(100, -100, -100),
////                        new Vertex(-100, -100, 100),
////                        Color.BLUE));
////
////                for (int i = 0; i < 4; i++) {
////                    tris = inflate(tris);
////                }
////
////                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
////                // initialize array with extremely far away depths
////                for (int q = 0; q < zBuffer.length; q++) {
////                    zBuffer[q] = Double.NEGATIVE_INFINITY;
////                }
////
////                for (Triangle t : tris) {
////                    Vertex v1 = transform.transform(t.v1);
////                    v1.x += getWidth() / 2;
////                    v1.y += getHeight() / 2;
////                    Vertex v2 = transform.transform(t.v2);
////                    v2.x += getWidth() / 2;
////                    v2.y += getHeight() / 2;
////                    Vertex v3 = transform.transform(t.v3);
////                    v3.x += getWidth() / 2;
////                    v3.y += getHeight() / 2;
////
////                    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
////                    Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
////                    Vertex norm = new Vertex(
////                            ab.y * ac.z - ab.z * ac.y,
////                            ab.z * ac.x - ab.x * ac.z,
////                            ab.x * ac.y - ab.y * ac.x
////                    );
////                    double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
////                    norm.x /= normalLength;
////                    norm.y /= normalLength;
////                    norm.z /= normalLength;
////
////                    double angleCos = Math.abs(norm.z);
////
////                    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
////                    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
////                    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
////                    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
////
////                    double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
////
////                    for (int y = minY; y <= maxY; y++) {
////                        for (int x = minX; x <= maxX; x++) {
////                            double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
////                            double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
////                            double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
////                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
////                                double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
////                                int zIndex = y * img.getWidth() + x;
////                                if (zBuffer[zIndex] < depth) {
////                                    img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
////                                    zBuffer[zIndex] = depth;
////                                }
////                            }
////                        }
////                    }
////
////                }
////
////    public static Color getShade(Color color, double shade) {
////        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
////        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
////        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;
////
////        int red = (int) Math.pow(redLinear, 1/2.4);
////        int green = (int) Math.pow(greenLinear, 1/2.4);
////        int blue = (int) Math.pow(blueLinear, 1/2.4);
////
////        return new Color(red, green, blue);
////    }
////
////    public static List<Triangle> inflate(List<Triangle> tris) {
////        List<Triangle> result = new ArrayList<>();
////        for (Triangle t : tris) {
////            Vertex m1 = new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2, (t.v1.z + t.v2.z)/2);
////            Vertex m2 = new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2, (t.v2.z + t.v3.z)/2);
////            Vertex m3 = new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2, (t.v1.z + t.v3.z)/2);
////            result.add(new Triangle(t.v1, m1, m3, t.color));
////            result.add(new Triangle(t.v2, m1, m2, t.color));
////            result.add(new Triangle(t.v3, m2, m3, t.color));
////            result.add(new Triangle(m1, m2, m3, t.color));
////        }
////        for (Triangle t : result) {
////            for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
////                double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
////                v.x /= l;
////                v.y /= l;
////                v.z /= l;
////            }
////        }
////        return result;
////    }
//
//}