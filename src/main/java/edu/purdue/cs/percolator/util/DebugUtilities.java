package edu.purdue.cs.percolator.util;

import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * The {@link DebugUtilities} class contains methods which
 * display useful information to help instructors and students
 * debug their code when a test case fails. Assignment developers
 * can call these methods from their test cases to fail the current
 * test and display debugging information.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.1
 * @since 1.1
 */
public final class DebugUtilities {

    /**
     * Private default constructor so no objects can be created of this type.
     */
    private DebugUtilities() {
    }

    /**
     * Displays a generic failure message
     * <i>(<code>"Test case testCase failed unexpectedly."</code>)</i> when a test case
     * fails for unknown (or non-disclosed) reasons.
     *
     * @param testCase the name of the test case
     */
    public static void failWithoutCause(String testCase) {
        Assert.fail(String.format("Test case %s failed unexpectedly.", testCase));
    }

    /**
     * Displays the stack trace of an {@link Exception} thrown
     * during testing that is caused by student error.
     *
     * @param e the {@link Exception} thrown during testing
     */
    public static void failWithStackTrace(Throwable e) {
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(error));
        Assert.fail(error.toString());
    }

}
