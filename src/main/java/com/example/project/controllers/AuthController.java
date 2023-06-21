package com.example.project.controllers;

import com.example.project.models.Client;
import com.example.project.models.Users;
import com.example.project.service.ClientService;
import com.example.project.service.UsersDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController{

    private final UsersDetailsService usersDetailsService;
    private final ClientService clientService;

    @Autowired
    public AuthController(UsersDetailsService usersDetailsService, ClientService clientService) {
        this.usersDetailsService = usersDetailsService;
        this.clientService = clientService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute(name = "client")Client client){
        return "auth/register";
    }

    @PostMapping("/register")
    public String registration(Model model,
                               @ModelAttribute("client") @Valid Client client,
                               BindingResult bindingResult,
                               @RequestParam(name = "password") String pass){
        if(pass == null || pass.length() < 6){
            model.addAttribute("badpass", true);
            return "/auth/register";
        }
        if(bindingResult.hasErrors())
            return "/auth/register";

        client.setUser(usersDetailsService.register(new Users(null, pass, "ROLE_CLIENT")));

        clientService.save(client);

        return "redirect:/auth/login?id=" + client.getUser().getId();
    }
}
