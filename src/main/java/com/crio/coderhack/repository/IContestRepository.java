package com.crio.coderhack.repository;

import com.crio.coderhack.entity.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContestRepository extends MongoRepository<Contest,String> {

}
