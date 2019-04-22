// this file is UI tester

$( document ).ready(function() {
    $('#loggedOut').show('slow');
    $('#loggedIn').show('slow');
    // Add the PIXI canvas
    document.body.appendChild(app.view);

});


let seats = [];
let pocketCards;
let communityCards;
let bets;
let buttons;
let options;
let optionAmouns;
//let statusMessage;

//var connection = new WebSocket('ws://192.168.2.39:4444');

// connection.onopen = function () {
//     console.log('client connected.');
//     connection.send('hello from javascript client.'); // Send the message 'Ping' to the server
// };
//
// connection.onerror = function (error) {
//     console.log('WebSocket Error ' + error);
// };
//
// connection.onmessage = function (e) {
//     console.log('server: ' + e.data);
//     if (e.data.includes('ante')) {
//         bets = JSON.parse(e.data);
//         console.log('bets update received.');
//     }
//     if (e.data.includes('dealerPosition')) {
//         buttons = JSON.parse(e.data);
//         console.log('buttons update received.');
//         drawButtons(buttons);
//     }
//     if (e.data.includes('chips')) { // TODO !!! split to 2 messages (playermove / table)
//         seats = JSON.parse(e.data);
//         console.log('seats update received.');
//         updateTable();
//     }
//     if (e.data.includes('card1')) {
//         pocketCards = JSON.parse(e.data);
//         console.log('pocketCards update received.');
//         player.cards=[pocketCards.card1, pocketCards.card2];
//         drawPocketCards(seats);
//     }
//     if (e.data.includes('status')) {
//         statusMessage = JSON.parse(e.data);
//         console.log('status message received.');
//         drawStatusMessage(statusMessage);
//     }
//
//     // if (message.contains("status")) {
//     //     System.out.println("status update received.");
//     //     JsonObject statusJSON = new Gson().fromJson(message, JsonObject.class);
//     //     statusMessage = statusJSON.get("value").getAsString();
//     //     setStatusMessage(statusMessage);
//     // }
//
//
//
//     if (e.data.includes('community')) {
//         communityCards = JSON.parse(e.data);
//         console.log('communityCards update received.');
//         drawFlop(communityCards.flop1, communityCards.flop2, communityCards.flop3);
//         console.log('flop cards: ' + communityCards.flop1 + ' ' + communityCards.flop2 + ' ' +communityCards.flop1 + '.');
//         if (communityCards.hasOwnProperty('turn')){
//             console.log('turn card: ' + communityCards.turn);
//             drawTurn(communityCards.turn);
//         }
//         if (communityCards.hasOwnProperty('river')){
//             console.log('river card: ' + communityCards.river);
//             drawRiver(communityCards.river);
//         }
//     }
//
//     // server notify it waits for a player command
//     if (e.data.includes('waitaction')) {
//         var commandJSON = JSON.parse(e.data);
//         console.log('waitaction command received.');
//         // server waits for us
//         if (player.name === commandJSON.player){
//             console.log("server is waiting for our command.");
//             console.log("updating ctx options/amounts.");
//             options = commandJSON.options;
//             optionAmouns = commandJSON.optionAmounts;
//
//
//
//             drawActionButtons();
//         } else {
//             console.log("its " + commandJSON.player + "'s turn.");
//         }
//     }
//
//
//     // received: {"command":"waitaction","player":"fff"}
//
// };

// player is a seated user
let user;   // the logged-in user
//let player;
let player = {
    name: '',
    cards: []
};


// var login = function(){
//     var username = $('#username').val();
//     var password = $('#password').val();
//     connection.send("login" + "," + username + "," + password);
//     console.log('client logged in.');
//     user = username;
//     player.name = username;
//     $('#loggedOut').show();
//     connection.send('statusrequest');
// };
//
// var logout = function(){
//     var username = $('#username').val();
//     connection.send("logout" + "," + username);
//     console.log('client logout requested.');
// };
//
// var register = function(){
//     var username = $('#username').val();
//     var gamename = $('#gamename').val();
//     connection.send("register" + "," + username + ',' + gamename);
//     console.log('client registration requested.');
// };

