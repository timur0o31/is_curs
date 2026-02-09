import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"controller", "service", "mapper", "repository"})
@EnableJpaRepositories("repository")
public class IsCursApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsCursApplication.class, args);
    }

}
