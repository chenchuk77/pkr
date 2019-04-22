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
let app = new Application({
        width: 800,
        height: 600,
        antialiasing: true,
        transparent: false,
        resolution: 1
    }
);

//load all images and invoke setup() when done;
PIXI.loader
    .add(imageFiles)
    .load(setup);

//This `setup` function will run when the image has loaded
function setup() {
    console.log('setup() called.');
    drawTable();
    //drawTable();
    createSeatsContainers();

    let data={
        "0":{"name":"ddd","chips":4500,"commited":150,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "1":{"name":"p1","chips":7000,"commited":300,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"5c","strHole2":"Kd"},
        "2":{"name":"eee","chips":7000,"commited":300,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"5c","strHole2":"Kd"},
        "3":{"name":"fff","chips":24000,"commited":0,"isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "4":{"name":"p4","chips":24000,"commited":0,"isChecking":false,"position":4,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "5":{"name":"p5","chips":24000,"commited":0,"isChecking":false,"position":5,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "6":{"name":"p6","chips":24000,"commited":0,"isChecking":false,"position":6,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "7":{"name":"iii","chips":1900,"commited":900,"isChecking":false,"position":7,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "8":{"name":"p8","chips":1900,"commited":900,"isChecking":false,"position":8,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}};

    // let p1 = newPlayerContainer(0, 300, 500);
    // let p2 = newPlayerContainer(2, 100, 300);
    // let p3 = newPlayerContainer(3, 300, 100);
    // let p4 = newPlayerContainer(7, 500, 300);
    // app.stage.addChild(p1);
    // app.stage.addChild(p2);
    // app.stage.addChild(p3);
    // app.stage.addChild(p4);


    updateFull(data);

    let playre_move_data = {
        "type":"playermove",
        "seat":3,
        "player":{"name":"eee","chips":9940,"commited":60,
                  "isChecking":false,"position":0,"inGame":true,
                  "inHand":true,"strHole1":"XX","strHole2":"XX"},
        "command":"call",
        "value":30,
        "pot":240};

    updatePlayerMove(playre_move_data);

    let community_cards_data2 = {"type":"community","flop1":"3h","flop2":"3d","flop3":"Ad", "turn": "8h", "": "9s"};
    updateCommunityCards(community_cards_data2);
    let community_cards_data1 = {"type":"community","flop1":"Ah","flop2":"Ad","flop3":"Ac"};
    updateCommunityCards(community_cards_data1);

    updatePot(100);
    updatePot(1720);

    let waitaction_data = {"type":"waitaction", "player":"fff", "options":["fold","raise","allin","check"], "optionAmounts":{"max_raise":9940,"min_raise":120,"allin":9940}};
    drawActionButtons(waitaction_data);

    let cards_data = {"type":"cards","seat":3,"card1":"5c","card2":"9c"};
    updateMyHoleCards(cards_data);


    let hcc = seats[1].getChildByName('pc')
                      .getChildByName('hcc');

    clearHoleCards(hcc);






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

// let pc = newPlayerContainer(i, 'images/avatars/girl1.jpeg', p.name, p.chips, p.commited, p.strHole1, p.strHole2);









function updateCommited(container, commited){
    container.getChildByName("committed").text = commited;

}

function updateChips(container, chips){
    container.getChildByName("chips-behind").text = chips;

}





// function makeDeckContainer() {
//     // create container for deck
//     var deck = new PIXI.Container();
//     deck.name = "deck-ctr";
//     deck.scale.x = 0.5;
//     deck.scale.y = 0.5;
//
//     var cards = new Sprite(resources['images/cards/back.svg'].texture);
//     cards.position.set(0, 0);
//     cards.scale.x = 0.5;
//     cards.scale.y = 0.5;
//     deck.addChild(cards);
//
//     return deck;
//
// }


// seats is positioned container. it has absolute position on the table
function createSeatsContainers() {
    for (let i = 0; i < 9; i++) {
        seats[i] = new PIXI.Container();
        seats[i].name = "seat-" + i;
        seats[i].position.set(POS[i].position.x, POS[i].position.y);
        addRect(seats[i]);
        app.stage.addChild(seats[i]);
    }
}
// tester
function addRect(container) {
    let rectangle = new PIXI.Graphics();
    rectangle.lineStyle(4, 0x44e300, 1);
    rectangle.beginFill(0x99CCFF);
    rectangle.drawRect(0, 0, 60, 60);
    rectangle.endFill();
    // rectangle.x = 0;
    // rectangle.y = 0;
    container.addChild(rectangle);

}



// // update community cards
// function updateCommunityCards(container, flop1, flop2, flop3, turn, river){
//     var commCards = container.getChildByName("community-ctr");
//     container.removeChild(commCards);
//     // var newCommCards = makeCommunityCardsContainer("3c", "8s", "8d", turn, river);
//     var newCommCards = makeCommunityCardsContainer(flop1, flop2, flop3, turn, river);
//     newCommCards.position.set(0, 0);
//     container.addChild(newCommCards);
// }

// update pot
// function updatePot(container, amount){
//     var potChips = container.getChildByName("pot-chips");
//     container.removeChild(potChips);
//     var newPotChips = makeChipsContainerButtomUp(amount);
//     newPotChips.position.set(0, 0);
//     container.addChild(newPotChips);
// }


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

// create visual containers for each players and add to seats[]
function updateFull(data){
    console.log(data);
    console.log("" + data);
    let numOfPlayers = Object.keys(data).length;
    for (let i=0; i<numOfPlayers; i++){
        // seat[0] may have player[x]
        let p = data[Object.keys(data)[i]];
        console.log("working on player " + p + ", i=" + i);
        console.log("--- " + p.name + " " + p.chips + " "+p.commited + " " + p.strHole1 + " " + p.strHole2);
        // let pc = createPlayerContainer(i, 'images/avatars/girl1.jpeg', p.name, p.chips, p.commited, p.strHole1, p.strHole2);
        let pc = newPlayerContainer(i, 'images/avatars/girl1.jpeg', p.name, p.chips, p.commited, p.strHole1, p.strHole2);
        // let pc = createPlayerContainer(i, 'images/avatars/human4.jpeg', data[i].name, data[i].chips, data[i].commited, data[i].strHole1, data[i].strHole2);
        seats[i].addChild(pc)
    }
}
function updatePlayerMove(data){
    // {"type":"playermove","seat":0,"player":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":9970,"commited":30,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"sb","value":30,"pot":30}
    if (data.type !== 'playermove') return -1;
    let pc = seats[data.seat].getChildByName('pc');
    pc.getChildByName('ncc')
      .getChildByName('chipsrect')
      .getChildByName('chips').text = data.player.chips;
    // let commited = newChipsContainer(data.player.commited);
    let commited = newChipsContainer(data.player.commited);
    let bet = pc.getChildByName('bet');
    commited.position.set(0, 0);
    bet.addChild(commited);
}

function updateHolrCards(data){
    //
    //if (data.type !== 'playermove') return -1;
    let hcc = seats[data.seat].getChildByName('pc')
        .getChildByName('hcc');
    drawHoleCards(1,2, hcc);
    // // let commited = newChipsContainer(data.player.commited);
    // let commited = newChipsContainer(data.player.commited);
    // let bet = pc.getChildByName('bet');
    // commited.position.set(0, 0);
    // bet.addChild(commited);
}


function updateCommunityCards(data){
    // {"type":"community","flop1":"Ah","flop2":"Ad","flop3":"Ac"}
    if (data.type !== 'community') return -1;
    // let tc = app.stage.getChildByName('tc').getChildByName('table');
    let tc = app.stage.getChildByName('tc');
    // remove old container
    tc.removeChild(tc.getChildByName('ccc'));
    let ccc = newCommunityCardsContainer(data);
    ccc.position.set(TABLE.ccc.x, TABLE.ccc.y);
    tc.addChild(ccc);
}

function updateMyHoleCards(data) {
    // ignore: {"type":"cards","seat":3,"card1":"5c"}
    // {"type":"cards","seat":3,"card1":"5c","card2":"9c"}
    if (data.type !== 'cards') return -1;

    if (data.card2 !== undefined){
        let hcc = seats[data.seat].getChildByName('pc')
            .getChildByName('hcc');
        drawHoleCards(data.card1 ,data.card2, hcc);
    }
}





function updatePot(amount){
    let tc = app.stage.getChildByName('tc').getChildByName('table');
    // remove old container
    tc.removeChild(tc.getChildByName('pot'));
    let pot = newPotContainer(amount);
    pot.position.set(TABLE.pot.x, TABLE.pot.y);
    tc.addChild(pot);
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



//{"type":"playermove",
// "seat":0,
// "player":// {"name":"eee","chips":9940,"commited":60,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
// "command":"call",
// "value":30,
// "pot":240}