// var startGame = function(){
//     var gamename = $('#gamename').val();
//     connection.send("start-game" + "," + gamename);
//     console.log('start-game requested [ADMIN].');
// };

// var createGame = function(){
//     var username = $('#username').val();
//     var gamename = $('#gamename').val();
//     connection.send("register" + "," + username + ',' + gamename);
// console.log('create-game requested [ADMIN].');
// };

//
//
// var bet = function(){
//     // connection.send('bet: 300');
//     actionCommand = {"action": "bet", "amount": 500};
//     connection.send(actionCommand);
//
//
// };
// var raise = function(){
//     // connection.send('raise: 700');
//     actionCommand = {"action": "raise", "amount": 700};
//     connection.send(actionCommand);
//
// };
// var fold = function(){
//     actionCommand = {"action": "fold", "amount": 0};
//     // connection.send('fold');
//     connection.send(JSON.stringify(actionCommand));
//     // actionJSON.addProperty("action", "fold");
//     // actionJSON.addProperty("amount", 0);
//
//
// };
// var call = function(){
//     // connection.send('call: 300');
//     actionCommand = {"action": "call", "amount": 0};
//     // connection.send('fold');
//     connection.send(actionCommand);
//
// };

// // use this to send commands (will be replaced by action buttons when implemented)
// function sendAction(cmd, amount){
//
//     console.log(cmd);
//     console.log(amount);
//     console.log("cmd:" + cmd + ", amount: " + amount);
//     //console.log("cmd:" + cmd.type() + ", amount: " + amount.type());
//
//     // if (str.contains(',')) {
//     //cmd = str.split(',')[0];
//    // amount = parseInt(str.split(',')[1]);
//     actionCommand = {"action": cmd, "amount": amount};
//     console.log(actionCommand);
//
//
//     console.log("sending command: " + actionCommand);
//     connection.send(JSON.stringify(actionCommand));
//
//     // } else {
//     //     console.log("unsupported... use full commands. ie: call,50 or fold,0 ")
//     // }
//
// }

//////////////// PIXI APPLICATION ///////////////////

// Create a Pixi Application
let app = new Application({
        width: 800,
        height: 600,
        antialiasing: true,
        transparent: false,
        resolution: 1
    }
);
// // Add the PIXI canvas
// document.body.appendChild(app.view);


//load all images and invoke setup() when done;
PIXI.loader
    .add(imageFiles)
    .load(setup);

// function drawTable(){
//     table = new Sprite(resources['images/other/poker_table.png'].texture);
//     app.stage.addChild(table);
// }

//This `setup` function will run when the image has loaded
function setup() {
    console.log('setup() called.');
    drawTable();
    createSeatsContainers();

    // normal, no cards
    // let data={"0":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":10000,"commited":0,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"1":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"fff","chips":10000,"commited":0,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"2":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"iii","chips":10000,"commited":0,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"3":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"ddd","chips":10000,"commited":0,"isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}}

    // eee face up
    let data={"0":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":10000,"commited":0,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"5c","strHole2":"Kd"},"1":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"fff","chips":10000,"commited":0,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"2":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"iii","chips":10000,"commited":0,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"3":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"ddd","chips":10000,"commited":0,"isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}}

    updateFull(data);
    //addUserContainers();





    //updateCommited(c1, 4444);
    //updateChips(c2, 8000);
    //updateCommited(c1, 30);

    //player.cards=['Kd','8s'];
    //drawAllCards(players);

    //drawFlop('Ts', '2h', 'As');
    //Set the game state
    state = play;

    //Start the game loop
    app.ticker.add(delta => gameLoop(delta));
}


function gameLoop(delta){
    //Update the current game state:
    state(delta);
}
function play(delta) {
    //console.log('in play loop...')
}



