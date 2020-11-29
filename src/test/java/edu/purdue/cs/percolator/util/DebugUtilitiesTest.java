package edu.purdue.cs.percolator.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link DebugUtilities} class.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.1
 * @since 1.1
 */
public class DebugUtilitiesTest {

    @Test(timeout = 1000)
    public void failWithoutCause_test() {
        final String testCase = "failWithoutCause_test";

        try {
            DebugUtilities.failWithoutCause(testCase);
        } catch (AssertionError e) {
            Assert.assertTrue(e.getMessage().contains(testCase));
            return;
        }

        Assert.fail("failWithoutCause did not throw AssertionError.");
    }

    @Test(timeout = 1000)
    public void failWithStackTrace_test() {
        RuntimeException exception = new RuntimeException("failWithStackTrace_test");

        try {
            DebugUtilities.failWithStackTrace(exception);
        } catch (AssertionError e) {
            Assert.assertTrue(e.getMessage().contains(exception.getMessage()));
            return;
        }

        Assert.fail("failWithStackTrace did not throw AssertionError.");
    }

}
