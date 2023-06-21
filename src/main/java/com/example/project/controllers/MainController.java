package com.example.project.controllers;

import com.example.project.models.Item;
import com.example.project.models.Users;
import com.example.project.security.UsersDetails;
import com.example.project.service.ItemService;
import com.example.project.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

@Controller
public class MainController {
    private Users user;
    private final ItemService itemService;
    private final UsersDetailsService usersDetailsService;

    @Autowired
    public MainController(ItemService itemService, UsersDetailsService usersDetailsService) {
        this.itemService = itemService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping("/home")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        user = usersDetails.getUser();

        if (user.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
            return "redirect:/client";
        }
    }

    @GetMapping("/item/{id}/delete")
    public String delete(@PathVariable("id") Integer id){
        Item item = itemService.findById(id);
        if(user.getRole().equalsIgnoreCase("ROLE_ADMIN") || Objects.equals(item.getOwner().getUser().getId(), user.getId())){
            itemService.delete(id);
            return "redirect:/home";
        } else{
            return "redirect:/403";
        }


    }


    @GetMapping("/403")
    public String error(){
        return "403";
    }
}
