package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;

/**
 * The {@link OutputFormatter} interface standardizes formatting output for all
 * grading platforms.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.2
 */
interface OutputFormatter {

    /**
     * Prints the contents of the {@link Grader} object
     * to standard out in a format that grading platform can understand.
     *
     * @param grader the test case grader
     */
    void printGradingResults(Grader grader);

    /**
     * Saves the contents of the {@link Grader} object
     * to a format that the grading platform can understand.
     *
     * @param grader the test case grader
     */
    void saveGradingResults(Grader grader);

}
