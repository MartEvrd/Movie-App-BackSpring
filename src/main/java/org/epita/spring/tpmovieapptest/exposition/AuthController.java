package org.epita.spring.tpmovieapptest.exposition;

import org.epita.spring.tpmovieapptest.application.IUserService;
import org.epita.spring.tpmovieapptest.domain.dto.mapper.UserMapper;
import org.epita.spring.tpmovieapptest.domain.dto.user.UserDto;
import org.epita.spring.tpmovieapptest.domain.dto.user.UserPostDto;
import org.epita.spring.tpmovieapptest.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final IUserService service;
    final AuthenticationManager authenticationManager;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final UserMapper userMapper;

    public AuthController(IUserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, UserMapper userMapper) {
        this.service = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public UserDto createUser(@RequestBody UserPostDto uDto){
        return userMapper.entityToUserDto(service.createUser(userMapper.dtoPostToEntity(uDto)));
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody UserPostDto uDto){
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(uDto.username(), uDto.password())
        );

        if(authenticate.isAuthenticated()){
            return ok(this.jwtService.generateToken(uDto.username()));
        }
        return ResponseEntity.badRequest().build();
    }
}
