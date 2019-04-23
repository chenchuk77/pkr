
/*
* this file is a containers UI components definitions
* it uses data.js for values
* */

function newPlayerContainer(i, avatar, name, chips, commited, card1, card2, x, y){
    console.log('creating a player container for seat #' + i);

    // player main container
    let pc = new PIXI.Container();
    pc.name = 'pc';

    // betting chips container
    let bet = new PIXI.Container();
    bet.name = 'bet';
    bet.position.set(PLAYER[i].bet.x, PLAYER[i].bet.y);
    pc.addChild(bet);

    // middle square for bg
    let midRect = new PIXI.Graphics();
    midRect.lineStyle(1, 0x808080, 1);
    midRect.beginFill(0x808080);
    midRect.drawRect(0, 0, 64, 64);
    // midRect.position.set(32, 0);
    midRect.position.set(PLAYER[i].midrect.x, PLAYER[i].midrect.y);
    pc.addChild(midRect);

    // avatar container
    let ac = new PIXI.Container();
    // avatar bg circle
    let circle = new Graphics();
    circle.beginFill(0x808080);
    circle.lineStyle(1);
    circle.drawCircle(0, 0, 32);
    circle.endFill();
    circle.position.set(32, 32);
    ac.addChild(circle);
    // avatar image masked with a circle
    let mask = new Graphics();
    mask.beginFill(0x808080);
    mask.lineStyle(0);
    mask.drawCircle(0, 0, 30);
    mask.endFill();
    mask.position.set(32, 32);
    ac.addChild(mask);
    let img = new Sprite(resources[avatar].texture);
    img.mask = mask;
    ac.addChild(img);
    ac.position.set(PLAYER[i].ac.x, PLAYER[i].ac.y);
    // ac.position.set(0, 0);
    pc.addChild(ac);

    // hole cards container
    let hcc = new PIXI.Container();
    hcc.name = 'hcc';
    hcc.position.set(PLAYER[i].hcc.x, PLAYER[i].hcc.y);
    pc.addChild(hcc);
    drawHoleCards(card1, card2, hcc);

    // name and chips container
    let ncc = new PIXI.Container();
    ncc.name = 'ncc';
    let nameRect = new PIXI.Graphics();
    nameRect.lineStyle(1, 0x44e300, 1);
    nameRect.beginFill(0x808080);
    nameRect.drawRect(0, 0, 64, 32);
    //nameRect.endFill();
    let nameText = new PIXI.Text(name, {font: '14px Arial Bold', fill: '#FFFFFF'});
    // nameText.anchor.set(32, 16);
    nameRect.addChild(nameText);
    nameRect.position.set(0, 0);
    //pc1.addChild(nameRect);
    ncc.addChild(nameRect);
    let chipsRect = new PIXI.Graphics();
    chipsRect.name = 'chipsrect';
    chipsRect.lineStyle(1, 0x44e300, 1);
    chipsRect.beginFill(0x808080);
    chipsRect.drawRect(0, 0, 64, 32);
    // chipsRect.endFill();
    let chipsText = new PIXI.Text(chips, {font: '14px Arial', fill: '#FFFFFF'});
    chipsText.name = 'chips';
    // chipsText.anchor.set(96, 48);
    chipsRect.addChild(chipsText);
    chipsRect.position.set(0, 32);
    ncc.addChild(chipsRect);
    // pc1.addChild(chipsRect);
    //ncc.position.set(64, 0);
    ncc.position.set(PLAYER[i].ncc.x, PLAYER[i].ncc.y);
    pc.addChild(ncc);

    pc.position.set(x, y);
    return pc;

}

