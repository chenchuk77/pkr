package com.kukinet.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TesterJson {
    public static void main(String[] args) {
//        JsonObject optionsJSON = new JsonObject();
//        JsonObject optionsJSON = new JsonObject();
//        Gson gson = gsonBuilder.create();

        List<Object> validOptions = new ArrayList<>();
        validOptions.add("fold");
        validOptions.add("check");

        Map<String, Integer> optionAmounts = new HashMap<>();
        optionAmounts.put("max_raise", 4000);
        optionAmounts.put("min_raise", 1000);
        optionAmounts.put("call", 750);
        //validOptions.add("raise_min");


        // list to JSON
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            //String validCommandsJSON = gson.toJson(validOptions);

            JsonObject optionsJSON = new JsonObject();
        optionsJSON.addProperty("type", "waitaction");
        optionsJSON.add("options", new Gson().toJsonTree(validOptions));
        optionsJSON.add("optionAmounts", new Gson().toJsonTree(optionAmounts));

        //optionsJSON.add("options", validOptions);
          //  optionsJSON.addProperty("options", gson.toJson(validOptions));
            //optionsJSON.addProperty(KEY_ARRAY, gson.toJson(list));
System.out.println(optionsJSON.toString());



    }
}
