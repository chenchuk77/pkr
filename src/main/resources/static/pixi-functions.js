
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

function getRaiseButtonSprite(){
    raiseButtonSprite = new Sprite(resources['images/buttons_120x40/button_raise.png'].texture);
    // raiseButtonSprite.scale.x=0.1;
    // raiseButtonSprite.scale.y=0.1;
    return raiseButtonSprite;
}

function getCallButtonSprite(){
    callButtonSprite = new Sprite(resources['images/buttons_120x40/button_call.png'].texture);
    // callButtonSprite.scale.x=0.1;
    // callButtonSprite.scale.y=0.1;
    return callButtonSprite;
}

function getBbSprite() {
    bbSprite = new Sprite(resources['images/other/bbButton.png'].texture);
    bbSprite.scale.x=0.1;
    bbSprite.scale.y=0.1;
    return bbSprite;
}
function getSbSprite() {
    sbSprite = new Sprite(resources['images/other/sbButton.png'].texture);
    sbSprite.scale.x=0.1;
    sbSprite.scale.y=0.1;
    return sbSprite;
}
function getDealerSprite() {
    dealerSprite = new Sprite(resources['images/other/dealerButton.png'].texture);
    dealerSprite.scale.x=0.1;
    dealerSprite.scale.y=0.1;
    return dealerSprite;
}

function drawTable(){
    console.log('drawTable() called.')
    table = new Sprite(PIXI.loader.resources['images/other/poker_table.png'].texture);
    app.stage.addChild(table);
}

function updateTable(tableSprite){
    console.log('updateTable() called. // TODO //')
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
    card1 = getCardSprite(code1);
    card1.x=200;
    card1.y=200;
    card2 = getCardSprite(code2);
    card2.x=255;
    card2.y=200;
    card3 = getCardSprite(code3);
    card3.x=310;
    card3.y=200;
    app.stage.addChild(card1);
    app.stage.addChild(card2);
    app.stage.addChild(card3);
}
function drawTurn(code){
    turnCard = getCardSprite(code);
    turnCard.x=380;
    turnCard.y=200;
    app.stage.addChild(turnCard);

}

function drawRiver(code){
    riverCard = getCardSprite(code);
    riverCard.x=450;
    riverCard.y=200;
    app.stage.addChild(riverCard);
}


function drawActionButtons(){
    console.log('drawActionButtons() called.');
    callButton = getCallButtonSprite();
    callButton.x= 100;
    callButton.y= 450;

    raiseButton = getRaiseButtonSprite();
    raiseButton.x= 250;
    raiseButton.y= 450;
    app.stage.addChild(callButton);
    app.stage.addChild(raiseButton);


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
    card2.x=location.x+20;
    card2.y=location.y+20;
    app.stage.addChild(card1);
    app.stage.addChild(card2);
}
