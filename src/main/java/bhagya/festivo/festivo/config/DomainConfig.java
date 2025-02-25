package bhagya.festivo.festivo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("bhagya.festivo.festivo.domain")
@EnableJpaRepositories("bhagya.festivo.festivo.repos")
@EnableTransactionManagement
public class DomainConfig {
}
