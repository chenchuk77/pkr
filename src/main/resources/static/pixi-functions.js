
var foldButton;
var checkButton;
var betButton;
var raiseButton;
var callButton;
var statusMessage;

function setStausMessageSprite(message){
    // function getStausMessageSprite(message){
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

    statusMessage = new PIXI.Text(message.value, style);
    //var statusMessage = new PIXI.Text(message.value, style);
    statusMessage.x = 10;
    statusMessage.y = 550;

    //return statusMessage;
}

function getCardSprite(cardCode) {
    let cc = new PIXI.Container();
    //console.log("getCardSprite called. with " + cardCode);
    //console.log(cardCode);
    // card background
    let cardBg = new PIXI.Graphics();
    cardBg.name = 'bg';
    cardBg.lineStyle(1, 0xeefffc, 1);
    cardBg.beginFill(0xeeffcc);
    cardBg.drawRoundedRect(0, 0,119, 155, 12);
    cc.addChild(cardBg);
    // face down card drawn as rect
    if (cardCode === "XX"){
        let hiddenCard = new PIXI.Graphics();
        hiddenCard.name = 'hidden';
        hiddenCard.lineStyle(1, 0x023363, 1);
        hiddenCard.beginFill(0x023363);
        hiddenCard.drawRoundedRect(0, 0,115, 151, 12);
        hiddenCard.position.set(2, 2);
        cc.addChild(hiddenCard);
        return cc;
    }
    // face up card
    for (let i = 0; i < cards_array.length; i++) {
        if (cards_array[i].code === cardCode) {
            let card = new Sprite(resources[cards_array[i].file].texture);
            card.position.set(2,2);
            cc.addChild(card);
            return cc;
        }
    }
    return null;
}

function getBetButtonSprite(amount){
    let betButtonSprite = new Sprite(resources['images/buttons_120x40/button_bet.png'].texture);
    betButtonSprite.interactive = true;
    betButtonSprite.click = function() {
        console.log("bet button clicked.");
        sendAction("bet", optionAmouns["min_bet"]);
    };
    // betButtonSprite.scale.x=0.1;
    // betButtonSprite.scale.y=0.1;
    let text = new PIXI.Text(amount, {font: '14px Arial Bold', fill: '#FFFFFF'});
    betButtonSprite.addChild(text);
    return betButtonSprite;
}
function getRaiseButtonSprite(amount){
    let raiseButtonSprite = new Sprite(resources['images/buttons_120x40/button_raise.png'].texture);
    raiseButtonSprite.interactive = true;
    raiseButtonSprite.click = function() {
        console.log("raise button clicked.");
        sendAction("raise", optionAmouns["min_raise"]);
    };
    // raiseButtonSprite.scale.x=0.1;
    // raiseButtonSprite.scale.y=0.1;
    let text = new PIXI.Text(amount, {font: '14px Arial Bold', fill: '#FFFFFF'});
    raiseButtonSprite.addChild(text);
    return raiseButtonSprite;
}

function getCallButtonSprite(amount){
    let callButtonSprite = new Sprite(resources['images/buttons_120x40/button_call.png'].texture);
    callButtonSprite.interactive = true;
    callButtonSprite.click = function() {
        console.log("call button clicked.");
        sendAction("call", optionAmouns["call"])
    };
    // callButtonSprite.scale.x=0.1;
    // callButtonSprite.scale.y=0.1;
    let text = new PIXI.Text(amount, {font: '14px Arial Bold', fill: '#FFFFFF'});
    callButtonSprite.addChild(text);
    return callButtonSprite;
}

