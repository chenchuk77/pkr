// constants
let pocketCardPosition =[
    {x: 100, y:250},
    {x: 200, y:300},
    {x: 300, y:300},
    {x: 400, y:250}
];
let betsPosition =[
    {x: 120, y:200},
    {x: 220, y:250},
    {x: 320, y:250},
    {x: 420, y:200}
];
let buttonsPosition =[
    {x: 80, y:200},
    {x: 180, y:250},
    {x: 280, y:250},
    {x: 380, y:200}
];

let actionButtonsPosition =[
    {x: 100, y:600},
    {x: 350, y:600},
    {x: 500, y:600},
    {x: 650, y:600}
];


let cards_array = [
    { code: 'Tc', file: 'images/cards/Tc.svg'},
    { code: 'Td', file: 'images/cards/Td.svg'},
    { code: 'Th', file: 'images/cards/Th.svg'},
    { code: 'Ts', file: 'images/cards/Ts.svg'},
    { code: '2c', file: 'images/cards/2c.svg'},
    { code: '2d', file: 'images/cards/2d.svg'},
    { code: '2h', file: 'images/cards/2h.svg'},
    { code: '2s', file: 'images/cards/2s.svg'},
    { code: '3c', file: 'images/cards/3c.svg'},
    { code: '3d', file: 'images/cards/3d.svg'},
    { code: '3h', file: 'images/cards/3h.svg'},
    { code: '3s', file: 'images/cards/3s.svg'},
    { code: '4c', file: 'images/cards/4c.svg'},
    { code: '4d', file: 'images/cards/4d.svg'},
    { code: '4h', file: 'images/cards/4h.svg'},
    { code: '4s', file: 'images/cards/4s.svg'},
    { code: '5c', file: 'images/cards/5c.svg'},
    { code: '5d', file: 'images/cards/5d.svg'},
    { code: '5h', file: 'images/cards/5h.svg'},
    { code: '5s', file: 'images/cards/5s.svg'},
    { code: '6c', file: 'images/cards/6c.svg'},
    { code: '6d', file: 'images/cards/6d.svg'},
    { code: '6h', file: 'images/cards/6h.svg'},
    { code: '6s', file: 'images/cards/6s.svg'},
    { code: '7c', file: 'images/cards/7c.svg'},
    { code: '7d', file: 'images/cards/7d.svg'},
    { code: '7h', file: 'images/cards/7h.svg'},
    { code: '7s', file: 'images/cards/7s.svg'},
    { code: '8c', file: 'images/cards/8c.svg'},
    { code: '8d', file: 'images/cards/8d.svg'},
    { code: '8h', file: 'images/cards/8h.svg'},
    { code: '8s', file: 'images/cards/8s.svg'},
    { code: '9c', file: 'images/cards/9c.svg'},
    { code: '9d', file: 'images/cards/9d.svg'},
    { code: '9h', file: 'images/cards/9h.svg'},
    { code: '9s', file: 'images/cards/9s.svg'},
    { code: 'Ac', file: 'images/cards/Ac.svg'},
    { code: 'Ad', file: 'images/cards/Ad.svg'},
    { code: 'Ah', file: 'images/cards/Ah.svg'},
    { code: 'As', file: 'images/cards/As.svg'},
    { code: 'Jc', file: 'images/cards/Jc.svg'},
    { code: 'Jd', file: 'images/cards/Jd.svg'},
    { code: 'Jh', file: 'images/cards/Jh.svg'},
    { code: 'Js', file: 'images/cards/Js.svg'},
    { code: 'Kc', file: 'images/cards/Kc.svg'},
    { code: 'Kd', file: 'images/cards/Kd.svg'},
    { code: 'Kh', file: 'images/cards/Kh.svg'},
    { code: 'Ks', file: 'images/cards/Ks.svg'},
    { code: 'Qc', file: 'images/cards/Qc.svg'},
    { code: 'Qd', file: 'images/cards/Qd.svg'},
    { code: 'Qh', file: 'images/cards/Qh.svg'},
    { code: 'Qs', file: 'images/cards/Qs.svg'},
    { code: '00', file: 'images/cards/back.svg'}
];

