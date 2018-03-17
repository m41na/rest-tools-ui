package com.jarredweb.rest.tools.ui.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.jarredweb.rest.tools.ui"})
@EnableTransactionManagement
public class RestToolsConfig {

    @Bean
    public DataSource dataSource() {
        String dbPath = "./data/rest-tools-ui.db";
        // Create the ConnectionPoolDataSource
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:" + dbPath);
        ds.setUsername("sa");
        ds.setPassword("sa");
        ds.setDriverClassName("org.h2.Driver");

        // return datasource bean
        return ds;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