function makeTextSprite(name) {

    var style = {
        // font: 'bold italic 36px Arial'
        font: 'bold 24px Arial',
        fill: '#F7EDCA',
        stroke: '#4a1850',
        strokeThickness: 5,
        dropShadow: true,
        dropShadowColor: '#000000',
        dropShadowAngle: Math.PI / 6,
        dropShadowDistance: 6//,
        //wordWrap: true,
        //wordWrapWidth: 440,

    };

    // return new PIXI.Text(name, style);
    var x = new PIXI.Text(name, style);
    //     statusMessage.x = 10;
//     statusMessage.y = 550;
    return x;
}


// function circleSprite(){
//     var p = new PIXI.Graphics();
//     p.beginFill(0x000000);
//     p.lineStyle(0);
//     p.drawCircle(100, 100, 10);
//     p.endFill();
//
//     var t = PIXI.RenderTexture.create(p.width, p.height);
//     PIXI.renderer.render(p, t);
//
//     var sprite = new PIXI.Sprite(t);
//     sprite.x = 100;
//     return sprite;
// }

var c1;
var c2;
var c3;
var c4;


function updateCommited(container, commited){
    container.getChildByName("committed").text = commited;

}

function updateChips(container, chips){
    container.getChildByName("chips-behind").text = chips;

}


// function getChipsContainer() {
//     var chips = new PIXI.Container();
//     // chips.scale.x = 0.7;
//     // chips.scale.y = 0.7;
//     // chips.position.set(x, y);
//
//     var stackableChip = new Sprite(resources['images/chips/trans_b-stack.png'].texture);
//     stackableChip.position.set(50, 60);
//
//     var topChip = new Sprite(resources['images/chips/trans_b-top.png'].texture);
//     topChip.position.set(50, 50);
//
//     chips.addChild(stackableChip);
//     chips.addChild(top);
//
//
//     return chips;
// }

//
function makeCommunityCardsContainer(flop1, flop2, flop3, turn, river) {
    // create container for deck
    var commCards = new PIXI.Container();
    commCards.name = "community-ctr";
    commCards.name = "deck-ctr";
    commCards.scale.x = 0.5;
    commCards.scale.y = 0.5;

    if (flop1 !== undefined){
        var flop1_card = getCardSprite(flop1);
        flop1_card.position.set(100, 0);
        // flop1_card.scale.x = 0.5;
        // flop1_card.scale.y = 0.5;
        commCards.addChild(flop1_card);
    }
    if (flop2 !== undefined){
        var flop2_card = getCardSprite(flop2);
        flop2_card.position.set(200, 0);
        // flop2_card.scale.x = 0.5;
        // flop2_card.scale.y = 0.5;
        commCards.addChild(flop2_card);
    }
    if (flop3 !== undefined){
        var flop3_card = getCardSprite(flop3);
        flop3_card.position.set(300, 0);
        // flop3_card.scale.x = 0.5;
        // flop3_card.scale.y = 0.5;
        commCards.addChild(flop3_card);
    }
    if (turn !== undefined){
        var turn_card = getCardSprite(turn);
        turn_card.position.set(450, 0);
        // turn_card.scale.x = 0.5;
        // turn_card.scale.y = 0.5;
        commCards.addChild(turn_card);
    }
    if (river !== undefined){
        var river_card = getCardSprite(river);
        river_card.position.set(600, 0);
        // river_card.scale.x = 0.5;
        // river_card.scale.y = 0.5;
        commCards.addChild(river_card);
    }
    return commCards;
}




function makeDeckContainer() {
    // create container for deck
    var deck = new PIXI.Container();
    deck.name = "deck-ctr";
    deck.scale.x = 0.5;
    deck.scale.y = 0.5;

    var cards = new Sprite(resources['images/cards/back.svg'].texture);
    cards.position.set(0, 0);
    cards.scale.x = 0.5;
    cards.scale.y = 0.5;
    deck.addChild(cards);

    return deck;

}

