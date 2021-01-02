package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradescope.GradescopeJsonFormatter;

/**
 * The {@link GradescopeFormatter} class formats grading results
 * and prints them out so Gradescope can assign grades and display
 * the results.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.2
 */
class GradescopeFormatter implements OutputFormatter {

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
     * Prints the contents of the {@link Grader} object
     * to standard out.
     *
     * @param grader the test case grader
     */
    public void printGradingResults(Grader grader) {
        System.out.println(this.formatter.format(grader));
    }

}
