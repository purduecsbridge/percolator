package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;
import org.junit.runner.JUnitCore;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@link AutoGrader} class contains builder methods to create an auto-grader.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.0
 */
public final class AutoGrader {

    /**
     * The test suite classes to run.
     */
    private Class<?>[] testSuites;

    /**
     * The maximum score for the test suites.
     */
    private double maxScore;

    /**
     * The formatter used to output the grading results.
     */
    private OutputFormatter formatter;

    /**
     * The optional code style checker that will run after grading.
     */
    private StyleChecker styleChecker;

    /**
     * Private default constructor so objects are created using the {@link AutoGrader#grade} method.
     */
    private AutoGrader() {
    }

    /**
     * Creates a new {@link AutoGrader} with the given test suites. The maximum score defaults to 100.
     * The grading platform defaults to Gradescope.
     *
     * @param testSuites the classes to test
     * @return a new {@link AutoGrader} object
     */
    public static AutoGrader grade(Class<?>[] testSuites) {
        if (testSuites == null || Arrays.stream(testSuites).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("testSuites parameter cannot be null or contain null elements.");
        }

        AutoGrader grader = new AutoGrader();
        grader.testSuites = testSuites;
        grader.withMaxScore(100);
        grader.onGradescope();
        grader.styleChecker = null;

        return grader;
    }

    /**
     * Specifies a maximum score for the test suites.
     *
     * @param maxScore the maximum score for the test suites
     * @return the {@link AutoGrader} with the new max score
     */
    public AutoGrader withMaxScore(double maxScore) {
        if (maxScore < 0) {
            throw new IllegalArgumentException("maxScore cannot be negative.");
        }

        this.maxScore = maxScore;
        return this;
    }

    /**
     * Specifies a {@link StyleChecker} object to run a code style audit with
     * after grading.
     *
     * @param styleChecker the style checker to run
     * @return the {@link AutoGrader} with the new style checker
     */
    public AutoGrader withStyleChecker(StyleChecker styleChecker) {
        this.styleChecker = styleChecker;
        return this;
    }

    /**
     * Specifies the grading platform to be Gradescope.
     *
     * @return the {@link AutoGrader} with the new platform setting
     */
    public AutoGrader onGradescope() {
        if (!(this.formatter instanceof GradescopeFormatter)) {
            this.formatter = new GradescopeFormatter();
        }
        return this;
    }

    /**
     * Specifies the grading platform to be Vocareum.
     *
     * When setting up an assignment in Vocareum, the rubric items
     * should be named "Test Cases" and "Code Style". You can
     * omit "Code Style" if you are not using a {@link StyleChecker}.
     *
     * In order to distinguish between the grading report and the
     * grading rubric results, the grading report is printed to
     * standard error, and the grading results are printed to
     * standard out. When writing your grading script, it is recommended
     * that you redirect output using the following sequence:
     * {@code 2>&1 1> $vocareumGradeFile}.
     *
     * @return the {@link AutoGrader} with the new platform setting
     */
    public AutoGrader onVocareum() {
        if (!(this.formatter instanceof VocareumFormatter)) {
            this.formatter = new VocareumFormatter();
        }
        return this;
    }

    /**
     * Runs the grader and prints out the results.
     */
    public void run() {
        Grader grader = new Grader();
        grader.setMaxScore(this.maxScore);
        TestCaseListener listener = new TestCaseListener(this.maxScore);

        JUnitCore runner = new JUnitCore();
        runner.addListener(listener);

        grader.startTimer();
        runner.run(this.testSuites);
        grader.stopTimer();

        listener.getTestResults().forEach(grader::addGradedTestResult);

        if (this.styleChecker != null) {
            runStyleChecker(grader);
        }

        formatter.printGradingResults(grader);
    }

    /**
     * Runs the style checker and adds the audit result to the report.
     *
     * @param grader the grader where the result will be added
     */
    private void runStyleChecker(Grader grader) {
        GradedTestResult result = this.styleChecker.grade();
        grader.addGradedTestResult(result);
    }

}