// returns a new container with chips graphic
function newChipsContainer(amount){
    let reminder = amount;
    let chips_1000 = 0;
    let chips_500  = 0;
    let chips_100  = 0;
    let chips_25   = 0;
    let chips_5    = 0;
    let chips_1    = 0;

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

    // console.log("1000:" + chips_1000 + "\n" +
    //     "100:" + chips_100 + "\n" +
    //     "25:" + chips_25 + "\n" +
    //     "5:" + chips_5 + "\n" +
    //     "1:" + chips_1 + "\n");

    // create container for chips
    let chips_ctr = new PIXI.Container();
    // chips_ctr.name = "player-chips";
    chips_ctr.scale.x = 0.5;
    chips_ctr.scale.y = 0.5;

    // stack all other chips
    let total_chips = chips_1000 + chips_100 + chips_25 + chips_5 + chips_1;
    let offset = 150;

    for (let i=0; i<chips_1; i++){
        let stackableChips1 = new Sprite(resources['images/chips/chip1_stack.png'].texture);
        offset -= 10;
        stackableChips1.position.set(50, offset);
        stackableChips1.scale.x = 0.2;
        stackableChips1.scale.y = 0.2;
        chips_ctr.addChild(stackableChips1);
    }
    for (let i=0; i<chips_5; i++){
        let stackableChips5 = new Sprite(resources['images/chips/chip5_stack.png'].texture);
        offset -= 10;
        stackableChips5.position.set(50, offset);
        stackableChips5.scale.x = 0.2;
        stackableChips5.scale.y = 0.2;
        chips_ctr.addChild(stackableChips5);
    }
    for (let i=0; i<chips_25; i++){
        let stackableChips25 = new Sprite(resources['images/chips/chip25_stack.png'].texture);
        offset -= 10;
        stackableChips25.position.set(50, offset);
        stackableChips25.scale.x = 0.2;
        stackableChips25.scale.y = 0.2;
        chips_ctr.addChild(stackableChips25);
    }
    for (let i=0; i<chips_100; i++){
        let stackableChips100 = new Sprite(resources['images/chips/chip100_stack.png'].texture);
        offset -= 10;
        stackableChips100.position.set(50, offset);
        stackableChips100.scale.x = 0.2;
        stackableChips100.scale.y = 0.2;
        chips_ctr.addChild(stackableChips100);
    }
    for (let i=0; i<chips_500; i++){
        let stackableChips500 = new Sprite(resources['images/chips/chip500_stack.png'].texture);
        offset -= 10;
        stackableChips500.position.set(50, offset);
        stackableChips500.scale.x = 0.2;
        stackableChips500.scale.y = 0.2;
        chips_ctr.addChild(stackableChips500);
    }
    for (let i=0; i<chips_1000; i++){
        let stackableChips1000 = new Sprite(resources['images/chips/chip1000_stack.png'].texture);
        offset -= 10;
        stackableChips1000.position.set(50, offset);
        stackableChips1000.scale.x = 0.2;
        stackableChips1000.scale.y = 0.2;
        chips_ctr.addChild(stackableChips1000);
    }
    // add top chip : FIXED COLOR for now
    let topChip = new Sprite(resources['images/chips/chip100_top.png'].texture);
    topChip.position.set(50, offset-10);
    topChip.scale.x = 0.2;
    topChip.scale.y = 0.2;
    chips_ctr.addChild(topChip);

    // add amount in text neat chips
    let amountText = new PIXI.Text(amount, {font: '38px Arial', fill: '#FFFFFF'});
    amountText.position.set(90, 130);
    chips_ctr.addChild(amountText);
    return chips_ctr;
}

function newPotContainer(amount){
    let potc = newChipsContainer(amount);
    potc.position.set(TABLE.pot.x, TABLE.pot.y);
    potc.name = 'pot';
    return potc;
}

function newTableContainer(){
    // table container (pot, deck, commcards)
    let tc = new PIXI.Container();
    tc.name = 'tc';

    let table = new PIXI.Graphics();
    table.lineStyle(4, 0x99CCFF, 1);
    table.name = 'table';
    table.beginFill(0x003333);
    table.drawRoundedRect(0, 0, 600, 300, 140);
    table.endFill();
    table.x = 50;
    table.y = 50;
    tc.addChild(table);

    let deck = newDeck();
    tc.addChild(deck);

    let ccc = new PIXI.Container();
    tc.addChild(ccc);

    return tc;

}

