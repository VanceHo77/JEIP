package com.configuration;

import java.io.File;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//取代web.xml(Servlet3.0之後不需要設定web.xml
/**
 *
 * @author Vance
 */
public class WebXMLInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * 載入SpringConfiguration設定檔
     *
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfiguration.class};
    }

    /**
     *
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 配置Filter：中文編碼、Hibernate Session(解決Lazy Load問題)
     *
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        //中文編碼
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        //Hibernate Session
        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();

        return new Filter[]{characterEncodingFilter, openEntityManagerInViewFilter};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        registration.setMultipartConfig(getMultipartConfigElement());
    }

    private MultipartConfigElement getMultipartConfigElement() {
        //檢查是否存在temp資料夾
        //C:\MyProject\MyKM\build\web\resources/temp/
        String[] pathAry = this.getClass().getResource("/").toString().split("/");
        String LOCATION = "";
        for (int i = 1; i < 6; i++) {
            LOCATION += pathAry[i] + "/";
        }
        LOCATION += "resources/temp/";
        File f = new File(LOCATION);
        if (!f.exists()) {
            f.mkdir();
        }
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }

    private static final long MAX_FILE_SIZE = 52428800; // 50MB : Max file size.
    // Beyond that size spring will throw exception.
    private static final long MAX_REQUEST_SIZE = 20971520; // 20MB : Total request size containing Multi part.

    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

}

//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRegistration;
// 
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//public class HelloWorldInitializer implements WebApplicationInitializer {
// 
//    public void onStartup(ServletContext container) throws ServletException {
// 
//        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//        ctx.register(HelloWorldConfiguration.class);
//        ctx.setServletContext(container);
// 
//        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
// 
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");
//    }
// 
//}
