package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * The {@link TestCaseListener} class is adapted from the
 * {@link com.github.tkutcher.jgrade.gradedtest.GradedTestListener} from Tim Kutcher's JGrade.
 * The listener attaches to a {@link org.junit.runner.JUnitCore} runner and stores the results
 * of the test runs, along with the metadata from the {@link TestCase} annotation.
 * <p>
 * One important distinction from {@link com.github.tkutcher.jgrade.gradedtest.GradedTestListener}
 * is that this listener <em>is</em> thread-safe, allowing the use of {@link org.junit.experimental.ParallelComputer}.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
 * @since 1.0
 */
class TestCaseListener extends RunListener {

    private final static PrintStream ORIGINAL_OUT = System.out;

    /**
     * List of test results to pass to Gradescope.
     */
    private final Map<String, GradedTestResult> testResults;

    /**
     * The output of the current test being ran.
     */
    private final Map<String, ByteArrayOutputStream> testOutputs;

    /**
     * The maximum score for the assignment.
     */
    private final double maxScore;

    /**
     * Creates a new {@link TestCaseListener} object.
     *
     * @param maxScore the maximum score to scale the test cases to
     */
    TestCaseListener(double maxScore) {
        this.testResults = new HashMap<>();
        this.testOutputs = new HashMap<>();
        this.maxScore = maxScore;
    }

    /**
     * Scales the test results to the maxScore and
     * returns the list of test results.
     *
     * @return the list of test results
     */
    Collection<GradedTestResult> getTestResults() {
        scaleTestCases();
        return this.testResults.values();
    }

    /**
     * Called when a JUnit test is about to start. Reads test case metadata and sets up the
     * testing environment.
     *
     * @param description the {@link Description} object given by JUnit
     */
    @Override
    public void testStarted(Description description) {
        final String testKey = description.getDisplayName();
        TestCase testCase = description.getAnnotation(TestCase.class);
        if (testCase != null) {
            this.testResults.put(testKey, new GradedTestResult(
                testCase.name(),
                testCase.group(),
                testCase.points(),
                testCase.visibility().toString()
            ));
            this.testResults.get(testKey).setScore(testCase.points());
        }

        this.testOutputs.put(testKey, new ByteArrayOutputStream());
        System.setOut(new PrintStream(this.testOutputs.get(testKey)));
    }

    /**
     * Called when a JUnit test has finished. Adds the test result and cleans up the
     * testing environment.
     *
     * @param description the {@link Description} object given by JUnit
     */
    @Override
    public void testFinished(Description description) {
        final String testKey = description.getDisplayName();
        final String testOutput = this.testOutputs.get(testKey).toString();
        if (this.testResults.containsKey(testKey)) {
            this.testResults.get(testKey).addOutput(testOutput);
        }

        System.setOut(ORIGINAL_OUT);
    }

    /**
     * Called when a JUnit test has failed. Sets the score to 0 and gives test output.
     *
     * @param failure the {@link Failure} object given by JUnit
     */
    @Override
    public void testFailure(Failure failure) {
        final String testKey = failure.getDescription().getDisplayName();

        // This is a setup/teardown method failure
        if (failure.getDescription().isSuite()) {
            String testSuiteName = failure.getDescription().getTestClass().getSimpleName();
            GradedTestResult result = new GradedTestResult(
                "INFO: " + testSuiteName,
                "",
                0.0,
                TestCase.Visibility.VISIBLE.toString()
            );
            result.addOutput(failure.getMessage() + "\n");
            this.testResults.put(testKey, result);
        } else {
            if (this.testResults.containsKey(testKey)) {
                GradedTestResult result = this.testResults.get(testKey);
                result.setScore(0);
                result.addOutput("TEST FAILED:\n");

                if (failure.getMessage() != null) {
                    result.addOutput(failure.getMessage());
                } else {
                    result.addOutput("No description provided.");
                }

                result.addOutput("\n");
                result.setPassed(false);
            }
        }
    }

    /**
     * Scales the test cases to the {@link TestCaseListener#maxScore}.
     * Called when the test results are requested.
     */
    void scaleTestCases() {
        final double total = this.testResults.values().stream().mapToDouble(GradedTestResult::getPoints).sum();
        final double ratio = total == 0 ? 0 : maxScore / total;
        this.testResults.values().forEach(r -> {
            r.setPoints(r.getPoints() * ratio);
            r.setScore(r.getScore() * ratio);
        });
    }

}
