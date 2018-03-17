package com.jarredweb.rest.tools.ui.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.jarredweb.rest.tools.ui"})
@EnableTransactionManagement
public class RestToolsConfig {

    @Autowired
    private Environment env;
    
    @Bean
    public DataSource dataSource() {
        // Create the ConnectionPoolDataSource
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getProperty("app.jdbc.url"));
        ds.setUsername(env.getProperty("app.jdbc.username"));
        ds.setPassword(env.getProperty("app.jdbc.password"));
        ds.setDriverClassName(env.getProperty("app.jdbc.driverClassName"));
        // return datasource bean
        return ds;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
