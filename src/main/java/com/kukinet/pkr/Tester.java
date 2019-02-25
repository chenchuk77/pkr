package com.kukinet.pkr;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Tester {

    private static String cards = "As4d5d7hKhJc2s";
    private static String jsonTestString = "{\"handType\":5,\"handRank\":2,\"value\":20482,\"handName\":\"straight\"}";

    public static void main(String[] args) {

        System.out.println(Dealer.rankHand(cards).getHandName());

    }

}
