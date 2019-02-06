$( document ).ready(function() {
    $('#loggedOut').show('slow');
    $('#loggedIn').show('slow');
});



// var connection = new WebSocket('ws://127.0.0.1:4444');
var connection = new WebSocket('ws://192.168.2.39:4444');

connection.onopen = function () {
    console.log('client connected.');
    connection.send('hello from javascript client.'); // Send the message 'Ping' to the server
};

// Log errors
connection.onerror = function (error) {
    console.log('WebSocket Error ' + error);
};

// Log messages from the server
connection.onmessage = function (e) {
    console.log('server: ' + e.data);
};


var user; // the logged-in user

var login = function(){
    var username = $('#username').val();
    var password = $('#password').val();
    connection.send("login" + "," + username + "," + password);
    console.log('client logged in.');
    user = username;
    $('#loggedOut').show();
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

var startGame = function(){
    var gamename = $('#gamename').val();
    connection.send("start-game" + "," + gamename);
    console.log('start-game requested [ADMIN].');
};

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

