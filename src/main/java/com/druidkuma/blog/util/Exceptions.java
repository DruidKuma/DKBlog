package com.druidkuma.blog.util;

import java.io.*;
import java.util.Arrays;

/**
 * Utility class for handling exceptions.
 *
 * @author Andreas Kühnel
 */
public class Exceptions {

    private Exceptions() {
    }


    /**
     * Extracts a message for logging from an exception
     */
    public static String getMessage(Throwable exception) {
        return getMessage(exception, null);
    }


    /**
     * Extracts a message for logging from an exception
     */
    public static String getMessage(Throwable exception, String location) {

        if (exception == null) {
            return location;
        }

        // extract the innermost root cause
        Throwable rootCause = getRootCause(exception);
        String rootCauseName = rootCause != null ? Strings.baseName(rootCause.getClass()) : null;
        String message = getLocalizedMessage(exception);

        // prevent adding the root cause several times to the message
        message = Strings.trim(message);
        message = Strings.removeStart(message, rootCauseName);
        message = Strings.trim(message);
        message = Strings.removeStart(message, location);
        message = Strings.trim(message);
        message = Strings.removeStart(message, ":");
        message = Strings.trim(message);

        // combine the root cause, message and location
        return Strings.combine(Strings.combine(rootCauseName, " ", location), ": ", message);
    }


    /**
     * Retrieves the root cause of an exception. If no cause is set, the
     * exception itself is returned.
     */
    public static Throwable getRootCause(Throwable exception) {

        Throwable lastCause = null;
        Throwable cause = exception;
        while (cause != null && cause != lastCause) {
            lastCause = cause;
            cause = cause.getCause();
        }
        return lastCause;
    }


    /**
     * Retrieves the localized message of this exception. If this is some
     * wrapper exception that does not provide any message, the first cause
     * with a message is retrieved and that message is returned.
     */
    public static String getLocalizedMessage(Throwable exception) {

        String result = exception.getLocalizedMessage();
        while (Strings.isEmpty(result)) {
            if (exception.getCause() == null || exception.getCause() == exception) {
                return null;
            }
            exception = exception.getCause();
            result = exception.getLocalizedMessage();
        }
        return result;
    }


    /**
     * Throws an exception with an adjusted stack trace of one level
     */
    public static <E extends Throwable> void adjustedThrow(E exception) throws E {

        exception.fillInStackTrace();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        if (stackTrace != null && stackTrace.length > 2) {
            stackTrace = Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
        }
        exception.setStackTrace(stackTrace);

        throw exception;
    }


    /**
     * Throws an exception with an adjusted stack trace of the given number
     * of levels
     */
    public static <E extends Throwable> void adjustedThrow(E exception, int levels) throws E {

        exception.fillInStackTrace();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        if (stackTrace != null && stackTrace.length > levels + 1) {
            stackTrace = Arrays.copyOfRange(stackTrace, levels + 1, stackTrace.length);
        }
        exception.setStackTrace(stackTrace);

        throw exception;
    }


    /**
     * Extracts the stacktrace as a string
     */
    public static String getStacktrace(Throwable exception) {

        StringWriter writer = new StringWriter(2000);
        exception.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}