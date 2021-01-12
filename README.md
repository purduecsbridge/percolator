# Percolator

[![build](https://img.shields.io/github/workflow/status/purduecsbridge/percolator/Deploy/main)](https://github.com/purduecsbridge/percolator/actions?query=workflow%3A%22Deploy%22+branch%3Amain)
[![javadoc](https://img.shields.io/badge/docs-javadoc-blue)](https://purduecsbridge.github.io/percolator/api/latest)

Percolator is a grading framework for Java programming assignments on Gradescope and Vocareum. It is meant to make the development and maintenance of programming assignments easier for courses using Java. How easy is it?

```java
import edu.purdue.cs.percolator.TestCase;

import org.junit.Test;

public class HelloWorldTests {

    @Test(timeout = 1000)
    @TestCase(name = "Hello test", points = 50.0)
    public void testHello() {
        // Test code here
    }
  
    @Test(timeout = 1000)
    @TestCase(name = "Goodbye test", points = 50.0)
    public void testGoodbye() {
        // Test code here
    }

}
```

That's a test suite written using Percolator. Pretty simple, right? Call the `AutoGrader.run()` method to run your test suites and automatically print the results in JSON for Gradescope.

```java
import edu.purdue.cs.percolator.AutoGrader;

public class TestRunner {

    public static void main(String[] args) {
        String[] testSuites = {TestSuiteOne.class, TestSuiteTwo.class};
        AutoGrader.grade(testSuites).run();
    }

}
```

You can customize the settings for the [AutoGrader](https://purduecsbridge.github.com/percolator/api/latest/edu/purdue/cs/percolator/AutoGrader.html) using its builder methods, including adding a code style checker.

```java
import edu.purdue.cs.percolator.AutoGrader;
import edu.purdue.cs.percolator.StyleChecker;

public class TestRunner {

    public static void main(String[] args) {
        String[] testSuites = {TestSuiteOne.class, TestSuiteTwo.class};
        StyleChecker codeStyleChecker = StyleChecker.lint(
                "/autograder/submission", "/autograder/source/checkstyle.xml")
                .withMaxScore(5.0)
                .withDeduction(1.0);
        AutoGrader.grade(testSuites)
                .withMaxScore(95.0)
                .withStyleChecker(codeStyleChecker)
                .run();
    }

}
```

The [StyleChecker](https://purduecsbridge.github.io/percolator/api/latest/edu/purdue/cs/percolator/StyleChecker.html) audits the Java source files in a given directory and enforces the rules defined in a [Checkstyle configuration](https://checkstyle.org/config.html).

By default, the [AutoGrader](https://purduecsbridge.github.com/percolator/api/latest/edu/purdue/cs/percolator/AutoGrader.html) will assume you are using Gradescope. If you are using Vocareum instead, just use the `onVocareum()` method when constructing your AutoGrader.

```java
import edu.purdue.cs.percolator.AutoGrader;

public class TestRunner {

    public static void main(String[] args) {
        String[] testSuites = {TestSuiteOne.class, TestSuiteTwo.class};
        AutoGrader.grade(testSuites)
                .onVocareum()
                .run();
    }

}
```

By default, after calling `run()`, the `AutoGrader` will print out the results to the console. If you'd rather have the grader automatically save the results to the proper location on disk, use the `autoSaveResults()` method (this is recommended for both Gradescope and Vocareum).

```java
import edu.purdue.cs.percolator.AutoGrader;

public class TestRunner {

    public static void main(String[] args) {
        String[] testSuites = {TestSuiteOne.class, TestSuiteTwo.class};
        AutoGrader.grade(testSuites)
                .onGradescope()
                .autoSaveResults()
                .run();
    }

}
```

## Installation
Instructions on how to add Percolator to your Maven project can be found [here](https://github.com/purduecsbridge/percolator/packages/).

## Contributing
Pull requests and new issues are always welcome.

## License
Licensed under the [MIT License](LICENSE).
