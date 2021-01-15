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


    /**
     * Print the grading report to standard output.
     *
     * @param grader the test case grader
     */
    @Override
    public void printGradingResults(Grader grader) {
        List<GradedTestResult> gradedRes = grader.getGradedTestResults();
        int numTestsFailed = (int) gradedRes.stream().filter(r -> !r.passed()).count();
        if (numTestsFailed == 0) {
            System.out.println("ALL TESTS PASSED!");
            return;
        }

        System.out.println(displayFailureHeader());
        gradedRes.forEach(res -> {
                if (!res.passed()) {
                    System.out.println(formatGradedItem(res));
                }
            }
        );
    }

    /**
     * Save the grading output to a specific filename, determined by {@code OUT_FILENAME}.
     *
     * @param grader the test case grader
     */
    public void saveGradingResults(Grader grader) {
        List<GradedTestResult> gradedRes = grader.getGradedTestResults();
        int numTestsFailed = (int) gradedRes.stream().filter(r -> !r.passed()).count();
        if (numTestsFailed == 0) {
            //all tests passed, no need to create writer resource and take up mem, exit out
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(displayFailureHeader());
        gradedRes.forEach(res -> {
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
     * Returns a {@code String} for when the failure message is displayed.
     *
     * @return Returns the header of the failure state message.
     */
    private String displayFailureHeader() {
        return "TESTS FAILED! SEE BELOW FOR REPORT" +
            "===========================================";
    }

}
