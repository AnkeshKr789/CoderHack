package com.crio.coderhack.entity;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import com.crio.coderhack.dto.RegisteredUser;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Contest {

  @Id
  private String id;
  private Set<RegisteredUser> registeredUsers=new HashSet<>();

  public Contest(){

  }

  public Contest(String id, Set<RegisteredUser> registeredUsers) {
    this.id = id;
    this.registeredUsers = registeredUsers;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setRegisteredUsers(Set<RegisteredUser> registeredUsers) {
    this.registeredUsers = registeredUsers;
  }

  public Set<RegisteredUser> getRegisteredUsers() {
    return registeredUsers;
  }


  public void registerUser(RegisteredUser registeredUser){

    if(registeredUsers.contains(registeredUser)){
      throw new RuntimeException("User with id "+registeredUser.getUserId()+" is enrolled already");
    }

    registeredUsers.add(registeredUser);

  }



  public RegisteredUser deRegisterUser(RegisteredUser registeredUser){

     registeredUsers.remove(registeredUser);
     return registeredUser;

  }

  public Optional<RegisteredUser> getUserById(String id){

    Iterator<RegisteredUser> registeredUserIterator=registeredUsers.iterator();

    while(registeredUserIterator.hasNext()){
      RegisteredUser registeredUser=registeredUserIterator.next();
      if(registeredUser.getUserId().equals(id)){
          return Optional.of(registeredUser);

      }
    }

    return Optional.empty();

  }




  
  
}
