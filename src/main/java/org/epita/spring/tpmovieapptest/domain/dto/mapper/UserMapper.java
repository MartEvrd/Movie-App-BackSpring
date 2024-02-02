package org.epita.spring.tpmovieapptest.domain.dto.mapper;

import org.epita.spring.tpmovieapptest.domain.UserEntity;
import org.epita.spring.tpmovieapptest.domain.dto.user.UserDto;
import org.epita.spring.tpmovieapptest.domain.dto.user.UserPostDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto entityToUserDto (UserEntity u){
        UserDto uDto = new UserDto(u.getUsername(), u.getMovies());
        return uDto;
    }

    public UserEntity dtoPostToEntity (UserPostDto uPostDto) {
        UserEntity u = new UserEntity(uPostDto.username(), uPostDto.password());
        return u;
    }

}
