window.onload = start;
function start(){
	document.body.appendChild(app.view);
	setTimeout(function() { deal(c1, {x: 400, y: 400}, {x: 400, y: 200}, {x: 600, y: 100})}, 0);
	setTimeout(function() { deal(c2, {x: 400, y: 400}, {x: 370, y: 150}, {x: 300, y: 100})}, 200);
	setTimeout(function() { deal(c3, {x: 400, y: 400}, {x: 170, y: 150}, {x: 100, y: 100})}, 400);
	setTimeout(function() { deal(c12, {x: 400, y: 400}, {x: 420, y: 200}, {x: 650, y: 100})}, 600);
	setTimeout(function() { deal(c22, {x: 400, y: 400}, {x: 400, y: 150}, {x: 350, y: 100})}, 800);
	setTimeout(function() { deal(c32, {x: 400, y: 400}, {x: 220, y: 150}, {x: 150, y: 100})}, 1000);
	setTimeout(function() { 
		app.stage.removeChild(c1);app.stage.removeChild(c2);app.stage.removeChild(c3);
		app.stage.removeChild(c12);app.stage.removeChild(c22);app.stage.removeChild(c32);
	}, 3000);
}

const app = new PIXI.Application({ backgroundColor: 0x1099bb });
let cc = new PIXI.Container();
let c1 = PIXI.Sprite.from('resources/card-small.png');
let c2 = PIXI.Sprite.from('resources/card-small.png');
let c3 = PIXI.Sprite.from('resources/card-small.png');
let c12 = PIXI.Sprite.from('resources/card-small.png');
let c22 = PIXI.Sprite.from('resources/card-small.png');
let c32 = PIXI.Sprite.from('resources/card-small.png');
c1.anchor.set(0.5);c2.anchor.set(0.5);c3.anchor.set(0.5);
c12.anchor.set(0.5);c22.anchor.set(0.5);c32.anchor.set(0.5);
c1.position.set(400,400);c2.position.set(400,400);c3.position.set(400,400);
c12.position.set(400,400);c22.position.set(400,400);c32.position.set(400,400);
app.stage.addChild(c1);app.stage.addChild(c2);app.stage.addChild(c3);
app.stage.addChild(c12);app.stage.addChild(c22);app.stage.addChild(c32);

function deal(sprite, start, via, end){
	function bezier(t, p0, p1, p2, p3){
		var cX = 3 * (p1.x - p0.x),
			bX = 3 * (p2.x - p1.x) - cX,
			aX = p3.x - p0.x - cX - bX;
		var cY = 3 * (p1.y - p0.y),
			bY = 3 * (p2.y - p1.y) - cY,
			aY = p3.y - p0.y - cY - bY;
		var x = (aX * Math.pow(t, 3)) + (bX * Math.pow(t, 2)) + (cX * t) + p0.x;
		var y = (aY * Math.pow(t, 3)) + (bY * Math.pow(t, 2)) + (cY * t) + p0.y;
		return {x: x, y: y};
	}
	let i=0;
	app.ticker.add(() => {
		if (i<=1){
			var p = bezier(i, start, via, end, end);
			sprite.x=p.x;
			sprite.y=p.y;
			i += 0.03;
			sprite.rotation += 1 * (1 - i);
		}
	});
}


