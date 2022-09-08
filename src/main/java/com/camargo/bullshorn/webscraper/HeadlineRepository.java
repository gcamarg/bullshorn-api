package com.camargo.bullshorn.webscraper;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface HeadlineRepository extends MongoRepository<HeadlineDTO, String> {

    boolean existsByUrl(String url);

    @Query("{ 'headline': {'$regex' : /.*?0*/, '$options' : 'i'}}")
    public List<HeadlineDTO> findContainingTerm(String searchTerm);


    public List<HeadlineDTO> findTop30ByOrderByCreatedAtDesc();
}
