/*
 * File:    UniqueVectorSet.java
 * Package: math.vector
 * Author:  Zachary Gill
 */

package math.vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a set of unique vectors.
 */
public class UniqueVectorSet {
    
    //Fields
    
    /**
     * The map of vectors in the set.
     */
    private Map<String, Vector> vectors = new HashMap<>();
    
    
    //Methods
    
    /**
     * Takes a list of vectors and modifies the list to ensure that the vectors within the list are unique within the set.
     *
     * @param in The list of vectors.
     */
    public void alignVectorsToSet(List<Vector> in) {
        for (int i = 0; i < in.size(); i++) {
            Vector vector = in.get(i);
            String key = getKeyForVector(vector);
            if (vectors.containsKey(key)) {
                in.set(i, vectors.get(key));
            } else {
                vectors.put(key, vector);
            }
        }
    }
    
    /**
     * Produces the key for a vector within the set.
     *
     * @param vector The vector.
     * @return The key for the vector.
     */
    private String getKeyForVector(Vector vector) {
        return vector.getX() + ":" + vector.getY() + ":" + vector.getZ();
    }
    
}
