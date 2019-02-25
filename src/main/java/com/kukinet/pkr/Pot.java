package com.kukinet.pkr;

import java.util.*;

public class Pot {
    List<Bet> bets;
    private int mainPot;
    private int foldedPlayersPot;
    private Map<Player,Integer> chipShare;

    public Pot(){
        bets=new ArrayList<>();
        chipShare=new HashMap<Player,Integer>();
        mainPot = 0;
        foldedPlayersPot = 0;
    }


    // add a player bet and sort by commited, small first
    public void addBet(Bet bet){
        bets.add(bet);
        bets.sort(Comparator.comparing(p -> p.getPlayer().commited()));
    }

    // get the best hand score to compare during a showdown
    private int getBestRank(){
        int bestRank = 0;
        for(Bet b:bets){
            if (b.getHandRank() > bestRank){
                bestRank = b.getHandRank();
            }
        }
        return bestRank;
    }

    private List<Player> getWinners(){
        List winners = new ArrayList<Player>();
        for(Bet b:bets){
            if (b.getHandRank() == getBestRank()){
                winners.add(b.getPlayer());
            }
        }
        return winners;
    }

    // for splitting pot among equal rank players
    public int getNumOfWinners(){
        return getWinners().size();
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

    // if raise called totally (no side pot) then we can move the bets to the main pot and clear
    private void clearBetsIfAllCalled(){
        int smallestBet = bets.get(0).getPlayer().commited();
        int totalNumOfBets = bets.size();
        int count = 0;
        for (Bet b: bets){
            if (b.getPlayer().commited() == smallestBet){
                count++;
            }
        }
        if (count == totalNumOfBets){
            mainPot += totalNumOfBets * smallestBet;
            bets = new ArrayList<>();
        }

    }

    // collect the win value from each sidepot and remove the bet if its 0 (means its already paid)
    private void collectFromAllSidePots(int betValue){
        for (Bet b: bets){
            // take the winning share from each bet in the pot
            b.getPlayer().setCommited(b.getPlayer().commited() - betValue);
            if (b.getPlayer().commited() == 0){
                // if the bet is 0, we can safely remove
                bets.remove(b);
            }
        }
    }

    // take the b0 (smallest) and see if p0 wins. if wins we credit him with that bet, and remove the bet.
    // we also need to take the same amount from all other side pots.
    // if not win, we add his bet to the main pot and remove the bet. since this bet removed, this player
    // cannot win any chips ( chips splitted ONLY between players in bets list ).
    public void checkSingleBet(){
        // get smallest bet from top of bets list
        Player p = bets.get(0).getPlayer();
        int betValue = p.commited();
        int betRank = bets.get(0).getHandRank();
        // this player wins, update chipShare
        if (betRank == getBestRank()){
            updateChipShare(p, betValue);
            // remove the record (already handled) and fold the list
            bets.remove(0);
            // get relative value from other bets, and fold the bets if 0
            collectFromAllSidePots(betValue);
            // if not, move the bet to the main pot for eval later by other players
        } else {
            mainPot += betValue;
            bets.remove(0);
        }
    }

    public void splitPot() {
        while (!bets.isEmpty()) {
            checkSingleBet();
        }
    }

    public Map<Player,Integer> getPots(){
        return chipShare;
    }
}