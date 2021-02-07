/*
 * File:    ListUtility.java
 * Package: commons.list
 * Author:  Zachary Gill
 */

package commons.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import commons.math.BoundUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource class that provides additional list functionality.
 */
public final class ListUtility {
    
    //Logger
    
    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ListUtility.class);
    
    
    //Functions
    
    /**
     * Converts an array to a list.
     *
     * @param array The array.
     * @param <T>   The type of the array.
     * @return The list built from the array.
     */
    public static <T> List<T> arrayToList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
    
    /**
     * Splits a list into a list of lists of a certain length.
     *
     * @param list   The list.
     * @param length The length of the resulting lists.
     * @param <T>    The type of the list.
     * @return The list of lists of the specified length.
     */
    public static <T> List<List<T>> split(List<T> list, int length) {
        length = BoundUtility.truncateNum(length, 1, list.size()).intValue();
        
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < (int) Math.ceil(list.size() / (double) length); i++) {
            result.add(new ArrayList<>());
        }
        
        for (int i = 0; i < list.size(); i++) {
            result.get(i / length).add(list.get(i));
        }
        return result;
    }
    
    /**
     * Determines if any element in a list is null.
     *
     * @param list The list.
     * @return Whether or not any element in the list is null.
     */
    public static boolean anyNull(List<Object> list) {
        for (Object entry : list) {
            if (entry == null) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Determines if any element in the list is null.
     *
     * @param list The list.
     * @return Whether or not any element in the list is null.
     * @see #anyNull(List)
     */
    public static boolean anyNull(Object... list) {
        return anyNull(Arrays.asList(list));
    }
    
    /**
     * Removes duplicate elements from a list.
     *
     * @param list The list to operate on.
     * @param <T>  The type of the list.
     * @return The list with duplicate elements removed.
     */
    public static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * Selects a random element from a list.
     *
     * @param list The list to select from.
     * @param <T>  The type of the list.
     * @return The random element from the list.
     */
    public static <T> T selectRandom(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        
        return selectNFromList(list, 1).get(0);
    }
    
    /**
     * Selects a random subset of a list.
     *
     * @param list The list to select from.
     * @param n    The number of elements to select.
     * @param <T>  The type of the list.
     * @return The random subset of the list.
     */
    @SuppressWarnings({"UnsecureRandomNumberGeneration", "StatementWithEmptyBody"})
    public static <T> List<T> selectNFromList(List<T> list, int n) {
        if (n >= list.size()) {
            return list;
        }
        
        List<Integer> previousChoices = new ArrayList<>();
        List<T> choices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int index;
            while (previousChoices.contains(index = (int) (Math.random() * (list.size() - 1)))) {
            }
            choices.add(list.get(index));
            previousChoices.add(index);
        }
        return choices;
    }
    
    /**
     * Copies a list to the end of itself a number of times making a list of n times the original length.
     *
     * @param list  The list to duplicate.
     * @param times The number of copies of the list to add.
     * @param <T>   The type of the list.
     * @return A list of double size with duplicated elements.
     */
    public static <T> List<T> duplicateListInOrder(List<T> list, int times) {
        if (times <= 0) {
            return new ArrayList<>();
        }
        
        List<T> finalList = new ArrayList<>(list.size() * times);
        for (int t = 0; t < times; t++) {
            finalList.addAll(list);
        }
        return finalList;
    }
    
    /**
     * Copies a list to the end of itself making a list of double size.
     *
     * @param list The list to duplicate.
     * @param <T>  The type of the list.
     * @return A list of double size with duplicated elements.
     * @see #duplicateListInOrder(List, int)
     */
    public static <T> List<T> duplicateListInOrder(List<T> list) {
        return duplicateListInOrder(list, 2);
    }
    
    /**
     * Sorts a list by the number of occurrences of each entry in the list and returning the list.
     *
     * @param list    The list to sort.
     * @param reverse Whether to sort in reverse or not.
     * @param <T>     The type of the list.
     * @return A list of sorted by the number of occurrences of each entry in the list.
     */
    public static <T> List<T> sortListByNumberOfOccurrences(List<T> list, boolean reverse) {
        Map<T, Integer> store = new LinkedHashMap<>();
        for (T entry : list) {
            if (store.containsKey(entry)) {
                store.replace(entry, store.get(entry) + 1);
            } else {
                store.put(entry, 1);
            }
        }
        
        List<T> sorted = new ArrayList<>();
        if (reverse) {
            store.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
                for (int i = 0; i < e.getValue(); i++) {
                    sorted.add(e.getKey());
                }
            });
        } else {
            store.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(e -> {
                for (int i = 0; i < e.getValue(); i++) {
                    sorted.add(e.getKey());
                }
            });
        }
        return sorted;
    }
    
    /**
     * Sorts a list by the number of occurrences of each entry in the list and returning the list.
     *
     * @param list The list to sort.
     * @param <T>  The type of the list.
     * @return A list of sorted by the number of occurrences of each entry in the list.
     * @see #sortListByNumberOfOccurrences(List, boolean)
     */
    public static <T> List<T> sortListByNumberOfOccurrences(List<T> list) {
        return sortListByNumberOfOccurrences(list, false);
    }
    
}
