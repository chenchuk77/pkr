


$( document ).ready(function() {
    $('#loggedOut').show('slow');
    $('#loggedIn').show('slow');
    // Add the PIXI canvas
    document.body.appendChild(app.view);
});

let my_pid = 99; // my player id will be calculated from a full update
let seats = [];
let ctx_seats = [];
let holeCards = [];
let communityCards;
let bets;
let buttons;
let options;
let optionAmouns;
let updateScreen=false;

let statusMessages = ['init message.'];
let highlighter;
let highlighter_count = 0;

// stash for non visible components
let stash = new PIXI.Container();
let dealer_button;

var connection = new WebSocket('ws://192.168.2.39:4444');

connection.onopen = function () {
    console.log('client connected.');
    connection.send('hello from javascript client.'); // Send the message 'Ping' to the server
};

connection.onerror = function (error) {
    console.log('WebSocket Error ' + error);
};

connection.onmessage = function (e) {
    console.log('server: ' + e.data);
    if (e.data.includes('ante')) {
        console.log('bets update received.');
        bets = JSON.parse(e.data);
    }
    if (e.data.includes('dealerPosition')) {
        console.log('buttons update received.');
        buttons = JSON.parse(e.data);
        moveDealerButton(buttons);
    }
    if (e.data.includes('chips') && !e.data.includes('playermove')) { // TODO !!! split to 2 messages (playermove / table)
        console.log('full seats update received.');
        if (updateScreen) {
            console.log('accepting full update.');
            ctx_seats = JSON.parse(e.data);
            // first calculate my own seat for shifting others
            updateMySeat(ctx_seats);
            updateFull(ctx_seats);
            updateScreen = false;
        } else {
            console.log('ignoring full update.');

        }
    }
    if (e.data.includes('playermove')) {
        console.log('playermove update received.');
        playermoveJSON = JSON.parse(e.data);
        updatePlayerMove(playermoveJSON)
    }
    if (e.data.includes('status')) {
        console.log('status message received.');
        console.log('----------------------------------');
        console.log(e.data);
        let statusJSON = JSON.parse(e.data);
        console.log(statusJSON);
        // drawStatusMessage(statusMessage);
        updateStatusMessages(statusJSON);
        if (statusJSON.value.includes('new hand')){
            console.log('starting a new hand.');
            removeAllHoleCards();
            updateScreen = true;
        }
        if (statusJSON.value.includes('dealing flop')){
            console.log('dealing flop.');
            clearAllPlayersBets();
        }
        if (statusJSON.value.includes('dealing turn')){
            console.log('dealing turn.');
            clearAllPlayersBets();
        }
        if (statusJSON.value.includes('dealing river')){
            console.log('dealing river.');
            clearAllPlayersBets();
        }
    }



            // {"type":"status","value":"new hand # 1"}

    // }
    if (e.data.includes('community')) {
        console.log('communityCards update received.');
        //newCommunityCardsContainer();
        communityCards = JSON.parse(e.data);
        updateCommunityCards(communityCards);
    }
    if (e.data.includes('cards')) {
        console.log('holecards update received.');
        // holeCards = 1 and 2 for ctx only
        updateMyHoleCards(JSON.parse(e.data));
    }
    if (e.data.includes('dealerPosition')) {
        let buttonsJSON = JSON.parse(e.data);
        moveDealerButton(buttonsJSON);
    }




    // server notify it waits for a player command
    if (e.data.includes('waitaction')) {
        let commandJSON = JSON.parse(e.data);
        console.log('waitaction command received.');
        // server waits for us
        if (player.name === commandJSON.player){
            console.log("server is waiting for our command.");
            console.log("updating ctx options/amounts.");
            options = commandJSON.options;
            optionAmouns = commandJSON.optionAmounts;
            drawActionButtons(commandJSON);
            addActionButtonHandlers(commandJSON)

        } else {
            // not our turn
            // removeActionButtons();
            console.log("its " + commandJSON.player + "'s turn.");
            highLightPlayer(commandJSON);
        }
    }

};

