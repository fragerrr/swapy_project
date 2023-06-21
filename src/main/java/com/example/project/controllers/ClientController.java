package com.example.project.controllers;

import com.example.project.models.Client;
import com.example.project.models.Item;
import com.example.project.models.Users;
import com.example.project.security.UsersDetails;
import com.example.project.service.ClientService;
import com.example.project.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/client")
public class ClientController {
    private Client client;
    private final ClientService clientService;
    private final ItemService itemService;

    @Autowired
    public ClientController(ClientService clientService, ItemService itemService) {
        this.clientService = clientService;
        this.itemService = itemService;
    }

    @GetMapping()
    public String home(Model model){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UsersDetails user = (UsersDetails) authentication.getPrincipal();
            Users users = user.getUser();

        client = clientService.findByUserId(users.getId());
        List<Item> itemList = itemService.findLast();

        model.addAttribute("client", client);
        model.addAttribute("itemList", itemList);

        return "client/index";
    }

    @GetMapping("/shopping")
    public String shopping(Model model){
        List<Item> itemList = itemService.findAll();

        model.addAttribute("itemList", itemList);

        return "client/shopping";
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/my-items")
    public String myItems(Model model){
        List<Item> itemList = itemService.findByClientId(client.getId());

        model.addAttribute("itemList", itemList);

        return "client/shopping";
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/new-item")
    public String newItem(@ModelAttribute(name = "item") Item item){
        return "client/new-item";
    }

    @PostMapping("/new-item")
    public String postItem(@ModelAttribute(name = "item") @Valid Item item,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "client/new-item";
        } else {
            item.setOwner(client);

            itemService.save(item);

            return "redirect:/client";
        }
    }

    @GetMapping("/details/{id}")
    public String itemInfo(Model model,
                           @PathVariable(name = "id")Integer id){

        Item item = itemService.findById(id);

        model.addAttribute("item", item);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails user = (UsersDetails) authentication.getPrincipal();
        Users users = user.getUser();

        if(users.getRole().equalsIgnoreCase("ROLE_ADMIN") || Objects.equals(item.getOwner().getUser().getId(), users.getId())){
            model.addAttribute("owner", true);
        }

        return "client/item-desc";
    }

}
