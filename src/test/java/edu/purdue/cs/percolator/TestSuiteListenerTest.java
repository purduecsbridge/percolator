package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Tests the {@link TestCaseListener} class.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
 * @since 1.0
 */
public class TestSuiteListenerTest {

    @Test(timeout = 1000)
    @SuppressWarnings("unchecked")
    public void scaleTestCases_testSimple() {
        final int maxScore = 100;
        TestCaseListener listener = new TestCaseListener(maxScore);

        Field testResultsField;
        try {
            testResultsField = listener.getClass().getDeclaredField("testResults");
            testResultsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Map<String, GradedTestResult> testResults;
        try {
            testResults = (Map<String, GradedTestResult>) testResultsField.get(listener);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 4; i++) {
            testResults.put(String.valueOf(i), new GradedTestResult("", "", 5, "visible"));
            testResults.get(String.valueOf(i)).setScore(5);
        }

        listener.scaleTestCases();

        boolean testsScaled = testResults.values().stream().allMatch(r -> r.getPoints() == 25 && r.getScore() == 25);
        double total = testResults.values().stream().mapToDouble(GradedTestResult::getPoints).sum();

        Assert.assertTrue(testsScaled);
        Assert.assertEquals(maxScore, total, 0.0);
    }

    @Test(timeout = 1000)
    @SuppressWarnings("unchecked")
    public void scaleTestCases_testRepeatingRemainder() {
        final int maxScore = 100;
        TestCaseListener listener = new TestCaseListener(maxScore);

        Field testResultsField;
        try {
            testResultsField = listener.getClass().getDeclaredField("testResults");
            testResultsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Map<String, GradedTestResult> testResults;
        try {
            testResults = (Map<String, GradedTestResult>) testResultsField.get(listener);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 3; i++) {
            testResults.put(String.valueOf(i), new GradedTestResult("", "", 1, "visible"));
            testResults.get(String.valueOf(i)).setScore(1);
        }

        listener.scaleTestCases();

        double total = testResults.values().stream().mapToDouble(GradedTestResult::getPoints).sum();

        Assert.assertEquals(maxScore, total, 0.0);
    }

}
