package edu.purdue.cs.percolator.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link SetupUtilities} class.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
 * @since 1.0
 */
public class SetupUtilitiesTest {

    @Before
    @After
    public void resetProperties() {
        System.clearProperty("env");
        System.clearProperty("solution.package.name");
        System.clearProperty("student.package.name");
    }

    @Test(timeout = 1000)
    public void getPackageToTest_testDefaultNoEnvSet() {
        final String expected = "";
        final String actual = SetupUtilities.getPackageToTest();
        Assert.assertEquals(expected, actual);
    }

    @Test(timeout = 1000)
    public void getPackageToTest_testDefaultEnvDev() {
        System.setProperty("env", "dev");
        final String expected = "solution.";
        final String actual = SetupUtilities.getPackageToTest();
        Assert.assertEquals(expected, actual);
    }

    @Test(timeout = 1000)
    public void getPackageToTest_testDefaultEnvProd() {
        System.setProperty("env", "prod");
        final String expected = "";
        final String actual = SetupUtilities.getPackageToTest();
        Assert.assertEquals(expected, actual);
    }

    @Test(timeout = 1000)
    public void getPackageToTest_testSolutionPackageSet() {
        System.setProperty("env", "dev");
        System.setProperty("solution.package.name", "edu.purdue.cs.cs180.hw00.solution");
        final String expected = "edu.purdue.cs.cs180.hw00.solution.";
        final String actual = SetupUtilities.getPackageToTest();
        Assert.assertEquals(expected, actual);
    }

    @Test(timeout = 1000)
    public void getPackageToTest_testStudentPackageSet() {
        System.setProperty("env", "prod");
        System.setProperty("student.package.name", "edu.purdue.cs.cs180.hw00");
        final String expected = "edu.purdue.cs.cs180.hw00.";
        final String actual = SetupUtilities.getPackageToTest();
        Assert.assertEquals(expected, actual);
    }

}
