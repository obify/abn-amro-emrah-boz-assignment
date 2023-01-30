package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.UserRepository;
import com.abnamro.emrahboz.assignment.retail.mock.MockData;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private MockData mockData;
    private UserRepository mockUserRepository;
    private UserService userService;

    @BeforeAll
    public void setup() {

        mockData = new MockData();
        mockUserRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(mockUserRepository);
        Optional<UserEntity> optionalUser = Optional.of(mockData.getCustomerRoleUserEntity());
        Mockito.doReturn(optionalUser)
                .when(mockUserRepository).findByUserName(any(String.class));

    }

    @Test
    public void givenValidUser_whenSelectUser_thenSucceeded() {

        Assertions.assertTrue(userService.getUser("user1", "password1").isPresent());
    }

    @Test
    public void givenInvalidUser_whenSelectUser_thenFailed() {

        Mockito.doReturn(Optional.empty())
                .when(mockUserRepository).findByUserName(any(String.class));

        Assertions.assertFalse(userService.getUser("user1", "password1").isPresent());
    }
}
