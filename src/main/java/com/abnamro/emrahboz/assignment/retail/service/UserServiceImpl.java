package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.UserRepository;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceNotFoundException;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Override
    public Optional<UserEntity> getUser(String username, String password) {
        log.debug("User Name : {} try to execute service", username);

        Optional<UserEntity> userEntity = repository.findByUserName(username);
        if (userEntity.isPresent() && userEntity.get().getPassword().equals(getHashedPassword(password))) {
            log.debug("User Name : {} access granted", username);
            return userEntity;
        }
        log.warn("User Name : {} access denied", username);
        return Optional.empty();
    }

    @Override
    public UserEntity findByUserName(String userName) {
        return repository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Specified User not found. User name is : %s", userName)));
    }

    private String getHashedPassword(String plainPassword) {
        return Hashing.sha256()
                .hashString(plainPassword, StandardCharsets.UTF_8)
                .toString();
    }
}
