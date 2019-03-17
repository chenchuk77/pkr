#!/usr/bin/node

// this is a nodejs ranker web server that accept a string of cards in the url
// and reply with hand rank json 
// 
// example request:
// curl -X GET localhost:3000/As2hAd5d4d2s2d
//
// example response:
// rank={"handType":7,"handRank":12,"value":28684,"handName":"full house"}
//
// run as:
// $ ./ranker-webserver.js

// web server params
const http = require('http');
const port = 3000;

// this library can evaluate 7 cards poker hand. example usage:
// var rank = PokerEvaluator.evalHand(["As", "Ks", "Qs", "Js", "Ts", "3c", "5h"]);
// var PokerEvaluator = require("/home/lumos/dev/poker-evaluator/lib/PokerEvaluator.js");
//var PokerEvaluator = require("/home/lumos/dev/SpringWebSocket/ranker/lib/PokerEvaluator.js");
var PokerEvaluator = require("/home/chenchuk/dev/pkr/ranker/lib/PokerEvaluator.js");

// eval hand from array of cards
function rankHand(cards){
  var rank = PokerEvaluator.evalHand(cards);
  return rank;
}

// convert 7 card string to array (example of 2 cards: from "As6h" to ["As", "6h"]
function buildCardArray(sevenCardsString){
  tempcards=[];
  for (var i=0 ; i<14 ; i+=2 ){
    tempcards.push(sevenCardsString[i] + sevenCardsString[i+1])
  }
  return tempcards;
}

// define cards in context
var cards = [];

const requestHandler = (request, response) => {
  var url = request.url;
  console.log("received url: " + url);
  var cardString = url.replace("/","");
  console.log("cardString: " + cardString);
  var rank = rankHand(buildCardArray(cardString));
  console.log("rank: " + JSON.stringify(rank));
  response.end(JSON.stringify(rank));
  // clear context
  cards = [];
}

// start ranker server
const server = http.createServer(requestHandler)
server.listen(port, (err) => {
  if (err) {
    return console.log('something bad happened', err)
  }

  console.log(`server is listening on ${port}`)
})

