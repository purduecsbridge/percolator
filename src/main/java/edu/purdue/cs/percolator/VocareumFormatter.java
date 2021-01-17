package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.io.*;
import java.util.List;

/**
 * The {@link VocareumFormatter} class saves the grading results
 * so Vocareum can find them and assign grades.
 *
 * @author Kedar Abhyankar
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.2
 */
class VocareumFormatter implements OutputFormatter {

    /**
     * The environment variable that contains the location to save
     * the grading results to.
     */
    private final static String GRADE_FILE = "$vocareumGradeFile";

    /**
     * The environment variable that contains the location to save
     * the grading report to.
     */
    private final static String REPORT_FILE = "$vocareumReportFile";

    /**
     * The message displayed when all test cases pass.
     */
    private final static String ALL_PASS_MESSAGE = "ALL TESTS PASS!!!\n";

    /**
     * The header displayed before test output if one or more test cases failed.
     */
    private final static String TEST_OUTPUT_HEADER = "TESTS FAILED! SEE BELOW FOR REPORT\n" +
        "==================================\n";

    /**
     * The message displayed after test output.
     */
    private final static String TEST_OUTPUT_END = "END OF TEST FAILURES\n";

    /**
     * The header displayed before code style output.
     */
    private final static String CODE_STYLE_HEADER = "\nCODE STYLE AUDIT" +
        "================\n";

    /**
     * Saves the grading output from the {@link Grader} object to
     * {@code $vocareumReportFile}. Saves the grading results to
     * {@code $vocareumGradeFile}.
     *
     * @param grader the test case grader
     */
    public void saveGradingResults(Grader grader) {
        PrintWriter gradeWriter;
        PrintWriter reportWriter;
        try {
             gradeWriter = new PrintWriter(System.getenv(GRADE_FILE));
             reportWriter = new PrintWriter(System.getenv(REPORT_FILE));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.printGradingResults(grader, gradeWriter, reportWriter);
    }

    /**
     * Prints the grading results to standard out. The output
     * for the test cases is never printed or saved to disk.
     *
     * @param grader the test case grader
     */
    public void printGradingResults(Grader grader) {
        PrintWriter gradeWriter = new PrintWriter(System.out);
        this.printGradingResults(grader, gradeWriter, null);
    }

    /**
     * Prints the contents of the {@link Grader} object to the specified gradeWriter
     * and reportWriter.
     *
     * @param grader the test case grader
     * @param gradeWriter the writer used to write the grading results
     * @param reportWriter the writer used to write the grading report
     */
    private void printGradingResults(Grader grader, PrintWriter gradeWriter, PrintWriter reportWriter) {
        List<GradedTestResult> results = grader.getGradedTestResults();
        GradedTestResult codeStyle = null;
        if (results.get(results.size() - 1).getName().equals(StyleChecker.TEST_RESULT_NAME)) {
            GradedTestResult result = results.get(results.size() - 1);
            codeStyle = result;
            results.remove(result);
        }

        if (gradeWriter != null) {
            double testCasesScore = results.stream()
                .mapToDouble(GradedTestResult::getScore)
                .sum();
            double codeStyleScore = codeStyle != null ? codeStyle.getScore() : 0;

            gradeWriter.write(String.format("Test Cases, %f\n", testCasesScore));
            gradeWriter.write(String.format("Code Style, %f\n", codeStyleScore));
            gradeWriter.close();
        }

        if (reportWriter != null) {
            boolean allTestsPass = results.stream().allMatch(GradedTestResult::passed);
            if (allTestsPass) {
                reportWriter.write(ALL_PASS_MESSAGE);
            } else {
                reportWriter.write(TEST_OUTPUT_HEADER);
                results.forEach(r -> {
                    if (!r.passed()) {
                        reportWriter.write(String.format("%s: %f/%f\n%s\n",
                            r.getName(), r.getScore(), r.getPoints(), r.getOutput()));
                    }
                });
                reportWriter.write(TEST_OUTPUT_END);
            }

            if (codeStyle != null) {
                reportWriter.write(CODE_STYLE_HEADER);
                reportWriter.write(codeStyle.getOutput());
            }
            reportWriter.close();
        }
    }

}
