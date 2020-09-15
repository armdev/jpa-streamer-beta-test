/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.services;

import com.github.javafaker.Faker;
import com.speedment.jpastreamer.application.JPAStreamer;
import io.project.app.domain.User;
import io.project.app.domain.User$;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author armena
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private EntityManager em;

    private final JPAStreamer jpaStreamer;

    public UserService(JPAStreamer jpaStreamer) {
        this.jpaStreamer = jpaStreamer;
    }

    public void initData() {

        Faker faker = new Faker();
        User user = null;
        for (int i = 0; i < 50; i++) {
            faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            user = new User();
            user.setEmail(firstName + "." + lastName + "@gmail.com");
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
            user.setEmail(firstName + "." + lastName + "@yahoo.com");
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setZipcode(faker.address().zipCode());
            user.setMobileno("+200 " + System.currentTimeMillis());
            user.setStatus(i);
            user.setRegisterDate(new Date(System.currentTimeMillis()));
            em.persist(user);
            em.flush();

        }

    }
    
    
    public void read(){
//           EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("streamer");
//
//        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("streamer")
//                .build();
        jpaStreamer.stream(User.class) // Film.class is the @Entity representing the film-table
                .filter(User$.email.contains("gmail"))
               //// .sorted(User$.status.reversed().thenComparing(User$.email.comparator()))
                .skip(1)
                .limit(50)
                .forEach(System.out::println);

       /// entityManagerFactory.close();

    }

}
