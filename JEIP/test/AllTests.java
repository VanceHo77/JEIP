/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vance
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
//參數化測試
@RunWith(Suite.class)
//如果有多個測試Class，可以將它們組合成一個測試套件。運行測試套件將按照指定的順序執行所有測試Class。
//@SuiteClasses（{MyClassTest.class，MySecondClassTest.class}）
@SuiteClasses({ })
public class AllTests {
    
} 