function getFoldButtonSprite(){
    let foldButtonSprite = new Sprite(resources['images/buttons_120x40/button_fold.png'].texture);
    foldButtonSprite.name = 'fold';
    foldButtonSprite.interactive = true;
    // foldButtonSprite.click = function() {
    //     console.log("fold button clicked.");
    //     sendAction("fold", 0);
    // };
    return foldButtonSprite;
}
function getCheckButtonSprite(){
    let checkButtonSprite = new Sprite(resources['images/buttons_120x40/button_check.png'].texture);
    checkButtonSprite.interactive = true;
    checkButtonSprite.click = function() {
        console.log("check button clicked.");
        sendAction("check", 0)
    };
    return checkButtonSprite;
}

function getBbSprite() {
    let bbSprite = new Sprite(resources['images/other/bbButton.png'].texture);
    bbSprite.scale.x=0.1;
    bbSprite.scale.y=0.1;
    return bbSprite;
}
function getSbSprite() {
    let sbSprite = new Sprite(resources['images/other/sbButton.png'].texture);
    sbSprite.scale.x=0.1;
    sbSprite.scale.y=0.1;
    return sbSprite;
}
function getDealerSprite() {
    let dealerSprite = new Sprite(resources['images/other/dealerButton.png'].texture);
    dealerSprite.scale.set(0.5, 0.5);
    return dealerSprite;
}

function drawTable(){
    console.log('drawTable() called.');
    let table = newTableContainer();
    app.stage.addChild(table);
}
function drawStatusContainer(){
    console.log('drawStatusContainer() called.');
    let sc = newStatusContainer();
    app.stage.addChild(sc);
}


