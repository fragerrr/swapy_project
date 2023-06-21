package com.example.project.service;

import com.example.project.models.Admin;
import com.example.project.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> findAll(){
        return adminRepository.findAll();
    }
    public Admin findByUserId(Integer id){
        return adminRepository.findByUserId(id);
    }

    public Admin findById(Integer id){
        return adminRepository.findById(id).orElse(null);
    }

    public void delete(Admin admin){
        adminRepository.delete(admin);
    }

    public void save(Admin admin){
        adminRepository.save(admin);
    }
}
