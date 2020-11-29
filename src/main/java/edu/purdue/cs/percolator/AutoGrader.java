package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.OutputFormatter;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;
import com.github.tkutcher.jgrade.gradescope.GradescopeJsonFormatter;
import org.junit.runner.JUnitCore;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@link AutoGrader} class contains builder methods to create an auto-grader for a grading
 * platform. {@link AutoGrader} currently only supports Gradescope.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
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
     * The formatter used to output the grading report.
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
        grader.maxScore = 100;
        grader.formatter = new GradescopeJsonFormatter();
        ((GradescopeJsonFormatter)grader.formatter).setPrettyPrint(2);
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
     * Grading output will be formatted as Gradescope-compliant JSON.
     *
     * @return the {@link AutoGrader} with the new platform setting
     */
    public AutoGrader onGradescope() {
        if (!(this.formatter instanceof GradescopeJsonFormatter)) {
            GradescopeJsonFormatter formatter = new GradescopeJsonFormatter();
            formatter.setPrettyPrint(2);
            this.formatter = formatter;
        }
        return this;
    }

    /**
     * Runs the grader and outputs the results to System.out.
     */
    public void run() {
        Grader grader = new Grader();
        grader.setMaxScore(maxScore);
        TestCaseListener listener = new TestCaseListener(maxScore);

        JUnitCore runner = new JUnitCore();
        runner.addListener(listener);

        grader.startTimer();
        runner.run(testSuites);
        grader.stopTimer();

        listener.getTestResults().forEach(grader::addGradedTestResult);

        if (styleChecker != null) {
            runStyleChecker(grader);
        }

        System.out.println(formatter.format(grader));
    }

    /**
     * Runs the style checker and adds the audit result to the report.
     *
     * @param grader the grader where the result will be added
     */
    private void runStyleChecker(Grader grader) {
        GradedTestResult result = styleChecker.grade();
        grader.addGradedTestResult(result);
    }

}
