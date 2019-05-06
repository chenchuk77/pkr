$( document ).ready(function() {
    $('#loggedOut').show('slow');
    $('#loggedIn').show('slow');
    // Add the PIXI canvas
    document.body.appendChild(app.view);
});


// function addStatusMessage(status_array, message){
//     status_array.push(message);
//
// }
// function showStatusMessages(status_array, sprite){
//     console.log(sprite);
//     console.log(status_array);
//     // reset text
//     sprite.text = '\n';
//     // all messages can appear
//     if (status_array.length <= STATUS.messages){
//         for (let n=0; n<status_array.length; n++){
//             sprite.text += status_array[n] + "\n";
//         }
//     } else {
//         // more than N messages, extracting last N messages
//         for (let n=status_array.length-STATUS.messages; n<status_array.length; n++){
//             sprite.text += status_array[n] + "\n";
//         }
//     }
// }

let colorMatrix = [
    1, 0, 0, 1,
    0, 0, 0, 0,
    0, 0, 0, 0,
    1, 0, 0, 1
];

let app = new PIXI.Application({ width: 640, height: 360 });
let f = new PIXI.filters.ColorMatrixFilter();

﻿f.matrix = colorMatrix;

// document.body.appendChild(app.view);
let c1 = new PIXI.Graphics();
c1.beginFill(0xe4afe2);
c1.drawCircle(0, 0, 50);
c1.position.set(100, 100);
c1.lineStyle(12);
c1.filters = [f];
app.stage.addChild(c1);

let c2 = new PIXI.Graphics();
c2.beginFill(0x5cafe2);
c2.drawCircle(0, 0, 50);
c2.position.set(200, 200);
//c2.tint = 0xffffff;
app.stage.addChild(c2);

// accepts a container and add a dynamic text that will
// disappear in few seconds;
function addActionText(container, text){
    let ac = new PIXI.Container();
    ac.name = 'ac';

    // background
    let r1 = new PIXI.Graphics();
    r1.lineStyle(2, 0xF7DC6F, 1);
    r1.beginFill(0x273746);
    r1.drawRoundedRect(0, 0, 96, 32, 8);
    ac.addChild(r1);

    // text
    let t1 = new PIXI.Text(text, new PIXI.TextStyle(NAME_STYLE));
    t1.position.set(16, 6);
    ac.addChild(t1);

    // smoothly light and dark then removes itself from the container
    let x = 0;
    let countUp = true;
    app.ticker.add(() => {
        if (countUp){
            x += 0.02;
            if (x > 1 ) countUp = false;
        } else {
            x -= 0.01;
            if (x <= 0 ){
                container.removeChild(ac);
                return;
            }
        }
        ac.alpha = x
    });
    container.addChild(ac);
}

let a = new PIXI.Container();
app.stage.addChild(a);

addActionText(a, 'Raise');

// showAction(a, 'Raise');
//showAction(ac, 'Fold');




// blinks c1 as background for active player
let count = 0;
app.ticker.add(() => {
    count += (1/60)*Math.PI;
    c1.tint = 0xffffff*Math.abs(Math.sin(count)) ;
});







// function showAction(container, text){
//     let ac = new PIXI.Container();
//     ac.name = 'ac';
//
//     let r1 = new PIXI.Graphics();
//     r1.lineStyle(2, 0xF7DC6F, 1);
//     r1.beginFill(0x273746);
//     r1.drawRect(0, 0, 128, 64);
//     r1.position.set(10, 10);
//     ac.addChild(r1);
//
//     let t1 = new PIXI.Text(text, new PIXI.TextStyle(NAME_STYLE));
//     ac.addChild(t1);
//     let x = 0;
//     app.ticker.add(() => {
//         // ticker runs 60times/1sec
//         x += 1;
//         if (x > 60 * ACTION_SHOW_SECONDS ){
//             app.stage.removeChild(t1);
//         }
//     });
//     return ac;
// }












// function animateScene(delta) {
//     //Animat﻿e neonSign
//     c1.alpha = 0.1;
//     count += 2;
//     let bright = 2 + Math.sin(count);
//     //c1.brightness(bright, false);
// 	﻿
// 	requestAnimationFrame(animateScene);
// ﻿
// 	app.renderer.render(app.view);
// }
// app.ticker.add(animateScene);


// let c = new PIXI.Container();
// c.position.set(200, 200);
//
// let b1 = new PIXI.Graphics();
// b1.beginFill(0x5cafe2);
// b1.drawRect(0, 0, 200, 200);
// c.addChild(b1);
//
// let t1 = new PIXI.Text();
// t1.wordWrap = true;
// t1.wordWrapWidth = 200;
// // t1.width = 200;
// // t1.height = 200;
// c.addChild(t1);
// app.stage.addChild(c);


// const numOfMessages = 3;
// let statusMessages = ['m0-init message'];
// addStatusMessage(statusMessages, 'm1');
// addStatusMessage(statusMessages, 'm2');
// addStatusMessage(statusMessages, 'm3');
// addStatusMessage(statusMessages, 'm4');
// console.log(statusMessages);
// showStatusMessages(statusMessages, t1);



//let tinted = [];
//let count = 0;

// tinted.push(c1);
// tinted.push(c2);

// function highlightActivePlayer(delta) {
//     //count += 0.05;
//     count += Math.PI /60;
//     for (let i=0; i<tinted.length; i++){
//         tinted[i].tint +=  Math.sin(count);
//         //tinted[i].tint = 0xf0ffff + 0x10000 * Math.abs(Math.sin(count))
//         //tinted[i].tint += 0xf0ffff + 10 * Math.sin(count)
//     }
//    // c1.tint +=  Math.sin(count)
// }
// app.ticker.add(highlightActivePlayer);
// app.ticker.remove(highlightActivePlayer);



// // Listen for animate update
// app.ticker.add(function(delta) {
//     count += 0.1 * delta;
//     circle.tint += Math.sin(count)
// });


// function animateScene() {
//
//     //Animat﻿e neonSign
//     neonSign.alpha = 0.1;
//     count += 2;
//     var bright = 2 + Math.sin(count);
//     neonFilter.brightness(bright, false);
// 	﻿
// 	requestAnimationFrame(animateScene);
// ﻿
// 	renderer.render(street);
//
// }﻿﻿﻿

