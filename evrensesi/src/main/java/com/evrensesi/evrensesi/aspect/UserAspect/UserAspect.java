package com.evrensesi.evrensesi.aspect.UserAspect;

import com.evrensesi.evrensesi.model.User;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserAspect {

     @Before("execution(* com.evrensesi.evrensesi.service.UserCrudService.createUser(..))")
    public void BeforeCreateUser() {
       log.info("Create User method called");
    }

      @AfterReturning(
              pointcut = "execution(* com.evrensesi.evrensesi.service.UserCrudService.createUser(..))",
              returning = "createdUser"
      )
    public void AfterReturningCreateUser(User createdUser) {
          log.info("User '{}' is created", createdUser.getUsername());
      }
}
