package org.epita.spring.tpmovieapptest.application;

import org.epita.spring.tpmovieapptest.domain.UserEntity;
import org.epita.spring.tpmovieapptest.infrastructure.UserRepository;
import org.epita.spring.tpmovieapptest.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    final UserRepository repository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserEntity createUser(UserEntity u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repository.save(u);
    }
}
