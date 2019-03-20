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
        bets = JSON.parse(e.data);
        console.log('bets update received.');
    }
    if (e.data.includes('dealerPosition')) {
        buttons = JSON.parse(e.data);
        console.log('buttons update received.');
        drawButtons(buttons);
    }
    if (e.data.includes('chips')) { // TODO !!! split to 2 messages (playermove / table)
        seats = JSON.parse(e.data);
        console.log('seats update received.');
        updateTable();
    }
    if (e.data.includes('card1')) {
        pocketCards = JSON.parse(e.data);
        console.log('pocketCards update received.');
        player.cards=[pocketCards.card1, pocketCards.card2];
        drawPocketCards(seats);
    }
    if (e.data.includes('community')) {
        communityCards = JSON.parse(e.data);
        console.log('communityCards update received.');
        drawFlop(communityCards.flop1, communityCards.flop2, communityCards.flop3);
        console.log('flop cards: ' + communityCards.flop1 + ' ' + communityCards.flop2 + ' ' +communityCards.flop1 + '.');
        if (communityCards.hasOwnProperty('turn')){
            console.log('turn card: ' + communityCards.turn);
            drawTurn(communityCards.turn);
        }
        if (communityCards.hasOwnProperty('river')){
            console.log('river card: ' + communityCards.river);
            drawRiver(communityCards.river);
        }
    }

    // server notify it waits for a player command
    if (e.data.includes('waitaction')) {
        var commandJSON = JSON.parse(e.data);
        console.log('waitaction command received.');
        // server waits for us
        if (player.name === commandJSON.player){
            console.log("server is waiting for our command.");
            drawActionButtons();
        } else {
            console.log("its " + commandJSON.player + "'s turn.");
        }
    }


    // received: {"command":"waitaction","player":"fff"}

};

// player is a seated user
var user;   // the logged-in user
var player;
var player = {
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
    connection.send('bet: 300');
};
var raise = function(){
    connection.send('raise: 700');
};
var fold = function(){
    connection.send('fold');
};
var call = function(){
    connection.send('call: 300');
};


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
    console.log('setup() called.')
    drawTable();
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
