package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> getUser(String username, String password);

    UserEntity findByUserName(String username);

}
