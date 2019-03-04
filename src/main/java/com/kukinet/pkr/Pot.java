package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Pot {
    List<Bet> bets;
    private int mainPot;
    private int foldedPlayersPot;
    private Map<Player,Integer> chipShare;
    private List<Player> winners;
    private List<Player> players;

    private Logger logger = LoggerFactory.getLogger(Pot.class);


    public Pot(List<Player> players){
        bets=new ArrayList<>();
        winners = new ArrayList<>();
        chipShare=new HashMap<Player,Integer>();
        mainPot = 0;
        foldedPlayersPot = 0;
        this.players=players;
    }


    public void removeBet(Player p) {
        List<Bet> betsToRemove = new ArrayList<>();
        for (Bet b: bets) {
            if (b.getPlayer().equals(p)){
                logger.info("adding {} to mainpot for folded player {}", p.commited(), p.getName());
                mainPot += b.getPlayer().commited();
                logger.info("mainpot: {}", mainPot);
//                bets.remove(b); // concurency problems
                betsToRemove.add(b);
            }
        }
        logger.info("removing {} bets.", betsToRemove.size());
        bets.removeAll(betsToRemove);
    }

    // add/update a player bet and sort by commited, small first
    public void addBet(Bet bet){
        Player p = bet.getPlayer();
        for (Bet b: bets) {
            // update bet if already exists from this player
            if (b.getPlayer().equals(p)) {
                logger.info("bet exist for {}, of {}. adding: {}", b.getPlayer().getName(), b.getPlayer().commited(), bet.getAmount());
                b.getPlayer().setCommited(b.getPlayer().commited() + bet.getAmount());
                //bets.put(bets.get(0).getPlayer(), betValue/getNumOfWinners());
                bets.sort(Comparator.comparing(x -> x.getPlayer().commited()));
                logger.info("sorting bets: {}", bets);
                return;
            }
        }
        // add a new bet (first in this round from this player)
        logger.info("new bet for player: {}, adding: {}", p.getName(), p.commited());
        p.setCommited(bet.getAmount());
        bets.add(bet);
        bets.sort(Comparator.comparing(x -> x.getPlayer().commited()));
        logger.info("sorting bets: {}", bets);

    }


    // get the best hand score to compare during a showdown
    private int getBestHandValue(List<Player> players){
        int bestHandValue = 0;
        for(Player p: players){
            if (p.inHand() && p.getHand().getValue() > bestHandValue){
                bestHandValue = p.getHand().getValue();
            }
        }
        return bestHandValue;
    }

    private void setWinners(){
        logger.info("setting winners from list of players with {} players", players.size());
        winners = new ArrayList<>();
        for(Player p: players){
            if (p.inHand() && p.getHand().getValue() == getBestHandValue(players)){
                winners.add(p);
            }
        }
        logger.info("setting winners contains {} players", winners.size());

    }

    // for splitting pot among equal rank players
    private int getNumOfWinners(){
        return winners.size();
    }

    // update or add a player win record
    private void updateChipShare(Player p, Integer betValue){
        // update player chipshare
        if (chipShare.containsKey(p)){
            chipShare.put(p, chipShare.get(p) + betValue/getNumOfWinners());
            // new record
        } else {
            chipShare.put(bets.get(0).getPlayer(), betValue/getNumOfWinners());
        }

    }

    public void refundUncoveredBet(){
        if (bets.size() < 2) return;
        Player highest = bets.get(bets.size()-1).getPlayer();
        Player beforeHighest = bets.get(bets.size()-2).getPlayer();
        // if raiser have uncovered bet, he got chips back
        if (highest.commited() > beforeHighest.commited()){

            int diff = highest.commited() - beforeHighest.commited();
            highest.setCommited(highest.commited() - diff);
            highest.setChips(highest.getChips() + diff);
            logger.warn("player {} got refund: {}.", highest.getName(), diff);
        }

    }

    // if raise called totally (no side pot) then we can move the bets to the main pot and clear
    public void clearBetsIfAllCalled(){
        if (bets.isEmpty()){
            return;
        }
        int smallestBet = bets.get(0).getPlayer().commited();
        int totalNumOfBets = bets.size();
        int count = 0;
        for (Bet b: bets){
            if (b.getPlayer().commited() == smallestBet){
                count++;
            }
        }
        if (count == totalNumOfBets){
            this.mainPot += totalNumOfBets * smallestBet;
            this.bets = new ArrayList<>();
        }
    }

    // collect all bets.
    // in case a player didnt get called, he takes all pots with no showdown
    public int getAllBets(){
        int total = this.mainPot;
        for (Bet b: bets){
            total += b.getPlayer().commited();
        }
        return total;
    }

    // collect the win value from each sidepot and remove the bet if its 0 (means its already paid)
    private void collectFromAllSidePots(int betValue){
        List<Bet> betsToRemove = new ArrayList<>();
        for (Bet b: bets){
            // take the winning share from each bet in the pot
            b.getPlayer().setCommited(b.getPlayer().commited() - betValue);
            if (b.getPlayer().commited() == 0){
                // if the bet is 0, we can safely remove
                betsToRemove.add(b);
            }
        }
        bets.removeAll(betsToRemove);

    }

    // splitting all bets until it gets empty
    public Map<Player,Integer> splitPot() {

        setWinners();
        List<Player> inHandPlayers = new ArrayList<>();
        for (Player p: players){
            if (p.inHand()){
                inHandPlayers.add(p);
            }
        }
        logger.warn("split pot to {} player/s." , inHandPlayers.size());
        // only 1 player take it all
        if (inHandPlayers.size() == 1){
            int winningChips = getAllBets();
            Player winner = inHandPlayers.get(0);
            chipShare.put(winner, winningChips);
            logger.warn("chipshare for 1 player: {}." , showChipshare());
            winner.setChips(winner.getChips() + winningChips);
            return chipShare;
        }
        // more than 1 player, empty bets
        logger.warn("split {} bets to {} winners" , bets.size(), winners.size());

        while (!bets.isEmpty()) {
            splitSingleBet();
        }
        // split main pot
        logger.warn("split main pot of {} to players: {}." , mainPot, showWinners());

        for (Player p: winners){
            // update player chipshare
            if (chipShare.containsKey(p)){
                chipShare.put(p, chipShare.get(p) + mainPot/getNumOfWinners());
            // new record
            } else {
                chipShare.put(p, mainPot/getNumOfWinners());
            }
        }
        logger.warn("chipshare for many player: {}." , showChipshare());
        // charge winners
        for (Player winner: chipShare.keySet()){
            winner.setChips(winner.getChips() + chipShare.get(winner));
        }
        return chipShare;

    }

    private void creditPlayers(){
        for (Player winner: chipShare.keySet()){
            winner.setChips(winner.getChips() + chipShare.get(winner));
        }
    }
    private String showWinners(){
        StringBuilder sb = new StringBuilder();
        if (!winners.isEmpty()){
            sb.append("[");
            for (Player p: winners){
                sb.append(p.getName()).append(", ");
            }
            sb.append("]");
        }
        return sb.toString();
    }

    private String showChipshare(){
        StringBuilder sb = new StringBuilder();
        if (!chipShare.isEmpty()){
            sb.append("[");
            for (Map.Entry<Player,Integer> entry: chipShare.entrySet()){
                sb.append(entry.getKey().getName())
                        .append(":")
                        .append(entry.getValue())
                        .append(", ");

            }
            sb.append("]");
        }
        return sb.toString();
    }
    // take the b0 (smallest) and see if p0 wins. if wins we credit him with that bet, and remove the bet.
    // we also need to take the same amount from all other side pots.
    // if not win, we add his bet to the main pot and remove the bet. since this bet removed, this player
    // cannot win any chips ( chips splitted ONLY between players in bets list ).
    public void splitSingleBet(){

        // get smallest bet from top of bets list
        Player p = bets.get(0).getPlayer();
        int amount = p.commited();
        int handValue = bets.get(0).getPlayer().getHand().getValue();
        // this player wins, update chipShare
        if (handValue == getBestHandValue(players)){
            updateChipShare(p, amount);
            // remove the record (already handled) and fold the list
            bets.remove(0);
            // get relative value from other bets, and fold the bets if 0
            collectFromAllSidePots(amount);
            // if not, move the bet to the main pot for eval later by other players
        } else {
            mainPot += amount;
            bets.remove(0);
        }
    }

    public Map<Player,Integer> singlePlayerWin(){
        for (Bet b: bets){
            Player p = b.getPlayer();
            if (p.inHand()){
                chipShare.put(p, getAllBets());
//                p.setStartingStack(p.getChips());
            }
        }
        return chipShare;
    }

//    public Map<Player,Integer> getPots(){
//        return chipShare;
//    }


//    public int getMainPot() {
//        return mainPot;
//    }
}