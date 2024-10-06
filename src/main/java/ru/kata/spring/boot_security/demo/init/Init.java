package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void init() {
        Role role1 = new Role("ADMIN");
        Role role2 = new Role("USER");
        roleService.saveRole(role1);
        roleService.saveRole(role2);


        User user1 = new User();
        user1.setName("Иван");
        user1.setSurname("Иванов");
        user1.setEmail("admin@mail.ru");
        user1.setPassword("admin");
        user1.addRole(role1);
        user1.addRole(role2);

        User user2 = new User();
        user2.setName("Петр");
        user2.setSurname("Петров");
        user2.setEmail("user@mail.ru");
        user2.setPassword("user");
        user2.addRole(role2);




        userService.saveUser(user1);
        userService.saveUser(user2);

    }
}