// player is a seated user
let user;   // the logged-in user
let player = {
    name: '',
    cards: []
};


var login = function(){
    var username = $('#username').val();
    var password = $('#password').val();
    connection.send("login" + "," + username + "," + password);
    console.log('client logged in.');
    user = username;
    player.name = username;
    $('#loggedOut').show();
    connection.send('statusrequest');
};

var logout = function(){
    var username = $('#username').val();
    connection.send("logout" + "," + username);
    console.log('client logout requested.');
};

var register = function(){
    var username = $('#username').val();
    var gamename = $('#gamename').val();
    connection.send("register" + "," + username + ',' + gamename);
    console.log('client registration requested.');
};

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



var bet = function(){
    // connection.send('bet: 300');
    actionCommand = {"action": "bet", "amount": 500};
    connection.send(actionCommand);


};
var raise = function(){
    // connection.send('raise: 700');
    actionCommand = {"action": "raise", "amount": 700};
    connection.send(actionCommand);

};
var fold = function(){
    actionCommand = {"action": "fold", "amount": 0};
    // connection.send('fold');
    connection.send(JSON.stringify(actionCommand));
    // actionJSON.addProperty("action", "fold");
    // actionJSON.addProperty("amount", 0);


};
var call = function(){
    // connection.send('call: 300');
    actionCommand = {"action": "call", "amount": 0};
    // connection.send('fold');
    connection.send(actionCommand);

};

// use this to send commands (will be replaced by action buttons when implemented)
function sendAction(cmd, amount){
    let actionCommand = {"action": cmd, "amount": amount};
    //console.log(actionCommand);


    console.log("sending command: " + actionCommand);
    connection.send(JSON.stringify(actionCommand));

    // } else {
    //     console.log("unsupported... use full commands. ie: call,50 or fold,0 ")
    // }

}

