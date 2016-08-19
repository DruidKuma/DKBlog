package com.druidkuma.blog.util;

import org.apache.commons.lang.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class NormalizationUtil {

    /**
     * key pattern contains alphanumeric characters and some special characters that are need to build unique keys
     * <p>
     * IMPORTANT: the name keys are also used in solr, and there are some special characters used as delimiters,
     * so make sure that the name key doesn't contain any special characters that are used by solr (like at the moment '.')!
     */
    private static final Pattern ALPHANUMERIC_KEY_PATTERN = Pattern.compile("[^\\p{L}\\p{N}_-]");

    private static final Pattern ALPHANUMERIC_URL_KEY_PATTERN = Pattern.compile("[^\\p{L}\\p{N}_]");
    
    /**
     * only alphanumeric characters
     */
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("[^\\p{L}\\p{N}]");

    /**
     * normalizes a string to use it as a name key: use lower case and replace special characters with '_'
     * (e.g. "CHENG SHIN" -> "cheng_shin", "4x4 Geländewagen" -> "4x4_gela_ndewagen")
     *
     * <p>IMPORTANT: the name keys are also used in solr, and there are some special characters used as delimiters,
     * so make sure that the name key doesn't contain any special characters that are used by solr (like at the moment '.')!
     *
     * @param unnormalizedString
     * @return normalizedString
     */
    public static String normalizeNameKey(String unnormalizedString) {
        // decompose special characters
        String normalizedString = Normalizer.normalize(unnormalizedString, Normalizer.Form.NFD).toLowerCase();
        // replace all non alpha numeric characters
        return ALPHANUMERIC_KEY_PATTERN.matcher(normalizedString).replaceAll("_");
    }
    
    /**
     * normalizes a string to use it as a name key: use lower case and replace special characters with '_', including '-'
     * (e.g. "CHENG SHIN" -> "cheng_shin", "4x4 Geländewagen" -> "4x4_gela_ndewagen")
     *
     * <p>IMPORTANT: the name keys are also used in solr, and there are some special characters used as delimiters,
     * so make sure that the name key doesn't contain any special characters that are used by solr (like at the moment '.')!
     *
     * @param unnormalizedString
     * @return normalizedString
     */
    public static String normalizeUrlNameKey(String unnormalizedString) {
        // decompose special characters
        String normalizedString = Normalizer.normalize(unnormalizedString, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        // replace all non alpha numeric characters
        String replacedPunctuation = ALPHANUMERIC_URL_KEY_PATTERN.matcher(normalizedString).replaceAll("_");

        return StringUtils.strip(replacedPunctuation, "_").replaceAll("_+", "_");
    }

    /**
     * normalizes a name to be able to recognize different spelling variants during mapping: <br>
     * - lower case <br>
     * - remove all non-alphanumeric characters, even whitespaces! <br>
     * 
     * e.g. "CHENG SHIN" -> "chengshin"
     * 
     * @param unnormalizedString
     * @return normalizedString
     */
    public static String normalizeName(String unnormalizedString) {
        // decompose special characters
        String normalizedString = Normalizer.normalize(unnormalizedString, Normalizer.Form.NFD).toLowerCase();
        // replace all non alpha numeric characters
        return ALPHANUMERIC_PATTERN.matcher(normalizedString).replaceAll("");
    }
}