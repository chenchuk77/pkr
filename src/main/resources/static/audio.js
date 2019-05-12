
console.log('start loading audio resources.');

let bet_file   = 'sounds/bet.mp3';
let raise_file = 'sounds/raise.mp3';
let call_file  = 'sounds/call.mp3';
let fold_file  = 'sounds/fold.mp3';
let check_file = 'sounds/check.mp3';

let human_sounds = {
  bet:   getSound(bet_file),
  raise: getSound(raise_file),
  call:  getSound(call_file),
  fold:  getSound(fold_file),
  check: getSound(check_file)
};

let dealer_sounds = {
  new_hand:      getSound('sounds/new-hand.mp3'),
  dealing_flop:  getSound('sounds/dealing-flop.mp3'),
  dealing_turn:  getSound('sounds/dealing-turn.mp3'),
  dealing_river: getSound('sounds/dealing-river.mp3')
};


let game_sounds = {
  myturn: getSound('sounds/my-turn.wav'),
  deal: getSound('sounds/deal4.wav')

};


// init 2 arrays for dealing sound cards (18 cards)
// because they will be used in parallel
let dealing_file = 'sounds/deal4.wav';
let dealCard1Sound = [];
let dealCard2Sound = [];
for (let i=0; i<NUMBER_OF_PLAYERS; i++){
  dealCard1Sound.push(getSound(dealing_file));
  dealCard2Sound.push(getSound(dealing_file));
}

function getSound(src) {
  let x = {};
  x.sound = document.createElement("audio");
  x.sound.src = src;
  x.sound.setAttribute("preload", "auto");
  x.sound.setAttribute("controls", "none");
  x.sound.style.display = "none";
  x.play = function () { x.sound.play();  };
  x.stop = function () { x.sound.pause(); };
  return x;
}
console.log('done loading audio resources.');
