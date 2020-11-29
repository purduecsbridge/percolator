package edu.purdue.cs.percolator.util;

import org.junit.Assert;

/**
 * The {@link StringUtilities} class contains useful tools for testing {@link String}s.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.1
 * @since 1.0
 */
public final class StringUtilities {

    /**
     * Private default constructor so no objects can be created of this type.
     */
    private StringUtilities() {
    }

    /**
     * Tests if two {@link String}s are "fuzzy-equal"
     * (i.e., ignoring all whitespace and capitalization).
     *
     * @param s1 the first {@link String}
     * @param s2 the second {@link String}
     * @return true if they are "fuzzy equal", false otherwise
     */
    public static boolean fuzzyEquals(String s1, String s2) {
        try {
            assertFuzzyEquals(s1, s2);
        } catch (AssertionError e) {
            return false;
        }

        return true;
    }

    /**
     * Asserts that two {@link String}s are "fuzzy-equal"
     * (i.e., ignoring all whitespace and capitalization).
     *
     * @param message the assertion message to display
     * @param s1      the first {@link String}
     * @param s2      the second {@link String}
     * @throws AssertionError if the {@link String}s are not "fuzzy-equal"
     */
    public static void assertFuzzyEquals(String message, String s1, String s2) throws AssertionError {
        s1 = removeAllWhitespace(s1).toLowerCase();
        s2 = removeAllWhitespace(s2).toLowerCase();
        Assert.assertEquals(message, s1, s2);
    }

    /**
     * Asserts that two {@link String}s are "fuzzy-equal"
     * (i.e., ignoring all whitespace and capitalization).
     *
     * @param s1 the first {@link String}
     * @param s2 the second {@link String}
     * @throws AssertionError if the {@link String}s are not "fuzzy-equal"
     */
    public static void assertFuzzyEquals(String s1, String s2) throws AssertionError {
        assertFuzzyEquals(null, s1, s2);
    }

    /**
     * Normalizes the line endings for a {@link String}.
     *
     * @param s the {@link String} to normalize
     * @return a {@link String} with only the current system's line endings
     */
    public static String normalizeLineEndings(String s) {
        return s.replaceAll("\\n|\\r\\n", System.lineSeparator());
    }

    /**
     * Removes all whitespace from a {@link String}.
     *
     * @param s the {@link String} to remove whitespace from
     * @return a {@link String} with no whitespace
     */
    public static String removeAllWhitespace(String s) {
        return s.replaceAll("\\s", "");
    }

}
