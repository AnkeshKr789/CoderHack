package com.crio.coderhack.controller;

import java.util.List;
import com.crio.coderhack.dto.RegisteredUser;
import com.crio.coderhack.dto.rest_input_format.ScoreUpdate;
import com.crio.coderhack.dto.rest_input_format.UserInput;
import com.crio.coderhack.entity.Contest;
import com.crio.coderhack.exception.ContestProcessingCouldNotProceedException;
import com.crio.coderhack.exception.ContestResourcesNotFoundException;
import com.crio.coderhack.exception.InvalidInputException;
import com.crio.coderhack.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coderhack/api/v1/users")
public class ContestController {

  @Autowired
  private ContestService contestService;


  @GetMapping(value = "/contests")
  public List<Contest> getContests() {

    return contestService.getContests();
  }

  // Register a user to the contest
  // POST request /user
  @PostMapping
  public ResponseEntity<RegisteredUser> registerUser(@RequestBody UserInput userInput) {

    RegisteredUser registeredUser = null;
    try {
      registeredUser = this.contestService.registerUser(userInput);

    } catch (InvalidInputException | ContestProcessingCouldNotProceedException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<RegisteredUser>(registeredUser, HttpStatus.OK);

  }


  // Get all the registered users
  @GetMapping
  public ResponseEntity<List<RegisteredUser>> getRegisteredUsers() {

    List<RegisteredUser> registeredUsers = null;
    try {
      registeredUsers = contestService.getRegisteredUsers();

    } catch (ContestResourcesNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<List<RegisteredUser>>(registeredUsers, HttpStatus.OK);
  }

  // Get a specific registered user
  @GetMapping(value = "/{userId}")
  public ResponseEntity<RegisteredUser> getRegisteredUser(@PathVariable String userId) {

    RegisteredUser registeredUser = null;
    try {
      registeredUser = contestService.getRegisteredUser(userId);

    } catch (ContestResourcesNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    } catch (InvalidInputException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<RegisteredUser>(registeredUser, HttpStatus.OK);

  }

  // update score of registered user
  @PutMapping(value = "/{userId}")
  public ResponseEntity<RegisteredUser> setRegisteredUserScore(@PathVariable String userId,
      @RequestBody ScoreUpdate scoreUpdate) {
    if (scoreUpdate.getScore() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    RegisteredUser registeredUser;
    try {
      registeredUser = contestService.setRegisteredUserScore(userId, scoreUpdate.getScore());
    } catch (InvalidInputException | ContestProcessingCouldNotProceedException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(registeredUser, HttpStatus.OK);
  }


  // get leaderboard
  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> deRegisterUserFromContest(@PathVariable String userId) {
    try {
      contestService.deRegisterUser(userId);
    } catch (InvalidInputException | ContestProcessingCouldNotProceedException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
  }


  @GetMapping(value = "/leaderboard")
  public ResponseEntity<List<RegisteredUser>> getContestLeaderBoard() {

    List<RegisteredUser> registeredUsers = null;

    try {
      registeredUsers = contestService.getContestLeaderBoard();
    } catch (ContestResourcesNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<List<RegisteredUser>>(registeredUsers, HttpStatus.OK);
  }



}