function makeChipsContainerButtomUp(amount){
    var reminder = amount;
    var chips_1000 = 0;
    var chips_500  = 0;
    var chips_100  = 0;
    var chips_25   = 0;
    var chips_5    = 0;
    var chips_1    = 0;

    chips_1000 = Math.floor(reminder / 1000);
    reminder   = reminder - chips_1000 * 1000;
    chips_500 = Math.floor(reminder / 500);
    reminder   = reminder - chips_500 * 500;
    chips_100  = Math.floor(reminder / 100);
    reminder   = reminder - chips_100 * 100;
    chips_25   = Math.floor(reminder / 25);
    reminder   = reminder - chips_25 * 25;
    chips_5   = Math.floor(reminder / 5);
    chips_1   = reminder - chips_5 * 5;

    console.log("1000:" + chips_1000 + "\n" +
        "100:" + chips_100 + "\n" +
        "25:" + chips_25 + "\n" +
        "5:" + chips_5 + "\n" +
        "1:" + chips_1 + "\n");

    // create container for chips
    var chips_ctr = new PIXI.Container();
    chips_ctr.name = "player-chips";

    chips_ctr.scale.x = 0.5;
    chips_ctr.scale.y = 0.5;

    // stack all other chips
    var total_chips = chips_1000 + chips_100 + chips_25 + chips_5 + chips_1;
    var offset = 150;

    for (var i=0; i<chips_1; i++){
        var stackableChips1 = new Sprite(resources['images/chips/chip1_stack.png'].texture);
        offset -= 10;
        stackableChips1.position.set(50, offset);
        stackableChips1.scale.x = 0.2;
        stackableChips1.scale.y = 0.2;
        chips_ctr.addChild(stackableChips1);
    }
    for (var i=0; i<chips_5; i++){
        var stackableChips5 = new Sprite(resources['images/chips/chip5_stack.png'].texture);
        offset -= 10;
        stackableChips5.position.set(50, offset);
        stackableChips5.scale.x = 0.2;
        stackableChips5.scale.y = 0.2;
        chips_ctr.addChild(stackableChips5);
    }
    for (var i=0; i<chips_25; i++){
        var stackableChips25 = new Sprite(resources['images/chips/chip25_stack.png'].texture);
        offset -= 10;
        stackableChips25.position.set(50, offset);
        stackableChips25.scale.x = 0.2;
        stackableChips25.scale.y = 0.2;
        chips_ctr.addChild(stackableChips25);
    }
    for (var i=0; i<chips_100; i++){
        var stackableChips100 = new Sprite(resources['images/chips/chip100_stack.png'].texture);
        offset -= 10;
        stackableChips100.position.set(50, offset);
        stackableChips100.scale.x = 0.2;
        stackableChips100.scale.y = 0.2;
        chips_ctr.addChild(stackableChips100);
    }
    for (var i=0; i<chips_500; i++){
        var stackableChips500 = new Sprite(resources['images/chips/chip500_stack.png'].texture);
        offset -= 10;
        stackableChips500.position.set(50, offset);
        stackableChips500.scale.x = 0.2;
        stackableChips500.scale.y = 0.2;
        chips_ctr.addChild(stackableChips500);
    }
    for (var i=0; i<chips_1000; i++){
        var stackableChips1000 = new Sprite(resources['images/chips/chip1000_stack.png'].texture);
        offset -= 10;
        stackableChips1000.position.set(50, offset);
        stackableChips1000.scale.x = 0.2;
        stackableChips1000.scale.y = 0.2;
        chips_ctr.addChild(stackableChips1000);
    }
    // add top chip : FIXED COLOR for now
    var topChip = new Sprite(resources['images/chips/chip100_top.png'].texture);
    topChip.position.set(50, offset-10);
    topChip.scale.x = 0.2;
    topChip.scale.y = 0.2;
    chips_ctr.addChild(topChip);

    // add amount in text neat chips
    var amountText = new PIXI.Text(amount, {font: '38px Arial', fill: '#FFFFFF'});
    amountText.position.set(90, 130);
    chips_ctr.addChild(amountText);


    return chips_ctr;
}


// return new PIXI.Text(name, style);

