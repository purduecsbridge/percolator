package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link StyleChecker} class contains builder methods to create a code style checker
 * to audit code style errors. {@link StyleChecker} uses the {@link com.puppycrawl.tools.checkstyle.Checker} from
 * <a href="https://checkstyle.sourceforge.io">Checkstyle</a> to audit code style.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.2
 * @since 1.0
 */
public final class StyleChecker {

    /**
     * The {@link GradedTestResult} name for style check.
     */
    final static String TEST_RESULT_NAME = "Code Style";

    /**
     * The {@link GradedTestResult} output message when the style checker fails unexpectedly.
     */
    private final static String CHECK_FAIL_MESSAGE = "Code style check failed unexpectedly. Please contact an instructor.";

    /**
     * The directory that contains the files to audit.
     */
    private String directory;

    /**
     * The list of files to audit.
     */
    private List<File> files;

    /**
     * The configuration created from a Checkstyle configuration file.
     */
    private Configuration configuration;

    /**
     * The maximum score for the code style check.
     */
    private double maxScore;

    /**
     * The amount to deduct from the score per code style infraction.
     */
    private double deduction;

    /**
     * Private default constructor so no objects can be created of this type.
     */
    private StyleChecker() {
    }

    /**
     * Creates a new {@link StyleChecker} with the given directory and policy.
     * The directory specifies the directory that contains the files to run through
     * the code style checker. The policy file specifies the
     * <a href="https://checkstyle.sourceforge.io">Checkstyle</a> configuration to enforce.
     * The maximum score defaults to 0. The deduction amount per infraction defaults to 0.
     *
     * @param directory  the directory with Java files to audit for code style errors
     * @param policyFile the Checkstyle configuration file to enforce
     * @return a new {@link StyleChecker} object
     */
    public static StyleChecker lint(String directory, String policyFile) {
        File dir = new File(directory);
        if (!dir.exists() || !dir.canExecute() || !dir.isDirectory()) {
            throw new IllegalArgumentException("directory parameter must specify a valid directory that is readable.");
        }

        StyleChecker checker = new StyleChecker();
        checker.directory = dir.getAbsolutePath();
        checker.files = new ArrayList<>();
        checker.withMaxScore(0);
        checker.withDeduction(0);

        try {
            Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile() && path.toString().endsWith(".java")) {
                        checker.files.add(path.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
                    if (exc instanceof AccessDeniedException) {
                        return FileVisitResult.SKIP_SUBTREE;
                    } else {
                        return super.visitFileFailed(path, exc);
                    }
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try {
            checker.configuration = ConfigurationLoader.loadConfiguration(
                policyFile, new PropertiesExpander(System.getProperties())
            );
        } catch (CheckstyleException e) {
            throw new RuntimeException(e);
        }

        return checker;
    }

    /**
     * Specifies the maximum score for the code style check.
     *
     * @param maxScore the maximum score for the code style check
     * @return the {@link StyleChecker} with the new max score
     */
    public StyleChecker withMaxScore(double maxScore) {
        this.maxScore = maxScore;
        return this;
    }

    /**
     * Specifies the amount to deduct from the score per code style infraction.
     * While a deduction is made for each infraction, the student score will
     * never be below 0.
     *
     * @param points the number of points to deduct for each infraction
     * @return the {@link StyleChecker} with the new deduction setting
     */
    public StyleChecker withDeduction(double points) {
        this.deduction = points;
        return this;
    }

    /**
     * Runs the style checker and returns a {@link GradedTestResult}
     * with the audit results.
     *
     * @return the audit results from the code style run
     */
    GradedTestResult grade() {
        Checker checker = new Checker();
        ByteArrayOutputStream linterOutput = new ByteArrayOutputStream();
        DefaultLogger listener = new DefaultLogger(linterOutput, AutomaticBean.OutputStreamOptions.NONE);
        checker.addListener(listener);
        checker.setBasedir(directory);
        checker.setModuleClassLoader(Checker.class.getClassLoader());

        int numErrors;
        try {
            checker.configure(configuration);
            numErrors = checker.process(files);
        } catch (CheckstyleException e) {
            e.printStackTrace();
            GradedTestResult result = new GradedTestResult(TEST_RESULT_NAME, "", 0, GradedTestResult.VISIBLE);
            result.addOutput(CHECK_FAIL_MESSAGE);
            return result;
        }

        GradedTestResult result = new GradedTestResult(TEST_RESULT_NAME, "", this.maxScore, GradedTestResult.VISIBLE);
        result.setScore(Math.max(result.getPoints() - (this.deduction * numErrors), 0));
        result.addOutput(linterOutput.toString());
        return result;
    }

    /**
     * Runs the style checker and prints the results.
     * Can be used via the command line directly rather than creating an {@link AutoGrader}.
     * Takes two command line arguments: 1. the path of the directory to audit for code style
     * and 2. the path of the checkstyle configuration file, in that order.
     *
     * @param args the directory to audit followed by the checkstyle configuration file
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("StyleChecker takes two arguments: " +
                "(directory_to_audit, checkstyle_configuration_file)."
            );
        }
        StyleChecker checker = StyleChecker.lint(args[0], args[1]);
        GradedTestResult result = checker.grade();
        System.out.println(result.getOutput());
    }

}
