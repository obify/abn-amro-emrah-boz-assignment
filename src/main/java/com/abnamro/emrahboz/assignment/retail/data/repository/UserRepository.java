package com.abnamro.emrahboz.assignment.retail.data.repository;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
}