// seats is positioned container. it has absolute position on the table
function createSeatsContainers(){

    seats[0] = new PIXI.Container();
    seats[0].name = "seat-0";
    seats[0].x=350;
    seats[0].y=350;
    addRect(seats[0]);
    app.stage.addChild(seats[0]);

    seats[1] = new PIXI.Container();
    seats[1].name = "seat-1";
    seats[1].x=200;
    seats[1].y=350;
    addRect(seats[1]);
    app.stage.addChild(seats[1]);

    seats[2] = new PIXI.Container();
    seats[2].name = "seat-2";
    seats[2].x=50;
    seats[2].y=200;
    addRect(seats[2]);
    app.stage.addChild(seats[2]);

    seats[3] = new PIXI.Container();
    seats[3].name = "seat-3";
    seats[3].x=200;
    seats[3].y=50;
    addRect(seats[3]);
    app.stage.addChild(seats[3]);

    seats[4] = new PIXI.Container();
    seats[4].name = "seat-4";
    seats[4].x=300;
    seats[4].y=50;
    addRect(seats[4]);
    app.stage.addChild(seats[4]);

    seats[5] = new PIXI.Container();
    seats[5].name = "seat-5";
    seats[5].x=400;
    seats[5].y=50;
    addRect(seats[5]);
    app.stage.addChild(seats[5]);

    seats[6] = new PIXI.Container();
    seats[6].name = "seat-6";
    seats[6].x=500;
    seats[6].y=50;
    addRect(seats[6]);
    app.stage.addChild(seats[6]);


    seats[7] = new PIXI.Container();
    seats[7].name = "seat-7";
    seats[7].x=650;
    seats[7].y=200;
    addRect(seats[7]);
    app.stage.addChild(seats[7]);

    seats[8] = new PIXI.Container();
    seats[8].name = "seat-8";
    seats[8].x=500;
    seats[8].y=350;
    addRect(seats[8]);
    app.stage.addChild(seats[8]);



}
// tester
function addRect(container) {
    let rectangle = new PIXI.Graphics();
    rectangle.lineStyle(4, 0x44e300, 1);
    rectangle.beginFill(0x99CCFF);
    rectangle.drawRect(0, 0, 64, 64);
    rectangle.endFill();
    // rectangle.x = 0;
    // rectangle.y = 0;
    container.addChild(rectangle);

}


// player container
function createPlayerContainer(seatNumber, avatarFile, name, chips, committed, card1, card2){
    let userContainer = new PIXI.Container();
    userContainer.scale.x=0.7;
    userContainer.scale.y=0.7;

    // player name and avatar
    let nameSprite = makeTextSprite(name);
    nameSprite.position.set(20, 260);
    userContainer.addChild(nameSprite);
    let avatarSprite = new Sprite(resources[avatarFile].texture);
    avatarSprite.position.set(10, 160);
    avatarSprite.scale.x = 0.4;
    avatarSprite.scale.y = 0.4;
    userContainer.addChild(avatarSprite);

    // chips
    let chipsSprite = makeTextSprite(chips);
    chipsSprite.name = "chips-behind";
    chipsSprite.position.set(20, 290);
    userContainer.addChild(chipsSprite);

    // chips committed (text+graphic)
    let committedSprite = makeTextSprite(committed);
    committedSprite.name = "committed";
    committedSprite.position.set(0, 70);
    userContainer.addChild(committedSprite);
    let commitedChipsContainer = makeChipsContainerButtomUp(committed);
    commitedChipsContainer.position.set(0, 0);
    userContainer.addChild(commitedChipsContainer);



    console.log("******###" + card1)
    let card1Sprite = getCardSprite(card1);
    console.log(card1Sprite);
    card1Sprite.position.set(0, 100);
    card1Sprite.scale.x = 0.4;
    card1Sprite.scale.y = 0.4;
    userContainer.addChild(card1Sprite);

    let card2Sprite = getCardSprite(card2);
    card2Sprite.position.set(50, 100);
    card2Sprite.scale.x = 0.4;
    card2Sprite.scale.y = 0.4;
    userContainer.addChild(card2Sprite);

    // var chipCont = makeChipsContainer();
    // let chipCont = makeChipsContainerButtomUp(committed);
    // chipCont.position.set(0, 0);
    //
    // userContainer.addChild(chipCont);
    //userContainer.removeChild(chipCont);

    return userContainer;
    // var circle = circleSprite();
    // circle.position.set(20, 190);
    // userContainer.addChild(circle);

}

