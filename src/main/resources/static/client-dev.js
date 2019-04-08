// this file is UI tester

$( document ).ready(function() {
    $('#loggedOut').show('slow');
    $('#loggedIn').show('slow');
    // Add the PIXI canvas
    document.body.appendChild(app.view);

});


var seats;
var pocketCards;
var communityCards;
var bets;
var buttons;
var options;
var optionAmouns;
var statusMessage;

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
var user;   // the logged-in user
var player;
var player = {
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
    addUserContainer();
    updateCommited(c1, 4444);
    updateChips(c2, 8000);
    updateCommited(c1, 30);

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
    container.getChildByName("chips").text = chips;

}


function getUserContainer(avatarFile, name, chips, card1, card2, x, y){
    var userContainer = new PIXI.Container();
    userContainer.scale.x=0.7;
    userContainer.scale.y=0.7;
    userContainer.position.set(x, y);

    var avatarSprite = new Sprite(resources[avatarFile].texture);
    avatarSprite.position.set(10, 90);
    avatarSprite.scale.x = 0.4;
    avatarSprite.scale.y = 0.4;
    // avatarSprite.border
    userContainer.addChild(avatarSprite);


    var commitedSprite = makeTextSprite("");
    commitedSprite.name = "committed";
    commitedSprite.position.set(0, 0);
    userContainer.addChild(commitedSprite);


    var nameSprite = makeTextSprite(name);
    nameSprite.position.set(20, 190);
    userContainer.addChild(nameSprite);

    // var circle = circleSprite();
    // circle.position.set(20, 190);
    // userContainer.addChild(circle);

    var chipsSprite = makeTextSprite("");
    chipsSprite.name = "chips";
    chipsSprite.position.set(20, 210);
    userContainer.addChild(chipsSprite);

    //var cardSprite = new Sprite(resources[cards_array[i].file].texture);
    // cardSprite.scale.x=0.1;
    // cardSprite.scale.y=0.1;
    //cardSprite.scale.x=0.25;
    //cardSprite.scale.y=0.25;

    var card1Sprite = getCardSprite(card1);
    card1Sprite.position.set(0, 30);
    card1Sprite.scale.x = 0.4;
    card1Sprite.scale.y = 0.4;
    var card2Sprite = getCardSprite(card2);
    card2Sprite.position.set(50, 30);
    card2Sprite.scale.x = 0.4;
    card2Sprite.scale.y = 0.4;
    userContainer.addChild(card1Sprite);
    userContainer.addChild(card2Sprite);

    return userContainer;
}
function addUserContainer() {
    // var c1 = getUserContainer("ddd", 7400, "Kc", "7h", 200, 300);
    c1 = getUserContainer('images/avatars/human4.jpeg', "ddd", 7400, "00", "00", 200, 300);
    c2 = getUserContainer('images/avatars/human4.jpeg', "eee", 1300, "4c", "4s", 300, 300);
    // var c3 = getUserContainer("fff", 23500, "Ad", "3h", 400, 300);
    c3 = getUserContainer('images/avatars/girl1.jpeg', "fff", 23500, "00", "00", 400, 300);

    app.stage.addChild(c1);
    app.stage.addChild(c2);
    app.stage.addChild(c3);
}

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
