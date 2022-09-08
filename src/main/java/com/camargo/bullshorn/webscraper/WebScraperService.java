package com.camargo.bullshorn.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@EnableScheduling
@EnableAsync
public class WebScraperService {

    @Autowired
    private HeadlineRepository headlineRepository;

    @Scheduled(fixedRate = 900000)
    @Async
     public void getNYTNews(){
        Document document;
        try {
            String URI = "https://www.nytimes.com";
            document = Jsoup.connect(URI + "/section/business").get();

            List<HeadlineDTO> headlineDTOList = new ArrayList<>();

            Elements articles = document.select("article");

            for(Element article : articles){
                HeadlineDTO headlineDTO = new HeadlineDTO();
                headlineDTO.setHeadline(article.select("a").text());
                headlineDTO.setUrl(URI.concat(article.select("a").attr("href")));
                headlineDTO.setCreatedAt(new Date());
                headlineDTOList.add(headlineDTO);
            }

            for(HeadlineDTO news : headlineDTOList){
                saveHeadline(news);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 900000)
    @Async
    public void getYahooNews(){
        Document document;
        try {
            String URI = "https://finance.yahoo.com";
            document = Jsoup.connect(URI + "/topic/stock-market-news/").get();

            List<HeadlineDTO> headlineDTOList = new ArrayList<>();

            Elements articles = document.select("#Fin-Stream li.js-stream-content a");

            for(Element article : articles){
                String newsHeadline = article.text();
                String newsUrl = article.attr("href");
                if(!newsUrl.contains("gemini") && !newsUrl.contains("adinfo")) {
                    HeadlineDTO headlineDTO = new HeadlineDTO();
                    headlineDTO.setHeadline(newsHeadline);
                    headlineDTO.setUrl(URI.concat(newsUrl));
                    headlineDTO.setCreatedAt(new Date());
                    headlineDTOList.add(headlineDTO);
                }
            }

            for(HeadlineDTO news : headlineDTOList){
                saveHeadline(news);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveHeadline(HeadlineDTO news){
        if(!headlineRepository.existsByUrl(news.getUrl())) {
            HeadlineDTO response = headlineRepository.save(news);
            System.out.println(response);
            return;
        }
        System.out.println("Document already exists");
    }

    public List<HeadlineDTO>  findAll(){

        List<HeadlineDTO> headlineList = headlineRepository.findTop30ByOrderByCreatedAtDesc();//findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return headlineList;
    }

    public List<HeadlineDTO> searchTerm(String searchTerm){

        String terms = searchTerm.replace("-", " ");

        List<HeadlineDTO> headlineList = headlineRepository.findContainingTerm(terms);

        List<String> stringList = new ArrayList<>();
        return headlineList;

    }

    public void deleteAll(){
        headlineRepository.deleteAll();
    }

}
