package com.example.sessionauth.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * This class allows configuring and using multiple datasource's. The primary and session DB
 * Primary DB is used to store the applications necessary details i.e. (Employee and Role object)
 * Whilst Session DB is used to store (Session details)
 * Links below explains better explanation
 * <a href="https://springhow.com/spring-session-different-database/">...</a>
 * <a href="https://www.baeldung.com/spring-boot-configure-multiple-datasources">...</a>
 * **/
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    /**
     * This method allows to connect primary details in application properties to the
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.primary")
    @Qualifier(value = "primaryProperties")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @Qualifier(value = "primaryDataSource")
    public DataSource primaryDataSource(@Qualifier(value = "primaryProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
                .initializeDataSourceBuilder() //
                .type(HikariDataSource.class) //
                .build();
    }

    /**
     * I am choosing to write my own sql query for instead of relying on JPA. Then link below best explains
     * making the configurations in application.properties file
     * <p>
     * <a href="https://www.baeldung.com/spring-boot-data-sql-and-schema-sql">...</a>
     * */
    @Bean
    public DataSourceInitializer primaryDataSourceInitializer(@Qualifier(value = "primaryDataSource") DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("database/drop-main.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("database/schema-main.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

        return  dataSourceInitializer;
    }


    @Bean
    @ConfigurationProperties("spring.datasource.session")
    @Qualifier("sessionDataSourceProperties")
    public DataSourceProperties sessionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier("sessionDataSource")
    @SpringSessionDataSource
    public DataSource sessionDataSource(@Qualifier("sessionDataSourceProperties") DataSourceProperties sessionProp) {
        return sessionProp
                .initializeDataSourceBuilder() //
                .type(HikariDataSource.class) //
                .build();
    }

    /**
     * Method helps to define the Session table we are getting from Spring Security
     * Basically populates Session DB with the necessary tables
     * <a href="https://stackoverflow.com/questions/51146269/spring-boot-2-multiple-datasources-initialize-schema">...</a>
     * */
    @Bean
    public DataSourceInitializer sessionDataSourceInitializer(@Qualifier(value = "sessionDataSource") DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("database/schema-drop-mysql.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("database/schema.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

        return  dataSourceInitializer;
    }

    /**
     * Since we are using spring session JDBC for session authentication, we would need to define a template for this
     * Link below explains best
     * <a href="https://docs.spring.io/spring-session/docs/2.4.6/reference/html5/guides/boot-jdbc.html">...</a>
     * */
    @Bean
    public JdbcTemplate sessionTemplate(@Qualifier(value = "sessionDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setExceptionTranslator(new SQLErrorCodeSQLExceptionTranslator(dataSource));
        jdbcTemplate.afterPropertiesSet();
        return jdbcTemplate;
    }

}