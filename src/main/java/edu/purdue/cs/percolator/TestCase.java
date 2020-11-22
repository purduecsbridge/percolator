package edu.purdue.cs.percolator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link TestCase} annotation allows test methods to be graded using an {@link AutoGrader}.
 *
 * @author Andrew Davis, asd@alumni.purdue.edu
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCase {

    /**
     * The name of the test case.
     *
     * @return the name of the test case
     */
    String name() default "Unnamed test";

    /**
     * The group this test case belongs to.
     *
     * @return the test case group identifier
     */
    String group() default "";

    /**
     * The amount of points the test case is worth.
     *
     * @return the weight of the test case
     */
    double points() default 1.0;

    /**
     * The visibility level of the test case.
     *
     * @return the visibility of the test case
     */
    Visibility visibility() default Visibility.VISIBLE;


    /**
     * The {@link Visibility} enum defines the test visibility
     * in Gradescope.
     */
    enum Visibility {

        /**
         * The test case results will always be shown to students.
         */
        VISIBLE("visible"),

        /**
         * The test case results will be shown after grades are published.
         */
        AFTER_PUBLISH("after_published"),

        /**
         * The test case results will be shown after the due date has passed.
         */
        AFTER_DUE("after_due_date"),

        /**
         * The test case results will never be shown to students.
         */
        HIDDEN("hidden");

        private final String visibility;

        Visibility(String visibility) {
            this.visibility = visibility;
        }

        @Override
        public String toString() {
            return this.visibility;
        }
    }

}
