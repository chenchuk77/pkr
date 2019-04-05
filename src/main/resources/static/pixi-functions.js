
var foldButton;
var checkButton;
var betButton;
var raiseButton;
var callButton;


function getCardSprite(cardCode) {
    for (var i = 0; i < cards_array.length; i++) {
        if (cards_array[i].code == cardCode) {
            cardSprite = new Sprite(resources[cards_array[i].file].texture);
            // cardSprite.scale.x=0.1;
            // cardSprite.scale.y=0.1;
            cardSprite.scale.x=0.25;
            cardSprite.scale.y=0.25;
            return cardSprite;
        }
    }
    return null;
}

function getBetButtonSprite(){
    var betButtonSprite = new Sprite(resources['images/buttons_120x40/button_bet.png'].texture);
    betButtonSprite.interactive = true;
    betButtonSprite.click = function() {
        console.log("bet button clicked.");
        sendAction("bet", optionAmouns["min_bet"]);
    };
    // betButtonSprite.scale.x=0.1;
    // betButtonSprite.scale.y=0.1;
    return betButtonSprite;
}
function getRaiseButtonSprite(){
    var raiseButtonSprite = new Sprite(resources['images/buttons_120x40/button_raise.png'].texture);
    raiseButtonSprite.interactive = true;
    raiseButtonSprite.click = function() {
        console.log("raise button clicked.");
        sendAction("raise", optionAmouns["min_raise"]);
    };
    // raiseButtonSprite.scale.x=0.1;
    // raiseButtonSprite.scale.y=0.1;
    return raiseButtonSprite;
}

function getCallButtonSprite(){
    var callButtonSprite = new Sprite(resources['images/buttons_120x40/button_call.png'].texture);
    callButtonSprite.interactive = true;
    callButtonSprite.click = function() {
        console.log("call button clicked.");
        sendAction("call", optionAmouns["call"])
    };
    // callButtonSprite.scale.x=0.1;
    // callButtonSprite.scale.y=0.1;
    return callButtonSprite;
}

function getFoldButtonSprite(){
    var foldButtonSprite = new Sprite(resources['images/buttons_120x40/button_fold.png'].texture);
    foldButtonSprite.interactive = true;
    //foldButtonSprite.on('click', sendAction("fold", 0));
    foldButtonSprite.click = function() {
        console.log("fold button clicked.");
        sendAction("fold", 0)
    };
    // callButtonSprite.scale.x=0.1;
    // callButtonSprite.scale.y=0.1;
    return foldButtonSprite;
}
function getCheckButtonSprite(){
    var checkButtonSprite = new Sprite(resources['images/buttons_120x40/button_check.png'].texture);
    checkButtonSprite.interactive = true;
    checkButtonSprite.click = function() {
        console.log("check button clicked.");
        sendAction("check", 0)
    };
    return checkButtonSprite;
}

function getBbSprite() {
    var bbSprite = new Sprite(resources['images/other/bbButton.png'].texture);
    bbSprite.scale.x=0.1;
    bbSprite.scale.y=0.1;
    return bbSprite;
}
function getSbSprite() {
    var sbSprite = new Sprite(resources['images/other/sbButton.png'].texture);
    sbSprite.scale.x=0.1;
    sbSprite.scale.y=0.1;
    return sbSprite;
}
function getDealerSprite() {
    var dealerSprite = new Sprite(resources['images/other/dealerButton.png'].texture);
    dealerSprite.scale.x=0.1;
    dealerSprite.scale.y=0.1;
    return dealerSprite;
}

function drawTable(){
    console.log('drawTable() called.')
    var table = new Sprite(PIXI.loader.resources['images/other/poker_table.png'].texture);
    app.stage.addChild(table);
}

function updateTable(tableSprite){
    console.log('updateTable() called. // TODO //')
    app.stage.removeChild(foldButton);
    app.stage.removeChild(checkButton);
    app.stage.removeChild(betButton);
    app.stage.removeChild(raiseButton);
    app.stage.removeChild(callButton);
    console.log('buttons removed')

    // table = new Sprite(PIXI.loader.resources['images/other/poker_table.png'].texture);
    // table.
    // app.stage.addChild(table);
}


