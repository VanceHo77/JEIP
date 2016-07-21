/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vance
 */
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MyClassTest {
    //如果該方法不拋出指定的異常錯誤。
    //expected:預期的
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrown() {
        MyClass tester = new MyClass();
        tester.multiply(1000, 5);
    }

    @Test
    public void testMultiply() {
        MyClass tester = new MyClass();
        //預期結果應為:60，.multiply()傳入10,6則回傳10*6=60，測試正確                                 
        assertEquals("10 x 6 must be 60", 60, tester.multiply(10, 6));
    }
}
