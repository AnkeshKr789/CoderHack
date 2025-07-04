package com.crio.coderhack.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.crio.coderhack.dto.RegisteredUser;
import com.crio.coderhack.dto.rest_input_format.UserInput;
import com.crio.coderhack.entity.Contest;
import com.crio.coderhack.exception.ContestProcessingCouldNotProceedException;
import com.crio.coderhack.exception.ContestResourcesNotFoundException;
import com.crio.coderhack.exception.InvalidInputException;
import com.crio.coderhack.repository.IContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestService {


  @Autowired
  private IContestRepository iContestRepository;


  public ContestService(IContestRepository iContestRepository) {
    this.iContestRepository = iContestRepository;
  }

  public RegisteredUser registerUser(UserInput userInput) throws ContestProcessingCouldNotProceedException, InvalidInputException {

   
    if(userInput.getUserId()==null||userInput.getUserId().isEmpty()){
      throw new InvalidInputException("Invalid user id "+userInput.getUserId());

    }

   
    if(userInput.getUsername()==null||userInput.getUsername().isEmpty()){
      throw new InvalidInputException("Invalid username "+userInput.getUsername());

    }

    // Get the contest

    List<Contest> contests=iContestRepository.findAll();
    Contest contest;
    if(contests.isEmpty()){

      // Create a new Contest

      contest=new Contest();

    }else{
      contest=contests.get(0);
    }


    // Check if user is already registered
    Set<RegisteredUser> registeredUserSet=contest.getRegisteredUsers();

    RegisteredUser registeredUser=new RegisteredUser(userInput.getUsername(), userInput.getUserId());



    if(registeredUserSet.contains(registeredUser)){
      throw new ContestProcessingCouldNotProceedException("User "+userInput.getUsername()+" with id "+userInput.getUserId()+" is registered already");

    }

    // Add the registered user to the contest

    contest.registerUser(registeredUser);
    

    
    Contest updatedContest;
    if(contests.isEmpty()){
      updatedContest=iContestRepository.insert(contest);
    }else{
      updatedContest=iContestRepository.save(contest);
    }

    Optional<RegisteredUser> registeredUserOptional=updatedContest.getUserById(userInput.getUserId());

    if(registeredUserOptional.isEmpty()){
      throw new ContestProcessingCouldNotProceedException("Registered user with id"+userInput.getUserId()+" does not exist");

    }

    return updatedContest.getUserById(userInput.getUserId()).get();
  }


  public List<RegisteredUser> getRegisteredUsers() throws ContestResourcesNotFoundException {

       List<Contest> contests = iContestRepository.findAll();

      
       if(contests.isEmpty()){
         throw new ContestResourcesNotFoundException("No contest is being held currently");
      
       }

       Contest contest=contests.get(0);

       Set<RegisteredUser> registeredUserSet=contest.getRegisteredUsers();
       List<RegisteredUser> registeredUsers=new ArrayList<>(registeredUserSet);

       return registeredUsers;
  }


  // Get a specific registered user

  public RegisteredUser getRegisteredUser(String userId) throws ContestResourcesNotFoundException, InvalidInputException {

   
    if(userId==null||userId.isEmpty()){
      throw new InvalidInputException("Invalid userId with id "+userId);
     
    }

    // Retreive the contest and fetch the registered users
    List<Contest> contests = iContestRepository.findAll();

    // 404
    if(contests.isEmpty()){
      throw new ContestResourcesNotFoundException("No contest is being held currently");
    
    }

    Contest contest=contests.get(0);

    Optional<RegisteredUser> registeredUserOptional=contest.getUserById(userId);

    
    if(registeredUserOptional.isEmpty()){
      throw new ContestResourcesNotFoundException("User with id "+userId+" is not registered in the contest");
    
    }


    return registeredUserOptional.get();
  }

  // update score of registered user

  public RegisteredUser setRegisteredUserScore(String userId,String score) throws InvalidInputException, ContestProcessingCouldNotProceedException {


    if(userId==null||userId.isEmpty()){
      throw new InvalidInputException("Invalid user with id "+userId);
     
    }

    int newScore=0;
    try {
      newScore = Integer.parseInt(score);
    }catch(Exception e){

      throw new InvalidInputException("Invalid score with value "+score);
    
    }


    if(newScore<=0||newScore>100){
      throw new ContestProcessingCouldNotProceedException(newScore+" is out of range ,score range should be between 1 and 100");
    
    }

    // Retreive the contest and fetch the registered users
    List<Contest> contests = iContestRepository.findAll();


    if(contests.isEmpty()){
      throw new ContestProcessingCouldNotProceedException("No contest is being held currently");
    
    }

    Contest contest=contests.get(0);

    Optional<RegisteredUser> registeredUserOptional=contest.getUserById(userId);


    if(registeredUserOptional.isEmpty()){
      throw new ContestProcessingCouldNotProceedException("User with id "+userId+" is not registered in the contest");
    
    }


    RegisteredUser registeredUser=contest.deRegisterUser(registeredUserOptional.get());

    registeredUser.setScore(newScore);

    // Contest has updated registered user
    contest.registerUser(registeredUser);

    // update the database

    iContestRepository.save(contest);


    return registeredUser;
    
  }

  public void deRegisterUser(String userId) throws InvalidInputException, ContestProcessingCouldNotProceedException {

   
    if(userId==null || userId.isEmpty()){
      throw new InvalidInputException("Invalid user with id "+userId);
     
    }

    List<Contest> contests = iContestRepository.findAll();


    if(contests.isEmpty()){
      throw new ContestProcessingCouldNotProceedException("No contest is being held currently");
    
    }

    Contest contest=contests.get(0);

    Optional<RegisteredUser> registeredUserOptional=contest.getUserById(userId);


    if(registeredUserOptional.isEmpty()){
      throw new ContestProcessingCouldNotProceedException("user with id "+userId+" has not registered the contest");
    
    }

    contest.deRegisterUser(registeredUserOptional.get());

    iContestRepository.save(contest);

  }

  // get leaderboard


  public List<RegisteredUser> getContestLeaderBoard() throws ContestResourcesNotFoundException {

    List<Contest> contests = iContestRepository.findAll();

    // 404
    if(contests.isEmpty()){
      throw new ContestResourcesNotFoundException("No contest is being held currently");
   
    }

    Contest contest=contests.get(0);

    List<RegisteredUser> registeredUsers = new ArrayList<>(contest.getRegisteredUsers());


    Comparator<RegisteredUser> decreasingScoresOrderComparator=(r1,r2)->r2.getScore()-r1.getScore();

    Collections.sort(registeredUsers,decreasingScoresOrderComparator);

    return registeredUsers;

  }

  public List<Contest> getContests(){

    return iContestRepository.findAll();
  }
}
