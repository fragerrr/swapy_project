package com.example.project.service;

import com.example.project.models.Users;
import com.example.project.repository.UsersRepository;
import com.example.project.security.UsersDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UsersDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersDetailsService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsersDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findById(Integer.parseInt(username));

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User with this id not found");
        }

        return new UsersDetails(user.get());
    }

        public Users register(Users user){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return usersRepository.save(user);
        }

        public void delete(Users users){
            usersRepository.delete(users);
        }
}
