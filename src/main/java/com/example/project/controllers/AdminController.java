package com.example.project.controllers;

import com.example.project.models.Admin;
import com.example.project.models.Client;
import com.example.project.models.Item;
import com.example.project.models.Users;
import com.example.project.security.UsersDetails;
import com.example.project.service.AdminService;
import com.example.project.service.ClientService;
import com.example.project.service.ItemService;
import com.example.project.service.UsersDetailsService;
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
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private Admin admin;
    private final AdminService adminService;
    private final ClientService clientService;
    private final ItemService itemService;
    private final UsersDetailsService usersDetailsService;

    @Autowired
    public AdminController(AdminService adminService, ClientService clientService, ItemService itemService, UsersDetailsService usersDetailsService) {
        this.adminService = adminService;
        this.clientService = clientService;
        this.itemService = itemService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails users = (UsersDetails) authentication.getPrincipal();
        Users user = users.getUser();

        admin = adminService.findByUserId(user.getId());

        List<Item> itemList = itemService.findLast();

        model.addAttribute("itemList", itemList);
        return "admin/index";
    }

    @GetMapping("/new-admin")
    public String newAdmin(@ModelAttribute("admin") Admin admin){
        return "admin/new-admin";
    }

    @PostMapping("/new-admin")
    public String postAdmin(Model model,
                            @ModelAttribute("admin") @Valid Admin admin,
                            BindingResult bindingResult,
                            @RequestParam(name = "password") String pass){

        if(pass == null || pass.length() < 6){
            model.addAttribute("badpass", true);
            return "/admin/new-admin";
        }
        if(bindingResult.hasErrors())
            return "/admin/new-admin";

        admin.setUser(usersDetailsService.register(new Users(null, pass, "ROLE_ADMIN")));

        adminService.save(admin);

        return "redirect:/admin";
    }

    @GetMapping("/client-list")
    public String clientList(Model model){
        List<Client> clientList = clientService.findAll();

        model.addAttribute("clientList", clientList);

        return "admin/client-list";
    }

    @GetMapping("/admin-list")
    public String adminList(Model model){

        List<Admin> adminList = adminService.findAll();

        adminList.removeIf(admin1 -> Objects.equals(admin1.getId(), admin.getId()));

        model.addAttribute("adminList", adminList);

        return "admin/admin-list";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id){
        Admin adminForDelete = adminService.findById(id);
        Integer user_id = adminForDelete.getUser().getId();

        adminService.delete(adminForDelete);

        usersDetailsService.delete(usersDetailsService.loadUserByUsername(user_id.toString()).getUser());

        return "redirect:/admin/admin-list";

    }

    //////////////////////////////////////////////  CLIENT

    @GetMapping("/client/{id}/delete")
    public String deleteClient(@PathVariable(name = "id") Integer id){
        Client client = clientService.findById(id);
        Integer user_id = client.getUser().getId();

        clientService.delete(client);

        usersDetailsService.delete(usersDetailsService.loadUserByUsername(user_id.toString()).getUser());

        return "redirect:/admin/client-list";
    }

    @GetMapping("/client/{id}")
    public String getClient(@PathVariable(name = "id") Integer id,
                            Model model){

        Client client = clientService.findById(id);
        model.addAttribute("client", client);

        List<Item> itemList = itemService.findByClientId(id);

        model.addAttribute("owner", true);
        model.addAttribute("itemList", itemList);

        return "admin/client-info";
    }

}