package com.kukinet.pkr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommandValidator {
    private Logger logger = LoggerFactory.getLogger(CommandValidator.class);

    private ActionCommand cmd;
    private Table table;
    private Player player;
    private Pot pot;

    private List<Object> validOptions;
    private Map<String, Integer> optionAmounts;

    public CommandValidator(){}

    public CommandValidator(Table table, Player player, Pot pot){
        this.table = table;
        this.player = player;
        this.pot = pot;
        this.validOptions = new ArrayList<>();
        this.optionAmounts = new HashMap<>();
        addOptions();
    }

    private void addOptions(){
        addFoldOption();
        if (pot.bets.size() == 0){
            addCheckOption();
            addBetOption(0, player.getChips());
        } else {
            // call value is the diff
            int maxBet = pot.getMaxBet();
            int callValue = maxBet - player.commited();
            // player doesnt have full calling chips
            if (player.getChips() < callValue){
                callValue = player.getChips();
                addCallOption(callValue);
            // player has more chips than needed to call
            } else {
                addCallOption(callValue);
                addRaiseOption(0, player.getChips());
                addAllInOption(player.getChips());
            }
        }
    }

    private void addFoldOption(){
        validOptions.add("fold");
    }
    private void addCallOption(int amount){
        validOptions.add("call");
        //optionAmounts.put("call", 750);
        optionAmounts.put("call", amount);

    }
    private void addCheckOption(){
        validOptions.add("check");
    }

    private void addBetOption(int min, int max){
        validOptions.add("bet");
//        optionAmounts.put("min_bet", 500);
//        optionAmounts.put("max_bet", 5200);
        optionAmounts.put("min_bet", min);
        optionAmounts.put("max_bet", max);

    }
    private void addRaiseOption(int min, int max){
        validOptions.add("raise");
//        optionAmounts.put("max_raise", 4000);
//        optionAmounts.put("min_raise", 1000);
        optionAmounts.put("max_raise", max);
        optionAmounts.put("min_raise", min);
    }
    private void addAllInOption(int amount){
        validOptions.add("allin");
        optionAmounts.put("allin", amount);
    }

    public String getValidOptions(){
        JsonObject optionsJSON = new JsonObject();
        optionsJSON.addProperty("type", "waitaction");
        optionsJSON.addProperty("player", player.getName());

        optionsJSON.add("options", new Gson().toJsonTree(validOptions));
        optionsJSON.add("optionAmounts", new Gson().toJsonTree(optionAmounts));
        return optionsJSON.toString();

    }

    public ActionCommand validate(ActionCommand cmd) {
        if (cmd.getAction().equals("fold")) {
            if (validOptions.contains("fold")) {
                return cmd;
            }
        }
        if (cmd.getAction().equals("check")) {
            if (validOptions.contains("check")) {
                return cmd;
            }
        }

        if (cmd.getAction().equals("call")) {
            if (validOptions.contains("call") && cmd.getAmount() == optionAmounts.get("call")) {
                return cmd;
            }
        }
        if (cmd.getAction().equals("bet")) {
            if (validOptions.contains("bet") &&
                    cmd.getAmount() <= optionAmounts.get("max_bet") &&
                    cmd.getAmount() >= optionAmounts.get("min_bet")) {
                return cmd;
            }
        }
        if (cmd.getAction().equals("raise")) {
            if (validOptions.contains("raise") &&
                    cmd.getAmount() <= optionAmounts.get("max_raise") &&
                    cmd.getAmount() >= optionAmounts.get("min_raise")) {
                return cmd;
            }
        }
        if (cmd.getAction().equals("allin")) {
            if (validOptions.contains("allin") &&
                    cmd.getAmount() == optionAmounts.get("allin")) {
                return cmd;
            }
        }
        logger.warn("invalid command: {} amount: {}", cmd.getAction(), cmd.getAmount());

        cmd.setAction("invalid");
        return cmd;

    }
}
