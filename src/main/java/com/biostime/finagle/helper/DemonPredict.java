package com.biostime.finagle.helper;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**魔鬼预言
 * @author Dylan
 * @time 2016-7-8
 */
public final class DemonPredict {

	private DemonPredict(){}
	
	/**
	 * @param expression
	 * @param message
	 * @param value
	 */
    public static void isTrue(boolean expression, String message, Object value) {
    	 isTrue(expression, new IllegalArgumentException(message + value));
    }

    /**
     * @param expression
     * @param message
     * @param value
     */
    public static void isTrue(boolean expression, String message, long value) {
    	 isTrue(expression, new IllegalArgumentException(message + value));
    }

   /**
    * @param expression
    * @param message
    * @param value
    */
    public static void isTrue(boolean expression, String message, double value) {
        isTrue(expression, new IllegalArgumentException(message + value));
    }

   /**
    * @param expression
    * @param message
    */
    public static void isTrue(boolean expression, String message) {
    	 isTrue(expression, new IllegalArgumentException(message));
    }

   /**
    * @param expression
    */
    public static void isTrue(boolean expression) {
        isTrue(expression, new IllegalArgumentException("The validated expression is false"));
    }
    
    /**
     * @param expression
     */
     public static void isTrue(boolean expression,RuntimeException exception) {
         if (expression == false) {
             throw exception;
         }
     }

    /**
     * @param object
     */
    public static void notNull(Object object) {
        notNull(object, "The validated object is null");
    }

    /**
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        notNull(object, new IllegalArgumentException(message));
    }
    
    /**
     * @param object
     * @param exception
     */
    public static void notNull(Object object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        }
    }

    /**
     * @param array
     * @param message
     */
    public static void notEmpty(Object[] array, String message) {
        notEmpty(array, new IllegalArgumentException(message));
    }

    /**
     * @param array
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "The validated array is empty");
    }
    
    /**
     * @param array
     */
    public static void notEmpty(Object[] array,RuntimeException exception) {
    	 if (array == null || array.length == 0) {
             throw exception;
         }
    }

    /**
     * @param collection
     * @param message
     */
    public static void notEmpty(Collection<Object> collection, String message) {
        notEmpty(collection,new IllegalArgumentException(message));
    }

    /**
     * @param collection
     */
    public static void notEmpty(Collection<Object> collection) {
        notEmpty(collection, "The validated collection is empty");
    }
    
    /**
     * @param collection
     */
    public static void notEmpty(Collection<Object> collection,RuntimeException exception) {
    	 if (collection == null || collection.size() == 0) {
             throw exception;
         }
    }

    /**
     * @param map
     * @param message
     */
    public static void notEmpty(Map<Object,Object> map, String message) {
        notEmpty(map,new IllegalArgumentException(message));
    }
    
    /**
     * @param map
     * @param exception
     */
    public static void notEmpty(Map<Object,Object> map, RuntimeException exception) {
        if (map == null || map.size() == 0) {
            throw exception;
        }
    }

    /**
     * @param map
     */
    public static void notEmpty(Map<Object,Object> map) {
        notEmpty(map, "The validated map is empty");
    }

    /**
     * @param string
     * @param message
     */
    public static void notEmpty(String string, String message) {
        notEmpty(string, new IllegalArgumentException(message));
    }
    
    public static void notEmpty(String string, RuntimeException exception) {
        if (string == null || string.length() == 0) {
            throw exception;
        }
    }

    /**
     * @param string
     */
    public static void notEmpty(String string) {
        notEmpty(string, "The validated string is empty");
    }

    /**
     * @param array
     * @param message
     */
    public static void noNullElements(Object[] array, String message) {
    	noNullElements(array, new IllegalArgumentException(message));
    }
    
    /**
     * @param array
     * @param exception
     */
    public static void noNullElements(Object[] array, RuntimeException exception) {
        notNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw exception;
            }
        }
    }

    /**
     * @param array
     */
    public static void noNullElements(Object[] array) {
        notNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException("The validated array contains null element at index: " + i);
            }
        }
    }

    /**
     * @param collection
     * @param message
     */
    public static void noNullElements(Collection<Object> collection, String message) {
        noNullElements(collection,new IllegalArgumentException(message));
    }
    
    /**
     * @param collection
     * @param exception
     */
    public static void noNullElements(Collection<Object> collection, RuntimeException exception) {
        notNull(collection);
        for (Iterator<Object> it = collection.iterator(); it.hasNext();) {
            if (it.next() == null) {
                throw exception;
            }
        }
    }

    /**
     * @param collection
     */
    public static void noNullElements(Collection<Object> collection) {
        notNull(collection);
        int i = 0;
        for (Iterator<Object> it = collection.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                throw new IllegalArgumentException("The validated collection contains null element at index: " + i);
            }
        }
    }

    /**
     * @param collection
     * @param clazz
     * @param message
     */
    public static <T> void allElementsOfType(Collection<Object> collection, Class<T> clazz, String message) {
        allElementsOfType(collection, clazz, new IllegalArgumentException(message));
    }
    
    /**
     * @param collection
     * @param clazz
     * @param exception
     */
    public static <T> void allElementsOfType(Collection<Object> collection, Class<T> clazz, RuntimeException exception) {
        notNull(collection);
        notNull(clazz);
        for (Iterator<Object> it = collection.iterator(); it.hasNext(); ) {
            if (clazz.isInstance(it.next()) == false) {
                throw exception;
            }
        }
    }

    /**
     * @param collection
     * @param clazz
     */
    public static <T> void allElementsOfType(Collection<Object> collection, Class<T> clazz) {
        notNull(collection);
        notNull(clazz);
        int i = 0;
        for (Iterator<Object> it = collection.iterator(); it.hasNext(); i++) {
            if (clazz.isInstance(it.next()) == false) {
                throw new IllegalArgumentException("The validated collection contains an element not of type "
                    + clazz.getName() + " at index: " + i);
            }
        }
    }
}
