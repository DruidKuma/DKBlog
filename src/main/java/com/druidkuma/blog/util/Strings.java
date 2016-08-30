package com.druidkuma.blog.util;

import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collection;

/**
 * Utility class for string handling functions. This extends StringUtils
 * from Apache commons and adds several own functions, as well as aliases for
 * existing functions.
 *
 * @author Andreas Kühnel
 */
public final class Strings extends org.apache.commons.lang3.StringUtils {
    /**
     * Returns true, if the given strings are not equal
     */
    public static boolean notEquals(CharSequence cs1, CharSequence cs2) {
        return !equals(cs1.toString(), cs2.toString());
    }

    /**
     * Returns an empty string if the given string is null.
     */
    public static String nz(String string) {
        return string != null ? string : EMPTY;
    }

    /**
     * Returns the seconds string if the first given string is null.
     */
    public static String either(String string1, String string2) {
        return string1 != null && string1.length() > 0 ? string1 : string2;
    }


    /**
     * Returns the value of .toString(), or null if the object was null.
     */
    public static String valueOf(Object object) {
        return object != null ? object.toString() : null;
    }


    /**
     * Returns true, if the cmp string equals one of the given values
     */
    public static boolean isOneOf(String cmp, String... values) {
        for (String s : values) {
            if (equals(cmp, s))
                return true;
        }
        return false;
    }


    /**
     * Returns true, if the cmp string equals one of the given values
     */
    public static boolean isOneOfIgnoreCase(String cmp, String... values) {

        for (String s : values) {
            if (equalsIgnoreCase(cmp, s))
                return true;
        }
        return false;
    }


    /**
     * Encodes the given string as HTML
     */
    public static String encodeHtml(String string) {

        return string != null ? HtmlUtils.htmlEscape(string) : "";
    }


    /**
     * Encodes the given string as URL parameter.
     */
    public static String encodeUrl(String string) {

        try {
            return string != null ? URLEncoder.encode(string, "UTF-8") : "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Encodes the given string as URL parameter.
     */
    public static String encodeUrl(String string, String encoding) {

        try {
            return string != null ? URLEncoder.encode(string, encoding) : "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Adds several Strings together, inserting a separator where necessary
     */
    public static String combine(Collection<String> strings, String separator) {
        if (strings == null || strings.size() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (String str : strings) {
            if (str != null && str.length() > 0) {
                if (result.length() > 0) {
                    result.append(separator);
                }
                result.append(str);
            }
        }

        return result.toString();
    }

    /**
     * Adds several Strings together, inserting a separator where necessary
     */
    public static String combineMany(String separator, String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (String str : strings) {
            if (str != null && str.length() > 0) {
                if (result.length() > 0) {
                    result.append(separator);
                }
                result.append(str);
            }
        }

        return result.toString();
    }

    /**
     * Returns the substring after the first occurence of the separator. Works
     * like {@link Strings#substringAfter(String, String)}. However, if nothing
     * is found, the original string is returned.
     */
    public static String substringAfterAny(String str, String separator) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(pos + separator.length());
    }


    /**
     * Returns the substring after the last occurence of the separator. Works
     * like {@link Strings#substringAfter(String, String)}. However, if nothing
     * is found, the original string is returned.
     */
    public static String substringAfterLastAny(String str, String separator) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(pos + separator.length());
    }


    /**
     * Adds two strings together, inserting a separator if both strings are not
     * empty.
     */
    public static String combine(String str1, String separator, String str2) {
        if (str1 == null || str1.length() == 0) {
            return str2;
        }
        if (str2 == null || str2.length() == 0) {
            return str1;
        }
        return separator != null ? str1 + separator + str2
                : str1 + str2;
    }

    /**
     * Returns the basename of a class name.
     */
    public static String baseName(Class<?> clazz) {
        if (clazz != null) {
            String name = clazz.getName();
            int dotPos = name.lastIndexOf('.');
            int dollarPos = name.lastIndexOf('$');
            return name.substring(Math.max(dotPos, dollarPos) + 1);
        }
        return null;
    }


    /**
     * Returns the basename of a class name.
     */
    public static String baseName(String className) {
        if (className != null) {
            int dotPos = className.lastIndexOf('.');
            int dollarPos = className.lastIndexOf('$');
            return className.substring(Math.max(dotPos, dollarPos) + 1);
        }
        return null;
    }


    /**
     * Returns an Integer parsed from a string or null, if it is not an integer value
     */
    public static Integer parseInteger(String str) {
        if (str != null && str.length() > 0) {
            try {
                return Integer.parseInt(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }


    /**
     * Returns an int parsed from a string or the default, if it is not an integer value
     */
    public static int parseInteger(String str, int defaultValue) {
        if (str != null && str.length() > 0) {
            try {
                return Integer.parseInt(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }


    /**
     * Returns a Long parsed from a string or null, if it is not an integer value
     */
    public static Long parseLong(String str) {
        if (str != null && str.length() > 0) {
            try {
                return Long.parseLong(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }


    /**
     * Returns a long parsed from a string or the default, if it is not an integer value
     */
    public static long parseLong(String str, long defaultValue) {
        if (str != null && str.length() > 0) {
            try {
                return Long.parseLong(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }


    /**
     * Returns a Double parsed from a string or null, if it is not a double value
     */
    public static Double parseDouble(String str) {
        if (str != null && str.length() > 0) {
            try {
                return Double.parseDouble(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }


    /**
     * Returns a double parsed from a string or the default, if it is not a double value
     */
    public static double parseDouble(String str, double defaultValue) {
        if (str != null && str.length() > 0) {
            try {
                return Double.parseDouble(str.trim());
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }


    /**
     * Returns a Boolean parsed from a string or null, if it is not a boolean value
     */
    public static Boolean parseBoolean(String str) {
        if (str != null && str.length() > 0) {
            return Boolean.parseBoolean(str.trim());
        }
        return null;
    }


    /**
     * Returns a boolean parsed from a string or null, if it is not a boolean value
     */
    public static boolean parseBoolean(String str, boolean defaultValue) {
        if (str != null && str.length() > 0) {
            return Boolean.parseBoolean(str.trim());
        }
        return defaultValue;
    }


    /**
     * Formats a message using {@link MessageFormat}. The difference to
     * MessageFormat is that all numbers are displayed without grouping.
     */
    public static String formatMessage(String message, Object... params) {

        if (isEmpty(message) || params == null || params.length == 0) {
            return message;
        }

        // build a new parameter object with converted
        Object[] stringParams = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            stringParams[i] = Strings.valueOf(params[i]);
        }

        return MessageFormat.format(message, stringParams);
    }
}