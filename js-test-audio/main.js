

let af1 = './organfinale.mp3'
let af2 = './piano2-Audacity1.2.5.mp3'


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

const app = new PIXI.Application({ width: 800, height: 400, backgroundColor: 0x1099bb });

let s1 = getSound(af1);
let s2 = getSound(af2);
// let card;

window.onload = function () { 
  document.body.appendChild(s1.sound);
  document.body.appendChild(s2.sound);
  document.body.appendChild(app.view);

  let card = PIXI.Sprite.from('./card-small.png');
  card.position.set(app.screen.width/2, app.screen.height/2)
  card.anchor.set(0.5, 0.5);
  app.stage.addChild(card);
  app.ticker.add(() => {
      card.rotation += 0.5*Math.sin(1/120 * Math.PI*card.x);

  card.x += 1;
  card.y += Math.sin(1/120 * Math.PI*card.x);

  if (card.x > 500){
    this.stop();
  }
  console.log(card.x);
  });

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


// function sound(src) {
//   this.sound = document.createElement("audio");
//   this.sound.src = src;
//   this.sound.setAttribute("preload", "auto");
//   this.sound.setAttribute("controls", "none");
//   this.sound.style.display = "none";
//   document.body.appendChild(this.sound);
//   this.play = function(){
//     this.sound.play();
//   }
//   this.stop = function(){
//     this.sound.pause();
//   }
// }

// function start(){
//   console.log('start() called.');
//   // let f = sound(
//   //   );
//   // f.play;
// }

// function stop(){
//   console.log('stop() called.')
// }
