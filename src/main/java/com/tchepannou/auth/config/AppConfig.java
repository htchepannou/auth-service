package com.tchepannou.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    @Value("${database.driver}")
    private String driver;

    @Value ("${database.url}")
    private String url;

    @Value ("${database.username}")
    private String username;

    @Value ("${database.password}")
    private String password;


    //-- Beans
    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    DataSource dataSource (){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        return ds;
    }
}
