package org.epita.spring.tpmovieapptest.domain.dto.user;

import org.epita.spring.tpmovieapptest.domain.MovieEntity;

import java.util.List;

public record UserDto(
        String username,
        List<MovieEntity> movies
) {
}