function getUserContainer(avatarFile, name, chips, card1, card2, x, y){
    var userContainer = new PIXI.Container();
    userContainer.scale.x=0.7;
    userContainer.scale.y=0.7;
    userContainer.position.set(x, y);

    var avatarSprite = new Sprite(resources[avatarFile].texture);
    avatarSprite.position.set(10, 160);
    avatarSprite.scale.x = 0.4;
    avatarSprite.scale.y = 0.4;
    // avatarSprite.border
    userContainer.addChild(avatarSprite);


    var commitedSprite = makeTextSprite("");
    commitedSprite.name = "committed";
    commitedSprite.position.set(0, 70);
    userContainer.addChild(commitedSprite);

    var nameSprite = makeTextSprite(name);
    nameSprite.position.set(20, 260);
    userContainer.addChild(nameSprite);

    // var circle = circleSprite();
    // circle.position.set(20, 190);
    // userContainer.addChild(circle);

    var chipsSprite = makeTextSprite("");
    chipsSprite.name = "chips-behind";
    chipsSprite.position.set(20, 290);
    userContainer.addChild(chipsSprite);

    var card1Sprite = getCardSprite(card1);
    card1Sprite.position.set(0, 100);
    card1Sprite.scale.x = 0.4;
    card1Sprite.scale.y = 0.4;
    userContainer.addChild(card1Sprite);

    var card2Sprite = getCardSprite(card2);
    card2Sprite.position.set(50, 100);
    card2Sprite.scale.x = 0.4;
    card2Sprite.scale.y = 0.4;
    userContainer.addChild(card2Sprite);

    // var chipCont = makeChipsContainer();
    var chipCont = makeChipsContainerButtomUp(129);
    chipCont.position.set(0, 0);

    userContainer.addChild(chipCont);
    //userContainer.removeChild(chipCont);

    return userContainer;
}

function getDealerContainer(pot, x, y) {
    var dealerContainer = new PIXI.Container();
    dealerContainer.scale.x = 0.7;
    dealerContainer.scale.y = 0.7;
    dealerContainer.position.set(x, y);

    var deck = makeDeckContainer();
    deck.name = "deck";
    deck.position.set(20, 0);
    dealerContainer.addChild(deck);

    var potChips = makeChipsContainerButtomUp(pot);
    potChips.name = "pot-chips";
    potChips.position.set(0, 0);
    dealerContainer.addChild(potChips);

    return dealerContainer;
}
// function getMainPot(amount){
//     // var chipCont = makeChipsContainer();
//     var chipCont = makeChipsContainerButtomUp(amount);
//     chipCont.position.set(0, 0);
//
// }

// update community cards
function updateCommunityCards(container, flop1, flop2, flop3, turn, river){
    var commCards = container.getChildByName("community-ctr");
    container.removeChild(commCards);
    // var newCommCards = makeCommunityCardsContainer("3c", "8s", "8d", turn, river);
    var newCommCards = makeCommunityCardsContainer(flop1, flop2, flop3, turn, river);
    newCommCards.position.set(0, 0);
    container.addChild(newCommCards);
}

// update pot
function updatePot(container, amount){
    var potChips = container.getChildByName("pot-chips");
    container.removeChild(potChips);
    var newPotChips = makeChipsContainerButtomUp(amount);
    newPotChips.position.set(0, 0);
    container.addChild(newPotChips);
}


// update the chips and bets of that player
function updatePlayerBets(container, amount){
    var playerChips = container.getChildByName("player-chips");
    container.removeChild(playerChips);
    var newPlayerChips = makeChipsContainerButtomUp(amount);
    newPlayerChips.position.set(0, 0);
    container.addChild(newPlayerChips);
}

