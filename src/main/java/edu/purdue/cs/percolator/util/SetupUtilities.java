package edu.purdue.cs.percolator.util;

/**
 * The {@link SetupUtilities} class contains useful tools for setting up
 * test suites.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
 * @since 1.0
 */
public final class SetupUtilities {

    /**
     * Private default constructor so no objects can be created of this type.
     */
    private SetupUtilities() {
    }

    /**
     * Returns the package to test. This is determined by
     * the {@code env} system property.
     * <p>
     * If the {@code env} property is set to {@code dev},
     * the solution package, defined by the {@code solution.package.name}
     * system property, is returned, followed by a terminating period.
     * If the {@code solution.package.name} property is not set,
     * the method returns {@code solution.}, including the terminating
     * period.
     * <p>
     * If the {@code env} property is set to {@code prod} or anything
     * but {@code dev}, the student package, defined by the
     * {@code student.package.name} system property, is returned, followed
     * by a terminating period. If the {@code student.package.name} property
     * is not set, the method returns an empty string, signifying the
     * {@code default} package.
     * <p>
     * This method's main purpose is to determine the package name for the
     * classes you are testing when writing tests that use {@link java.lang.reflect}.
     * <br><p>
     * <b>Example:</b>
     * <p>
     * If your students are uploading Java code that rests in the {@code default}
     * package (i.e., there is no package statement in the file), then you do not
     * need to do anything.
     * If your students are uploading Java code that is inside of a package, set
     * the {@code student.package.name} to the entire package name, not including
     * a terminating period.
     * If you want to test your solution, set the {@code env} property to {@code dev}
     * and set the {@code solution.package.name} property to the package name of your
     * solution.
     *
     * @return the name of the package to test
     */
    public static String getPackageToTest() {
        String environment = System.getProperty("env", "prod");

        if (environment.equals("dev")) {
            String solutionPackage = System.getProperty("solution.package.name", "solution");
            return solutionPackage + ".";
        } else {
            String studentPackage = System.getProperty("student.package.name", "");
            if (!studentPackage.isEmpty()) {
                studentPackage += ".";
            }
            return studentPackage;
        }
    }

}
