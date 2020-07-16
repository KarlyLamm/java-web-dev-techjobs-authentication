package org.launchcode.javawebdevtechjobsauthentication.models;
//bycrypt hash algorithm in build.gradle
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;


@Entity
public class User extends AbstractEntity {
    private String username;
    private String pwHash;

    public User() {}

    //encoder used to create hash from password given:
    public User(String username, String password) {
        this.username = username;
        this.pwHash = encoder.encode(password);
    }
//BCryptPasswordEncoder class.. creates and verifies hashes
    //provided by dependency in build.gradle
    //static so it can be shared by all User objects
    //encoder used to create hash from password
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //encoder.matches method determines if a given password is a match for the hash stored by the object
    //encoder.matches carries out additional steps for salting
    public boolean isMatchingPassword(String password){
        return encoder.matches(password,pwHash);
    }

    public String getUsername() {
        return username;
    }




}