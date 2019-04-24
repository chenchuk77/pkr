// this file is UI tester
// it ignores websocket and can process direct input

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
let updateScreen=true;




// player is a seated user
let user;   // the logged-in user
//let player;
let player = {
    name: '',
    cards: []
};






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

    // simulating logged-in user
    user = 'p6';
    player.name = 'p6';

    drawTable();
    createSeatsContainers();

    let data={
        "0":{"name":"p0","chips":1000,"commited":150,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "1":{"name":"p1" ,"chips":2000,"commited":300,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "2":{"name":"p2","chips":7000,"commited":300,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "3":{"name":"p3","chips":24000,"commited":0, "isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "4":{"name":"p4", "chips":24000,"commited":0, "isChecking":false,"position":4,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "5":{"name":"p5", "chips":24000,"commited":0, "isChecking":false,"position":5,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "6":{"name":"p6", "chips":24000,"commited":0, "isChecking":false,"position":6,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "7":{"name":"p7","chips":1900,"commited":900,"isChecking":false,"position":7,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},
        "8":{"name":"p8", "chips":1900,"commited":900,"isChecking":false,"position":8,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}};

    // first calculate my own seat
    updateMySeat(data);
    updateFull(data);

    // let playre_move_data = {
    //     "type":"playermove",
    //     "seat":6,
    //     "player":{"name":"p6","chips":9940,"commited":6666,
    //               "isChecking":false,"position":6,"inGame":true,
    //               "inHand":true,"strHole1":"XX","strHole2":"XX"},
    //     "command":"call",
    //     "value":30,
    //     "pot":240};
    // updatePlayerMove(playre_move_data);

    let playre_move_data0 = {"type":"playermove","seat":0,"player":{"name":"p0","chips":1000,"commited":2000,"isChecking":false,"position":1,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};0
    updatePlayerMove(playre_move_data0);
    let playre_move_data1 = {"type":"playermove","seat":1,"player":{"name":"p1","chips":1100,"commited":2100,"isChecking":false,"position":1,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data1);
    let playre_move_data2 = {"type":"playermove","seat":2,"player":{"name":"p2","chips":1200,"commited":2200,"isChecking":false,"position":2,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data2);
    let playre_move_data3 = {"type":"playermove","seat":3,"player":{"name":"p3","chips":1300,"commited":2300,"isChecking":false,"position":2,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data3);
    let playre_move_data4 = {"type":"playermove","seat":4,"player":{"name":"p4","chips":1400,"commited":2400,"isChecking":false,"position":6,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data4);
    let playre_move_data5 = {"type":"playermove","seat":5,"player":{"name":"p5","chips":1500,"commited":2500,"isChecking":false,"position":0,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data5);
    let playre_move_data6 = {"type":"playermove","seat":6,"player":{"name":"p6","chips":1600,"commited":2600,"isChecking":false,"position":6,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data6);
    let playre_move_data7 = {"type":"playermove","seat":7,"player":{"name":"p7","chips":1700,"commited":2700,"isChecking":false,"position":0,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data7);
    let playre_move_data8 = {"type":"playermove","seat":8,"player":{"name":"p8","chips":1800,"commited":2800,"isChecking":false,"position":6,"inGame":true, "inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"call","value":30,"pot":240};
    updatePlayerMove(playre_move_data8);


    //let community_cards_data2 = {"type":"community","flop1":"3h","flop2":"Ac","flop3":"Ad", "turn": "8h", "": "9s"};
    //updateCommunityCards(community_cards_data2);
    let community_cards_data1 = {"type":"community","flop1":"7c","flop2":"2h","flop3":"Tc"};
    updateCommunityCards(community_cards_data1);

    //let mySeat = findMySeat(data);
    //console.error("XXXXXXXXXXXXXXx + " + mySeat);


    //updatePot(100);
    //updatePot(1720);

    //let waitaction_data = {"type":"waitaction", "player":"fff", "options":["fold","raise","allin","check"], "optionAmounts":{"max_raise":9940,"min_raise":120,"allin":9940}};
    //drawActionButtons(waitaction_data);

    let cards_data = {"type":"cards","seat":6,"card1":"6h","card2":"6c"};
    updateMyHoleCards(cards_data);


    //let hcc = seats[1].getChildByName('pc')
    //                   .getChildByName('hcc');

    //clearHoleCards(hcc);






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





function updateCommited(container, commited){
    container.getChildByName("committed").text = commited;
}

function updateChips(container, chips){
    container.getChildByName("chips-behind").text = chips;
}

// seats is positioned container. it has absolute position on the table
function createSeatsContainers() {
    for (let i = 0; i < 9; i++) {
        seats[i] = new PIXI.Container();
        seats[i].name = "seat-" + i;
        seats[i].position.set(PLAYER[i].position.x, PLAYER[i].position.y);
        //seats[i].anchor.set(0.5, 0.5);
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

// find my own player data to bind to seat[0]
function updateMySeat(data){
    for (let i=0; i<Object.keys(data).length; i++ ){
        console.log ("checking " + data[i].name);
        if (data[i].name === player.name){
            my_pid = i;
            console.log ("my player id: " + my_pid);
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
    console.log("real=%d, visual=%d", realPlayerIndex, visualSeatId);
    return visualSeatId;
}


// create visual shifted containers for each players and add to seats[]
function updateFull(data){
    // {"0":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"eee","chips":10000,"commited":0,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"1":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"fff","chips":10000,"commited":0,"isChecking":false,"position":1,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"2":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"iii","chips":10000,"commited":0,"isChecking":false,"position":2,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"3":{"logger":{"name":"com.kukinet.pkr.Player"},"name":"ddd","chips":10000,"commited":0,"isChecking":false,"position":3,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"}}
    console.log(data);
    console.log("" + data);
    let numOfPlayers = Object.keys(data).length;
    console.log("numOfPlayers=" +numOfPlayers);
    for (let i=0; i<numOfPlayers; i++){
        // s will hold the actual seat number for this player
        // this will let player at data[2] (i=2) to seat in seat[0] (s=0)
        let s = seatOf(i);
        let p = data[Object.keys(data)[i]];
        console.log("seating %s from data[%d] at seat[%d].", p.name, i, s);
        let pc = newPlayerContainer(s, 'images/avatars/girl1.jpeg', p.name, p.chips, p.commited, p.strHole1, p.strHole2);
        seats[s].addChild(pc)
    }
}

function updatePlayerMove(data){
    // {"type":"playermove","seat":0,"player":{"name":"eee","chips":9970,"commited":30,"isChecking":false,"position":0,"inGame":true,"inHand":true,"strHole1":"XX","strHole2":"XX"},"command":"sb","value":30,"pot":30}
    if (data.type !== 'playermove') return -1;

    let seat = seatOf(data.seat);
    updatePlayerChips(seat, data.player.chips);
    updatePlayerBet(seat, data.player.commited);


    // let pc = seats[data.seat].getChildByName('pc');
    // pc.getChildByName('ncc')
    //   .getChildByName('chipsrect')
    //   .getChildByName('chips').text = data.player.chips;
    // let commited = newChipsContainer(data.player.commited);
    // let commited = newChipsContainer(data.player.commited);
    // let bet = pc.getChildByName('bd');
    // commited.position.set(0, 0);
    // bet.addChild(commited);
}

function updatePlayerChips(seat, amount){
    let pc = seats[seat].getChildByName('pc');
    pc.getChildByName('ncc')
        .getChildByName('chipsrect')
        .getChildByName('chips').text = amount;
}

function updatePlayerBet(seat, amount){
    let bc = seats[seat].getChildByName('pc')
                        .getChildByName('bc');
    // remove old container if exists
    let oldc = bc.getChildByName('bet');
    if ( oldc !== undefined ) {
        bc.removeChild(oldc)
    }
    let bet = newBetContainer(amount);
    // bet.name = 'bet';
    bc.addChild(bet);
}

// function updatePlayerBet(player_seat, amount){
//     chipsc = newChipsContainer()
//
// }






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
        let hcc = seats[seatOf(data.seat)]
            .getChildByName('pc')
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