/*
 * File:    MetadataUtility.java
 * Package: commons.media
 * Author:  Zachary Gill
 */

package commons.media;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to media metadata.
 */
public class MetadataUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MetadataUtility.class);
    
    
    //Functions
    
    /**
     * Extracts the metadata tags from a file.
     *
     * @param file      The file.
     * @param directory The metadata directory to get metadata from.
     * @return The list of metadata tags from the file, or an empty list if there is an error or the metadata cannot be found, or null if the file does not exist.
     */
    public static List<MetadataTag> getMetadata(File file, String directory) {
        if ((file == null) || !file.exists()) {
            return null;
        }
        
        List<MetadataTag> metadataTags = new ArrayList<>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            
            for (Directory dir : metadata.getDirectories()) {
                if ((directory != null) && !dir.getName().equals(directory)) {
                    continue;
                }
                
                for (Tag tag : dir.getTags()) {
                    metadataTags.add(
                            new MetadataTag(dir.getName(), tag.getTagName(), tag.getDescription(), tag.getTagType()));
                }
                
                if (dir.hasErrors()) {
                    for (String error : dir.getErrors()) {
                        metadataTags.add(
                                new MetadataTag(dir.getName(), "ERROR", error, Integer.MIN_VALUE));
                    }
                }
            }
            
        } catch (Exception ignored) {
        }
        
        return metadataTags;
    }
    
    /**
     * Extracts the metadata tags from a file.
     *
     * @param file The file.
     * @return The list of metadata tags from the file, or an empty list if there is an error or the metadata cannot be found.
     * @see #getMetadata(File, String)
     */
    public static List<MetadataTag> getMetadata(File file) {
        return getMetadata(file, null);
    }
    
    /**
     * Extracts a metadata tag from a file.
     *
     * @param file      The file.
     * @param directory The metadata tag directory.
     * @param name      The metadata tag name.
     * @return The specified metadata tag from the file, or null if it does not exist.
     */
    public static MetadataTag getMetadataTag(File file, String directory, String name) {
        return getMetadata(file, directory).stream()
                .filter(e -> e.name.equals(name))
                .findFirst().orElse(null);
    }
    
    /**
     * Extracts a metadata tag from a file.
     *
     * @param file The file.
     * @param name The metadata tag name.
     * @return The specified metadata tag from the file, or null if it does not exist.
     * @see #getMetadataTag(File, String, String)
     */
    public static MetadataTag getMetadataTag(File file, String name) {
        return getMetadataTag(file, null, name);
    }
    
    
    //Inner Classes
    
    /**
     * Defines a Metadata Tag.
     */
    public static class MetadataTag {
        
        //Fields
        
        /**
         * The directory of the Metadata Tag.
         */
        public String directory;
        
        /**
         * The name of the Metadata Tag.
         */
        public String name;
        
        /**
         * The value of the Metadata Tag.
         */
        public String value;
        
        /**
         * The type of the Metadata Tag.
         */
        public int type;
        
        
        //Constructors
        
        /**
         * Creates a new Metadata Tag.
         *
         * @param directory The directory of the Metadata Tag.
         * @param name      The name of the Metadata Tag.
         * @param value     The value of the Metadata Tag.
         * @param type      The type of the Metadata Tag.
         */
        public MetadataTag(String directory, String name, String value, int type) {
            this.directory = directory;
            this.name = name;
            this.value = value;
            this.type = type;
        }
        
        /**
         * The default no-argument constructor for a Metadata Tag.
         */
        public MetadataTag() {
        }
        
        
        //Methods
        
        /**
         * Returns the string representation of a Metadata Tag.
         *
         * @return The string representation of a Metadata Tag.
         */
        @Override
        public String toString() {
            return "[" + directory + "] " + name + " : " + value;
        }
        
        /**
         * Determines if another Metadata Tag is equal to this Metadata Tag.
         *
         * @param o The other Metadata Tag.
         * @return Whether the other Metadata Tag is equal to this Metadata Tag or not.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MetadataTag)) {
                return false;
            }
            MetadataTag other = (MetadataTag) o;
            
            return directory.equals(other.directory) &&
                    name.equals(other.name) &&
                    value.equals(other.value) &&
                    (type == other.type);
        }
        
    }
    
}
