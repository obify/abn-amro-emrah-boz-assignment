package com.abnamro.emrahboz.assignment.retail.data.model;

import lombok.*;

import javax.persistence.*;


@Entity(name = "UserEntity")
@Table(name = "USERS")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private String role;


    public UserEntity() {

    }
}
