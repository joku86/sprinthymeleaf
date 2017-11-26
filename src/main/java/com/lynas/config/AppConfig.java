package com.lynas.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="com.lynas.data")

public class AppConfig implements WebMvcConfigurer {


    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }


   /* @Bean
    protected LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }
    */

    private DataSource dataSource() {


        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(5);

        ds.setDataSourceClassName(MysqlDataSource.class.getName());
        ds.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/mydb?createDatabaseIfNotExist=true&serverTimezone=UTC");
        ds.addDataSourceProperty("user", "root1");
        ds.addDataSourceProperty("password", "root1");
       //s ds.addDataSourceProperty("cachePrepStmts", true);
       // ds.addDataSourceProperty("prepStmtCacheSize", 250);
        //ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        // ds.addDataSourceProperty("useServerPrepStmts", true);
        return ds;
    }
    /*
   @Bean
   public DataSource dataSource() {
       DriverManagerDataSource datasource = new DriverManagerDataSource();
       datasource.setDriverClassName("com.mysql.jdbc.Driver");
       datasource.setUrl("jdbc:mysql://localhost:3306/mydb?createDatabaseIfNotExist=true&serverTimezone=UTC");
       datasource.setUsername("root1");
       datasource.setPassword("root1");
       return datasource;
   }
*/

    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put("hibernate.dialect",
                "org.hibernate.dialect.MySQL5Dialect");
        hibernateProp.put("hibernate.hbm2ddl.auto", "create");
        hibernateProp.put("hibernate.format_sql", true);
        hibernateProp.put("hibernate.use_sql_comments", true);
        hibernateProp.put("hibernate.show_sql", true);
        return hibernateProp;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("com.lynas");
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.afterPropertiesSet();
        return factoryBean.getNativeEntityManagerFactory();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode("HTML5");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        Set<IDialect> dialects = new HashSet<IDialect>();
        dialects.add(new LayoutDialect());

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setAdditionalDialects(dialects);
        return engine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());

        //  resolver.setOrder(1);
        //  resolver.setViewNames(new String[]{"*.html", "*.xhtml"});

        return resolver;
    }


}
