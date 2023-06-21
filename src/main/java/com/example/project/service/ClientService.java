package com.example.project.service;

import com.example.project.models.Client;
import com.example.project.repository.ClientRepository;
import com.example.project.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientService {
    private final UsersRepository usersRepository;
    private ClientRepository clientRepository;

    @Autowired
    public ClientService(UsersRepository usersRepository, ClientRepository clientRepository) {
        this.usersRepository = usersRepository;
        this.clientRepository = clientRepository;
    }

    public void save(Client client){
        clientRepository.save(client);
    }
    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public void delete(Client client){
        clientRepository.delete(client);
    }

    public Client findById(Integer id){
        return clientRepository.findById(id).orElse(null);
    }
    public Client findByUserId(Integer id){
        return clientRepository.findByUserId(id);
    }
}
