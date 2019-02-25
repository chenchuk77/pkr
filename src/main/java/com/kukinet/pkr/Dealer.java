package com.kukinet.pkr;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Dealer {

    // returns a Hand object from a ranker-service
    public static Hand rankHand(String cardString){
        RestTemplate restTemplate = new RestTemplate();
        String evaluatorUrl= "http://localhost:3000/";
        ResponseEntity<String> response = restTemplate.getForEntity(evaluatorUrl + "/" + cardString, String.class);

        // create Hand object from the reply
        Gson gson = new Gson();
        Hand hand= gson.fromJson(response.getBody(), Hand.class);
        //System.out.println(hand.getHandName());
        return hand;
    }
}
