/*
 * File:    Mandelbrot.java
 * Package: graphy.main.drawing
 * Author:  Zachary Gill
 */

package graphy.main.drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import commons.math.vector.BigVector;
import commons.math.vector.Vector;
import commons.math.vector.Vector2;
import commons.media.ImageUtility;
import graphy.camera.CaptureHandler;
import graphy.main.Environment;
import graphy.main.Environment2D;
import graphy.main.EnvironmentBase;
import graphy.object.base.Drawing;

/**
 * A Mandelbrot drawing.
 */
public class Mandelbrot extends Drawing {
    
    //Enums
    
    /**
     * An enumeration of Super Sample Types.
     */
    public enum SuperSampleType {
        SUPER_SAMPLE_NONE("LOW"),
        SUPER_SAMPLE_2X("MEDIUM"),
        SUPER_SAMPLE_4X("HIGH"),
        SUPER_SAMPLE_4X_9("VERY HIGH"),
        SUPER_SAMPLE_9X("ULTRA");
        
        
        //Fields
        
        /**
         * The quality of the Super Sample Type.
         */
        private String quality;
        
        
        //Constructors
        
        SuperSampleType(String quality) {
            this.quality = quality;
        }
        
        
        //Getters
        
        /**
         * Returns the quality of the Super Sample Type.
         *
         * @return The quality of the Super Sample Type.
         */
        public String getQuality() {
            return quality;
        }
        
    }
    
    
    //Constants
    
    /**
     * The size of the screen to render the Mandelbrot on.
     */
    public static final Vector SCREEN_SIZE = new Vector(Environment.MAX_SCREEN_WIDTH, Environment.MAX_SCREEN_HEIGHT);
    
    /**
     * The number of threads to use while rendering the Mandelbrot.
     */
    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    
    /**
     * The number of sectors to divide the screen into.
     */
    public static final Vector SECTOR_COUNT = new Vector(8, 6);
    
    /**
     * The available palettes.
     */
    public static final Map<String, String> PALETTES = new LinkedHashMap<>();
    
    static {
        PALETTES.put("Default", "ff0000,00ff00,0000ff");
        PALETTES.put("Electric", "000000,00ff00,0000ff");
        PALETTES.put("Lava Flow", "ff0000,000000,0000ff");
        PALETTES.put("Lightning", "000000,000000,0000ff");
        PALETTES.put("Lime", "ff0000,00ff00,000000");
        PALETTES.put("Fire", "ff0000,000000,000000");
        PALETTES.put("Toxic", "000000,00ff00,000000");
        PALETTES.put("Chaos", "00ff00,0000ff,ff0000");
    }
    
    /**
     * The available points of interest.
     */
    public static final Map<String, String> POINTS_OF_INTEREST = new LinkedHashMap<>();
    
    static {
        POINTS_OF_INTEREST.put("Default", "s=3.0,r=-0.75,i=0,l=1024");
        POINTS_OF_INTEREST.put("Blobby", "s=3.945027e-59,r=-1.3227493705340553096531973724638014676749612091968246708254143707705,i=0.11215137792107857749242449374269442912190722464230227685405588576261,l=6912");
        POINTS_OF_INTEREST.put("Bloodshot", "s=2.150840e-56,r=0.14355621452983444961422555375035989741273570589747906301679965568498,i=0.65207264731080062833688926376479464152101757916253449412791770164159,l=4096");
        POINTS_OF_INTEREST.put("Bug", "s=1.427670e-138,r=-0.971032204400862970605987740386365852939384365338919468147782957462652312603921836333801202961425909028627510212125295497295628506148768050810945512,i=0.2612660740286018204953256014557660587654785993590555741175523439185933470325270191056673621149815610803329195944719721374780342203183742681608999151,l=6400");
        POINTS_OF_INTEREST.put("Cell", "s=4.247607e-31,r=-0.108051643551278269838603388506189745374,i=0.9620211831311302584670781799668062667422,l=25688");
        POINTS_OF_INTEREST.put("Chains", "s=1.124769e-70,r=0.0592259535587252647034707909559267210253988289806344064262750819859936433500952,i=0.6557594816076711674183332361851895776456694102993657000539496152792567276033998,l=35279");
        POINTS_OF_INTEREST.put("Chaos", "s=2.078600e-29,r=-0.5316466253832941645290557688141759096,i=0.66853083001213043887310856441016391928,l=8764");
        POINTS_OF_INTEREST.put("Coil", "s=8.3753E-31,r=-1.03918570511782999161387683890297352128,i=0.34877372380369074273892218342440212736,l=5878");
        POINTS_OF_INTEREST.put("Coral", "s=5.437534e-65,r=-0.207975238753095626766617136501241794405143243672131396312340491439384909,i=1.1091582640935547192530875722735779387477797090182606408430312106976345148,l=16640");
        POINTS_OF_INTEREST.put("Cross", "s=1.048582e-41,r=-1.322749370534055309653197372463801467674961485530,i=0.1121513779210785774924244937426944291219065259685,l=4864");
        POINTS_OF_INTEREST.put("Deep", "s=5.054594e-264,r=-0.8635169079336723787909852673550036317112290092662339023565685902909598581747910666789341701733177524889420466740410475917304931409405515021801432520061688309437600296551693365761424365795272805469550118785509705439232403959541588349498522971590667888487052154368155355344563441,i=0.24770085085542684897920154941114532978571652912585207591199032605489162434475579901621342900504326332001572471388836875257693078071821918832702805395251556576917743455093070180103998083138219966104076957094394557391349705788109482159372116384541942314989586824711640815455948160,l=13824");
        POINTS_OF_INTEREST.put("Diamond", "s=1.117441e-35,r=-0.139979599473469010707455127696075574742775,i=0.9920965960349917387545321133656865993637438,l=4608");
        POINTS_OF_INTEREST.put("Fans", "s=6.66575E-26,r=-1.479734172917307109953944973796048347,i=-0.000330734912098132398970523800624035,l=63019");
        POINTS_OF_INTEREST.put("Flower", "s=3.957286e-52,r=-0.70217838117458919972428923622442030818709415137067763432163,i=0.350188994078575685784373023205753300349944168843451225598036,l=40000");
        POINTS_OF_INTEREST.put("H", "s=1.003089e-43,r=-0.05204127701672578557069728343040824112099645303821,i=0.880002647049300592795822899476444917140801358994528,l=8384");
        POINTS_OF_INTEREST.put("II", "s=6.406352e-70,r=0.059225953558725264703470790955926721025398828980634406424909773141061905353101,i=0.655759481607671167418333236185189577645669410299365700053795401428262327401521,l=65174");
        POINTS_OF_INTEREST.put("Jaws", "s=4.810315E-26,r=-1.996375168276809430933711379256262298,i=0.0000000188809999371773610027511384265,l=15000");
        POINTS_OF_INTEREST.put("Leaves", "s=8.814956e-23,r=-1.7474366027062676704548196087803,i=0.00209136049119104200902757584216,l=3130");
        POINTS_OF_INTEREST.put("Linear", "s=5.619169e-104,r=0.4118306744365560082964589189706382555701781300452311348621277450361136524958556822550976866656858976069727388697,i=0.1380696332743936010084385729679151224189479304585324506091992009330151487315020061216480891465199919055936312788,l=7168");
        POINTS_OF_INTEREST.put("Lines", "s=2.877312e-37,r=0.267631092094222042989030536901435635989043689,i=0.003868418790357451575907772520676739621003884,l=4352");
        POINTS_OF_INTEREST.put("Mosaic", "s=3.096137e-46,r=0.372137738770323258373356630885867793129508737871396193,i=-0.09039824543417816169295241115100981930266548257065052,l=15512");
        POINTS_OF_INTEREST.put("Mosaic2", "s=2.672520e-67,r=0.372137738770323258373356630885867793129508737741343871454145013316342011146500,i=-0.09039824543417816169295241115100981930266548256454002516957239220293676019424,l=13652");
        POINTS_OF_INTEREST.put("Mosaic3", "s=5.135241e-35,r=0.05922595355872526470347079095592672097075447,i=0.65575948160767116741833323618518957235028203,l=16843");
        POINTS_OF_INTEREST.put("MultiH", "s=6.153873e-59,r=-0.1506862823015115390461202865224991362340614194980360244896989964209,i=1.04400017290791700772628571989824352249520720507595502233603826804338,l=100000");
        POINTS_OF_INTEREST.put("Octogram", "s=1.500000e-27,r=-0.1611932416283335497932037769942650,i=1.03962741547672189695078100332888192,l=5009");
        POINTS_OF_INTEREST.put("Pinwheel", "s=9.7855E-49,r=-0.531646625383294164529055768810730015611279206489655328452073,i=0.6685308300121304388731085643978211266805614576504294116119685,l=87640");
        POINTS_OF_INTEREST.put("Ripple", "s=6.29145E-16,r=0.29631325659167238737315235,i=-0.0171709257359399121326943,l=46041");
        POINTS_OF_INTEREST.put("Saw Wheel", "s=8.497763e-41,r=0.41149469307133465265064840380739746478574726220760,i=-0.1615556761608112511088531693650175956110688024952,l=2304");
        POINTS_OF_INTEREST.put("Scales", "s=1.500000e-38,r=-0.0131008420619275573740848458568377199950399054,i=0.71505414382185825698128995207711460876924722308,l=7065");
        POINTS_OF_INTEREST.put("Shells", "s=7.501533e-13,r=0.250117332491014475344,i=0.000001918001511940442,l=32000");
        POINTS_OF_INTEREST.put("Spirals", "s=5.4293E-22,r=-1.25020813153752052032981824051180,i=-0.00559127510520559732398195501999,l=201367");
        POINTS_OF_INTEREST.put("Square", "s=1.212211e-25,r=-0.113979801322772454491978011429001,i=0.9695771423604055236556120247391928,l=17600");
        POINTS_OF_INTEREST.put("Squares", "s=1.569206e-24,r=-0.5568913253743347124102704332799,i=0.63531080567597055494660396989646,l=204608");
        POINTS_OF_INTEREST.put("Starfish", "s=1.500000e-47,r=-0.0985495895311992649056087971516313253373317948314309418,i=0.92469813784454892364548419957293771795418531023091926976,l=422304");
        POINTS_OF_INTEREST.put("Swirl", "s=1.500000e-103,r=-0.82998929490074390782101955568191178347129471944369585853623324386860499343009075841076043604620167229827754399864,i=0.207323411905610537658733717263516140945954483021827627752742705260322000441056991350069865164280164346562577524889,l=24784");
        POINTS_OF_INTEREST.put("Tentacle", "s=5.208408e-63,r=-0.2920978056529359352550294641024754177870277135355227740921239837754571905,i=0.65902369962977701211090046594765415228793874786420545507471841694969945884,l=23609");
        POINTS_OF_INTEREST.put("Tunnel", "s=8.6469E-42,r=0.27522000732176228425895258844200966443778996152984,i=-0.0081106651170550430962879743826939514780333731558,l=50000");
        POINTS_OF_INTEREST.put("Twist", "s=3.144748e-70,r=0.4118306744365560082964589189706382555701781300452311348621277450361136171271985237,i=0.1380696332743936010084385729679151224189479304585324506091992009330151207163408492,l=5120");
        POINTS_OF_INTEREST.put("Wave", "s=1.185776e-33,r=-0.10091453933399909667486636783772556781141577,i=0.956386937512286698962132938892362132434188422,l=6400");
    }
    
    /**
     * The zoom file that holds the information about the current zoom project.
     */
    private static final File zoomFile = new File("images", "zoom.txt");
    
