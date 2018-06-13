package es.pharmashop.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "sqliteEntityManagerFactory",
  transactionManagerRef = "sqliteTransactionManager",
  basePackages = { "es.pharmashop.persistence.sqlite.repository" }
)
public class SqliteConfiguration {

  private static final String PERSISTENCE_UNIT_NAME = "sqlite";
  private static final String ENTITY_PACKAGES = "es.pharmashop.persistence.sqlite";
  private static final String HIBERNATE_FORMAT = "hibernate.%s";

  @Bean
  @ConfigurationProperties(prefix = "sqlite.datasource")
  public DataSource sqliteDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @ConfigurationProperties(prefix = "sqlite.jpa.hibernate")
  public Map<String, String> sqliteHibernateProperties() {
    return new HashMap<>();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean sqliteEntityManagerFactory(
    EntityManagerFactoryBuilder builder,
    DataSource sqliteDataSource,
    Map<String, String> sqliteHibernateProperties
  ) {

    return
      builder
        .dataSource(sqliteDataSource)
        .packages(ENTITY_PACKAGES)
        .properties(
          sqliteHibernateProperties
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
              entry -> String.format(HIBERNATE_FORMAT, entry.getKey()),
              Map.Entry::getValue
            ))
        )
        .persistenceUnit(PERSISTENCE_UNIT_NAME)
        .build();
  }

  @Bean
  public PlatformTransactionManager sqliteTransactionManager(EntityManagerFactory sqliteEntityManagerFactory) {
    return new JpaTransactionManager(sqliteEntityManagerFactory);
  }
}
