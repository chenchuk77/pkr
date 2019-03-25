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

    // create a validator with the relevant options
    public CommandValidator(Table table, Player player, Pot pot){
        this.table = table;
        this.player = player;
        this.pot = pot;
        this.validOptions = new ArrayList<>();
        this.optionAmounts = new HashMap<>();
        addOptions();
    }

    // loading the validator with valid options
    private void addOptions(){
        addFoldOption();
        if (pot.bets.size() == 0){
            addCheckOption();
            // min bet is bb or player chips, the lowest value
            int min_bet = table.getBigBlind() * 2;
            if (player.getChips() < min_bet){
                min_bet = player.getChips();
            }
            addBetOption(min_bet, player.getChips());
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
                // min raise is bb or player chips, the lowest value
                int min_raise = callValue + table.getBigBlind();
                if (player.getChips() < min_raise){
                    min_raise = player.getChips();
                }
                addRaiseOption(min_raise, player.getChips());
                addAllInOption(player.getChips());
            }
        }
    }

    private void addFoldOption(){
        validOptions.add("fold");
    }

    private void addCallOption(int amount){
        validOptions.add("call");
        optionAmounts.put("call", amount);
    }

    private void addCheckOption(){
        validOptions.add("check");
    }

    private void addBetOption(int min, int max){
        validOptions.add("bet");
        optionAmounts.put("min_bet", min);
        optionAmounts.put("max_bet", max);
    }

    private void addRaiseOption(int min, int max){
        validOptions.add("raise");
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
        cmd.setAmount(0);
        return cmd;
    }

    public boolean isCheckAllowed(){
        return validOptions.contains("check");
    }
}
