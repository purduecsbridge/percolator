package edu.purdue.cs.percolator.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link StringUtilities} class.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
 * @since 1.0
 */
public class StringUtilitiesTest {

    @Test(timeout = 1000)
    public void fuzzyEquals_test() {
        final String s1 = "HeLlO\tWoRlD!!\n";
        final String s2 = "helloworld!!";
        final String s3 = "hello world";

        Assert.assertTrue(StringUtilities.fuzzyEquals(s1, s2));
        Assert.assertFalse(StringUtilities.fuzzyEquals(s2, s3));
    }

    @Test(timeout = 1000)
    public void assertFuzzyEquals_testPass() {
        final String s1 = "Hello   !";
        final String s2 = "Hello!";

        try {
            StringUtilities.assertFuzzyEquals(s1, s2);
        } catch (AssertionError e) {
            Assert.fail();
        }
    }

    @Test(timeout = 1000)
    public void assertFuzzyEquals_testFailWithMessage() {
        final String s1 = "Hello";
        final String s2 = "Goodbye";
        final String message = "Make sure that your output matches.";

        try {
            StringUtilities.assertFuzzyEquals(message, s1, s2);
        } catch (AssertionError e) {
            Assert.assertTrue(e.getMessage().startsWith(message));
            return;
        }

        Assert.fail("assertFuzzyEquals did not throw AssertionError.");
    }

    @Test(timeout = 1000)
    public void normalizeLineEndings_test() {
        final String input = "Hello\r\nWorld";
        final String expected = String.format("Hello%sWorld", System.lineSeparator());
        final String actual = StringUtilities.normalizeLineEndings(input);

        Assert.assertEquals(expected, actual);
    }

    @Test(timeout = 1000)
    public void removeAllWhitespace_test() {
        final String input = "H\te\r\nl l\fo";
        final String expected = "Hello";
        final String actual = StringUtilities.removeAllWhitespace(input);

        Assert.assertEquals(expected, actual);
    }

}
