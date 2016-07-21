/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.*;

/**
 *
 * @author Vance
 */
@RunWith(Parameterized.class)
public class ParameterizedTestFields {

    // fields used together with @Parameter must be public
    @Parameter
    public int m1;
    @Parameter(value = 1)
    public int m2;

    // creates the test data
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{1, 2}, {5, 3}, {121, 4}, {0, 4}};
        return Arrays.asList(data);
    }

    @Test
    public void testMultiplyException() {
        MyClass tester = new MyClass();
        assertEquals("m1+m2", m1 + m2, tester.multiply(m1, m2));
    }

    // class to be tested
    class MyClass {

        public int multiply(int i, int j) {
            return i + j;
        }
    }

}
