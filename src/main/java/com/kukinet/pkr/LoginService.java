package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginService {
    Logger logger = LoggerFactory.getLogger(LoginService.class);
    private List<User> users;

    public LoginService(){
        users = Arrays.asList(
                new User("zzz1", "1234", 1000),
                new User("zzz2", "1234", 2222),
                new User("zzz3", "1234", 3333),
                new User("zzz4", "1234", 4444),
                new User("aaa", "1234", 2000),
                new User("bbb", "1234", 2000),
                new User("ccc", "1234", 1000),
                new User("ddd", "1234", 1000),
                new User("eee", "1234", 1000),
                new User("fff", "1234", 1000),
                new User("ggg", "1234", 1000),
                new User("hhh", "1234", 7000),
                new User("iii", "1234", 1000));
    }

    public User login(String name, String password){
        for (User user: users){
            if (user.getName().equals(name)){
                if (user.getPassword().equals(password)){
                    logger.info("user {} authenticated.", name);
                    return user;
                } else { logger.error("incorrect password for user {}.", name); }
            }
        }
        return null;
    }

}
