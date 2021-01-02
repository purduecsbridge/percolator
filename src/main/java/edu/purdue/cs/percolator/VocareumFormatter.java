package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link VocareumFormatter} class formats grading results
 * and prints them out so Vocareum can assign grades and display
 * the results.
 *
 * @author Kedar Abhyankar
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.2
 */
class VocareumFormatter implements OutputFormatter {

    /**
     * The header displayed before test output.
     */
    private final static String TEST_OUTPUT_HEADER = "TEST CASES\n" +
        "==========";

    /**
     * The header displayed before code style output.
     */
    private final static String CODE_STYLE_HEADER = "CODE STYLE\n" +
        "==========";

    /**
     * Prints the contents of the {@link Grader} object
     * to standard out/error.
     *
     * @param grader the test case grader
     */
    public void printGradingResults(Grader grader) {
        List<GradedTestResult> testCases = grader.getGradedTestResults();

        // Extract Code Style result, if applicable
        GradedTestResult codeStyle = null;
        if (testCases.get(testCases.size() - 1).getName().equals(StyleChecker.TEST_RESULT_NAME)) {
            codeStyle = testCases.get(testCases.size() - 1);
            testCases.remove(codeStyle);
        }

        printGradingReport(testCases, codeStyle);
        printGradingResults(testCases, codeStyle);
    }

    /**
     * Prints the grading report to {@link System#err}.
     *
     * @param testCases the test case results
     * @param codeStyle the code style result
     */
    private static void printGradingReport(List<GradedTestResult> testCases, GradedTestResult codeStyle) {
        System.err.println(TEST_OUTPUT_HEADER);

        // TEST CASES (passed)
        AtomicInteger numTestCasesPassed = new AtomicInteger();
        testCases.stream().filter(GradedTestResult::passed).forEach(r -> {
            if (!r.getNumber().isBlank()) {
                System.err.printf("%s) ", r.getNumber());
            }
            System.err.printf("PASSED: %s\n", r.getName());
            numTestCasesPassed.incrementAndGet();
        });

        if (numTestCasesPassed.get() != 0) {
            System.err.print("\n");
        }

        // TEST CASES (failed)
        AtomicInteger numTestCasesFailed = new AtomicInteger();
        testCases.stream().filter(r -> !r.passed()).forEach(r -> {
            if (!r.getNumber().isBlank()) {
                System.err.printf("%s) ", r.getNumber());
            }
            System.err.printf("FAILED: %s\n", r.getName());
            System.err.println(r.getOutput());
            numTestCasesFailed.incrementAndGet();
        });

        if (numTestCasesFailed.get() == 0) {
            System.err.println("ALL TESTS PASS!");
        } else {
            System.err.printf("%d TEST%s FAILED.\n",
                numTestCasesFailed.get(),
                numTestCasesFailed.get() == 1 ? "" : "S"
            );
        }

        // CODE STYLE
        if (codeStyle != null) {
            System.err.println("\n\n" + CODE_STYLE_HEADER);
            System.err.println(codeStyle.getOutput());
        }
    }

    /**
     * Prints the grading results to {@link System#out}.
     *
     * @param testCases the test case results
     * @param codeStyle the code style result
     */
    private static void printGradingResults(List<GradedTestResult> testCases, GradedTestResult codeStyle) {
        double testCasesScore = testCases.stream()
            .mapToDouble(GradedTestResult::getScore)
            .sum();
        System.out.println("Test Cases," + testCasesScore);

        if (codeStyle != null) {
            System.out.println(StyleChecker.TEST_RESULT_NAME + "," + codeStyle.getScore());
        }
    }

}