//
// let cards_array = [
//     { code: 'Tc', file: 'images/cards/10_of_clubs.png'},
//     { code: 'Td', file: 'images/cards/10_of_diamonds.png'},
//     { code: 'Th', file: 'images/cards/10_of_hearts.png'},
//     { code: 'Ts', file: 'images/cards/10_of_spades.png'},
//     { code: '2c', file: 'images/cards/2_of_clubs.png'},
//     { code: '2d', file: 'images/cards/2_of_diamonds.png'},
//     { code: '2h', file: 'images/cards/2_of_hearts.png'},
//     { code: '2s', file: 'images/cards/2_of_spades.png'},
//     { code: '3c', file: 'images/cards/3_of_clubs.png'},
//     { code: '3d', file: 'images/cards/3_of_diamonds.png'},
//     { code: '3h', file: 'images/cards/3_of_hearts.png'},
//     { code: '3s', file: 'images/cards/3_of_spades.png'},
//     { code: '4c', file: 'images/cards/4_of_clubs.png'},
//     { code: '4d', file: 'images/cards/4_of_diamonds.png'},
//     { code: '4h', file: 'images/cards/4_of_hearts.png'},
//     { code: '4s', file: 'images/cards/4_of_spades.png'},
//     { code: '5c', file: 'images/cards/5_of_clubs.png'},
//     { code: '5d', file: 'images/cards/5_of_diamonds.png'},
//     { code: '5h', file: 'images/cards/5_of_hearts.png'},
//     { code: '5s', file: 'images/cards/5_of_spades.png'},
//     { code: '6c', file: 'images/cards/6_of_clubs.png'},
//     { code: '6d', file: 'images/cards/6_of_diamonds.png'},
//     { code: '6h', file: 'images/cards/6_of_hearts.png'},
//     { code: '6s', file: 'images/cards/6_of_spades.png'},
//     { code: '7c', file: 'images/cards/7_of_clubs.png'},
//     { code: '7d', file: 'images/cards/7_of_diamonds.png'},
//     { code: '7h', file: 'images/cards/7_of_hearts.png'},
//     { code: '7s', file: 'images/cards/7_of_spades.png'},
//     { code: '8c', file: 'images/cards/8_of_clubs.png'},
//     { code: '8d', file: 'images/cards/8_of_diamonds.png'},
//     { code: '8h', file: 'images/cards/8_of_hearts.png'},
//     { code: '8s', file: 'images/cards/8_of_spades.png'},
//     { code: '9c', file: 'images/cards/9_of_clubs.png'},
//     { code: '9d', file: 'images/cards/9_of_diamonds.png'},
//     { code: '9h', file: 'images/cards/9_of_hearts.png'},
//     { code: '9s', file: 'images/cards/9_of_spades.png'},
//     { code: 'Ac', file: 'images/cards/ace_of_clubs.png'},
//     { code: 'Ad', file: 'images/cards/ace_of_diamonds.png'},
//     { code: 'Ah', file: 'images/cards/ace_of_hearts.png'},
//     { code: 'As', file: 'images/cards/ace_of_spades.png'},
//     { code: 'Jc', file: 'images/cards/jack_of_clubs.png'},
//     { code: 'Jd', file: 'images/cards/jack_of_diamonds.png'},
//     { code: 'Jh', file: 'images/cards/jack_of_hearts.png'},
//     { code: 'Js', file: 'images/cards/jack_of_spades.png'},
//     { code: 'Kc', file: 'images/cards/king_of_clubs.png'},
//     { code: 'Kd', file: 'images/cards/king_of_diamonds.png'},
//     { code: 'Kh', file: 'images/cards/king_of_hearts.png'},
//     { code: 'Ks', file: 'images/cards/king_of_spades.png'},
//     { code: 'Qc', file: 'images/cards/queen_of_clubs.png'},
//     { code: 'Qd', file: 'images/cards/queen_of_diamonds.png'},
//     { code: 'Qh', file: 'images/cards/queen_of_hearts.png'},
//     { code: 'Qs', file: 'images/cards/queen_of_spades.png'},
//     { code: '00', file: 'images/other/back.png'}
// ];





// for PIXI loader, to load all images
var imageFiles = [];
for (var i = 0; i < cards_array.length; i++) {
    imageFiles.push(cards_array[i].file);
}
imageFiles.push('images/other/poker_table.png');
imageFiles.push('images/other/sbButton.png');
imageFiles.push('images/other/bbButton.png');
imageFiles.push('images/other/dealerButton.png');
imageFiles.push('images/buttons_120x40/button_call.png');
imageFiles.push('images/buttons_120x40/button_raise.png');

// TODO: remove, this for testing only
//var players=['ddd','eee','fff','iii-starter'];
//data='players=[ddd,false,10000,10000,0,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=302,ante=0,sb=0,bb=0'


// PIXI Aliases
let Application = PIXI.Application,
    Container = PIXI.Container,
    loader = PIXI.loader,
    resources = PIXI.loader.resources,
    TextureCache = PIXI.utils.TextureCache,
    Sprite = PIXI.Sprite;
