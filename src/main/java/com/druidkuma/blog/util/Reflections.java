package com.druidkuma.blog.util;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reflection-related operations.
 *
 * @author Andreas Kühnel
 */
public class Reflections {

    public static final Map<Class<?>, CachedReflectionData> reflectionCache = Maps.newConcurrentMap();

    private Reflections() {
    }

    /**
     * Retrieves a method call by method name. The method must exist and may not
     * be overloaded with multiple argument types.
     */
    public static Method getMethod(Class<?> clazz, String methodName) {

        CachedReflectionData cachedReflectionData = reflectionCache.get(clazz);
        if (cachedReflectionData == null) {
            cachedReflectionData = new CachedReflectionData();
            reflectionCache.put(clazz, cachedReflectionData);
        }

        Method method = cachedReflectionData.methodsByName.get(methodName);
        if (method == null) {
            method = findMethodByName(clazz, methodName);
            if (method == null) throw new RuntimeException("Method " + clazz.getName() + "." + methodName + "() not found");

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            cachedReflectionData.methodsByName.put(methodName, method);
        }

        return method;
    }


    // ------------------------------------------------------------------------
    // Property and field access
    // ------------------------------------------------------------------------

    /**
     * Retrieves a bean getter method by property name. The getter method must
     * exist in the form get[PropertyName] or is[PropertyName].
     */
    public static Method getGetterMethod(Class<?> clazz, String propertyName) {

        CachedReflectionData cachedReflectionData = reflectionCache.get(clazz);
        if (cachedReflectionData == null) {
            cachedReflectionData = new CachedReflectionData();
            reflectionCache.put(clazz, cachedReflectionData);
        }

        Method method = cachedReflectionData.getterMethods.get(propertyName);
        if (method == null) {
            String propertyMethodName = Strings.capitalize(propertyName);
            Method getMethod = findMethodByName(clazz, "get" + propertyMethodName);
            Method isMethod = findMethodByName(clazz, "is" + propertyMethodName);

            if (getMethod == null && isMethod == null) throw new RuntimeException("No getter method for property " + propertyName + " found in " + clazz);

            method = Beans.either(getMethod, isMethod);

            if (method.getParameterTypes().length != 0) {
                throw new RuntimeException("Method " + clazz.getName()
                        + "." + method.getName() + "() has an unexpected number of parameters");
            }

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            cachedReflectionData.getterMethods.put(propertyName, method);
        }

        return method;
    }

    /**
     * Retrieves a bean setter method by property name. The setter method must
     * exist in the form set[PropertyName]( value ).
     */
    public static Method getSetterMethod(Class<?> clazz, String propertyName) {

        CachedReflectionData cachedReflectionData = reflectionCache.get(clazz);
        if (cachedReflectionData == null) {
            cachedReflectionData = new CachedReflectionData();
            reflectionCache.put(clazz, cachedReflectionData);
        }

        Method method = cachedReflectionData.setterMethods.get(propertyName);
        if (method == null) {
            method = findMethodByName(clazz, "set" + Strings.capitalize(propertyName));

            if (method == null) {
                throw new RuntimeException("No setter method for property "
                        + propertyName + " found in " + clazz);
            }

            if (method.getParameterTypes().length != 1) {
                throw new RuntimeException("Method " + clazz.getName()
                        + "." + method.getName() + "() has an unexpected number of parameters");
            }

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            cachedReflectionData.setterMethods.put(propertyName, method);
        }

        return method;
    }

    /**
     * Retrieves a method by its name. If no such method is found, null is
     * returned. If there are multiple methods with the same name, this will
     * cause an exception.
     */
    public static Method findMethodByName(Class<?> clazz, String methodName) {

        // we need to iterate over all superclasses
        while (clazz != null) {
            Method foundMethod = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (Strings.equals(method.getName(), methodName)) {
                    // check for overloaded methods
                    if (foundMethod != null
                            && !Arrays.equals(method.getParameterTypes(), foundMethod.getParameterTypes())) {
                        throw new RuntimeException("Multiple definitions of method "
                                + clazz.getName() + "." + methodName + "() were found");
                    }
                    foundMethod = method;
                }
            }

            if (foundMethod != null) {
                return foundMethod;
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * Retrieves a list of methods with the given annotation
     */
    public static <T extends Annotation> List<Method> findMethodsWithAnnotation(
            Class<?> clazz, Class<T> annotationClass) {

        List<Method> methods = new ArrayList<>();
        while (clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotationClass)) {
                    methods.add(method);
                }
            }

            clazz = clazz.getSuperclass();
        }

        return methods;
    }

    /**
     * Retrieves a public or private field using reflection
     */
    private static Field findField(Object object, String fieldName) {

        Class<?> clazz = object instanceof Class ? (Class<?>) object : object.getClass();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    if ((field.getModifiers() & Modifier.PUBLIC) == 0) {
                        field.setAccessible(true);
                    }
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }

        throw new RuntimeException("Unknown field " + fieldName + " in " + object.getClass().getName());
    }

    /**
     * Retrieves the value of a field of the given object. This also works for
     * a private field.
     */
    @SneakyThrows
    public static Object getFieldValue(Object object, String fieldName) {
        return findField(object, fieldName).get(object);
    }

    /**
     * Retrieves the value of a field of the given object. This also works for
     * a private field.
     */
    @SneakyThrows
    public static void setFieldValue(Object object, String fieldName, Object value) {
        findField(object, fieldName).set(object, value);
    }

    /**
     * Retrieves the value of a property via its getter method.
     */
    public static Object getProperty(Object object, String propertyName) {

        Method getterMethod = getGetterMethod(object.getClass(), propertyName);
        try {
            return getterMethod.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(Exceptions.getMessage(e,
                    "getting property " + Strings.baseName(object.getClass()) + "." + propertyName), e);
        }
    }

    /**
     * Updates the value of a property via its setter method. For this to work,
     * the setter method may not be overloaded.
     */
    public static Object setProperty(Object object, String propertyName, Object value) {

        Method setterMethod = getSetterMethod(object.getClass(), propertyName);
        try {
            return setterMethod.invoke(object, value);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(Exceptions.getMessage(e,
                    "setting property " + Strings.baseName(object.getClass()) + "." + propertyName), e);
        }
    }

    /**
     * Unwraps any spring proxies of the given object and returns the proxied
     * original object.
     */
    public static Object unwrapSpringProxies(Object object)  {

        for (int i = 0; i < 100; i++) {
            if (object instanceof SpringProxy && object instanceof Advised) {
                try {
                    object = ((Advised) object).getTargetSource().getTarget();
                    continue;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return object;
        }
        throw new RuntimeException("Cannot unwrap proxies for " + object.toString());
    }

    /**
     * Retrieves the class with the given name. If the class is not known, null
     * is returned.
     */
    public static <T> Class<T> getClass(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    // ------------------------------------------------------------------------
    // Other methods
    // ------------------------------------------------------------------------

    /**
     * Creates a new instance of class
     */
    @SneakyThrows
    public static <T> T newInstance(Class<T> clazz) {
        return clazz.newInstance();
    }

    /**
     * Simple record to store all cached reflection data for a class.
     */
    public static class CachedReflectionData {
        Map<String, Method> methodsByName = Maps.newConcurrentMap();
        Map<String, Method> getterMethods = Maps.newConcurrentMap();
        Map<String, Method> setterMethods = Maps.newConcurrentMap();
    }
}