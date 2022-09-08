package com.camargo.bullshorn.webscraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/news")
public class WebScraperController {

    @Autowired
    private WebScraperService webScraperService;

    /*
    @GetMapping("/getDocument")
    public ResponseEntity<Object> getDocumentById(@RequestParam String newsId){
        Optional<HeadlineDTO> news = webScraperService.findById(newsId);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }
    */

    /*
    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteDocumentById(@RequestParam String newsId){
        webScraperService.deleteById(newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

     */

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllDocuments")
    public ResponseEntity<Object> getAllDocuments(){

        List<HeadlineDTO> headlineList = webScraperService.findAll();

        return new ResponseEntity<>(headlineList, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/searchTerm")
    public ResponseEntity<List<HeadlineDTO>> searchTerm(@RequestParam String term){

        List<HeadlineDTO> suggestionList = webScraperService.searchTerm(term);

        return new ResponseEntity<>(suggestionList, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Object> deleteAll(){

        webScraperService.deleteAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
