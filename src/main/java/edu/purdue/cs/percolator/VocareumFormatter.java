package edu.purdue.cs.percolator;

import com.github.tkutcher.jgrade.Grader;
import com.github.tkutcher.jgrade.gradedtest.GradedTestResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class VocareumFormatter implements OutputFormatter {

    public static final String OUT_FILE_NAME = "GRADED_OUT";

    //rubric name, number,
    @Override
    public void printGradingResults(Grader grader) {
        List<GradedTestResult> gradedRes = grader.getGradedTestResults();
        int numTestsFailed = (int) gradedRes.stream().filter(r -> !r.passed()).count();
        if (numTestsFailed == 0) {
            System.out.println("ALL TESTS PASSED!");
            return;
        }

        System.out.println(displayFailureHeader());
        gradedRes.forEach(res -> {
                if (!res.passed()) {
                    System.out.println(formatGradedItem(res));
                }
            }
        );
    }

    public void saveGradingResults(Grader grader) {
        List<GradedTestResult> gradedRes = grader.getGradedTestResults();
        int numTestsFailed = (int) gradedRes.stream().filter(r -> !r.passed()).count();
        if (numTestsFailed == 0) {
            //all tests passed, no need to create writer resource and take up mem, exit out
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(displayFailureHeader());
        gradedRes.forEach(res -> {
            if (!res.passed()) {
                sb.append(formatGradedItem(res)).append("\n");
            }
        });

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_FILE_NAME))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String formatGradedItem(GradedTestResult gtr) {
        if (gtr.passed()) {
            return gtr.getName() + ": TEST PASSED!";
        } else {
            return gtr.getName() + ": " + gtr.getScore() + " / " +
                gtr.getPoints() + "TEST FAILED. See result below.";
        }
    }

    private String displayFailureHeader() {
        return "TESTS FAILED! SEE BELOW FOR REPORT" +
            "===========================================";
    }

}
