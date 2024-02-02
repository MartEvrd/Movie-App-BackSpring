package org.epita.spring.tpmovieapptest.application;

import org.epita.spring.tpmovieapptest.domain.UserEntity;

public interface IUserService {

    UserEntity createUser(UserEntity u);

}