function drawPocketCards(seats){
    console.log('drawPocketCards() called.')
    for (var i=0; i<9; i++){
        console.log(i);
        if (seats[i] === null) {
            // dont draw null players (probably empty seat)
            continue;}
        if (seats[i] === undefined) {
            // for testing, using 4 players -> seats[4-8] is undefined
            continue;}

        if (! (seats[i].inHand && seats[i].inGame )) {
            // dont draw folded hands
            continue;}
        if (seats[i].name === player.name) {
            // draw my hand face-up
            drawCards(player.cards[0], player.cards[1], i);
        } else {
            // draw opponents back cards (face-down)
            drawCards('00','00', i);
        }
    }
}

// function drawAllCards(players){
//     console.log(players);
//     for (var i=0; i<players.length; i++){
//         if (players[i] === player.name){
//             // my hand
//             drawCards(player.cards[0], player.cards[1], 100*(i+1), 400, 20, 20);
//         }else{
//             // others
//             drawCards('00','00',100*(i+1), 400, 20, 20);
//         }
//     }
// }

function drawFlop(code1, code2, code3){
    var card1 = getCardSprite(code1);
    card1.x=200;
    card1.y=200;
    var card2 = getCardSprite(code2);
    card2.x=255;
    card2.y=200;
    var card3 = getCardSprite(code3);
    card3.x=310;
    card3.y=200;
    app.stage.addChild(card1);
    app.stage.addChild(card2);
    app.stage.addChild(card3);
}
function drawTurn(code){
    var turnCard = getCardSprite(code);
    turnCard.x=380;
    turnCard.y=200;
    app.stage.addChild(turnCard);

}

function drawRiver(code){
    var riverCard = getCardSprite(code);
    riverCard.x=450;
    riverCard.y=200;
    app.stage.addChild(riverCard);
}


function drawActionButtons(){
    console.log('drawActionButtons() called.');

    if (options.includes("fold")){
        foldButton = getFoldButtonSprite();
        foldButton.x= 100;
        foldButton.y= 450;
        app.stage.addChild(foldButton);
    }
    if (options.includes("call")) {
        callButton = getCallButtonSprite();
        callButton.x = 250;
        callButton.y = 450;
        app.stage.addChild(callButton);
    }
    if (options.includes("check")) {
        checkButton = getCheckButtonSprite();
        checkButton.x = 250;
        checkButton.y = 500;
        app.stage.addChild(checkButton);
    }
    if (options.includes("bet")) {
        betButton = getBetButtonSprite();
        betButton.x = 400;
        betButton.y = 450;
        app.stage.addChild(betButton);
    }
    if (options.includes("raise")) {
        raiseButton = getRaiseButtonSprite();
        raiseButton.x = 400;
        raiseButton.y = 500;
        app.stage.addChild(raiseButton);
    }

}


function drawButtons(buttons){
    console.log('drawButtons() called.');
    sb_seat = buttons.sbPosition;
    sb_sprite = getSbSprite();
    sb_sprite.x = buttonsPosition[sb_seat].x;
    sb_sprite.y = buttonsPosition[sb_seat].y;

    bb_seat = buttons.bbPosition;
    bb_sprite = getBbSprite();
    bb_sprite.x = buttonsPosition[bb_seat].x;
    bb_sprite.y = buttonsPosition[bb_seat].y;

    dealer_seat = buttons.dealerPosition;
    dealer_sprite = getDealerSprite();
    dealer_sprite.x = buttonsPosition[dealer_seat].x;
    dealer_sprite.y = buttonsPosition[dealer_seat].y;

    app.stage.addChild(sb_sprite);
    app.stage.addChild(bb_sprite);
    app.stage.addChild(dealer_sprite);
}


function drawBets(amount, seat_id){

}
// drawing cards of specific seat
function drawCards(code1, code2, seat_id){
    var location = pocketCardPosition[seat_id];
    card1 = getCardSprite(code1);
    card1.x=location.x;
    card1.y=location.y;
    card2 = getCardSprite(code2);
    card2.x=location.x+40;
    card2.y=location.y+10;
    app.stage.addChild(card1);
    app.stage.addChild(card2);
}
