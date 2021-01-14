package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.util.List;

class VocareumFormatter implements OutputFormatter {

    //rubric name, number,
    @Override
    public void printGradingResults(Grader grader) {
        List<GradedTestResult> gradedRes = grader.getGradedTestResults();
        int numTestsFailed = (int) gradedRes.stream().filter(r -> !r.passed()).count();
        if (numTestsFailed == 0) {
            System.out.println("ALL TESTS PASSED!");
            return;
        }
        System.out.println("TESTS FAILED! SEE BELOW FOR REPORT");
        System.out.println("===========================================");
        gradedRes.forEach(res -> {
                if (!res.passed()) {
                    System.out.println(formatGradedItem(res));
                }
            }
        );
    }

    public void saveGradingResults(Grader grader) {
        // TODO
    }

    private String formatGradedItem(GradedTestResult gtr) {
        if (gtr.passed()) {
            return gtr.getName() + ": TEST PASSED!";
        } else {
            return gtr.getName() + ": " + gtr.getScore() + " / " +
                gtr.getPoints() + "TEST FAILED. See result below.";
        }
    }

}
