package io.project.app.streamer.jpastreamer;

import com.github.javafaker.Faker;
import com.speedment.jpastreamer.application.JPAStreamer;
import io.project.app.domain.User;
import io.project.app.domain.User$;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableJpaRepositories("io.project.app.repositories")
@ComponentScan(basePackages = {"io.project"})
@EntityScan("io.project.app.domain")
@EnableAsync
@Slf4j
public class JpaStreamerApplication {

    @Autowired
    private EntityManager em;

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(JpaStreamerApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    @Transactional
    public void init() {
        log.info("You can once run this for db migration");
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/globus", "postgres", "globus2021").load();
        flyway.clean();
        flyway.migrate();
        Faker faker = new Faker();
        User user = null;
        for (int i = 0; i < 50; i++) {
            faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            user = new User();
            user.setEmail(firstName + "." + lastName + ".@gmail.com");
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setZipcode(faker.address().zipCode());
            user.setMobileno("+101 " + System.currentTimeMillis());
            user.setStatus(i);
            user.setRegisterDate(new Date(System.currentTimeMillis()));
            em.persist(user);
            em.flush();

        }

        for (int i = 0; i < 25; i++) {
            faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            user = new User();
            user.setEmail(firstName + "." + lastName + ".@yahoo.com");
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setZipcode(faker.address().zipCode());
            user.setMobileno("+200 " + System.currentTimeMillis());
            user.setStatus(i);
            user.setRegisterDate(new Date(System.currentTimeMillis()));
            em.persist(user);
            em.flush();

        }

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("streamer");

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("streamer")
                .build();
        jpaStreamer.stream(User.class) // Film.class is the @Entity representing the film-table
                .filter(User$.email.contains("gmail.com"))
                .sorted(User$.status.reversed().thenComparing(User$.email.comparator()))
                .skip(3)
                .limit(50)
                .forEach(System.out::println);

        entityManagerFactory.close();

    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
