package com.druidkuma.blog.util;

import com.google.common.collect.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * General utility class for handling Java objects
 */
public final class Beans {

    private static Random rand = new Random();

    /**
     * Compares if two beans are equal. Takes null values into account
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == o2 || !(o1 == null || o2 == null) && o1.equals(o2);
    }


    /**
     * Compares if two beans are not equal. Takes null values into account
     */
    public static boolean notEquals(Object o1, Object o2) {

        if (o1 == o2) {
            return false;
        }
        if (o1 == null || o2 == null) {
            return true;
        }

        return !o1.equals(o2);
    }

    public static <T> T deepCopy(T object) {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            // serialize and pass the object
            oos.writeObject(object);
            oos.flush();
            ByteArrayInputStream bin =
                    new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            // return the new object
            return (T) ois.readObject();
        } catch(Exception e) {
            throw new RuntimeException(Exceptions.getMessage(e,
                    "cloning object of " + object.getClass()));
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(ois);
        }
    }


    /**
     * Retrieves the hashCode of an object, or 0 if the object was null
     */
    public static int hashCode(Object object) {
        return object == null ? 0 : object.hashCode();
    }

    /**
     * Returns either the first object, or if that is null, the second one
     */
    public static <T> T either(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }

    /**
     * Returns either the first object, or if that is null, the second one, if
     * that is null, the third one
     */
    public static <T> T either(T o1, T o2, T o3) {
        return o1 != null ? o1 : o2 != null ? o2 : o3;
    }

    /**
     * Utility method for accessing null Doubles
     */
    public static double nz(Double d) {
        return d != null ? d : 0;
    }

    /**
     * Utility method for accessing null Integers
     */
    public static int nz(Integer i) {
        return i != null ? i : 0;
    }

    /**
     * Utility method for accessing null Longs
     */
    public static long nz(Long l) {
        return l != null ? l : 0;
    }

    /**
     * Utility method for accessing null Floats
     */
    public static float nz(Float f) {
        return f != null ? f : 0f;
    }

    /**
     * Utility method for accessing null Booleans
     */
    public static boolean nz(Boolean b) {
        return b != null ? b : false;
    }

    /**
     * Utility method for iterating over null Lists
     */
    public static <T> List<T> nz(List<T> list) {
        return list != null ? list : Collections.<T>emptyList();
    }

    /**
     * Utility method for iterating over null Sets
     */
    public static <T> Set<T> nz(Set<T> set) {
        return set != null ? set : Collections.<T>emptySet();
    }

    /**
     * Utility method for iterating over null Collections
     */
    public static <T> Collection<T> nz(Collection<T> list) {
        return list != null ? list : Collections.<T>emptyList();
    }

    /**
     * Utility method for iterating over null Maps
     */
    public static <K, V> Map<K, V> nz(Map<K, V> map) {
        return map != null ? map : Collections.<K, V>emptyMap();
    }

    /**
     * Retrieves an element from a list, avoiding IndexOutOfBoundsExceptions
     */
    public static <T> T elementAt(List<T> list, int index) {
        return list == null || index < 0 || index >= list.size() ? null : list.get(index);
    }

    /**
     * Retrieves an element from an array, avoiding IndexOutOfBoundsExceptions
     */
    public static <T> T elementAt(T[] array, int index) {
        return array == null || index < 0 || index >= array.length ? null : array[index];
    }

    /**
     * Retrieves the first element from a list, avoiding IndexOutOfBoundsExceptions
     */
    public static <T> T first(List<T> list) {
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * Retrieves the first element from a set
     */
    public static <T> T first(Set<T> set) {
        return set == null || set.isEmpty() ? null : set.iterator().next();
    }

    /**
     * Retrieves the first element from a collection
     */
    public static <T> T first(Collection<T> collection) {
        return collection == null || collection.isEmpty() ? null : collection.iterator().next();
    }

    /**
     * Retrieves the first entry from a map
     */
    public static <K, V> Entry<K, V> first(Map<K, V> map) {
        return map == null || map.isEmpty() ? null : map.entrySet().iterator().next();
    }

    /**
     * Retrieves the first key from a map
     */
    public static <K, V> K firstKey(Map<K, V> map) {
        return map == null || map.isEmpty() ? null : map.keySet().iterator().next();
    }

    /**
     * Retrieves the first entry from a map
     */
    public static <K, V> V firstValue(Map<K, V> map) {
        return map == null || map.isEmpty() ? null : map.values().iterator().next();
    }

    /**
     * Retrieves the first element from an array, avoiding IndexOutOfBoundsExceptions
     */
    public static <T> T first(T[] array) {
        return array == null || array.length == 0 ? null : array[0];
    }

    /**
     * Converts a list of key value pairs to a map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(Object... values) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(values.length / 2);
        for (int i = 0; i < values.length; i += 2) {
            map.put((K) values[i], (V) values[i + 1]);
        }
        return map;
    }

    /**
     * Utility method for creating a set
     */
    public static <T> Set<T> toSet(T... array) {
        return new LinkedHashSet<T>(Arrays.asList(array));
    }

    /**
     * Utility method for creating a list
     */
    public static <T> List<T> toList(T... array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Returns true, if a collection is empty
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Returns true, if a collection is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Returns true, if an array is empty
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Returns true, if a collection is not empty
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return coll != null && !coll.isEmpty();
    }

    /**
     * Returns true, if a map is not empty
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Returns true, if a map is not empty
     */
    public static boolean isNotEmpty(Multimap<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Returns true, if an array is not empty
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length != 0;
    }

    /**
     * Returns true, if the given obj equals one of the given values
     */
    public static <T> boolean isOneOf(T obj, T... values) {

        for (T cmp : values) {
            if (equals(obj, cmp)) return true;
        }
        return false;
    }

    /**
     * Returns true, if the obj does not equals one of the given values
     */
    public static <T> boolean isNoneOf(T obj, T... values) {

        for (T cmp : values) {
            if (equals(obj, cmp)) return false;
        }
        return true;
    }


    /**
     * Builds a simple string representation using the objects fields
     */
    public static String toString(Object object) {
        return ReflectionToStringBuilder.toString(object);
    }


    /**
     * Returns a clone of the given object, or null if the object was null.
     * This method is guaranteed to return a deep copy for Lists, Sets, Maps
     * and Arrays.
     * <p>
     * Note that this method cannot know if the called clone() method returns
     * a deep or a shallow copy of the elements.
     * <p>
     * No steps are taken to keep track of object identity, a list of 3 times
     * the same object will be cloned to a list of 3 different objects with the
     * same content.
     * <p>
     * Therefore, it is the callers responsibility to make sure that the object
     * that is cloned does not contain cycles. For example, a list containing
     * itself should not be cloned with this method.
     */
    @SuppressWarnings("rawtypes")
    public static <T> T clone(T object) {

        if (object == null) {
            return null;
        }

        try {
            // deep clone for lists and sets
            if (object instanceof Collection) {
                Collection copy = (Collection) object.getClass().newInstance();
                for (Object entry : (Collection) object) {
                    copy.add(cloneIfPossible(entry));
                }

                return (T) copy;
            }

            // deep clone for lists
            if (object instanceof Map) {
                Map copy = (Map) object.getClass().newInstance();
                for (Entry entry : (Set<Entry>) ((Map) object).entrySet()) {
                    Object key = cloneIfPossible(entry.getKey());
                    Object value = cloneIfPossible(entry.getValue());

                    copy.put(key, value);
                }

                return (T) copy;
            }

            // deep clone for arrays
            if (object instanceof Object[]) {
                Object[] copy = ((Object[]) object).clone();

                for (int i = 0; i < copy.length; i++) {
                    if (copy[i] instanceof Cloneable) {
                        copy[i] = Beans.clone(copy[i]);
                    }
                }

                return (T) copy;
            }

            if (object instanceof Multimap) {
                Multimap clone;
                if (object instanceof ArrayListMultimap) {
                    clone = ArrayListMultimap.create();
                } else if (object instanceof HashMultimap) {
                    clone = HashMultimap.create();
                } else if (object instanceof LinkedHashMultimap) {
                    clone = LinkedHashMultimap.create();
                } else if (object instanceof LinkedListMultimap) {
                    clone = LinkedListMultimap.create();
                } else if (object instanceof TreeMultimap) {
                    clone = TreeMultimap.create();
                } else {
                    throw new RuntimeException("clone not supported for " + object.getClass().getName());
                }

                //noinspection unchecked
                for (Map.Entry entry : (Collection<Entry>) ((Multimap) object).entries()) {
                    Object key = cloneIfPossible(entry.getKey());
                    Object value = cloneIfPossible(entry.getValue());
                    //noinspection unchecked
                    clone.put(key, value);
                }
                //noinspection unchecked
                return (T) clone;
            }


            Method cloneMethod = Reflections.getMethod(object.getClass(), "clone");
            return (T) cloneMethod.invoke(object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(Exceptions.getMessage(e,
                    "cloning object of " + object.getClass()));
        }
    }


    /**
     * Returns a clone of the given object if it implements {@link Cloneable},
     * otherwise it returns the object itself
     */
    public static <T> T cloneIfPossible(T object) {
        return object instanceof Cloneable ? Beans.clone(object) : object;
    }


    /**
     * Sleeps for a given amount of milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignore
        }
    }


    /**
     * Sleeps for a random amount of milliseconds in the given range
     */
    public static void sleep(int minMilliseconds, int maxMilliseconds) {
        try {
            int sleepTime = minMilliseconds + rand.nextInt(maxMilliseconds - minMilliseconds);
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            // ignore
        }
    }


    /**
     * Takes a collections of objects and builds a map, using the values of a
     * given property as the map key.
     */
    @SuppressWarnings("null")
    public static <K, T> Map<K, T> mapByProperty(Collection<T> values, String propertyName) {

        if (values == null) {
            return null;
        }

        Class<?> lastClass = null;
        Method getterMethod = null;

        Map<K, T> result = new LinkedHashMap<>(values.size());
        for (T entry : values) {

            if (entry == null) {
                result.put(null, null);
                continue;
            }

            try {
                // prevent subsequent reflection lookup for the same getter method
                Class<?> entryClass = entry.getClass();
                if (entryClass != lastClass) {
                    lastClass = entryClass;
                    getterMethod = Reflections.getGetterMethod(entryClass, propertyName);
                }

                K key = (K) getterMethod.invoke(entry);
                result.put(key, entry);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(Exceptions.getMessage(e,
                        "getting property " + Strings.baseName(entry.getClass()) + "." + propertyName), e);
            }
        }

        return result;
    }


    /**
     * Takes a collections of objects and groups them into a map of lists,
     * using the values of a given property as the map key.
     */
    @SuppressWarnings("null")
    public static <K, T> Map<K, List<T>> groupByProperty(Collection<T> values, String propertyName) {

        if (values == null) {
            return null;
        }

        Class<?> lastClass = null;
        Method getterMethod = null;

        Map<K, List<T>> result = new LinkedHashMap<>();
        for (T entry : values) {

            if (entry == null) {
                List<T> list = result.get(null);
                if (list == null) {
                    list = new ArrayList<T>();
                    result.put(null, list);
                }
                list.add(null);
                continue;
            }

            try {
                // prevent subsequent reflection lookup for the same getter method
                Class<?> entryClass = entry.getClass();
                if (entryClass != lastClass) {
                    lastClass = entryClass;
                    getterMethod = Reflections.getGetterMethod(entryClass, propertyName);
                }

                K key = (K) getterMethod.invoke(entry);

                List<T> list = result.get(key);
                if (list == null) {
                    list = new ArrayList<T>();
                    result.put(key, list);
                }
                list.add(entry);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(Exceptions.getMessage(e,
                        "getting property " + Strings.baseName(entry.getClass()) + "." + propertyName), e);
            }
        }

        return result;
    }


}