    /**
     * The font size of the overlay data of the Mandelbrot.
     */
    public static final int FONT_SIZE = 9;
    
    /**
     * The height of ui components.
     */
    public static final int BAR_HEIGHT = 8;
    
    
    //Static Fields
    
    /**
     * The coordinates of the Mandelbrot centre.
     */
    public static BigVector centre = new BigVector(BigDecimal.valueOf(-0.75), BigDecimal.ZERO);
    
    /**
     * The maximum number of iterations to use while rendering the Mandelbrot.
     */
    public static int iterationLimit = 1024;
    
    /**
     * The super sample type to use for rendering the Mandelbrot.
     */
    public static SuperSampleType sampleType = SuperSampleType.SUPER_SAMPLE_NONE;
    
    /**
     * The buffer used to store the Mandelbrot calculation.
     */
    public static IndexBuffer2D buffer;
    
    /**
     * The palette filters to use when rendering the Mandelbrot.
     */
    public static int[] paletteFilters = new int[3];
    
    static {
        String[] paletteValues = PALETTES.get("Default").split(",");
        for (int i = 0; i < paletteValues.length; i++) {
            paletteFilters[i] = Integer.valueOf(paletteValues[i], 16);
        }
    }
    
    /**
     * The actual width of the Mandelbrot rendering.
     */
    public static double actualWidth = 0.0;
    
    /**
     * The number of extra exponent divisions needed to represent the size of the Mandelbrot rendering.
     */
    public static int sizeExtraExponent = 0;
    
    /**
     * The math context to use for BigDecimal math within the program.
     */
    public static MathContext mathContext = new MathContext(0);
    
    
    //Fields
    
    /**
     * The rendered Mandelbrot.
     */
    private BufferedImage image;
    
    /**
     * The approximation of the Mandelbrot centre.
     */
    private Approximation centreApproximation = new Approximation();
    
    /**
     * The width of the Mandelbrot.
     */
    private BigDecimal size = BigDecimal.valueOf(3.0);
    
    /**
     * The palette used to color the Mandelbrot.
     */
    private Palette palette = new Palette();
    
    /**
     * The current number of threads being used to render the Mandelbrot.
     */
    private AtomicInteger threadCount = new AtomicInteger(NUM_THREADS);
    
    /**
     * The current sector of the screen being calculated.
     */
    private AtomicInteger sector = new AtomicInteger(-1);
    
    /**
     * The current progress of the rendering being calculated.
     */
    private AtomicInteger progress;
    
    /**
     * The temporary progress of the rendering being calculated.
     */
    private AtomicInteger[] tmpProgress;
    
    /**
     * The number of points that are out of bounds for the current calculation.
     */
    private AtomicInteger outOfBoundsCount;
    
    /**
     * The menu bar for options on rendering the Mandelbrot.
     */
    private JMenuBar menuBar;
    
    /**
     * The progress bar used while rendering the Mandelbrot.
     */
    private JProgressBar progressBar;
    
    /**
     * The string representing the time it took to render the last frame of the Mandelbrot.
     */
    private String calculationTime;
    
    /**
     * The coordinates of the last mouse click.
     */
    private Vector mousePressed;
    
    /**
     * The coordinates of the last location the mouse was dragged to.
     */
    private Vector mouseSelected;
    
    /**
     * The size of the last window created my dragging the mouse.
     */
    private int mouseDraggedSize;
    
    /**
     * Whether or not to automatically slow zoom in on a point.
     */
    private boolean slowZoom = false;
    
    /**
     * The point to automatically slow zoom to.
     */
    private BigVector zoomPoint = new BigVector(new BigDecimal("-0.2920978056529359352550294641024754177870277135355227740921239837754571905"), new BigDecimal("0.65902369962977701211090046594765415228793874786420545507471841694969945884"));
    
    /**
     * The name of the Point of Interest to zoom in on; overrides the zoomPoint.
     */
    private String zoomPointName = "DEEP";
    
    /**
     * The name of the Point of Interest to zoom in on; overrides the zoomPoint.
     */
    private boolean zoomUseFile = true;
    
    /**
     * The factor by which to zoom in on each iteration.
     */
    private double zoomFactor = 0.95;
    
    /**
     * Whether or not to record the the Mandelbrot.
     */
    private boolean record = true;
    
    /**
     * The directory to store the frames in during recording.
     */
    private File recordDir;
    
    /**
     * The index of the current captured frame.
     */
    private long frameIndex = 0;
    
    /**
     * A flag indicating whether to display metrics on the image or not.
     */
    public boolean displayMetrics = true;
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     * @throws Exception When the Drawing cannot be created.
     */
    public static void main(String[] args) throws Exception {
        runDrawing(Mandelbrot.class);
    }
    
    
    //Constructors
    
    /**
     * Constructs a Mandelbrot.
     *
     * @param environment The Environment to render the Mandelbrot in.
     */
    public Mandelbrot(Environment2D environment) {
        super(environment);
        environment.setSize((int) SCREEN_SIZE.getX(), (int) SCREEN_SIZE.getY());
        EnvironmentBase.registerShutdownTask(this::shutdown);
    }
    
    
    //Methods
    
    /**
     * Sets up components for the Mandelbrot.
     */
    @Override
    public void initComponents() {
        environment.frame.setTitle("Mandelbrot");
        
        environment.setBackground(Color.BLACK);
        
        progressBar = new JProgressBar(0, Environment2D.screenWidth * Environment2D.screenHeight);
        progressBar.setSize(new Dimension(Environment2D.screenWidth - 1, BAR_HEIGHT));
        progressBar.setPreferredSize(new Dimension(Environment2D.screenWidth, BAR_HEIGHT));
        progressBar.setBounds(0, Environment2D.screenHeight - BAR_HEIGHT, progressBar.getWidth(), progressBar.getHeight());
        environment.frame.getContentPane().add(progressBar, BorderLayout.SOUTH);
        
        createBuffer();
        JMenu qualityOptions = new JMenu("Quality");
        ActionListener qualityActionListener = e -> {
            for (SuperSampleType qualityEntry : SuperSampleType.values()) {
                if (qualityEntry.getQuality().equals(e.getActionCommand())) {
                    sampleType = qualityEntry;
                    createBuffer();
                    
                    updateImage();
                }
            }
        };
        for (SuperSampleType qualityEntry : SuperSampleType.values()) {
            JMenuItem qualityOption = new JMenuItem(qualityEntry.getQuality());
            qualityOption.addActionListener(qualityActionListener);
            qualityOptions.add(qualityOption);
        }
        
        JMenu paletteOptions = new JMenu("Palette");
        ActionListener paletteActionListener = e -> {
            String[] paletteValues = PALETTES.get(e.getActionCommand()).split(",");
            for (int i = 0; i < paletteValues.length; i++) {
                paletteFilters[i] = Integer.valueOf(paletteValues[i], 16);
            }
            
            if (image != null) {
                image = buffer.makeTexture(palette);
                environment.run();
            }
        };
        for (Map.Entry<String, String> paletteEntry : PALETTES.entrySet()) {
            JMenuItem paletteOption = new JMenuItem(paletteEntry.getKey());
            paletteOption.addActionListener(paletteActionListener);
            paletteOptions.add(paletteOption);
        }
        
        JMenu pointOptions = new JMenu("Points of Interest");
        ActionListener pointActionListener = e -> {
            String[] pointData = POINTS_OF_INTEREST.get(e.getActionCommand()).split(",");
            
            size = new BigDecimal(pointData[0].substring(2));
            centre = new BigVector(new BigDecimal(pointData[1].substring(2)), new BigDecimal(pointData[2].substring(2)));
            iterationLimit = Integer.parseInt(pointData[3].substring(2));
            
            updateImage();
        };
        for (Map.Entry<String, String> pointEntry : POINTS_OF_INTEREST.entrySet()) {
            JMenuItem pointOption = new JMenuItem(pointEntry.getKey());
            pointOption.addActionListener(pointActionListener);
            pointOptions.add(pointOption);
        }
        
        menuBar = new JMenuBar();
        menuBar.add(qualityOptions);
        menuBar.add(paletteOptions);
        menuBar.add(pointOptions);
        
        environment.frame.setJMenuBar(menuBar);
        environment.frame.pack();
    }
    
