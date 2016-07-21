/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JunitWebTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 *
 * @author Vance
 */
public class AddJSPTest {

    private WebClient webClient;
    private HtmlPage page;

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // WebClient 代表一個瀏覽器
        webClient = new WebClient();
        // 指定 URL 取得 HTML 結果
        page = webClient.getPage(
                "http://localhost:8081/JEIP");
    }

    /**
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        // 關閉瀏覽視窗
        webClient.close();
    }
    
    /**
     * /測試初次造訪頁面
     * @throws Exception
     */
    @Test
    public void testNoError() throws Exception {
        // 以標準 DOM API 取得頁面元素進行斷言
        assertEquals("<error>首頁的預期網頁title名稱:","JEIP入口網站-登入", page.getTitleText());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testError() throws Exception {
        // 取得表單元素
        HtmlForm form = page.getForms().get(0);
        // 取得發送按鈕
        HtmlButton button = form.getButtonByName("btnLogin");

        // 發送表單，取得回應頁面
        HtmlPage page2 = button.click();
        // 以標準 DOM API 取得頁面元素進行斷言
        assertEquals("新增書籤失敗",
                page2.getElementsByTagName("h1")
                .item(0).getFirstChild().getNodeValue());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testParameters() throws Exception {
        //預期結果
        String title = "主控台";
        HtmlForm form = page.getForms().get(0);
        // 取得輸入欄位
        HtmlTextInput account = form.getInputByName("UserAccount");
        HtmlPasswordInput password = form.getInputByName("Password");
        // 在欄位填值
        account.setValueAttribute("vance");
        password.setValueAttribute("123");
        HtmlButton button = form.getButtonByName("btnLogin");

        HtmlPage page2 = button.click();
        //驗證失敗時才會出現驗證錯誤訊息
        assertEquals("<error>登入後的預期網頁title名稱:",title,
                page2.getTitleText());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testSubmitSuccess() throws Exception {
        HtmlForm form = page.getForms().get(0);
        form.getInputByName("url").setValueAttribute("http://test");
        form.getInputByName("title").setValueAttribute("測試");
        form.getSelectByName("category").getOption(1).setSelected(true);

        HtmlSubmitInput button = form.getInputByValue("送出");

        // 假設發送成功的回應頁面標題為"新增書籤功成功"
        HtmlPage page2 = button.click();
        assertEquals("新增書籤成功", page2.getTitleText());
    }
}
