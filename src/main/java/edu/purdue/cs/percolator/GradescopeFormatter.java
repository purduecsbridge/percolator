package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradescope.GradescopeJsonFormatter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * The {@link GradescopeFormatter} class saves the grading results
 * so Gradescope can find them and assign grades.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.2
 */
class GradescopeFormatter implements OutputFormatter {

    /**
     * The location to save the results to.
     */
    private final static String RESULTS_FILE = "/autograder/results/results.json";

    /**
     * Inner {@link GradescopeJsonFormatter} object to handle JSON formatting.
     */
    private final GradescopeJsonFormatter formatter;

    /**
     * Creates a new {@link GradescopeFormatter}.
     */
    GradescopeFormatter() {
        this.formatter = new GradescopeJsonFormatter();
        this.formatter.setPrettyPrint(2);
    }

    /**
     * Saves the contents of the {@link Grader} object
     * to {@code /autograder/results/results.json}.
     *
     * @param grader the test case grader
     */
    public void saveGradingResults(Grader grader) {
        try (PrintWriter writer = new PrintWriter(RESULTS_FILE)) {
            this.printGradingResults(grader, writer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints the contents of the {@link Grader} object
     * to standard out.
     *
     * @param grader the test case grader
     */
    public void printGradingResults(Grader grader) {
        try (PrintWriter writer = new PrintWriter(System.out)) {
            this.printGradingResults(grader, writer);
        }
    }

    /**
     * Prints the contents of the {@link Grader} object
     * to the specified writer.
     *
     * @param grader the test case grader
     * @param writer the writer used to write results
     */
    private void printGradingResults(Grader grader, PrintWriter writer) {
        String output = this.formatter.format(grader);
        writer.write(output);
        writer.close();
    }

}
