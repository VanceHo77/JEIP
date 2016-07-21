package com.configuration;

import com.filter.LoginInterceptor;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
//import javax.sql.DataSource;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//取代spring-configxml
/**
 *
 * @author Vance
 */
//組態設定檔
@Configuration
//對應到組態檔裡的<mvc:annotation-driven>
@EnableWebMvc
//跟組態檔裡的ComponentScan是一樣的，差別在於Annotation有分basePackage和baskePackages，後者參數是陣列，代表多個basepackages
@ComponentScan(basePackages = {"com.*"})
//啟用事務管理器
@EnableTransactionManagement
public class SpringConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 附件上傳解析器
     *
     * @return
     */
    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver resolver() {
        return new StandardServletMultipartResolver();
    }

    /**
     * 視圖解析器
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        //國際化視圖解析器，具有UrlBasedViewResolver特性（把視圖名轉換URL），它支持InternalResourceView，JstlView 和TilesView等視圖。
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //設置這個視圖解析器創建視圖的class
        viewResolver.setViewClass(InternalResourceView.class);
        //前綴
        viewResolver.setPrefix("/WEB-INF/");
        //後綴
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/").setCachePeriod(31556926);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登入攔截器
        registry.addInterceptor(new LoginInterceptor());
        //交易攔截器
//	    registry.addInterceptor((HandlerInterceptor) new TransactionInterceptor()).addPathPatterns("/person/save/*");
    }

    /**
     * 適用於所有環境的FactoryBean，能全面控制EntityManagerFactory配置，非常適合那種需要細粒度定制的環境
     *
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        //指定資料庫來源
        em.setDataSource(dataSource());
        //指定持久化單元名字，即 JPA 配置文件中指定的
        em.setPersistenceUnitName("MyKMPersistenceUnit");
        //指定掃描哪個Package底下的class，當 persistenceUnitName 和 packagesToScan 屬性同時存在時，會使用 persistenceUnitName 屬性
        em.setPackagesToScan(new String[]{"com.model"});
        //JPA方言:可不配置
        em.setJpaDialect(new HibernateJpaDialect());
        //JPA配置
        em.setJpaPropertyMap(jpaProperties());
        //指定實現廠商專用特性，即 generateDdl= false 表示不自動生成 DDL，database= HSQL 表示使用 hsqld b數據庫
        em.setJpaVendorAdapter(jpaVendorAdapter());

        //JPA persistence.xml配置
//        em.setPersistenceXmlLocation("classpath:/persistenceXXX.xml");
        return em;
    }

    /**
     * JpaVendorAdapter:實現廠商專用特性
     *
     * @return
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.HSQL);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
        return jpaVendorAdapter;
    }

    /**
     * JPA特性配置
     *
     * @return
     */
    @Bean
    public Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", DerbyTenSevenDialect.class.getName());
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.connection.charSet", "UTF-8");
        return props;
    }

    /**
     * 資料庫來源
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/KM");
        dataSource.setUsername("vance");
        dataSource.setPassword("gn951753");
        //連線池(使用DBCP Library)
        dataSource.setInitialSize(10);//初始連線數
        dataSource.setMaxActive(100);//最大連線數
        return dataSource;
    }

    /**
     * 事務管理器
     *
     * @return
     * @throws Exception
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        jpaTransactionManager.setDataSource(dataSource());
        jpaTransactionManager.setJpaDialect(new HibernateJpaDialect());
        return jpaTransactionManager;
    }

    /**
     * EntityManager 主要在管理Entity實例生命週期，透過 EntityManager對資料庫執行CRUD動作
     *
     * @param entityManagerFactory
     * @return
     */
    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    /**
     *
     * @return
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Bean屬性注入
     *
     * @return
     */
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

}
