package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import static java.rmi.server.LogStream.log;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String showUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "userPanel";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("thisUser", thisUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "adminPanel";
    }

    @PostMapping("/admin/create")
    public String creat(@ModelAttribute("newUser") User user,
                        @RequestParam(value = "identifiers", required = false) Long[] identifiers) {
        if (identifiers != null) {
            for (Long roleId : identifiers) {
                Role role = roleService.findRoleById(roleId);
                if (role != null) {
                    user.addRole(role);
                } else {
                    log("Role not found");
                }
            }
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String update(@ModelAttribute("newUser") User user, @RequestParam(value = "index", required = false) Long[] identifiers) {

        if (identifiers != null) {
            for (Long roleId : identifiers) {
                user.addRole(roleService.findRoleById(roleId));
            }
        } else {
            user.addRole(roleService.findRoleById(2L));
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