    /**
     * Sets up controls for the Mandelbrot.
     */
    @Override
    public void setupControls() {
        environment.renderPanel.addMouseListener(new MouseListener() {
            
            //Fields
            
            /**
             * A flag indicating whether the mouse is pressed or not.
             */
            private boolean mouseIsPressed;
            
            
            //Methods
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (threadCount.get() > 0) {
                    return;
                }
                
                int x = e.getX();
                int y = e.getY();
                
                if (e.isControlDown() && SwingUtilities.isRightMouseButton(e)) {
                    size = BigDecimal.valueOf(3.0);
                    centre = new BigVector(BigDecimal.valueOf(-0.75), BigDecimal.ZERO);
                    iterationLimit = 1024;
                    
                    updateImage();
                    return;
                }
                
                BigVector offset = new BigVector(BigDecimal.ZERO, BigDecimal.ZERO);
                BigDecimal leftSizeScale = BigDecimal.ZERO;
                BigDecimal rightSizeScale = BigDecimal.ZERO;
                boolean doScaling = false;
                
                if (e.getClickCount() == 2) {
                    Vector mul = new Vector(
                            ((double) x / Environment2D.screenHeight) - ((Environment2D.screenWidth / 2.0) / Environment2D.screenHeight),
                            ((Environment2D.screenHeight / 2.0) - y) / Environment2D.screenHeight);
                    offset = new BigVector(mul).scale(size);
                    leftSizeScale = BigDecimal.valueOf(0.2);
                    rightSizeScale = BigDecimal.valueOf(5);
                    doScaling = true;
                    
                } else {
                    if ((mouseDraggedSize > 0) && !SwingUtilities.isRightMouseButton(e)) {
                        int s = mouseDraggedSize / 2;
                        if (((x - mouseSelected.getX()) <= s) && ((mouseSelected.getX() - x) <= s)) {
                            s = (mouseDraggedSize * Environment2D.screenHeight) / Environment2D.screenWidth / 2;
                            if (((y - mouseSelected.getY()) < s) && ((mouseSelected.getY() - y) < s)) {
                                
                                Vector mul = new Vector(
                                        (mouseSelected.getX() / Environment2D.screenHeight) - (Environment2D.screenWidth * 0.5 / Environment2D.screenHeight),
                                        (0.5 * Environment2D.screenHeight - mouseSelected.getY()) / Environment2D.screenHeight);
                                offset = new BigVector(mul).scale(size);
                                leftSizeScale = BigDecimal.valueOf(mouseDraggedSize / (double) Environment2D.screenWidth);
                                rightSizeScale = BigDecimal.valueOf(Environment2D.screenWidth / (double) mouseDraggedSize);
                                doScaling = true;
                            }
                        }
                    }
                }
                
                if (doScaling) {
                    int sizeScale = size.scale();
                    if (offset.getX().scale() > (sizeScale + 4)) {
                        offset.setX(offset.getX().setScale(sizeScale + 4, BigDecimal.ROUND_HALF_DOWN));
                    }
                    if (offset.getY().scale() > (sizeScale + 4)) {
                        offset.setY(offset.getY().setScale(sizeScale + 4, BigDecimal.ROUND_HALF_DOWN));
                    }
                    
                    if (SwingUtilities.isRightMouseButton(e)) {
                        centre = centre.minus(offset);
                        size = size.multiply(rightSizeScale);
                    } else {
                        centre = centre.plus(offset);
                        size = size.multiply(leftSizeScale);
                    }
                    size = size.setScale(6 - size.precision() + size.scale(), RoundingMode.HALF_UP);
                    
                    centre = centre.stripTrailingZeros();
                    size = size.stripTrailingZeros();
                    
                    updateImage();
                }
                
                if (mouseDraggedSize != 0) {
                    mouseDraggedSize = 0;
                    environment.run();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                mouseIsPressed = true;
                mousePressed = new Vector(e.getX(), e.getY());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseIsPressed = false;
            }
            
        });
        
        environment.renderPanel.addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                
                mouseDraggedSize = 2 * Math.abs(x - (int) mousePressed.getX());
                int ds2 = (Math.abs(y - (int) mousePressed.getY()) * 2 * Environment2D.screenWidth) / Environment2D.screenHeight;
                mouseDraggedSize = Math.max(mouseDraggedSize, ds2);
                environment.run();
                mouseSelected = mousePressed.clone();
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            
        });
        
        environment.frame.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_I) {
                    displayMetrics = !displayMetrics;
                    environment.run();
                }
                
                if (e.isControlDown()) {
                    if (key == KeyEvent.VK_Q) {
                        size = BigDecimal.valueOf(3.0);
                        centre = new BigVector(BigDecimal.valueOf(-0.75), BigDecimal.ZERO);
                        iterationLimit = 1024;
                        
                        updateImage();
                    }
                    
                    if (key == KeyEvent.VK_P) {
                        if (environment.frame.getJMenuBar().isVisible()) {
                            menuBar.setVisible(false);
                            environment.frame.setSize(new Dimension(environment.frame.getWidth(), environment.frame.getHeight() - (BAR_HEIGHT * 3) + 1));
                            environment.frame.setPreferredSize(new Dimension(environment.frame.getPreferredSize().width, environment.frame.getPreferredSize().height - (BAR_HEIGHT * 3) + 1));
                            environment.frame.pack();
                        } else {
                            environment.frame.setSize(new Dimension(environment.frame.getWidth(), environment.frame.getHeight() + (BAR_HEIGHT * 3) - 1));
                            environment.frame.setPreferredSize(new Dimension(environment.frame.getPreferredSize().width, environment.frame.getPreferredSize().height + (BAR_HEIGHT * 3) - 1));
                            environment.frame.pack();
                            menuBar.setVisible(true);
                        }
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    }
    
    /**
     * Starts drawing the Mandelbrot.
     */
    @Override
    public void run() {
        initSlowZoom();
        updateImage();
    }
    
    /**
     * Shuts down the Mandelbrot.
     */
    public void shutdown() {
        if (record) {
            CaptureHandler.encodeFrames(recordDir, slowZoom ? 20 : 1);
        }
    }
    
    /**
     * Renders the Mandelbrot.
     *
     * @return The rendered Mandelbrot.
     */
    @Override
    public BufferedImage draw() {
        return image;
    }
    
    /**
     * Produces an overlay for the Mandelbrot.
     *
     * @param g The graphics output.
     */
    @Override
    public void overlay(Graphics2D g) {
        g.setColor(Color.GRAY);
        if (mouseDraggedSize > 0) {
            int width = mouseDraggedSize;
            int height = (mouseDraggedSize * Environment2D.screenHeight) / Environment2D.screenWidth;
            g.draw3DRect((int) mousePressed.getX() - width / 2, (int) mousePressed.getY() - height / 2, width, height, true);
        }
        
        if (displayMetrics) {
            g.setColor(Color.WHITE);
            String[] data = new String[] {
                    "r = " + centre.getX().toPlainString(),
                    "i = " + centre.getY().toPlainString(),
                    "s = " + size.toPlainString(),
                    "iteration_limit = " + iterationLimit,
                    "calculation_time = " + ((calculationTime == null) ? "0.0 s" : calculationTime)
            };
            g.setFont(new Font("Console", Font.PLAIN, FONT_SIZE));
            
            int y = Environment2D.screenHeight - (FONT_SIZE * (data.length + 4));
            for (String d : data) {
                g.drawString(d, FONT_SIZE, y += FONT_SIZE);
            }
        }
    }
    
    /**
     * Sets up the slow zoom parameters if it is enabled.
     */
    private void initSlowZoom() {
        if (slowZoom) {
            boolean initialized = false;
            if (zoomUseFile && zoomFile.exists()) {
                try {
                    List<String> zoomData = Files.readAllLines(Paths.get(zoomFile.getAbsolutePath()));
                    centre = new BigVector(new BigDecimal(zoomData.get(0)), new BigDecimal(zoomData.get(1)));
                    size = new BigDecimal(zoomData.get(2));
                    zoomFactor = Double.parseDouble(zoomData.get(3));
                    iterationLimit = Integer.parseInt(zoomData.get(4));
                    frameIndex = Long.parseLong(zoomData.get(5));
                    initialized = true;
                } catch (Exception ignored) {
                }
            }
            if (!initialized && (zoomPointName != null) && POINTS_OF_INTEREST.containsKey(zoomPointName)) {
                String[] pointData = POINTS_OF_INTEREST.get(zoomPointName).split(",");
                centre = new BigVector(new BigDecimal(pointData[1].substring(2)), new BigDecimal(pointData[2].substring(2)));
                initialized = true;
            }
            if (!initialized && (zoomPoint != null)) {
                centre = zoomPoint;
                initialized = true;
            }
            if (!initialized) {
                slowZoom = false;
            }
        }
    }
    
    /**
     * Updates the slow zoom after an iteration if it is enabled.
     */
    private void updateSlowZoom() {
        if (slowZoom) {
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                size = size
                        .multiply(BigDecimal.valueOf(zoomFactor))
                        .setScale(6 - size.precision() + size.scale(), RoundingMode.HALF_UP)
                        .stripTrailingZeros();
                if (frameIndex < (100 * zoomFactor)) {
                    iterationLimit = 1024;
                }
                
                if (zoomUseFile) {
                    try {
                        if (!zoomFile.exists()) {
                            //noinspection ResultOfMethodCallIgnored
                            zoomFile.createNewFile();
                        }
                        
                        List<String> zoomData = new ArrayList<>();
                        zoomData.add(centre.getX().toPlainString());
                        zoomData.add(centre.getY().toPlainString());
                        zoomData.add(size.toPlainString());
                        zoomData.add(String.valueOf(zoomFactor));
                        zoomData.add(String.valueOf(iterationLimit));
                        zoomData.add(String.valueOf(frameIndex));
                        
                        FileWriter writer = new FileWriter(zoomFile);
                        for (String data : zoomData) {
                            writer.write(data + "\r\n");
                        }
                        writer.close();
                        
                    } catch (Exception ignored) {
                    }
                }
                updateImage();
            }, 250, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Updates the Mandelbrot.
     */
    private void updateImage() {
        progressBar.setValue(0);
        progressBar.setMaximum(1024);
        
        long startTime = System.currentTimeMillis();
        
        int scale = size.scale();
        int precision = size.precision();
        int exp = 0;
        precision = scale - precision + 8;
        if (precision < 7) {
            precision = 7;
        }
        mathContext = new MathContext(precision, RoundingMode.HALF_UP);
        
        double imageSize;
        BigDecimal bd280 = BigDecimal.valueOf(1e-280);
        if (size.compareTo(bd280) < 0) {
            BigDecimal modSize = size;
            while (modSize.compareTo(bd280) < 0) {
                modSize = modSize.movePointRight(1);
                exp++;
            }
            imageSize = modSize.doubleValue();
        } else {
            imageSize = size.doubleValue();
        }
        actualWidth = ((imageSize / 2) * (int) SCREEN_SIZE.getX()) / (int) SCREEN_SIZE.getY();
        sizeExtraExponent = exp;
        
        centreApproximation = new Approximation();
        centreApproximation.fillInCubic(new Vector(0, 0), iterationLimit);
        centreApproximation = new FindBestReferencePoint(centreApproximation).calculate();
        
        iterationLimit = (progress != null) ? Math.max(iterationLimit, getNewLimit()) : iterationLimit;
        
        progress = new AtomicInteger();
        tmpProgress = new AtomicInteger[16];
        for (int i = 0; i < 16; i++) {
            tmpProgress[i] = new AtomicInteger(0);
        }
        outOfBoundsCount = new AtomicInteger();
        buffer.clear(2969);
        
        threadCount.set(NUM_THREADS);
        sector.set(-1);
        Thread calculationThread = new Thread() {
            @Override
            public void run() {
                int sector;
                do {
                    sector = Mandelbrot.this.sector.getAndIncrement();
                    if (sector < 0) { //One time initialisation
                        for (int i = 0; i < threadCount.get(); i++) {
                            new Thread(this).start();
                        }
                        return;
                    }
                    if (sector >= (int) (SECTOR_COUNT.getX() * SECTOR_COUNT.getY())) {
                        break;
                    }
                    calculateSector(sector);
                } while (true);
                
                threadCount.decrementAndGet();
            }
        };
        calculationThread.start();
        
        ScheduledExecutorService spinner = Executors.newSingleThreadScheduledExecutor();
        spinner.scheduleAtFixedRate(() -> {
            if (threadCount.get() == 0) {
                spinner.shutdown();
                
                image = buffer.makeTexture(palette);
                progressBar.setMaximum(buffer.width * buffer.height);
                progressBar.setValue(progressBar.getMaximum());
                calculationTime = (double) (System.currentTimeMillis() - startTime) / 1000 + " s";
                
                environment.run();
                updateSlowZoom();
            } else {
                progressBar.setMaximum(buffer.width * buffer.height);
                progressBar.setValue(progress.get());
            }
        }, 100, 15, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Calculates a sector of the Mandelbrot image.
     *
     * @param sector The index of the sector to calculate.
     */
    private void calculateSector(int sector) {
        double screenWidth = 2.0;
        int adjustedHeight = buffer.height;
        if (sampleType == SuperSampleType.SUPER_SAMPLE_2X) {
            adjustedHeight -= 1;
            adjustedHeight /= 2;
            adjustedHeight += 1;
        }
        
        Vector p0 = new Vector(
                (int) (buffer.width * ((sector % (int) SECTOR_COUNT.getX()) / SECTOR_COUNT.getX())),
                (int) (adjustedHeight * ((sector / (int) SECTOR_COUNT.getX()) / SECTOR_COUNT.getY())));
        Vector p1 = new Vector(
                (int) (buffer.width * (((sector % (int) SECTOR_COUNT.getX()) + 1) / SECTOR_COUNT.getX())),
                (int) (adjustedHeight * (((sector / (int) SECTOR_COUNT.getX()) + 1) / SECTOR_COUNT.getY())));
        
        Vector corner = new Vector(-screenWidth / 2, -screenWidth / 2 * adjustedHeight / buffer.width).plus(
                centreApproximation.screenOffsetFromCenter).minus(
                centreApproximation.screenOffset);
        Vector center = corner.plus(new Vector(screenWidth, screenWidth).times(
                new Vector((p0.getX() + p1.getX()), (p0.getY() + p1.getY()))).scale(0.5 / buffer.width));
        
        double newScreenWidth = (screenWidth * (p1.getX() - p0.getX())) / buffer.width;
        Approximation approximation = new Approximation();
        approximation.initializeCubic(centreApproximation, center, newScreenWidth, centreApproximation);
        
        subCalculate(newScreenWidth, adjustedHeight, approximation, buffer.subBuffer(p0, p1));
    }
    
    /**
     * Calculates a rectangular region of the Mandelbrot image.
     *
     * @param screenWidth    The width of the rectangular region.
     * @param adjustedHeight The adjusted height of the rectangular region.
     * @param approximation  The approximation holding the reference point of the rectangular region.
     * @param buffer         The sub buffer to store the calculation in.
     */
    private void subCalculate(double screenWidth, int adjustedHeight, Approximation approximation, IndexBuffer2D buffer) {
        Vector corner = new Vector(-screenWidth / 2, -screenWidth / 2 * buffer.height / buffer.width).plus(
                approximation.screenOffsetFromCenter);
        
        if (approximation == centreApproximation) {
            corner = corner.minus(centreApproximation.screenOffset);
        }
        
        if (buffer.width <= 3 && buffer.height <= 3) {
            Vector delta = new Vector(screenWidth / buffer.width, screenWidth / buffer.width);
            corner = corner.plus(delta.scale(0.5));
            
            for (int y = 0; y < buffer.height; y++) {
                for (int x = 0; x < buffer.width; x++) {
                    int value = approximation.calculateIterations(centreApproximation, corner.plus(delta.times(new Vector(x, y))));
                    buffer.set(x, y, value);
                    
                    progress.getAndIncrement();
                    if (value == 0) {
                        outOfBoundsCount.getAndIncrement();
                    } else if (value >= (iterationLimit - (16 * 256)) && (value < iterationLimit)) {
                        tmpProgress[(value - (iterationLimit - (16 * 256))) >> 8].incrementAndGet();
                    }
                }
            }
            
            if (sampleType == SuperSampleType.SUPER_SAMPLE_2X) {
                corner = corner.minus(delta.scale(0.5));
                for (int y = 0; y < buffer.height && y + (buffer.offset / buffer.stride) < adjustedHeight - 1; y++) {
                    for (int x = 0; x < buffer.width; x++) {
                        int value = approximation.calculateIterations(centreApproximation, corner.plus(delta.times(new Vector(x, y))));
                        buffer.set(x, y + adjustedHeight, value);
                        
                        progress.getAndIncrement();
                        if (value == 0) {
                            outOfBoundsCount.getAndIncrement();
                        } else if (value >= (iterationLimit - (16 * 256)) && (value < iterationLimit)) {
                            tmpProgress[(value - (iterationLimit - (16 * 256))) >> 8].incrementAndGet();
                        }
                    }
                }
            }
            return;
        }
        
        Vector[] p = new Vector[3];
        p[0] = new Vector(0, 0);
        p[1] = new Vector(0, 0);
        p[2] = new Vector(0, 0);
        Vector count = new Vector(0, 0);
        
        if (buffer.width > 1) {
            count.setX(2);
            //noinspection IntegerDivisionInFloatingPointContext
            p[1].setX(buffer.width / 2);
            p[2].setX(buffer.width);
        } else {
            count.setX(1);
            p[1].setX(buffer.width);
        }
        if (buffer.height > 1) {
            count.setY(2);
            //noinspection IntegerDivisionInFloatingPointContext
            p[1].setY(buffer.height / 2);
            p[2].setY(buffer.height);
        } else {
            count.setY(1);
            p[1].setY(buffer.height);
        }
        
        Approximation newApproximation = new Approximation();
        for (int y = 0; y < count.getY(); y++) {
            for (int x = 0; x < count.getX(); x++) {
                Vector center = corner.plus(new Vector(screenWidth, screenWidth).times(
                        new Vector((p[x].getX() + p[x + 1].getX()), (p[y].getY() + p[y + 1].getY())).scale(0.5 / buffer.width)));
                
                double newScreenWidth = (screenWidth * (p[x + 1].getX() - p[x].getX())) / buffer.width;
                newApproximation.initializeCubic(approximation, center, newScreenWidth, centreApproximation);
                
                subCalculate(newScreenWidth, adjustedHeight, newApproximation,
                        buffer.subBuffer(new Vector(p[x].getX(), p[y].getY()), new Vector(p[x + 1].getX(), p[y + 1].getY())));
            }
        }
    }
    
    /**
     * Creates the buffer to be used for the Mandelbrot calculation.
     */
    private void createBuffer() {
        switch (sampleType) {
            case SUPER_SAMPLE_NONE:
                buffer = new IndexBuffer2D((int) SCREEN_SIZE.getX(), (int) SCREEN_SIZE.getY());
                break;
            case SUPER_SAMPLE_2X:
                buffer = new IndexBuffer2D((int) SCREEN_SIZE.getX() + 1, ((int) SCREEN_SIZE.getY() * 2) + 1);
                break;
            case SUPER_SAMPLE_4X:
                buffer = new IndexBuffer2D((int) SCREEN_SIZE.getX() * 2, (int) SCREEN_SIZE.getY() * 2);
                break;
            case SUPER_SAMPLE_4X_9:
                buffer = new IndexBuffer2D(((int) SCREEN_SIZE.getX() * 2) + 1, ((int) SCREEN_SIZE.getY() * 2) + 1);
                break;
            case SUPER_SAMPLE_9X:
                buffer = new IndexBuffer2D((int) SCREEN_SIZE.getX() * 3, (int) SCREEN_SIZE.getY() * 3);
                break;
            default:
                buffer = null;
        }
    }
    
    /**
     * Determines the new iteration limit based on the previous calculation.
     *
     * @return The new iteration limit based on the previous calculation.
     */
    private int getNewLimit() {
        if ((tmpProgress[15].get() > outOfBoundsCount.get()) || ((2000 * tmpProgress[15].get()) > progress.get()) || ((tmpProgress[15].get() > 50) && (tmpProgress[15].get() > (tmpProgress[14].get() * 0.5)))) {
            double ratio = tmpProgress[15].doubleValue() / tmpProgress[14].doubleValue();
            double old_ratio = tmpProgress[14].doubleValue() / tmpProgress[13].doubleValue();
            
            if (old_ratio < ratio) {
                double y = Math.log(ratio) / Math.log(old_ratio);
                ratio = Math.pow(ratio, Math.pow(y, 9));
            }
            
            double x = Math.log((progress.doubleValue() / 4000.0) / tmpProgress[15].doubleValue()) / Math.log(ratio);
            if (x < 1) {
                x = 1;
            }
            
            int extra = (int) (x * 256);
            if ((extra > 5000) && (extra > (iterationLimit / 2))) {
                extra = Math.min(5000, iterationLimit / 2);
            }
            return iterationLimit + extra;
        }
        return iterationLimit;
    }
    
    
    //Sub-Classes
    
    /**
     * Holds a buffer of the calculation of the Mandelbrot data.
     */
    private class IndexBuffer2D {
        
        //Fields
        
        /**
         * The width of the buffer.
         */
        public int width;
        
        /**
         * The height of the buffer.
         */
        public int height;
        
        /**
         * The stride of the buffer.
         */
        public int stride;
        
        /**
         * The offset of the buffer.
         */
        public int offset;
        
        /**
         * The buffer of the calculation of the Mandelbrot data.
         */
        public int[] buffer;
        
        
        //Constructors
        
        /**
         * Constructs a new IndexBuffer2D of a specified dimension.
         *
         * @param width  The width of the buffer.
         * @param height The height of the buffer.
         */
        public IndexBuffer2D(int width, int height) {
            this.buffer = new int[width * height];
            this.width = width;
            this.height = height;
            this.stride = width;
        }
        
        /**
         * Constructs a new IndexBuffer2D.
         *
         * @param buffer The initial buffer of the calculation of the Mandelbrot data.
         * @param width  The width of the buffer.
         * @param height The height of the buffer.
         * @param stride The stride of the buffer.
         * @param offset The offset of the buffer.
         */
        public IndexBuffer2D(int[] buffer, int width, int height, int stride, int offset) {
            this.buffer = buffer;
            this.width = width;
            this.height = height;
            this.stride = stride;
            this.offset = offset;
        }
        
        
        //Methods
        
        /**
         * Gets a value from the buffer at a specified position.
         *
         * @param x The x coordinate of the value.
         * @param y The y coordinate of the value.
         * @return The value from the buffer at the specified position.
         */
        public int get(int x, int y) {
            return buffer[offset + x + y * stride];
        }
        
        /**
         * Sets a value in the buffer at a specified position.
         *
         * @param x     The x coordinate of the value to set.
         * @param y     The y coordinate of the value to set.
         * @param value The value to set at the position.
         */
        public void set(int x, int y, int value) {
            buffer[offset + x + y * stride] = value;
        }
        
        /**
         * Clears the buffer and sets a default value to all positions.
         *
         * @param value The default value to set for all positions in the buffer.
         */
        public void clear(int value) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    set(x, y, value);
                }
            }
        }
        
        /**
         * Creates a sub buffer for a rectangular area of a parent buffer.
         *
         * @param p1 The upper left point of the rectangular area for the sub buffer.
         * @param p2 The lower right point of the rectangular area for the sub buffer.
         * @return A sub buffer for the rectangular area specified.
         */
        public IndexBuffer2D subBuffer(Vector p1, Vector p2) {
            return new IndexBuffer2D(buffer, (int) (p2.getX() - p1.getX()), (int) (p2.getY() - p1.getY()), stride, (int) ((p1.getY() * stride) + p1.getX() + offset));
        }
        
        /**
         * Creates an image from the buffer using a palette to color the data.
         *
         * @param palette The palette used to color the data in the buffer.
         * @return An image of the buffer data of the Mandelbrot.
         */
        public BufferedImage makeTexture(Palette palette) {
            BufferedImage image = null;
            int i, j, k, l, m, n, o, p, q, w, h;
            
            try {
                switch (Mandelbrot.sampleType) {
                    case SUPER_SAMPLE_NONE:
                        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                        
                        for (int y = 0, y2 = height - 1; y < height; y++, y2--) {
                            for (int x = 0; x < width; x++) {
                                i = get(x, y);
                                image.setRGB(x, y2, palette.getAverageColor(i));
                                
                            }
                        }
                        break;
                    case SUPER_SAMPLE_2X:
                        w = width - 1;
                        h = height / 2;
                        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                        
                        for (int y = 0, y2 = h - 1; y < h; y++, y2--) {
                            for (int x = 0; x < w; x++) {
                                i = get(x, y);
                                j = get(x, y + 1);
                                k = get(x, y + h + 1);
                                l = get(x + 1, y + h + 1);
                                image.setRGB(x, y2, palette.getAverageColor(i, j, k, l));
                            }
                        }
                        break;
                    case SUPER_SAMPLE_4X:
                        w = width / 2;
                        h = height / 2;
                        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                        
                        for (int y = 0, y2 = height - 2; y < height; y += 2, y2 -= 2) {
                            for (int x = 0; x < width; x += 2) {
                                i = get(x, y);
                                j = get(x, y + 1);
                                k = get(x + 1, y + 1);
                                l = get(x + 1, y);
                                image.setRGB(x / 2, y2 / 2, palette.getAverageColor(i, j, k, l));
                            }
                        }
                        break;
                    case SUPER_SAMPLE_4X_9:
                        w = width / 2;
                        h = height / 2;
                        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                        
                        for (int y = 0, y2 = height - 3; y < height - 1; y += 2, y2 -= 2) {
                            for (int x = 0; x < width - 1; x += 2) {
                                i = get(x, y);
                                j = get(x, y + 1);
                                k = get(x + 1, y + 1);
                                l = get(x + 1, y);
                                m = get(x + 2, y);
                                n = get(x + 2, y + 1);
                                o = get(x + 2, y + 2);
                                p = get(x + 1, y + 2);
                                q = get(x, y + 2);
                                image.setRGB(x / 2, y2 / 2, palette.getAverageColor(i, j, k, l, m, n, o, p, q));
                            }
                        }
                        break;
                    case SUPER_SAMPLE_9X:
                        w = width / 3;
                        h = height / 3;
                        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                        
                        for (int y = 0, y2 = height - 2; y < height; y += 3, y2 -= 3) {
                            for (int x = 0; x < width; x += 3) {
                                i = get(x, y);
                                j = get(x, y + 1);
                                k = get(x + 1, y + 1);
                                l = get(x + 1, y);
                                m = get(x + 2, y);
                                n = get(x + 2, y + 1);
                                o = get(x + 2, y + 2);
                                p = get(x + 1, y + 2);
                                q = get(x, y + 2);
                                image.setRGB(x / 3, y2 / 3, palette.getAverageColor(i, j, k, l, m, n, o, p, q));
                            }
                        }
                        break;
                }
            } catch (OutOfMemoryError e) {
                return null;
            }
            
            if (record) {
                if (recordDir == null) {
                    recordDir = new File(CaptureHandler.CAPTURE_DIR, CaptureHandler.getCaptureName(environment, true));
                }
                String frameName = String.format("%08d", frameIndex++);
                ImageUtility.saveImage(image, new File(recordDir, recordDir.getName() + "~" + frameName + ".jpg"));
            }
            
            return image;
        }
        
    }
    
    /**
     * Finds the best reference point for a calculation of the Mandelbrot.
     */
    private static class FindBestReferencePoint {
        
        //Fields
        
        /**
         * The approximation that will hold the best reference point that is determined.
         */
        private Approximation approximation;
        
        /**
         * The store of potential best reference points.
         */
        private TreeMap<Integer, FBRPEntry> store;
        
        /**
         * The current best reference point.
         */
        private Map.Entry<Integer, FBRPEntry> maxCount;
        
        /**
         * The depth of the current best reference point.
         */
        private int maxCountDepth;
        
        /**
         * A store of the iteration limit of different potential best reference points.
         */
        private int[][] data;
        
        /**
         * The number of potential best reference points in the store that have an iteration higher than the expected iteration limit.
         */
        private int countAboveAccuracyLimit;
        
        /**
         * The step to move when calculating the best reference point.
         */
        private Vector step;
        
        /**
         * The number of steps used when calculating the best reference point.
         */
        private Vector steps;
        
        /**
         * The starting points to use when calculating the best reference point.
         */
        private Vector start;
        
        
        //Constructors
        
        /**
         * Creates a new FindBestReferencePoint.
         *
         * @param initialApproximation The initial approximation to find the best reference point for.
         */
        public FindBestReferencePoint(Approximation initialApproximation) {
            approximation = initialApproximation;
            store = new TreeMap<>();
            data = new int[20][20];
            countAboveAccuracyLimit = 0;
        }
        
        
        //Methods
        
        /**
         * Determines the best reference point.
         *
         * @return The approximation holding the best reference point.
         */
        public Approximation calculate() {
            if (approximation.numIterationsN > Mandelbrot.iterationLimit - 200) {
                return approximation;
            }
            Approximation centerCheckApproximation = null;
            
            Vector point = new Vector(0, 0);
            Vector initialPoint = approximation.screenOffset.clone();
            Vector oldMaxPoint = new Vector(0, 0);
            
            double range = 2.0;
            int initial;
            
            doAPass(point, 1.6, 9);
            
            boolean skipZoomBodge = false;
            for (int pass = 0; pass < 100; pass++) {
                Iterator<Map.Entry<Integer, FBRPEntry>> iterator = store.entrySet().iterator();
                List<Map.Entry<Integer, FBRPEntry>> top = new ArrayList<>();
                top.add(iterator.next());
                if (iterator.hasNext()) {
                    top.add(iterator.next());
                } else {
                    top.add(top.get(0));
                }
                top.add(maxCount);
                
                if (((top.get(1).getValue()).count > ((top.get(0).getValue()).count * 5)) ||
                        (((top.get(1).getValue()).count >= (3 * (top.get(0).getValue()).count)) && ((top.get(2).getValue()).count >= (3 * (top.get(0).getValue()).count)))) {
                    top.set(0, top.get(1));
                } else if (((top.get(2).getValue()).count > ((top.get(1).getValue()).count * 5)) &&
                        ((-top.get(2).getKey() > approximation.numIterationsN) || ((top.get(2).getValue()).count > (maxCountDepth * 2)))) {
                    top.set(0, top.get(2));
                }
                point = (top.get(0).getValue()).total.scale(1.0 / (top.get(0).getValue()).count);
                
                if ((pass == 0) && ((Math.abs(point.getX()) > 0.3) || (Math.abs(point.getY()) > 0.3))) {
                    centerCheckApproximation = new Approximation(approximation);
                }
                
                if ((top.get(0).getKey() >= (approximation.numIterationsN + 1)) ||
                        (((top.get(0).getValue()).count > 10) && ((top.get(0).getKey() > (approximation.numIterationsN - 100)) || (top.get(0) == top.get(2))))) {
                    if (((top.get(0).getValue()).count == 1) && (Vector2.squareSum(point) < (Math.pow(range, 2) * 0.005))) {
                        initial = -1;
                    } else {
                        initial = approximation.numIterationsN;
                        int test = approximation.calculateIterations(approximation, point.minus(approximation.screenOffset));
                        if (((test > initial) || (test == 0)) && (((top.get(0).getValue()).count <= 10) || (test == -top.get(0).getKey()))) {
                            approximation.reFillInCubic(point);
                        } else {
                            initial = -1;
                        }
                    }
                    
                    if ((initial >= approximation.numIterationsN) || (initial == -1)) {
                        point = (top.get(0).getValue()).nonAveraged.clone();
                        approximation.reFillInCubic(point);
                    }
                    
                    if ((((top.get(0).getValue()).count > 10) || (countAboveAccuracyLimit > 10)) || skipZoomBodge || (approximation.numIterationsN < initial)) {
                        double delta = range * 0.025;
                        point = oldMaxPoint.plus(new Vector(
                                delta * ((point.getX() > oldMaxPoint.getX()) ? 1 : -1),
                                delta * ((point.getY() > oldMaxPoint.getY()) ? 1 : -1)));
                        range *= 0.95;
                        skipZoomBodge = true;
                    }
                } else {
                    int test;
                    if (((Math.abs((top.get(0).getValue()).nonAveraged.getX() - approximation.screenOffset.getX()) > (range / 8)) ||
                            (Math.abs((top.get(0).getValue()).nonAveraged.getY() - approximation.screenOffset.getY()) > (range / 8))) &&
                            ((top.get(0).getValue()).count >= 3)) {
                        if (-top.get(0).getKey() > (approximation.calculateIterations(approximation, new Vector(0.001, 0.001)) + 500)) {
                            point = (top.get(0).getValue()).nonAveraged.clone();
                            approximation.reFillInCubic(point);
                        }
                    }
                }
                
                if (((top.get(0).getValue()).count <= 10) && (countAboveAccuracyLimit <= 10) && !skipZoomBodge) {
                    if (!((range > 0.02) && (approximation.numIterationsN < (Mandelbrot.iterationLimit - 100)))) {
                        break;
                    }
                    range /= 3.0;
                } else {
                    if (approximation.numIterationsN >= (Mandelbrot.iterationLimit - 100)) {
                        break;
                    }
                    skipZoomBodge = ((top.get(0).getValue()).count > 40);
                }
                
                store = new TreeMap<>();
                countAboveAccuracyLimit = 0;
                
                if ((Math.abs(oldMaxPoint.getX() - point.getX()) * 2) > range) {
                    range = Math.abs(oldMaxPoint.getX() - point.getX()) * 2.1;
                }
                if ((Math.abs(oldMaxPoint.getY() - point.getY()) * 2) > range) {
                    range = Math.abs(oldMaxPoint.getY() - point.getY()) * 2.1;
                }
                
                oldMaxPoint = point.clone();
                doAPass(point, range, 10);
            }
            
            if (centerCheckApproximation != null) {
                if ((Math.abs(point.getX()) > 0.3) || (Math.abs(point.getY()) > 0.3)) {
                    point = new Vector(0, 0);
                    range = 0.6;
                    
                    for (int pass = 0; pass < 50; pass++) {
                        skipZoomBodge = false;
                        
                        store = new TreeMap<>();
                        countAboveAccuracyLimit = 0;
                        doAPass(point, range, 10);
                        
                        Iterator<Map.Entry<Integer, FBRPEntry>> iterator = store.entrySet().iterator();
                        List<Map.Entry<Integer, FBRPEntry>> top = new ArrayList<>();
                        top.add(iterator.next());
                        if (iterator.hasNext()) {
                            top.add(iterator.next());
                        } else {
                            top.add(top.get(0));
                        }
                        top.add(maxCount);
                        
                        if (((top.get(0).getValue()).count == 1) && ((top.get(1).getValue()).count == 1) &&
                                ((Math.abs((top.get(0).getValue()).total.getX() - (top.get(1).getValue()).total.getX()) > (range / 13.5)) ||
                                        (Math.abs((top.get(0).getValue()).total.getY() - (top.get(1).getValue()).total.getY()) > (range / 13.5))) &&
                                ((top.get(2).getValue().count < 10) || (-top.get(2).getKey() < centerCheckApproximation.numIterationsN))) {
                            int maxIterations = -1;
                            Vector maxPoint = new Vector(0, 0);
                            
                            Vector repeater = initialPoint.minus(start.dividedBy(step));
                            for (int y = 0; y < steps.getX() - 1; y++) {
                                for (int x = 0; x < steps.getY() - 1; x++) {
                                    int i = data[y][x] + data[y][x + 1] + data[y + 1][x + 1] + data[y + 1][x];
                                    i -= Math.max(Math.max(data[y][x], data[y][x + 1]), Math.max(data[y + 1][x + 1], data[y + 1][x]));
                                    
                                    if ((repeater.getX() >= x) && (repeater.getX() <= (x + 1)) && (repeater.getY() >= y) && (repeater.getY() <= (y + 1))) {
                                        i -= 1000;
                                    }
                                    if (i > maxIterations) {
                                        maxPoint = new Vector(x, y);
                                        maxIterations = i;
                                    }
                                }
                            }
                            
                            point = new Vector(0, 0);
                            for (int i = 0; i <= 1; i++) {
                                for (int j = 0; j <= 1; j++) {
                                    point = point.plus(start.plus(
                                            step.times(new Vector(maxPoint.getX() + i, maxPoint.getY() + j)).scale(
                                                    data[(int) maxPoint.getY() + j][(int) maxPoint.getX() + i] - store.lastKey())));
                                }
                            }
                            point = point.scale(1.0 / ((-4 * store.lastKey()) +
                                    data[(int) maxPoint.getY()][(int) maxPoint.getX()] +
                                    data[(int) maxPoint.getY()][(int) maxPoint.getX() + 1] +
                                    data[(int) maxPoint.getY() + 1][(int) maxPoint.getX() + 1] +
                                    data[(int) maxPoint.getY() + 1][(int) maxPoint.getX()]));
                            
                            range *= 0.5;
                            if (range <= 0.02) {
                                break;
                            }
                            
                            int test = approximation.calculateIterations(approximation, point.minus(approximation.screenOffset));
                            if ((test > approximation.numIterationsN) || (test == 0)) {
                                approximation.reFillInCubic(point);
                            }
                            
                            store = new TreeMap<>();
                            countAboveAccuracyLimit = 0;
                            doAPass(point, range, 10);
                            continue;
                        }
                        
                        if (((top.get(1).getValue()).count > ((top.get(0).getValue()).count * 5)) ||
                                (((top.get(1).getValue()).count >= (3 * (top.get(0).getValue()).count)) && ((top.get(2).getValue()).count >= (3 * (top.get(0).getValue()).count)))) {
                            top.set(0, top.get(1));
                        } else if (((top.get(2).getValue()).count > ((top.get(1).getValue()).count * 5)) && (-top.get(2).getKey() > centerCheckApproximation.numIterationsN) && ((maxCount.getValue()).count >= (maxCountDepth * 0.67777))) {
                            top.set(0, top.get(2));
                        }
                        
                        point = (top.get(0).getValue()).total.scale(1.0 / (top.get(0).getValue()).count);
                        
                        if (((-top.get(0).getKey() >= (centerCheckApproximation.numIterationsN + 1)) ||
                                (((top.get(0).getValue()).count > 10) && (-top.get(0).getKey() > (centerCheckApproximation.numIterationsN - 100))))) {
                            if (((top.get(0).getValue()).count >= 5) && (approximation != centerCheckApproximation)) {
                                approximation = centerCheckApproximation;
                                skipZoomBodge = true;
                            }
                            
                            if (approximation == centerCheckApproximation) {
                                if ((top.get(0).getValue()).count == 1) {
                                    initial = -1;
                                } else {
                                    initial = approximation.numIterationsN;
                                    int test = approximation.calculateIterations(approximation, point.minus(approximation.screenOffset));
                                    if ((test > initial) || (test == 0)) {
                                        approximation.reFillInCubic(point);
                                    } else {
                                        initial = -1;
                                    }
                                }
                                
                                if ((initial >= approximation.numIterationsN) || (initial == -1)) {
                                    point = (top.get(0).getValue()).nonAveraged.clone();
                                    approximation.reFillInCubic(point);
                                }
                            }
                        } else {
                            break;
                        }
                        
                        if (!((range > 0.02) && (approximation.numIterationsN < (Mandelbrot.iterationLimit - 100)))) {
                            break;
                        }
                        
                        if ((countAboveAccuracyLimit < 10) && !skipZoomBodge) {
                            range /= 3.0;
                        } else {
                            range *= 0.95;
                        }
                    }
                }
            }
            
            return approximation;
        }
        
        /**
         * Performs a pass with the current parameters trying to determine the best reference point.
         *
         * @param point   The point to center around.
         * @param dim     The dimension of the square around the center point to test.
         * @param numRows The number of rows in the dimension.
         */
        private void doAPass(Vector point, double dim, int numRows) {
            Vector dimension = new Vector(dim, dim);
            step = dimension.scale(1.0 / (numRows - 1));
            steps = new Vector(numRows, numRows);
            start = point.minus(dimension.scale(0.5));
            Vector end = point.plus(dimension.plus(step).scale(0.5));
            
            int y = 0;
            for (double q = start.getY(); q < end.getY(); q += step.getY()) {
                int x = 0;
                for (double p = start.getX(); p < end.getX(); p += step.getX()) {
                    Vector currentPoint = new Vector(p, q);
                    
                    int i = approximation.calculateIterations(approximation, currentPoint.minus(approximation.screenOffset));
                    if (i == 0) {
                        i = 0x7fffffff;
                    }
                    data[y][x] = i;
                    
                    if (i > (approximation.numIterationsN + 200)) {
                        countAboveAccuracyLimit++;
                    }
                    
                    FBRPEntry entry = store.get(-i);
                    if (entry == null) {
                        entry = new FBRPEntry();
                        store.put(-i, entry);
                    }
                    
                    entry.count++;
                    entry.total = entry.total.plus(currentPoint);
                    
                    double d = Vector2.squareSum(currentPoint);
                    if (d < entry.d) {
                        entry.nonAveraged = currentPoint.clone();
                        entry.d = d;
                    }
                    
                    x++;
                }
                y++;
            }
            
            int maxCount = 0;
            int depth = 0;
            for (Map.Entry<Integer, FBRPEntry> storeEntry : store.entrySet()) {
                FBRPEntry thisEntry = storeEntry.getValue();
                if (thisEntry.count > maxCount) {
                    this.maxCount = storeEntry;
                    maxCount = thisEntry.count;
                    maxCountDepth = depth;
                }
                depth += thisEntry.count;
            }
        }
        
        
        //Sub-Classes
        
        /**
         * Holds data about a specific candidate for the best reference point.
         */
        private static class FBRPEntry {
            
            //Fields
            
            /**
             * The number of times this candidate has been passed.
             */
            public int count = 0;
            
            /**
             * The sum of the points that this candidate has hit.
             */
            public Vector total = new Vector(0, 0);
            
            /**
             * The last point that this candidate has hit.
             */
            public Vector nonAveraged = new Vector(0, 0);
            
            /**
             * The minimum hypotenuse of all the points this candidate has hit.
             */
            public double d = 1000000.0;
            
        }
        
    }
    
    /**
     * Holds an approximation of a reference point in the Mandelbrot image.
     */
    private static class Approximation {
        
        //Fields
        
        /**
         * The maximum number of iterations to use.
         */
        public int iterationLimit;
        
        /**
         * The number of iterations this approximation takes us up to.
         */
        public int numIterationsN;
        
        /**
         * The number of iterations needed to complete this approximation.
         */
        public int numIterations;
        
        /**
         * The offset from center of the screen in screen space.
         */
        public Vector screenOffsetFromCenter = new Vector(0, 0);
        
        /**
         * The offset from center of the screen the the approximation center at iteration n.
         */
        public Vector approximationOffsetFromCenter = new Vector(0, 0);
        
        /**
         * The constants used to calculate the x offset from the approximation center to an arbitrary point at iteration n.
         */
        public Vector[] coefficients = new Vector[3];
        
        /**
         * The reference point for the approximation.
         */
        public Vector x0;
        
        /**
         * A list of reference points around the approximation.
         */
        public Vector[] x;
        
        /**
         * The distance to the edge for each reference point in the list of reference points.
         */
        public double[] distanceToEdge;
        
        /**
         * The screen offset of the reference point.
         */
        public Vector screenOffset;
        
        /**
         * The image coordinates of the reference point.
         */
        public BigVector fullPointAfterApproximation;
        
        /**
         * The original approximation of the cubic.
         */
        public Approximation originalApproximation;
        
        
        //Constructors
        
        /**
         * The default no-argument constructor for an Approximation.
         */
        public Approximation() {
        }
        
        /**
         * Constructs an Approximation from a parent Approximation.
         *
         * @param clone The Approximation to clone.
         */
        public Approximation(Approximation clone) {
            iterationLimit = clone.iterationLimit;
            numIterationsN = clone.numIterationsN;
            numIterations = clone.numIterations;
            
            screenOffsetFromCenter = clone.screenOffsetFromCenter.clone();
            approximationOffsetFromCenter = clone.approximationOffsetFromCenter.clone();
            
            coefficients[0] = clone.coefficients[0].clone();
            coefficients[1] = clone.coefficients[1].clone();
            coefficients[2] = clone.coefficients[2].clone();
            
            x0 = clone.x0.clone();
            x = new Vector[iterationLimit - numIterationsN];
            distanceToEdge = new double[iterationLimit - numIterationsN];
            for (int i = 0; i < iterationLimit - numIterationsN; i++) {
                if (clone.x[i] != null) {
                    x[i] = clone.x[i].clone();
                } else {
                    x[i] = new Vector(0, 0);
                }
                distanceToEdge[i] = clone.distanceToEdge[i];
            }
            
            screenOffset = clone.screenOffset.clone();
            
            fullPointAfterApproximation = clone.fullPointAfterApproximation.clone();
            fullPointAfterApproximation.setMathContext(Mandelbrot.mathContext);
            
            originalApproximation = null;
            if (clone.originalApproximation != null) {
                originalApproximation = new Approximation(clone.originalApproximation);
            }
        }
        
        
        //Methods
        
        /**
         * Determines the constants used to calculate the x offset from the approximation center to an arbitrary point at iteration n.
         *
         * @param parent         The parent approximation.
         * @param referencePoint The reference point to calculate the approximation from.
         * @param screenWidth    The width of the screen.
         * @param approximation  The current approximation.
         */
        public void initializeCubic(Approximation parent, Vector referencePoint, double screenWidth, Approximation approximation) {
            this.screenOffsetFromCenter = referencePoint.clone();
            double accuracy = 0.00001 * Math.pow(2 / screenWidth, 2);
            
            Vector negateY = new Vector(1, -1);
            
            Vector[] screenDif = new Vector[3];
            screenDif[0] = referencePoint.minus(parent.screenOffsetFromCenter);
            screenDif[1] = new Vector(Math.pow(screenDif[0].getX(), 2) - Math.pow(screenDif[0].getY(), 2), 2 * screenDif[0].getX() * screenDif[0].getY());
            screenDif[2] = new Vector(screenDif[0].dot(screenDif[1].times(negateY)), screenDif[0].dot(screenDif[1].reverse()));
            
            coefficients[0] = parent.coefficients[0].plus(Vector2.dotFlop(parent.coefficients[1], screenDif[0]).scale(2)).plus(Vector2.dotFlop(parent.coefficients[2], screenDif[1]).scale(3));
            coefficients[1] = parent.coefficients[1].plus(Vector2.dotFlop(parent.coefficients[2], screenDif[0]).scale(3));
            coefficients[2] = parent.coefficients[2].clone();
            
            approximationOffsetFromCenter =
                    Vector2.dotFlop(parent.coefficients[0], screenDif[0]).plus(
                            Vector2.dotFlop(parent.coefficients[1], screenDif[1]).plus(
                                    Vector2.dotFlop(parent.coefficients[2], screenDif[2]).plus(
                                            parent.approximationOffsetFromCenter)));
            numIterationsN = parent.numIterationsN;
            
            Vector delta = referencePoint.scale(Mandelbrot.actualWidth * Math.pow(10, -Mandelbrot.sizeExtraExponent));
            
            Vector[] tmpCoefficients = new Vector[3];
            tmpCoefficients[0] = coefficients[0].clone();
            tmpCoefficients[1] = coefficients[1].clone();
            tmpCoefficients[2] = coefficients[2].clone();
            
            int extra = 0;
            int offset = parent.numIterationsN - approximation.numIterationsN;
            while (true) {
                if (extra + offset >= approximation.numIterations - approximation.numIterationsN - 1) {
                    break;
                }
                
                Vector localX = approximation.x[extra + offset].plus(approximationOffsetFromCenter);
                
                tmpCoefficients[0] = Vector2.dotFlop(localX, tmpCoefficients[0]).scale(2).plus(
                        new Vector(Mandelbrot.actualWidth * Math.pow(10, -Mandelbrot.sizeExtraExponent), 0));
                tmpCoefficients[1] = Vector2.dotFlop(localX, tmpCoefficients[1]).scale(2).plus(
                        new Vector(Vector2.squareDifference(coefficients[0]), 2 * coefficients[0].getX() * coefficients[0].getY()));
                tmpCoefficients[2] = Vector2.dotFlop(localX, tmpCoefficients[2]).scale(2).plus(
                        Vector2.dotFlop(coefficients[0], coefficients[1]).scale(2));
                
                if ((Vector2.squareSum(tmpCoefficients[0]) + Vector2.squareSum(tmpCoefficients[1])) > approximation.distanceToEdge[extra + offset]) {
                    break;
                }
                
                double accuracyMunge = 1 / (Math.abs(tmpCoefficients[0].getX()) + Math.abs(tmpCoefficients[0].getY()));
                if (Vector2.squareSum(tmpCoefficients[2].scale(accuracyMunge)) > (Math.pow(accuracy, 2) * Vector2.squareSum(tmpCoefficients[0].scale(accuracyMunge)))) {
                    break;
                }
                
                coefficients[0] = tmpCoefficients[0].clone();
                coefficients[1] = tmpCoefficients[1].clone();
                coefficients[2] = tmpCoefficients[2].clone();
                
                approximationOffsetFromCenter =
                        Vector2.dotFlop(approximation.x[extra + offset], approximationOffsetFromCenter).scale(2).plus(
                                new Vector(Vector2.squareDifference(approximationOffsetFromCenter), 2 * approximationOffsetFromCenter.getX() * approximationOffsetFromCenter.getY()).plus(
                                        delta));
                numIterationsN = parent.numIterationsN + (++extra);
            }
        }
        
        /**
         * Fills in the data for the approximation.
         *
         * @param referencePoint The reference point of the parent.
         */
        public void fillInCubic(Vector referencePoint, int iterationLimit) {
            this.screenOffset = referencePoint.clone();
            this.iterationLimit = iterationLimit;
            
            coefficients[0] = new Vector(Mandelbrot.actualWidth, 0);
            coefficients[1] = new Vector(0, 0);
            coefficients[2] = new Vector(0, 0);
            
            Vector[] tmpCoefficients = new Vector[3];
            
            BigVector c = new BigVector(screenOffset).scale(Mandelbrot.actualWidth);
            c.setMathContext(Mandelbrot.mathContext);
            if (Mandelbrot.sizeExtraExponent != 0) {
                c = c.movePointLeft(Mandelbrot.sizeExtraExponent);
            }
            c = c.plus(Mandelbrot.centre);
            
            BigVector pFullCurrent = c.clone();
            Vector pCurrent = new Vector2(c.getX().doubleValue(), c.getY().doubleValue());
            x0 = pCurrent.clone();
            
            double screenOffsetLimit = Math.pow((2 - x0.hypotenuse()), 2) * 0.99;
            double aspectRatio = ((double) Mandelbrot.buffer.width / Mandelbrot.buffer.height);
            if (aspectRatio < 1) {
                screenOffsetLimit *= Math.pow(aspectRatio, 2);
            }
            
            int extraExponent = Mandelbrot.sizeExtraExponent;
            double accuracy = 0.0000001;
            double width = Mandelbrot.actualWidth;
            
            int count = 1;
            if (Vector2.squareSum(coefficients[0]) <= screenOffsetLimit) {
                do {
                    tmpCoefficients[0] = Vector2.dotFlop(pCurrent, coefficients[0]).scale(2).plus(new Vector(width, 0));
                    tmpCoefficients[1] = Vector2.dotFlop(pCurrent, coefficients[1]).scale(2).plus(new Vector(Vector2.squareDifference(coefficients[0]), 2 * coefficients[0].getX() * coefficients[0].getY()));
                    tmpCoefficients[2] = Vector2.dotFlop(pCurrent, coefficients[2]).plus(Vector2.dotFlop(coefficients[0], coefficients[1])).scale(2);
                    
                    double accuracyMungeRcp = (Math.abs(tmpCoefficients[0].getX()) + Math.abs(tmpCoefficients[0].getY()));
                    double accuracyMunge = 1 / accuracyMungeRcp;
                    
                    double tmpCoefficientASquared = Vector2.squareSum(tmpCoefficients[0].scale(accuracyMunge));
                    if (Vector2.squareSum(tmpCoefficients[0].scale(accuracyMunge)) * Math.pow(accuracyMungeRcp, 2) > screenOffsetLimit) {
                        break;
                    }
                    
                    double tmpCoefficientCSquared = Vector2.squareSum(tmpCoefficients[2].scale(accuracyMunge));
                    if ((tmpCoefficientCSquared >= Math.pow(accuracy, 2) * tmpCoefficientASquared) && (tmpCoefficientCSquared != 0)) {
                        if (extraExponent != 0) {
                            do {
                                extraExponent--;
                                coefficients[0].scale(0.1);
                                coefficients[1].scale(0.01);
                                coefficients[2].scale(0.001);
                                tmpCoefficients[0].scale(0.1);
                                tmpCoefficients[1].scale(0.01);
                                tmpCoefficients[2].scale(0.001);
                                tmpCoefficientCSquared *= 0.0001;
                                width *= 0.1f;
                            }
                            while ((extraExponent != 0) && (tmpCoefficientCSquared >= Math.pow(accuracy, 2) * tmpCoefficientASquared) && (tmpCoefficientCSquared != 0.0));
                            
                            if ((tmpCoefficientCSquared >= Math.pow(accuracy, 2) * tmpCoefficientASquared) && (tmpCoefficientCSquared != 0.0)) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    
                    coefficients[0] = tmpCoefficients[0].clone();
                    coefficients[1] = tmpCoefficients[1].clone();
                    coefficients[2] = tmpCoefficients[2].clone();
                    
                    BigVector p2 = pFullCurrent.times(pFullCurrent);
                    p2.setX(p2.getX().add(c.getX().subtract(p2.getY(), Mandelbrot.mathContext), Mandelbrot.mathContext));
                    p2.setY(pFullCurrent.getX().multiply(pFullCurrent.getY(), Mandelbrot.mathContext));
                    p2.setY(p2.getY().add(c.getY().add(p2.getY(), Mandelbrot.mathContext), Mandelbrot.mathContext));
                    
                    pFullCurrent = p2.clone();
                    pCurrent = new Vector(pFullCurrent.getX().doubleValue(), pFullCurrent.getY().doubleValue());
                    
                    if (++count >= this.iterationLimit - 1) {
                        break;
                    }
                } while (true);
            }
            
            numIterationsN = count;
            approximationOffsetFromCenter = new Vector(0, 0);
            
            if (count < this.iterationLimit - 1) {
                if ((Vector2.squareSum(pCurrent) < (1.5 * Vector2.squareSum(coefficients[0]))) && (count > 30)) {
                    
                    Vector[] screen = new Vector[3];
                    screen[0] = Vector2.dotFlop(coefficients[0], pCurrent.scale(-1)).scale(1.0 / Vector2.squareSum(coefficients[0]));
                    
                    for (int i = 0; i < 1000; i++) {
                        screen[1] = new Vector2(Vector2.squareDifference(screen[0]), 2 * screen[0].getX() * screen[0].getY());
                        screen[2] = Vector2.dotFlop(screen[0], screen[1]);
                        
                        Vector f = Vector2.dotFlop(coefficients[0], screen[0]).plus(
                                Vector2.dotFlop(coefficients[1], screen[1])).plus(
                                Vector2.dotFlop(coefficients[2], screen[2])).plus(pCurrent);
                        double originalFSquared = Vector2.squareSum(f);
                        
                        Vector num = f.clone();
                        Vector den = coefficients[0].plus(
                                Vector2.dotFlop(screen[0], coefficients[1]).scale(2)).plus(
                                Vector2.dotFlop(screen[1], coefficients[2]).scale(3));
                        num = Vector2.dotFlopNegative(den, num);
                        
                        Vector currentScreen;
                        double factor = 8.0;
                        do {
                            Vector[] tmpScreen = new Vector[3];
                            tmpScreen[0] = screen[0].minus(num).scale(Vector2.squareSum(den) * factor);
                            tmpScreen[1] = new Vector2(Vector2.squareDifference(tmpScreen[0]), 2 * tmpScreen[0].getX() * tmpScreen[0].getY());
                            tmpScreen[2] = Vector2.dotFlop(tmpScreen[0], tmpScreen[1]);
                            
                            f = Vector2.dotFlop(coefficients[0], tmpScreen[0]).plus(
                                    Vector2.dotFlop(coefficients[1], tmpScreen[1])).plus(
                                    Vector2.dotFlop(coefficients[2], tmpScreen[2])).plus(pCurrent);
                            
                            if ((Vector2.squareSum(f) < originalFSquared) || (factor <= 0.000001f)) {
                                currentScreen = tmpScreen[0].clone();
                                break;
                            }
                            factor *= 0.5;
                        } while (true);
                        
                        screen[0] = currentScreen.clone();
                    }
                    
                    if (Vector2.squareSum(screen[0]) < 2) {
                        fullPointAfterApproximation = pFullCurrent.clone();
                        x = new Vector[iterationLimit - count];
                        distanceToEdge = new double[iterationLimit - count];
                        
                        reFillInCubic(screen[0].plus(referencePoint));
                        return;
                    }
                }
            }
            
            fullPointAfterApproximation = pFullCurrent.clone();
            
            x = new Vector[iterationLimit - count];
            distanceToEdge = new double[iterationLimit - count];
            
            int i = 0;
            x[i] = pCurrent.clone();
            distanceToEdge[i] = 2.0 - x[i].hypotenuse();
            distanceToEdge[i] = distanceToEdge[i] * Math.abs(distanceToEdge[i]);
            if (distanceToEdge[i] < 1e-7) {
                BigVector pSquared = pFullCurrent.times(pFullCurrent);
                BigDecimal distanceToEdge = BigDecimal.valueOf(4).subtract((pSquared.getX().add(pSquared.getY(), Mandelbrot.mathContext)), Mandelbrot.mathContext);
                double distance = (distanceToEdge.doubleValue() / 4) + (Math.pow(distanceToEdge.doubleValue(), 2) / 32);
                this.distanceToEdge[i] = distance * Math.abs(distance);
            }
            
            do {
                i++;
                
                BigVector p2 = pFullCurrent.times(pFullCurrent);
                p2.setX(p2.getX().add(c.getX().subtract(p2.getY(), Mandelbrot.mathContext), Mandelbrot.mathContext));
                p2.setY(pFullCurrent.getX().multiply(pFullCurrent.getY(), Mandelbrot.mathContext));
                p2.setY(p2.getY().add(c.getY().add(p2.getY(), Mandelbrot.mathContext), Mandelbrot.mathContext));
                
                pFullCurrent = p2.clone();
                
                if (++count >= this.iterationLimit) {
                    break;
                }
                
                x[i] = new Vector(pFullCurrent.getX().doubleValue(), pFullCurrent.getY().doubleValue());
                distanceToEdge[i] = 2.00 - x[i].hypotenuse();
                distanceToEdge[i] = distanceToEdge[i] * Math.abs(distanceToEdge[i]);
                if (distanceToEdge[i] < 1e-7) {
                    BigVector pSquared = pFullCurrent.times(pFullCurrent);
                    BigDecimal distanceToEdge = BigDecimal.valueOf(4).subtract((pSquared.getX().add(pSquared.getY(), Mandelbrot.mathContext)), Mandelbrot.mathContext);
                    double distance = (distanceToEdge.doubleValue() / 4) + (Math.pow(distanceToEdge.doubleValue(), 2) / 32);
                    this.distanceToEdge[i] = distance * Math.abs(distance);
                }
                
            } while (distanceToEdge[i] > 0);
            
            numIterations = count;
        }
        
        /**
         * Fills in the data for the approximation using a more accurate reference point.
         *
         * @param referencePoint The original reference point.
         */
        public void reFillInCubic(Vector referencePoint) {
            screenOffset = referencePoint.clone();
            
            Vector delta;
            if (originalApproximation != null) {
                delta = originalApproximation.calculateApproximation(referencePoint);
            } else {
                delta = calculateApproximation(referencePoint);
            }
            
            BigVector pFullCurrent = fullPointAfterApproximation.plus(new BigVector(delta));
            
            if (originalApproximation == null) {
                originalApproximation = new Approximation(this);
            }
            
            Vector[] screenOffset = new Vector[3];
            screenOffset[0] = referencePoint.clone();
            screenOffset[1] = new Vector2(Vector2.squareDifference(screenOffset[0]), 2 * screenOffset[0].getX() * screenOffset[0].getY());
            screenOffset[2] = Vector2.dotFlop(screenOffset[0], screenOffset[1]);
            
            coefficients[0] = originalApproximation.coefficients[0].plus(
                    Vector2.dotFlop(originalApproximation.coefficients[1], screenOffset[0]).scale(2)).plus(
                    Vector2.dotFlop(originalApproximation.coefficients[2], screenOffset[1]).scale(3));
            coefficients[1] = originalApproximation.coefficients[1].plus(
                    Vector2.dotFlop(originalApproximation.coefficients[2], screenOffset[0]).scale(3));
            coefficients[2] = originalApproximation.coefficients[2].clone();
            
            approximationOffsetFromCenter = new Vector(0, 0);
            numIterationsN = originalApproximation.numIterationsN;
            
            int i = 0;
            x[i] = new Vector(pFullCurrent.getX().doubleValue(), pFullCurrent.getY().doubleValue());
            distanceToEdge[i] = 2.0 - x[i].hypotenuse();
            distanceToEdge[i] = distanceToEdge[i] * Math.abs(distanceToEdge[i]);
            if (distanceToEdge[i] < 1e-7) {
                BigVector pSquared = pFullCurrent.times(pFullCurrent);
                BigDecimal distanceToEdge = BigDecimal.valueOf(4).subtract((pSquared.getX().add(pSquared.getY(), Mandelbrot.mathContext)), Mandelbrot.mathContext);
                double distance = (distanceToEdge.doubleValue() / 4) + (Math.pow(distanceToEdge.doubleValue(), 2) / 32);
                this.distanceToEdge[i] = distance * Math.abs(distance);
            }
            
            BigVector c;
            if (Mandelbrot.sizeExtraExponent != 0) {
                c = new BigVector(referencePoint.scale(Mandelbrot.actualWidth)).movePointLeft(Mandelbrot.sizeExtraExponent).plus(Mandelbrot.centre);
            } else {
                c = Mandelbrot.centre.plus(new BigVector(referencePoint.scale(Mandelbrot.actualWidth)));
            }
            x0 = new Vector(c.getX().doubleValue(), c.getY().doubleValue());
            
            int count = numIterationsN;
            do {
                i++;
                
                BigVector p = pFullCurrent.times(pFullCurrent);
                p.setX(p.getX().add(c.getX().subtract(p.getY(), Mandelbrot.mathContext), Mandelbrot.mathContext));
                p.setY(pFullCurrent.getX().multiply(pFullCurrent.getY(), Mandelbrot.mathContext));
                p.setY(p.getY().add(c.getY().add(p.getY(), Mandelbrot.mathContext), Mandelbrot.mathContext));
                
                pFullCurrent = p.clone();
                
                if (++count >= this.iterationLimit) {
                    break;
                }
                
                x[i] = new Vector(pFullCurrent.getX().doubleValue(), pFullCurrent.getY().doubleValue());
                distanceToEdge[i] = 2.0 - x[i].hypotenuse();
                distanceToEdge[i] *= Math.abs(distanceToEdge[i]);
                if (distanceToEdge[i] < 1e-7) {
                    BigVector pSquared = pFullCurrent.times(pFullCurrent);
                    BigDecimal distanceToEdge = BigDecimal.valueOf(4).subtract((pSquared.getX().add(pSquared.getY(), Mandelbrot.mathContext)), Mandelbrot.mathContext);
                    double distance = (distanceToEdge.doubleValue() / 4) + (Math.pow(distanceToEdge.doubleValue(), 2) / 32);
                    this.distanceToEdge[i] = distance * Math.abs(distance);
                }
                
            } while (distanceToEdge[i] > 0);
            
            numIterations = count;
        }
        
        /**
         * Calculates the first delta using the constants for the approximation.
         *
         * @param startDelta The starting delta.
         * @return The first delta for the approximation.
         */
        public Vector calculateApproximation(Vector startDelta) {
            Vector[] delta = new Vector[3];
            delta[0] = startDelta.clone();
            delta[1] = new Vector2(Vector2.squareDifference(delta[0]), 2 * delta[0].getX() * delta[0].getY());
            delta[2] = Vector2.dotFlop(delta[0], delta[1]);
            
            return Vector2.dotFlop(coefficients[0], delta[0]).plus(
                    Vector2.dotFlop(coefficients[1], delta[1]).plus(
                            Vector2.dotFlop(coefficients[2], delta[2]).plus(
                                    approximationOffsetFromCenter)));
        }
        
        /**
         * Calculates the number of iterations for the approximation.
         *
         * @param approximation The approximation containing a reference point.
         * @param screenDelta   The delta from the center of the screen.
         * @return The number of iterations for the approximation.
         */
        public int calculateIterations(Approximation approximation, Vector screenDelta) {
            Vector delta = screenDelta.scale(Mandelbrot.actualWidth * Math.pow(10, -Mandelbrot.sizeExtraExponent));
            Vector c = approximation.x0.plus(delta);
            Vector dx = calculateApproximation(screenDelta.minus(screenOffsetFromCenter));
            int extra = numIterationsN - approximation.numIterationsN;
            
            while (true) {
                double dxSquared = Vector2.squareSum(dx);
                if (dxSquared > 0.0001) {
                    break;
                }
                if (extra >= (approximation.numIterations - approximation.numIterationsN - 1)) {
                    break;
                }
                
                Vector currentX = approximation.x[extra].clone();
                
                if (dxSquared > approximation.distanceToEdge[extra]) {
                    if (dxSquared > 1E-10) {
                        break;
                    }
                    double del = currentX.dot(dx);
                    if (del * Math.abs(del) > approximation.distanceToEdge[extra] * Vector2.squareSum(currentX)) {
                        return approximation.numIterationsN + extra;
                    }
                }
                
                dx = Vector2.dotFlop(currentX, dx).scale(2).plus(delta).plus(
                        new Vector2(Vector2.squareDifference(dx), 2 * dx.getX() * dx.getY()));
                extra++;
            }
            dx = dx.plus(approximation.x[extra]);
            
            //optimized for speed
            double dxX, dxY;
            int count = approximation.numIterationsN + extra;
            while (Vector2.squareSum(dx) < 4) {
                dxX = Vector2.squareDifference(dx) + c.getX();
                dxY = 2 * dx.getX() * dx.getY() + c.getY();
                dx.setX(dxX);
                dx.setY(dxY);
                if (++count >= approximation.iterationLimit) {
                    return 0;
                }
            }
            return count;
        }
        
    }
    
    /**
     * Holds a color palette used to render the Mandelbrot calculations.
     */
    private static class Palette {
        
        //Constants
        
        /**
         * The number of bands to use to color the palette.
         */
        private static final int NUM_BANDS = 6;
        
        
        //Fields
        
        /**
         * The color palette used to render the Mandelbrot calculations.
         */
        private int[] palette;
        
        /**
         * The rate of divisions between colors in the palette.
         */
        private Vector[] division;
        
        /**
         * The rate of decay of colors in the palette.
         */
        private Vector decay;
        
        /**
         * The starts of the bands used to calculate the colors in the palette.
         */
        private Vector bandStarts;
        
        /**
         * The widths of the bands used to calculate the colors in the palette.
         */
        private Vector bandWidths;
        
        /**
         * The periods of the bands used to calculate the colors in the palette.
         */
        private Vector bandPeriods;
        
        /**
         * The color increment values of the bands used to calculate the colors in the palette.
         */
        private Vector[] bandIncrements;
        
        /**
         * The color scale values of the bands used to calculate the colors in the palette.
         */
        private Vector[] bandScales;
        
        
        //Constructors
        
        /**
         * Creates a new Palette.
         */
        public Palette() {
            palette = new int[0x10000];
            
            division = new Vector[3];
            division[0] = new Vector(0.1230459405F, 0.0730459405F);
            division[1] = new Vector(0.0274356756F, 0.0224356756F);
            division[2] = new Vector(0.0039432465F, 0.0079432465F);
            
            decay = new Vector(5000, 20000, 10000);
            
            bandStarts = new Vector(5000, 3000, 7000, 8500, 0, 0);
            bandWidths = new Vector(256, 256, 512, 256, 0, 0);
            bandPeriods = new Vector(5000, 4444, 7777, 9532, 0, 0);
            
            bandIncrements = new Vector[NUM_BANDS];
            bandIncrements[0] = new Vector(0, 0, 0);
            bandIncrements[1] = new Vector(28, 0, 0);
            bandIncrements[2] = new Vector(0, 0, 0);
            bandIncrements[3] = new Vector(0, 0, 0);
            bandIncrements[4] = new Vector(0, 0, 0);
            bandIncrements[5] = new Vector(0, 0, 0);
            
            bandScales = new Vector[NUM_BANDS];
            bandScales[0] = new Vector(208, 255, 255);
            bandScales[1] = new Vector(255, 191, 191);
            bandScales[2] = new Vector(255, 255, 224);
            bandScales[3] = new Vector(221, 221, 255);
            bandScales[4] = new Vector(255, 255, 255);
            bandScales[5] = new Vector(255, 255, 255);
        }
        
        
        //Methods
        
        /**
         * Determines a color based on an index from the palette.
         *
         * @param index The index.
         * @return The color based on the index from the palette.
         */
        public int getColor(int index) {
            if (index == 0) {
                return 0xff000000;
            }
            index &= 0xffff;
            
            if (palette[index] != 0) {
                return palette[index];
            }
            
            int r = (int) (256 * (index * (division[0].getX() + (1 - Math.exp(-index / decay.getX())) * (division[0].getY() - division[0].getX()))));
            int g = (int) (256 * (index * (division[1].getX() + (1 - Math.exp(-index / decay.getY())) * (division[1].getY() - division[1].getX()))));
            int b = (int) (256 * (index * (division[2].getX() + (1 - Math.exp(-index / decay.getZ())) * (division[2].getY() - division[2].getX()))));
            
            Vector color = new Vector(r & 255, g & 255, b & 255);
            for (int i = 0; i < NUM_BANDS; i++) {
                if ((int) bandWidths.get(i) == 0) {
                    continue;
                }
                
                int c = index - (int) bandStarts.get(i);
                if (bandPeriods.get(i) != 0) {
                    c %= bandPeriods.get(i);
                }
                
                if ((c > 0) && (c < bandWidths.get(i))) {
                    color = color.times(bandScales[i]).plus(bandIncrements[i]);
                }
            }
            
            palette[index] = ((int) color.getX() << 16) + ((int) color.getY() << 8) + (int) color.getZ();
            return palette[index];
        }
        
        /**
         * Determines the average color based on a set of indices from the palette.
         *
         * @param indices The set of indices.
         * @return The average color based on a set of indices from the palette.
         */
        public int getAverageColor(int... indices) {
            int[] colors = new int[indices.length];
            for (int i = 0; i < indices.length; i++) {
                colors[i] = getColor(indices[i]);
            }
            
            int r = 0;
            int g = 0;
            int b = 0;
            for (int color : colors) {
                r += (color & 0xff0000);
                g += (color & 0x00ff00);
                b += (color & 0x0000ff);
            }
            r += (int) Math.ceil(indices.length / 2.0);
            g += (int) Math.ceil(indices.length / 2.0);
            b += (int) Math.ceil(indices.length / 2.0);
            r /= indices.length;
            g /= indices.length;
            b /= indices.length;
            r &= Mandelbrot.paletteFilters[0];
            g &= Mandelbrot.paletteFilters[1];
            b &= Mandelbrot.paletteFilters[2];
            
            return r + g + b;
        }
        
    }
    
}