function updatePlayerChipsBehind(container, amount){
    container.getChildByName("chips-behind").text = amount;

}
// update the text of remaining chips behind
// function updatePlayerChipsBehind(container, amount){
//     var playerChipsBehind = container.getChildByName("chips-behind");
//     container.removeChild(playerChipsBehind);
//     var chipsBehind = makeChipsContainerButtomUp(amount);
//     chipCont.position.set(0, 0);
//     container.addChild(chipCont);



// }

// zzzzzz
//data={"0":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":10000,"commited":0,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"1":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"fff","chips":10000,"commited":0,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"2":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"iii","chips":10000,"commited":0,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"3":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"ddd","chips":10000,"commited":0,"isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}}

function updateFull(data){
    console.log(data);
    console.log("" + data);
    let numOfPlayers = Object.keys(data).length;
    for (let i=0; i<numOfPlayers; i++){
        console.log(seats[i].name);
        console.log("--- " + data[i].name + " " + data[i].chips + " "+ data[i].commited + " " + data[i].strHole1 + " " + data[i].strHole2);
        let pc = createPlayerContainer(i, 'images/avatars/human4.jpeg', data[i].name, data[i].chips, data[i].commited, data[i].strHole1, data[i].strHole2);
        seats[i].addChild(pc)
    }
}

function addUserContainers() {
    c1 = getUserContainer('images/avatars/human4.jpeg', "ddd", 7400, "00", "00", 200, 250);
    c2 = getUserContainer('images/avatars/human4.jpeg', "eee", 1300, "4c", "4s", 300, 250);
    c3 = getUserContainer('images/avatars/girl1.jpeg', "fff", 23500, "00", "00", 400, 250);
    c4 = getUserContainer('images/avatars/girl1.jpeg', "iii", 6500,  "00", "00", 500, 250);

    d = getDealerContainer(4170, 300, 200);

    // chips1 = getChipsContainer();

    app.stage.addChild(c1);
    app.stage.addChild(c2);
    app.stage.addChild(c3);
    app.stage.addChild(c4);
    app.stage.addChild(d);
    // app.stage.addChild(chips1);
    updatePlayerBets(c1, 999);
    updatePlayerChipsBehind(c1, 1400);
    updatePot(d, 29);
    // updateCommunityCards(d, "3c", "8d", "Kh", turn, river);
    updateCommunityCards(d, "3c", "8d", "Kh");
    updateCommunityCards(d, "3c", "8d", "Kd", "Ks", "2d");
    //initTest();
    //glow(c4);





}

function glow(container){
    // colorMatrix = [
    //     1, 0, 0, 0,
    //     0, 1, 0, 0,
    //     0, 0, 1, 0,
    //     0, 0, 0, 1
    // ];
    colorMatrix = [
        1, 1, 1, 1,
        1, 0, 0, 1,
        1, 0, 0, 1,
        1, 1, 1, 1
    ];

    filter = new PIXI.filters.ColorMatrixFilter();
    filter.matrix = colorMatrix;
    //app.stage.filters = [filter];
    container.filters = [filter];
    update(container);


}
var filter;
var count = 0;
var bright = 0;
function update(container) {
    requestAnimationFrame(update);

    var bright = 1 + Math.sin(count);
    // var bright = 1 + Math.cos(Math.PI);
    // var bright = 1 + Math.sin(count);
    filter.brightness(bright, false);

    // if (bright == 1) {
    //     bright = 0;
    // }else{
    //     bright = 1;
    // }


    count += 0.01;

    app.renderer.render(app.stage);
}


