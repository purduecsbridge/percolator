package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The {@link VocareumFormatter} class saves the grading results
 * so Vocareum can find them and assign grades.
 *
 * @author Kedar Abhyankar
 * @version 1.3
 * @since 1.3
 */
class VocareumFormatter implements OutputFormatter {

    /**
     * The filename used by Vocareum for outputting grade information to.
     */
    private final static String REPORT_FILE = "$vocareumGradeFile";

    private final static String FAILURE_HEADER = "TESTS FAILED! SEE BELOW FOR REPORT\n" +
        "===========================================";


    /**
     * Prints the grading output from the {@link Grader} object
     * to standard out. Saves the grading results to
     * {@code $vocareumGradeFile}.
     *
     * @param grader the test case grader
     */
    public void printGradingResults(Grader grader) {
        List<GradedTestResult> results = grader.getGradedTestResults();
        if (didFailNonzeroTests(results, 1, Optional.empty())) {
            results.forEach(res -> System.out.println(formatGradedItem(res)));
        } else {
            System.out.println("ALL TESTS PASSED!");
        }

        System.out.printf("Test Cases, %f%n\n", grader.getScore());
        System.out.printf("Code Style, %f%n\n", getCodeStyleScore(results).getScore());
    }

    /**
     * Save the grading output to a specific filename, determined by {@code OUT_FILENAME}.
     *
     * @param grader the test case grader
     */
    public void saveGradingResults(Grader grader) {
        List<GradedTestResult> results = grader.getGradedTestResults();
        StringBuilder sb = new StringBuilder();
        if (didFailNonzeroTests(results, 2, Optional.of(sb))) {
            results.forEach(res -> sb.append(formatGradedItem(res)).append("\n"));
        } else {
            sb.append("ALL TESTS PASSED!\n");
        }

        sb.append(String.format("Test Cases, %f\n", grader.getScore()));
        sb.append(String.format("Code Style, %f%n\n", getCodeStyleScore(results).getScore()));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getenv(REPORT_FILE)))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Format a graded item in the appropriate format based on the test pass/fail state.
     *
     * @param testCase the {@code GradedTestResult} to print output for
     * @return A formatted {@code String} detailing the test pass/fail message.
     */
    private static String formatGradedItem(GradedTestResult testCase) {
        return String.format("%s: %f/%f%n%s%n", testCase.getName(), testCase.getScore(),
            testCase.getPoints(), testCase.getOutput());
    }

    /**
     * Returns a boolean indicative of if the {@code List<GradedTestResults>} had 1 or more
     * tests that failed.
     *
     * @param resultList    the {@code List<GradedTestResults>} received from the {@code Grader} object.
     * @param outputType    a value indicating the output type - 1 for stdout, and 2 for a file
     * @param stringBuilder an Optional<StringBuilder> that should only be used if the {@code outputType} is 2.
     * @return true if there are more than 0 tests that failed, false otherwise.
     */
    private boolean didFailNonzeroTests(List<GradedTestResult> resultList, int outputType,
                                        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
                                            Optional<StringBuilder> stringBuilder) {
        boolean result = resultList.stream().anyMatch(r -> !r.passed());
        if (outputType == 1 && result) {
            System.out.println(VocareumFormatter.FAILURE_HEADER);
        } else if (outputType == 2 && result && stringBuilder.isPresent()) {
            stringBuilder.get().append(VocareumFormatter.FAILURE_HEADER);
        }
        resultList.removeIf(GradedTestResult::passed);
        return result;
    }

    /**
     * Get the code style score given a list of {@code GradedTestResult}.
     *
     * @param results the {@code List<GradedTestResults>} obtained from the grader.
     * @return a {@code GradedTestResult} representing the code style score, if found, or null otherwise.
     */
    private GradedTestResult getCodeStyleScore(List<GradedTestResult> results) {
        GradedTestResult codeStyle = null;
        if (results.get(results.size() - 1).getName().equals(StyleChecker.TEST_RESULT_NAME)) {
            GradedTestResult result = results.get(results.size() - 1);
            codeStyle = result;
            results.remove(result);
        }

        return codeStyle;
    }

}