//////////////// PIXI APPLICATION ///////////////////
let app = new Application({
        width: 1280,
        height: 800,
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

    // creating a dealer button
    dealer_button = getDealerSprite();
    stash.addChild(dealer_button);

    // creating highlighter object for active player
    highlighter = newHighlighter();

    // animate active player
    app.ticker.add(() => {
        highlighter_count += 2*(1/60)*Math.PI;
        highlighter.tint = 0xffffff*Math.abs(Math.cos(highlighter_count)) ;
    });


    drawTable();
    drawStatusContainer();
    createSeatsContainers();
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

function newHand(){

}

// seats is positioned container. it has absolute position on the table
function createSeatsContainers() {
    for (let i = 0; i < NUMBER_OF_PLAYERS; i++) {
        seats[i] = new PIXI.Container();
        seats[i].name = "seat-" + i;
        seats[i].position.set(PLAYER[i].position.x, PLAYER[i].position.y);
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

// find my own player data to bind to seat[0]
function updateMySeat(data){
    for (let i=0; i<Object.keys(data).length; i++ ){
        //console.log ("checking " + data[i].name);
        if (data[i].name === player.name){
            my_pid = i;
            console.debug("my player id: %d will seat at seat[0].", my_pid);
            return;
        }
    }
    console.error("cant find my seat");
}

// find my own player data to bind to seat[0]
function seatOf(realPlayerIndex){
    let numOfPlayers = seats.length;
    // s will hold the actual seat number for this player
    // this will let player at data[2] (i=2) to seat in seat[0] (s=0)
    let visualSeatId = realPlayerIndex - my_pid;
    if (visualSeatId < 0){
        visualSeatId += numOfPlayers;
    }
    //console.debug("real=%d, visual=%d", realPlayerIndex, visualSeatId);
    return visualSeatId;
}

// create visual shifted containers for each players and add to seats[]
function updateFull(data){
    // {"0":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":10000,"commited":0,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"1":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"fff","chips":10000,"commited":0,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"2":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"iii","chips":10000,"commited":0,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"3":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"ddd","chips":10000,"commited":0,"isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}}
    //console.debug(data);
    let numOfPlayers = Object.keys(data).length;
    //console.debug("numOfPlayers=" +numOfPlayers);
    for (let i=0; i<numOfPlayers; i++){
        // s will hold the actual seat number for this player
        // this will let player at data[2] (i=2) to seat in seat[0] (s=0)
        let s = seatOf(i);
        let p = data[Object.keys(data)[i]];
        //console.debug("seating %s from data[%d] at seat[%d].", p.name, i, s);
        let pc = newPlayerContainer(s, 'images/avatars/girl1.jpeg', p.name, p.chips, p.commited, p.strHole1, p.strHole2);
        seats[s].addChild(pc)
    }
}

function updateStatusMessages(data) {
    // {"type":"status","value":"new hand # 1"}
    // {"type":"status","value":"iii call 60."}
    if (data.type !== 'status') return -1;
    // add message to ctx
    addStatusMessage(statusMessages, data.value);
    // and show last N messages
    let messagesSprite = app.stage
                .getChildByName('sc')
                .getChildByName('messages');
    showStatusMessages(statusMessages, messagesSprite);
}

function moveDealerButton(data) {
    // server: {"sbPosition":0,"bbPosition":1,"dealerPosition":3}
    if (data.dealerPosition !== undefined){
        let dealerSeat = seatOf(data.dealerPosition);
        //console.debug("dealer button at seat: %d.", dealerSeat);
        // remove old dealer button
        let numOfPlayers = seats.length;
        console.log("numOfPlayers=" +numOfPlayers);
        for (let i=0; i<numOfPlayers; i++){
            if (dealerSeat === i){
            let dbc = seats[i].getChildByName('pc')
                              .getChildByName('dbc');
                console.log(" dealer at seat %s", dealerSeat);
                dbc.addChild(dealer_button);
            }
        }
    } else {
        console.log(data);
        console.log("err in dealer button move");
        return -1;
    }
}

function removeAllHoleCards(){
    for (let i=0; i<seats.length; i++){
        let pc = seats[i].getChildByName('pc');
        if (pc !== null){
            let hcc = pc.getChildByName('hcc');
            if (hcc !== null){
                removeHoleCards(hcc);
            }else{
                console.warn('should not be here hcc should be defined when pc exists')
            }

        } else {
            console.log('no pc for seat %d', i)
        }
    }
}

function updatePlayerMove(data){
    // {"type":"playermove","seat":0,"player":{"name":"eee","chips":9970,"commited":30,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"sb","value":30,"pot":30}
    if (data.type !== 'playermove') return -1;
    let seat = seatOf(data.seat);
    updatePlayerChips(seat, data.player.chips);
    updatePlayerBet(seat, data.player.commited);
    updatePot(data.pot);
    if (data.command === 'fold'){
        foldPlayer(seat);
    }
    // server: {"type":"playermove","seat":0,"player":{"name":"eee","chips":9970,
    // "commited":30,"isChecking":false,"position":0,"inGame":true,
    // "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"sb","value":30,"pot":30}

}

// attach click handlers
function addActionButtonHandlers(){
    console.debug('addActionButtonHandlers() called.');
    app.stage.getChildByName('abc')
        .getChildByName('fold').click = function() {
        console.log("fold button clicked.");
        foldPlayer(mySeat()); // fold me immediately
        sendAction("fold", 0);
        removeActionButtons();
    };
    app.stage.getChildByName('abc')
        .getChildByName('call').click = function() {
        console.log("call button clicked.");
        call(mySeat()); // fold me immediately
        sendAction("fold", 0);
        removeActionButtons();
    };



}

function foldPlayer(seat){
    console.log("foldPlayer(%d) called", seat);
    let hcc = seats[seat].getChildByName('pc')
                         .getChildByName('hcc');
    removeHoleCards(hcc);
}

function mySeat(){
    for (let i=0; i<seats.length; i++){
        let name = seats[i]
            .getChildByName('pc')
            .getChildByName('ncc')
            .getChildByName('namerect')
            .getChildByName('name').text
        if (name === player.name){
            return i;
        }
    }

}



function updatePlayerChips(seat, amount){
    console.debug("updatePlayerChips(%d,%d) called", seat, amount)
    let pc = seats[seat].getChildByName('pc');
    pc.getChildByName('ncc')
        .getChildByName('chipsrect')
        .getChildByName('chips').text = amount;
}


function clearAllPlayersBets(){
    for (let i=0; i<seats.length; i++){
        let bc = seats[i]
            .getChildByName('pc')
            .getChildByName('bc');
        let bet = bc.getChildByName('bet');
        if ( bet !== undefined ) {
            console.debug('clearing bet from seat %d.', i);
            bc.removeChild(bet)
        }
    }
}


function updatePlayerBet(seat, amount){
    let bc = seats[seat].getChildByName('pc')
                        .getChildByName('bc');
    // remove old container if exists
    let oldc = bc.getChildByName('bet');
    //console.log('bc is');
    //console.log(bc);

    if ( oldc !== undefined ) {
        console.debug('removing old bet.');
        bc.removeChild(oldc)
    }
    let bet = newBetContainer(amount);
    // bet.name = 'bet';
    bc.addChild(bet);
}

function updateCommunityCards(data){
    // {"type":"community","flop1":"Ah","flop2":"Ad","flop3":"Ac"}
    if (data.type !== 'community') return -1;
    // let tc = app.stage.getChildByName('tc').getChildByName('table');
    let tc = app.stage.getChildByName('tc');
    // // remove old container
    // tc.removeChild(tc.getChildByName('ccc'));
    let ccc = newCommunityCardsContainer(data);
    ccc.position.set(TABLE.ccc.x, TABLE.ccc.y);
    tc.addChild(ccc);
}

function updateMyHoleCards(data) {
    // ignore: {"type":"cards","seat":3,"card1":"5c"}
    // {"type":"cards","seat":3,"card1":"5c","card2":"9c"}
    if (data.type !== 'cards') return -1;
    if (data.card2 !== undefined){
        let hcc = seats[seatOf(data.seat)]
            .getChildByName('pc')
            .getChildByName('hcc');
        removeHoleCards(hcc);
        addHoleCards(data.card1 ,data.card2, hcc);

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

// function waitingPlayerMove(data) {
//     // {"type":"waitaction","player":"p5","options":["fold","call","raise","allin"],"optionAmounts":{"call":30,"max_raise":9970,"min_raise":120,"allin":9970}}
//     if (data.type !== 'waitaction') return -1;
//     highLightPlayer(data);
//
// }

function RemovePlayerHighlight(){
        // adding to stash will remove from UI
        stash.addChild(highlighter);
}

function highLightPlayer(data) {
    // {"type":"waitaction","player":"p5","options":["fold","call","raise","allin"],"optionAmounts":{"call":30,"max_raise":9970,"min_raise":120,"allin":9970}}
    if (data.type !== 'waitaction') return -1;
    console.debug('waitaction received ( waiting for %s to play.)', data.player);
    for (let i = 0; i < seats.length; i++) {
        let pc = seats[i].getChildByName('pc');
        let name = pc.getChildByName('ncc')
            .getChildByName('namerect')
            .getChildByName('name');
        //console.log('name.text=' + name.text);
        if (name.text === data.player) {
            console.debug('highlighting active player at seat ' + i);
            // highlight this seat
            let bg = pc.getChildByName('hcc')
                .getChildByName('bg');
            bg.addChild(highlighter);
        }
    }
}

function addStatusMessage(status_array, message){
    status_array.push(message);
}

function showStatusMessages(status_array, sprite){
    //console.log(sprite);
    //console.log(status_array);
    // reset text
    sprite.text = '\n';
    // all messages can appear
    if (status_array.length <= STATUS.messages){
        for (let n=0; n<status_array.length; n++){
            sprite.text += status_array[n] + "\n";
        }
    } else {
        // more than N messages, extracting last N messages
        for (let n=status_array.length-STATUS.messages; n<status_array.length; n++){
            sprite.text += status_array[n] + "\n";
        }
    }
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