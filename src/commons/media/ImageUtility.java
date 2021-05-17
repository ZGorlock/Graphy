/*
 * File:    ImageUtility.java
 * Package: commons.media
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/Java-Commons
 */

package commons.media;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import commons.access.Filesystem;
import commons.graphics.DrawUtility;
import commons.math.BoundUtility;
import commons.math.component.vector.IntVector;
import commons.math.component.vector.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles image operations.
 */
public class ImageUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ImageUtility.class);
    
    
    //Functions
    
    /**
     * Loads an image.
     *
     * @param file The image file.
     * @return The BufferedImage loaded from the file, or null if there was an error.
     */
    public static BufferedImage loadImage(File file) {
        if (Filesystem.logFilesystem()) {
            logger.trace("Filesystem: Loading image file: {}", file.getAbsolutePath());
        }
        
        try {
            BufferedImage tmpImage = ImageIO.read(file);
            BufferedImage image = new BufferedImage(tmpImage.getWidth(), tmpImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D imageGraphics = image.createGraphics();
            DrawUtility.drawImage(imageGraphics, tmpImage);
            DrawUtility.dispose(imageGraphics);
            return image;
            
        } catch (Exception e) {
            if (Filesystem.logFilesystem()) {
                logger.trace("Filesystem: Unable to load image file: {}", file.getAbsolutePath());
            }
            return null;
        }
    }
    
    /**
     * Saves an image to a file.
     *
     * @param image The image.
     * @param file  The output file.
     * @return Whether the image was successfully saved or not.
     */
    public static boolean saveImage(BufferedImage image, File file) {
        if (Filesystem.logFilesystem()) {
            logger.trace("Filesystem: Saving image file: {}", file.getAbsolutePath());
        }
        
        try {
            return file.mkdirs() && ImageIO.write(image, Filesystem.getFileType(file), file);
            
        } catch (Exception e) {
            if (Filesystem.logFilesystem()) {
                logger.trace("Filesystem: Unable to save image file: {}", file.getAbsolutePath());
            }
            return false;
        }
    }
    
    /**
     * Returns the pixel dimension of an image file.
     *
     * @param image The image file.
     * @return The pixel dimensions of the image file, or null if there is an error.
     */
    public static Dimension getDimensions(File image) {
        Dimension result = null;
        Iterator<ImageReader> imageReaders = ImageIO.getImageReadersBySuffix(Filesystem.getFileType(image));
        if (imageReaders.hasNext()) {
            ImageReader reader = imageReaders.next();
            try (ImageInputStream stream = new FileImageInputStream(image)) {
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (IOException ignored) {
            } finally {
                reader.dispose();
            }
        }
        return result;
    }
    
    /**
     * Returns the pixel width of an image file.
     *
     * @param image The image file.
     * @return The pixel width of the image file, or -1 if there is an error.
     */
    public static int getWidth(File image) {
        Dimension dimensions = getDimensions(image);
        if (dimensions == null) {
            return -1;
        }
        return dimensions.width;
    }
    
    /**
     * Returns the pixel height of an image file.
     *
     * @param image The image file.
     * @return The pixel height of the image file, or -1 if there is an error.
     */
    public static int getHeight(File image) {
        Dimension dimensions = getDimensions(image);
        if (dimensions == null) {
            return -1;
        }
        return dimensions.height;
    }
    
    /**
     * Returns the date the image was taken.
     *
     * @param image The image file.
     * @return The date the image was taken, or the 'last modified time' if that metadata is not present.
     */
    public static Date getDateTaken(File image) {
        MetadataUtility.MetadataTag dateTaken = null;
        switch (Filesystem.getFileType(image).toLowerCase()) {
            case "jpg":
            case "jpeg":
                dateTaken = MetadataUtility.getMetadataTag(image, "Date/Time Original");
                if (dateTaken != null) {
                    break;
                }
            case "png":
            case "gif":
            case "bmp":
            case "tif":
            case "tiff":
            case "wbmp":
                dateTaken = MetadataUtility.getMetadata(image).stream()
                        .filter(e -> e.name.equals("Textual Data") && e.value.startsWith("Creation Time: "))
                        .findFirst().orElse(null);
                if (dateTaken != null) {
                    dateTaken.value = dateTaken.value.substring(15);
                }
                break;
        }
        
        if (dateTaken != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                return sdf.parse(dateTaken.value);
            } catch (ParseException ignored) {
            }
        }
        return Filesystem.getLastModifiedTime(image);
    }
    
    /**
     * Deletes the extra data in an image file, reducing the file size.
     *
     * @param image            The image file.
     * @param preserveMetadata Whether or not to preserve the metadata of the image file.
     * @return Whether the image file was successfully cleaned or not.
     */
    public static boolean cleanImageFile(File image, boolean preserveMetadata) {
        if (Filesystem.logFilesystem()) {
            logger.trace("Filesystem: Cleaning image file: {}", image.getAbsolutePath());
        }
        
        try {
            File tmp = Filesystem.createTemporaryFile(Filesystem.getFileType(image));
            
            Map<String, FileTime> dates = Filesystem.readDates(image);
            
            if (preserveMetadata) {
                try (FileInputStream fileInputStream = new FileInputStream(image);
                     FileOutputStream fileOutputStream = new FileOutputStream(tmp)) {
                    
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(fileInputStream);
                    ImageReader reader = ImageIO.getImageReaders(imageInputStream).next();
                    reader.setInput(imageInputStream, true);
                    IIOMetadata metadata = reader.getImageMetadata(0);
                    IIOMetadata streamMetadata = reader.getStreamMetadata();
                    BufferedImage data = reader.read(0);
                    imageInputStream.flush();
                    
                    ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
                    ImageWriter writer = ImageIO.getImageWriter(reader);
                    writer.setOutput(imageOutputStream);
                    ImageWriteParam writeParams = writer.getDefaultWriteParam();
                    writeParams.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
                    
                    writer.write(streamMetadata, new IIOImage(data, null, metadata), writeParams);
                    writer.dispose();
                    imageOutputStream.flush();
                }
                
            } else {
                BufferedImage data = ImageIO.read(image);
                ImageIO.write(data, Filesystem.getFileType(image).toLowerCase(), tmp);
            }
            
            Filesystem.writeDates(tmp, dates);
            
            if (!Filesystem.safeReplace(tmp, image)) {
                throw new Exception("Failed to safely replace image file");
            }
            return true;
            
        } catch (Exception e) {
            if (Filesystem.logFilesystem()) {
                logger.trace("Filesystem: Unable to clean image file: {}", image.getAbsolutePath());
            }
            return false;
        }
    }
    
    /**
     * Deletes the extra data in an image file, reducing the file size.
     *
     * @param image The image file.
     * @return Whether the image file was successfully cleaned or not.
     * @see #cleanImageFile(File, boolean)
     */
    public static boolean cleanImageFile(File image) {
        return cleanImageFile(image, true);
    }
    
    /**
     * Crops an image.
     *
     * @param image The image.
     * @param rect  The bounds within the image to crop.
     * @return The cropped image.
     */
    public static BufferedImage cropImage(BufferedImage image, Rectangle rect) {
        rect.x = BoundUtility.truncateNum(rect.getX(), 0, image.getWidth() - 1).intValue();
        rect.y = BoundUtility.truncateNum(rect.getY(), 0, image.getHeight() - 1).intValue();
        rect.width = BoundUtility.truncateNum(rect.getWidth(), 1, image.getWidth() - rect.x).intValue();
        rect.height = BoundUtility.truncateNum(rect.getHeight(), 1, image.getHeight() - rect.y).intValue();
        
        BufferedImage cropped = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), image.getType());
        Graphics2D g2 = (Graphics2D) cropped.getGraphics();
        DrawUtility.drawImage(g2, image, new Vector(rect.getX(), rect.getY()), (int) rect.getWidth(), (int) rect.getHeight(), new IntVector(0, 0), (int) rect.getWidth(), (int) rect.getHeight());
        DrawUtility.dispose(g2);
        return cropped;
    }
    
    /**
     * Crops an image.
     *
     * @param image        The image.
     * @param offTheLeft   The number of pixels to crop off the left.
     * @param offTheTop    The number of pixels to crop off the top.
     * @param offTheRight  The number of pixels to crop off the right.
     * @param offTheBottom The number of pixels to crop off the bottom.
     * @return The cropped image.
     * @see #cropImage(BufferedImage, Rectangle)
     */
    public static BufferedImage cropImage(BufferedImage image, int offTheLeft, int offTheTop, int offTheRight, int offTheBottom) {
        offTheLeft = BoundUtility.truncateNum(offTheLeft, 0, image.getWidth() - 1).intValue();
        offTheTop = BoundUtility.truncateNum(offTheTop, 0, image.getHeight() - 1).intValue();
        offTheRight = BoundUtility.truncateNum(offTheRight, 0, image.getWidth() - 1).intValue();
        offTheBottom = BoundUtility.truncateNum(offTheBottom, 0, image.getHeight() - 1).intValue();
        
        Rectangle rect = new Rectangle(offTheLeft, offTheTop,
                BoundUtility.truncateNum((image.getWidth() - offTheLeft - offTheRight), 1, image.getWidth() - offTheLeft).intValue(),
                BoundUtility.truncateNum((image.getHeight() - offTheTop - offTheBottom), 1, image.getHeight() - offTheTop).intValue());
        return cropImage(image, rect);
    }
    
    /**
     * Scales an image.
     *
     * @param image The image.
     * @param scale The factor to scale the image by.
     * @return The scaled image.
     */
    public static BufferedImage scaleImage(BufferedImage image, double scale) {
        if (!BoundUtility.inBounds(scale, 0.0, 1.0, false, false)) {
            return image;
        }
        
        AffineTransform transform = new AffineTransform();
        transform.scale(scale, scale);
        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        BufferedImage scaled = new BufferedImage((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), image.getType());
        return transformOp.filter(image, scaled);
    }
    
    /**
     * Scales an image to a maximum width or height.
     *
     * @param image     The image.
     * @param maxWidth  The maximum width of the final image.
     * @param maxHeight The maximum height of the final image.
     * @return The scaled image with the specified maximum width or height, or the original image if it is within the specified maximum dimensions.
     * @see #scaleImage(BufferedImage, double)
     */
    public static BufferedImage scaleImage(BufferedImage image, int maxWidth, int maxHeight) {
        if ((image.getWidth() <= maxWidth) && (image.getHeight() <= maxHeight)) {
            return image;
        }
        
        double scale = Math.min(((double) maxWidth / image.getWidth()), ((double) maxHeight / image.getHeight()));
        return scaleImage(image, scale);
    }
    
    /**
     * Copies an image from the clipboard.
     *
     * @return The image, or null if there is no image on the clipboard.
     */
    public static BufferedImage copyImageFromClipboard() {
        BufferedImage clipboard = null;
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            
            try {
                clipboard = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                
            } catch (Exception e) {
                if (commons.access.Clipboard.logClipboard()) {
                    logger.trace("Clipboard: Unable to retrieve image contents from the clipboard");
                }
                return null;
            }
        }
        
        if (commons.access.Clipboard.logClipboard()) {
            logger.trace("Clipboard: Retrieved image contents of the clipboard");
        }
        return clipboard;
    }
    
    /**
     * Copies an image to the clipboard.
     *
     * @param image The image.
     */
    public static void copyImageToClipboard(final BufferedImage image) {
        Transferable transferableImage = new Transferable() {
            
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                DataFlavor[] flavors = new DataFlavor[1];
                flavors[0] = DataFlavor.imageFlavor;
                return flavors;
            }
            
            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                DataFlavor[] flavors = getTransferDataFlavors();
                return Arrays.stream(flavors).anyMatch(flavor::equals);
            }
            
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (flavor.equals(DataFlavor.imageFlavor)) {
                    return image;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
            
        };
        
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferableImage, (clipboard1, contents) -> {
            });
            if (commons.access.Clipboard.logClipboard()) {
                logger.trace("Clipboard: Published image contents to the clipboard");
            }
            
        } catch (Exception e) {
            if (commons.access.Clipboard.logClipboard()) {
                logger.trace("Clipboard: Failed to publish image contents to the clipboard");
            }
        }
    }
    
    /**
     * Returns a list of available image formats.
     *
     * @return A list of available image formats.
     */
    public static List<String> getAvailableImageFormats() {
        List<String> imageFormats = new ArrayList<>();
        Iterator<ImageWriterSpi> serviceProviders = IIORegistry.getDefaultInstance()
                .getServiceProviders(ImageWriterSpi.class, false);
        while (serviceProviders.hasNext()) {
            imageFormats.addAll(Arrays.asList(serviceProviders.next().getFormatNames()));
        }
        imageFormats = imageFormats.stream().map(String::toUpperCase).distinct().sorted().collect(Collectors.toList());
        return imageFormats;
    }
    
}