// EXAMPLES:
//
// server: {"type":"status","value":"new hand # 1"}
// server: {"ante":0,"sb":30,"bb":60}
// server: {"sbPosition":0,"bbPosition":1,"dealerPosition":3}
// server: {"type":"cards","seat":3,"card1":"5c"}
// server: {"type":"cards","seat":3,"card1":"5c","card2":"9c"}
// server: {"type":"playermove","seat":0,"player":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":9970,"commited":30,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"sb","value":30,"pot":30}
// server: {"type":"status","value":"iii call 60."}
// server: {"type":"waitaction","player":"eee","options":["fold","call","raise","allin"],"optionAmounts":{"call":30,"max_raise":9970,"min_raise":120,"allin":9970}}
// server: {"type":"status","value":"dealing flop."}
// server: {"type":"community","flop1":"Ah","flop2":"Ad","flop3":"Ac"}



















//
// //The cat
// let cat = new Sprite(id["cat.png"]);
// cat.position.set(16, 16);
//
// //The hedgehog
// let hedgehog = new Sprite(id["hedgehog.png"]);
// hedgehog.position.set(32, 32);
//
// //The tiger
// let tiger = new Sprite(id["tiger.png"]);
// tiger.position.set(64, 64);






//
// var setState = function(){
//     // received: players=[ddd,false,10000,10000,0,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=302,ante=0sb=0bb=0
//     // received: ddd,30,sb
//     // received: players=[ddd,false,10000,9970,30,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=013,ante=0sb=30bb=60
//     // received: eee,60,bb
//     // received: players=[ddd,false,10000,9970,30,eee,false,10000,9940,60,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=013,ante=0sb=30bb=60
//     data='players=[ddd,false,10000,10000,0,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=302,ante=0,sb=0,bb=0'
//
//     table_data=data.split(']')[1]
//     //,buttons=302,ante=0,sb=0,bb=0
//     players_data=data.split(']')[0]
//     players_data = players_data.split('[')[1]
//
// }
//
// // document.body.innerHTML += '<div style="position:absolute;width:100%;height:100%;opacity:0.3;z-index:100;background:#000;"></div>';
// // document.body.innerHTML += '<div id="myCanvas" style="position:absolute;width:50%;height:50%;opacity:0.3;z-index:100;background:#000;"></div>';
//
// document.body.innerHTML = '<canvas id="myCanvas" width="800" height="600"></canvas>';
// var canvas = document.getElementById("myCanvas");
// var ctx = canvas.getContext("2d");
//
// ctx.fillStyle = "#FF0000";
// ctx.fillRect(0, 0, 800, 600);
//
// var canvas = document.getElementById("myCanvas");
// var ctx = canvas.getContext("2d");
// ctx.fillStyle = "#FFFFFF";
// ctx.fillRect(0, 0, 32, 32);
// ///////////////////////////////////////////////////////////////////////////
//
//
// document.body.innerHTML = '<canvas id="myCanvas" width="800" height="600"></canvas>';
// var canvas = document.getElementById("myCanvas");
// var ctx = canvas.getContext("2d");
// // Draw the ellipse
// ctx.fillStyle = 'black';
// ctx.beginPath();
// ctx.ellipse(300, 300, 250, 150, Math.PI , 0, Math.PI * 2);
// ctx.fill();
// ctx.fillStyle = 'blue';
// ctx.beginPath();
// ctx.ellipse(300, 300, 230, 130, Math.PI , 0, Math.PI * 2);
// ctx.fill();
// ///////////////////////////////////////////////////////////////////////////
//
//
// var script = document.createElement('script');
// script.src = 'https://cdnjs.cloudflare.com/ajax/libs/pixi.js/4.5.1/pixi.min.js';
// document.head.appendChild(script);
//
// function setup() {
//     let three_clubs = new PIXI.Sprite(PIXI.loader.resources["cards/3_of_clubs.png"].texture);
//     app.stage.addChild(cat);
//
// );
// }
// PIXI.loader
//     .add([
//         "cards/3_of_clubs.png",
//         "cards/3_of_diamonds.png"
//     ])
//     .load(setup);
//
//
// //Create a Pixi Application
// let app = new PIXI.Application({width: 256, height: 256});
//
// //Add the canvas that Pixi automatically created for you to the HTML document
// document.body.appendChild(app.view);
