package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Formats an output appropriate for Vocareum.
 *
 * @author kedarabhyankar
 * @version 1.0
 * @since 1.0
 */
class VocareumFormatter implements OutputFormatter {

    /**
     * The filename used by Vocareum for outputting grade information to.
     */
    public static final String OUT_FILE_NAME = "$vocareumGradeFile";

    private static final String FAILURE_HEADER = "TESTS FAILED! SEE BELOW FOR REPORT\n" +
        "===========================================";


    /**
     * Print the grading report to standard output.
     *
     * @param grader the test case grader
     */
    public void printGradingResults(Grader grader) {
        List<GradedTestResult> results = grader.getGradedTestResults();
        if (didFailNonzeroTests(results)) {
            System.out.println(VocareumFormatter.FAILURE_HEADER);
            results.forEach(res -> {
                    if (!res.passed()) {
                        System.out.println(formatGradedItem(res));
                    }
                }
            );
        }
    }

    /**
     * Save the grading output to a specific filename, determined by {@code OUT_FILENAME}.
     *
     * @param grader the test case grader
     */
    public void saveGradingResults(Grader grader) {
        List<GradedTestResult> results = grader.getGradedTestResults();

        if (didFailNonzeroTests(results)) {
            StringBuilder sb = new StringBuilder();
            sb.append(VocareumFormatter.FAILURE_HEADER);
            results.forEach(res -> {
                if (!res.passed()) {
                    sb.append(formatGradedItem(res)).append("\n");
                }
            });

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_FILE_NAME))) {
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Format a graded item in the appropriate format based on the test pass/fail state.
     *
     * @param gtr the {@code GradedTestResult} to print output for
     * @return A formatted {@code String} detailing the test pass/fail message.
     */
    private String formatGradedItem(GradedTestResult gtr) {
        if (gtr.passed()) {
            return gtr.getName() + ": TEST PASSED!";
        } else {
            return gtr.getName() + ": " + gtr.getScore() + " / " +
                gtr.getPoints() + "TEST FAILED. See result below.";
        }
    }

    /**
     * Returns a boolean indicative of if the {@code List<GradedTestResults>} had 1 or more
     * tests that failed.
     *
     * @param gList the {@code List<GradedTestResults>} received from the {@code Grader} object.
     * @return true if there are more than 0 tests that failed, false otherwise.
     */
    private boolean didFailNonzeroTests(List<GradedTestResult> gList) {
        return (int) gList.stream().filter(r -> !r.passed()).count() != 0;
    }

}
