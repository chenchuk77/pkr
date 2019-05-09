
let af1 = 'resources/deal4.wav'
// let af2 = 'resources/piano2-Audacity1.2.5.mp3'

let s1 = getSound(af1);
let s2 = getSound(af1);
let s3 = getSound(af1);
let s4 = getSound(af1);
let s5 = getSound(af1);
let s6 = getSound(af1);

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

// page functions
function start(s){
  console.log('start() called.');
  s.play();
}

function stop(s){
  console.log('stop() called.');
  s.stop();
}

