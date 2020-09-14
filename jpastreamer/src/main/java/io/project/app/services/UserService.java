/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.services;

import com.speedment.jpastreamer.application.JPAStreamer;
import javax.persistence.EntityManager;
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
    
    
    public void init(){

    }
    
}
