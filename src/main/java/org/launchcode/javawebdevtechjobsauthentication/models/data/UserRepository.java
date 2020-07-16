package org.launchcode.javawebdevtechjobsauthentication.models.data;

import org.launchcode.javawebdevtechjobsauthentication.models.User;
import org.springframework.data.repository.CrudRepository;

//Repo to access User objects stored in database
//method takes a username and returns the given username to user
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}