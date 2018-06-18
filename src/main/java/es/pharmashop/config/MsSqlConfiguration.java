package es.pharmashop.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MsSqlConfiguration {
  private static final String HIBERNATE_FORMAT = "hibernate.%s";
  private static final String PERSISTENCE_UNIT_NAME = "mssql";

  @Primary
  @Bean(name = "dataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.jpa.hibernate")
  public Map<String, String> msSqlHibernateProperties() {
    return new HashMap<>();
  }

  @Primary
  @Bean(name = "transactionManager")
  PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean
  @Primary
  LocalContainerEntityManagerFactoryBean entityManagerFactory(
    EntityManagerFactoryBuilder builder,
    DataSource dataSource,
    Map<String, String> msSqlHibernateProperties
  ) {

    return builder
      .dataSource(dataSource)
      .properties(
        msSqlHibernateProperties
          .entrySet()
          .stream()
          .collect(Collectors.toMap(
            entry -> String.format(HIBERNATE_FORMAT, entry.getKey()),
            Map.Entry::getValue
          ))
      )
      .packages("es.pharmashop.batch")
      .persistenceUnit(PERSISTENCE_UNIT_NAME)
      .build();
  }

}