function updateTable(data){
    let numOfPlayers = Object.keys(data).length;
    for (let i=0; i<numOfPlayers; i++){
        let p = data[i];
        //getUserContainer()
        playerContainers[i] = getUserContainer('images/avatars/human4.jpeg', p.name, p.chips ,p.strHole1, p.strHole2, 200+i*100, 250);
    }

    console.log('updateTable() called. // TODO //');
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

function removeActionButtons(){
    let abc = app.stage.getChildByName('abc');
    app.stage.removeChild(abc);
}

function drawActionButtons(data){
    console.log('drawActionButtons() called.');
    let abc = newActionButtonsContainer(data);
    app.stage.addChild(abc);
}


// function drawButtons(buttons){
//     console.log('drawButtons() called.');
//     sb_seat = buttons.sbPosition;
//     sb_sprite = getSbSprite();
//     sb_sprite.x = buttonsPosition[sb_seat].x;
//     sb_sprite.y = buttonsPosition[sb_seat].y;
//
//     bb_seat = buttons.bbPosition;
//     bb_sprite = getBbSprite();
//     bb_sprite.x = buttonsPosition[bb_seat].x;
//     bb_sprite.y = buttonsPosition[bb_seat].y;
//
//     dealer_seat = buttons.dealerPosition;
//     dealer_sprite = getDealerSprite();
//     dealer_sprite.x = buttonsPosition[dealer_seat].x;
//     dealer_sprite.y = buttonsPosition[dealer_seat].y;
//
//     app.stage.addChild(sb_sprite);
//     app.stage.addChild(bb_sprite);
//     app.stage.addChild(dealer_sprite);
// }


function drawBets(amount, seat_id){

}

function removeHoleCards(container){
    //console.log("removeHoleCards() called with %s", container.name);
    //console.log(container);

    //console.log("container got children.");
    //console.log(container.children);

    let bg = container.getChildByName('bg');
    container.removeChild(bg);
    let hole1 = container.getChildByName('hole1');
    container.removeChild(hole1);
    let hole2 = container.getChildByName('hole2');
    container.removeChild(hole2);

    //console.log("container got children.");
    //console.log(container.children);
    //console.log("removeHoleCards() return");

}

// drawing card1 of specific seat
function addHole1(code, container){
    let bg = new PIXI.Graphics();
    bg.name = 'bg';
    container.addChild(bg);
    let hole1 = getCardSprite(code);
    hole1.name = 'hole1';
    hole1.position.set(0,0);
    hole1.scale.x = TABLE.cards.scale;
    hole1.scale.y = TABLE.cards.scale;
    container.addChild(hole1);
}
// drawing card2 of specific seat
function addHole2(code, container){
    let hole2 = getCardSprite(code);
    hole2.name = 'hole2';
    hole2.position.set(50,0);
    hole2.scale.x = TABLE.cards.scale;
    hole2.scale.y = TABLE.cards.scale;
    container.addChild(hole2);
}


// drawing cards of specific seat
function addHoleCards(code1, code2, container){
    // container for highlighter
    let bg = new PIXI.Graphics();
    bg.name = 'bg';
    container.addChild(bg);
    let hole1 = getCardSprite(code1);
    hole1.name = 'hole1';
    hole1.position.set(0,0);
    hole1.scale.x = TABLE.cards.scale;
    hole1.scale.y = TABLE.cards.scale;
    container.addChild(hole1);
    let hole2 = getCardSprite(code2);
    hole2.name = 'hole2';
    hole2.position.set(50,0);
    hole2.scale.x = TABLE.cards.scale;
    hole2.scale.y = TABLE.cards.scale;
    container.addChild(hole2);
}
// function clearHoleCards(container){
//     let bg = container.getChildByName('bg');
//     let hole1 = container.getChildByName('hole1');
//     let hole2 = container.getChildByName('hole2');
//     container.removeChild(bg);
//     container.removeChild(hole1);
//     container.removeChild(hole2);
// }



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

// animate dealing cards among curved path
function deal(sprite, audio, start, via, end) {
    function bezier(t, p0, p1, p2, p3) {
        let cX = 3 * (p1.x - p0.x),
            bX = 3 * (p2.x - p1.x) - cX,
            aX = p3.x - p0.x - cX - bX;
        let cY = 3 * (p1.y - p0.y),
            bY = 3 * (p2.y - p1.y) - cY,
            aY = p3.y - p0.y - cY - bY;
        let x = (aX * Math.pow(t, 3)) + (bX * Math.pow(t, 2)) + (cX * t) + p0.x;
        let y = (aY * Math.pow(t, 3)) + (bY * Math.pow(t, 2)) + (cY * t) + p0.y;
        return {x: x, y: y};
    }

    let i = 0;
    sprite.alpha = 1;
    app.ticker.add(() => {
        if (i <= 1) {
            let p = bezier(i, start, via, end, end);
            sprite.x = p.x;
            sprite.y = p.y;
            i += 0.03;
            sprite.rotation += (1 - i * i);
            sprite.alpha -= 0.02;
            // fade out faster at the end
            if (sprite.alpha < 0.5){
                sprite.alpha -= 0.05;
            }
        }
    });
    audio.play();
}
// // drawing status message (update message)
// function drawStatusMessage(message) {
//     console.log('drawStatusMessage() called. with message: ' + message);
//     //statusMessage.text = message;
//     //app.stage.removeChild(statusMessage);
//     // app.renderer.render(app.stage);
//     // statusMessage.destroy();
//     //if app.stage.contains()
//
//     //statusMessage = getStausMessageSprite(message);
//     setStausMessageSprite(message);
//     console.log(statusMessage);
//     console.log(typeof(statusMessage));
//     console.log(statusMessage.isSprite);
//
//     console.log("********STATUS MESSAGE = " + statusMessage);
//     app.stage.addChild(statusMessage);
//     // app.renderer.render(app.stage);
//
//     // var style = {
//     //     font: 'bold italic 36px Arial',
//     //     fill: '#F7EDCA',
//     //     stroke: '#4a1850',
//     //     strokeThickness: 5,
//     //     dropShadow: true,
//     //     dropShadowColor: '#000000',
//     //     dropShadowAngle: Math.PI / 6,
//     //     dropShadowDistance: 6,
//     //     wordWrap: true,
//     //     wordWrapWidth: 440
//     // };
//     //
//     // statusMessage = new PIXI.Text(message.value, style);
//     // statusMessage.x = 30;
//     // statusMessage.y = 180;
//     //
//
// }