function newActionButtonsContainer(data){
    // {"type":"waitaction", "player":"fff", "options":["fold","raise","allin","check"], "optionAmounts":{"max_raise":9940,"min_raise":120,"allin":9940}}
    let abc = new PIXI.Container();
    abc.name = "abc";
    abc.position.set(BUTTONS.abc.x, BUTTONS.abc.y);

    if (data.options.includes("fold")){
        let fold = getFoldButtonSprite();
        fold.position.set(BUTTONS.fold.x, BUTTONS.fold.y);
        abc.addChild(fold);
    }
    if (data.options.includes("check")){
        let check = getCheckButtonSprite();
        check.position.set(BUTTONS.check.x, BUTTONS.check.y);
        abc.addChild(check);
    }
    if (data.options.includes("call")){
        let call = getCallButtonSprite(data.optionAmounts["call"]);
        call.position.set(BUTTONS.call.x, BUTTONS.call.y);
        abc.addChild(call);
    }
    if (data.options.includes("bet")){
        let bet = getBetButtonSprite(data.optionAmounts["min_raise"]);
        bet.position.set(BUTTONS.bet.x, BUTTONS.bet.y);
        abc.addChild(bet);
    }
    if (data.options.includes("raise")){
        let raise = getRaiseButtonSprite(data.optionAmounts["min_raise"]);
        raise.position.set(BUTTONS.raise.x, BUTTONS.raise.y);
        abc.addChild(raise);
    }

    return abc;

}

function newDeck() {
    let deck = new Sprite(resources['images/cards/back.svg'].texture);
    deck.position.set(0, 0);
    deck.scale.x = TABLE.cards.scale;
    deck.scale.y = TABLE.cards.scale;
    deck.position.set(TABLE.deck.x, TABLE.deck.y);
    return deck;
}

// returns a new container for community cards (f1,f2,f3,t,r)
function newCommunityCardsContainer(data) {
    let ccc = new PIXI.Container();
    ccc.name = "ccc";
    // flop1, flop2, flop3, turn, river
    ccc.scale.x = TABLE.cards.scale;
    ccc.scale.y = TABLE.cards.scale;


    if (data.flop1 !== undefined){
        let flop1_card = getCardSprite(data.flop1);
        flop1_card.position.set(TABLE.flop1.x, TABLE.flop1.y);
        // flop1_card.scale.x = 0.5;
        // flop1_card.scale.y = 0.5;
        ccc.addChild(flop1_card);
    }
    if (data.flop2 !== undefined){
        let flop2_card = getCardSprite(data.flop2);
        flop2_card.position.set(TABLE.flop2.x, TABLE.flop2.y);
        // flop2_card.scale.x = 0.5;
        // flop2_card.scale.y = 0.5;
        ccc.addChild(flop2_card);
    }
    if (data.flop3 !== undefined){
        let flop3_card = getCardSprite(data.flop3);
        flop3_card.position.set(TABLE.flop3.x, TABLE.flop3.y);
        // flop3_card.scale.x = 0.5;
        // flop3_card.scale.y = 0.5;
        ccc.addChild(flop3_card);
    }
    if (data.turn !== undefined){
        let turn_card = getCardSprite(data.turn);
        turn_card.position.set(TABLE.turn.x, TABLE.turn.y);
        // turn_card.scale.x = 0.5;
        // turn_card.scale.y = 0.5;
        ccc.addChild(turn_card);
    }
    if (data.river !== undefined){
        let river_card = getCardSprite(data.river);
        river_card.position.set(TABLE.river.x, TABLE.river.y);
        // river_card.scale.x = 0.5;
        // river_card.scale.y = 0.5;
        ccc.addChild(river_card);
    }
    return ccc;
}

