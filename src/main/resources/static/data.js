/*
* Constants to be use by all project
* */

// const NUMBER_OF_PLAYERS=9;
const NUMBER_OF_PLAYERS=4;

// table container positions ( for table, cards, deck, pot )
const TABLE = {
    position:   {x: 200, y: 120},
    size:       {x: 900, y: 400},
    cards:      {scale: 0.4},
    ccc:        {x: 500, y: 300},
    deck:       {x: 180, y: 130},
    deckCards:  {x: 420, y: 360},
    flop1:      {x: 100, y: 0},
    flop2:      {x: 230, y: 0},
    flop3:      {x: 360, y: 0},
    turn:       {x: 520, y: 0},
    river:      {x: 680, y: 0},
    pot:        {x: 250, y: 120}
};

const BUTTONS = {
    position:   {x: 880, y: 600},
    // abc:   {x: 500, y: 600},
    fold:  {x: 30, y: 30},
    check: {x: 180, y: 30},
    call:  {x: 30, y: 80},
    bet:   {x: 180, y: 80},
    raise: {x: 180, y: 80}
};

// status container
const STATUS = {
    position: {x: 0, y: 600},
    cards: {scale: 0.4},
    messages: 8}
;

// positions inside containers
const PLAYER = {
    0: {class: "bottom-player",
        bet:        {x: 0, y: -32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: -95},
        dbc:        {x: 0, y: -95},
        midrect:    {x: 0, y: 32},
        ncc:        {x: 0, y: 64},
        position:   {x: 600, y: 470},
        position2:  {x: 650, y: 470},
        dealpath:   {x: 600, y: 470},
        size:       {x: 80, y:40},
        name:       {x: 0, y:40},
        chips:      {x: 0, y:60},
        avatar:     {x: 0, y:0},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}},

    1: {class: "bottom-player",
        bet:        {x: 0, y: -32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: -95},
        dbc:        {x: 0, y: -95},
        midrect:    {x: 0, y: 32},
        ncc:        {x: 0, y: 64},
        position:   {x: 400, y: 470},
        position2:  {x: 450, y: 470},
        dealpath:   {x: 400, y: 470},
        size:       {x: 40, y:80},
        name:       {x: 0, y:40},
        chips:      {x: 0, y:60},
        avatar:     {x: 0, y:0},
        avatar_cards:    {x: 100, y:500},
        committed: {x: 1, y:1}},
    2: {class: "left-player",
        bet:        {x: 32, y: 0},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 80, y: -80},
        dbc:        {x: 80, y: -20},
        midrect:    {x: -32, y: 0},
        ncc:        {x: -64, y: 0},
        position:   {x: 170, y: 260},
        position2:   {x: 220, y: 260},
        dealpath:   {x: 170, y: 260},
        size:       {x: 80, y:40},
        name:       {x: 0, y:0},
        chips:      {x: 0, y:20},
        avatar:     {x: 40, y:0},
        avatar_cards:    {x: 50, y:50},
        committed: {x: 100, y:0}},
    3: {class: "top-player",
        bet:        {x: 0, y: 32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: 20},
        dbc:        {x: 0, y: 20},
        midrect:    {x: 0, y: -32},
        ncc:        {x: 0, y: -64},
        position:   {x: 300, y: 70},
        position2:   {x: 350, y: 70},
        dealpath:   {x: 200, y: 170},
        size:       {x: 40, y:80},
        name:       {x: 0, y:0},
        chips:      {x: 0, y:20},
        avatar:     {x: 0, y:40},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}},
    4: {class: "top-player",
        bet:        {x: 0, y: 32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: 20},
        dbc:        {x: 0, y: 20},
        midrect:    {x: 0, y: -32},
        ncc:        {x: 0, y: -64},
        position:   {x: 500, y: 70},
        position2:   {x: 550, y: 70},
        dealpath:   {x: 400, y: 170},
        size:       {x: 40, y:80},
        name:       {x: 0, y:0},
        chips:      {x: 0, y:20},
        avatar:     {x: 0, y:40},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}},
    5: {class: "top-player",
        bet:        {x: 0, y: 32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: 20},
        dbc:        {x: 0, y: 20},
        midrect:    {x: 0, y: -32},
        ncc:        {x: 0, y: -64},
        position:   {x: 700, y: 70},
        position2:   {x: 750, y: 170},
        dealpath:   {x: 600, y: 120},
        size:       {x: 40, y:80},
        name:       {x: 0, y:0},
        chips:      {x: 0, y:20},
        avatar:     {x: 0, y:40},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}},
    6: {class: "top-player",
        bet:        {x: 0, y: 32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: 20},
        dbc:        {x: 0, y: 20},
        midrect:    {x: 0, y: -32},
        ncc:        {x: 0, y: -64},
        position:   {x: 900, y: 70},
        position2:   {x: 950, y: 70},
        dealpath:   {x: 800, y: 170},
        size:       {x: 40, y:80},
        name:       {x: 0, y:0},
        chips:      {x: 0, y:20},
        avatar:     {x: 0, y:40},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}},
    7: {class: "right-player",
        bet:        {x: -32, y: 0},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: -70, y: -40},
        dbc:        {x: -70, y: 20},
        midrect:    {x: 32, y: 0},
        ncc:        {x: 64, y: 0},
        position:   {x: 1065, y: 300},
        position2:   {x: 1115, y: 300},
        dealpath:   {x: 1065, y: 300},
        size:       {x: 80, y:40},
        name:       {x: 40, y:0},
        chips:      {x: 40, y:20},
        avatar:     {x: 0, y:0},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}},
    8: {class: "bottom-player",
        bet:        {x: 0, y: -32},
        ac:         {x: 0, y: 0},
        hcc:        {x: -12, y: 0},
        bc:         {x: 60, y: -95},
        dbc:        {x: 0, y: -95},
        midrect:    {x: 0, y: 32},
        ncc:        {x: 0, y: 64},
        position:   {x: 800, y: 470},
        position2:   {x: 850, y: 470},
        dealpath:   {x: 800, y: 470},
        size:       {x: 40, y:80},
        name:       {x: 1, y:1},
        chips:      {x: 1, y:1},
        avatar:     {x: 0, y:0},
        avatar_cards:    {x: 1, y:1},
        committed: {x: 1, y:1}}};


const ACTION_SHOW_SECONDS = 3;

const NAME_STYLE = {
    dropShadowColor: "#9e6262",
    fill: [
        "#2daa46",
        "#69e2dc"
    ],
    fontFamily: "Impact, Charcoal, sans-serif",
    fontSize: 20,
    padding: 1

};
// const STYLE_NAME = new PIXI.TextStyle();



// }
//     { code: 'Tc', file: 'images/cards/Tc.svg'},
//     { code: 'Td', file: 'images/cards/Td.svg'},
//     { code: 'Th', file: 'images/cards/Th.svg'},

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
    //{ code: '00', file: 'images/cards/back.svg'}
    { code: '00', file: 'images/cards/back-blue.png'}
];

// for PIXI loader, to load all images
let imageFiles = [];
for (let i = 0; i < cards_array.length; i++) {
    imageFiles.push(cards_array[i].file);
}
imageFiles.push('images/other/poker_table.png');
imageFiles.push('images/other/sbButton.png');
imageFiles.push('images/other/bbButton.png');
imageFiles.push('images/other/dealerButton.png');
imageFiles.push('images/buttons_120x40/button_bet.png');
imageFiles.push('images/buttons_120x40/button_call.png');
imageFiles.push('images/buttons_120x40/button_check.png');
imageFiles.push('images/buttons_120x40/button_fold.png');
imageFiles.push('images/buttons_120x40/button_raise.png');
imageFiles.push('images/avatars/human1t.png');
imageFiles.push('images/avatars/human2t.png');
imageFiles.push('images/avatars/human3.jpeg');
imageFiles.push('images/avatars/human4.jpeg');
imageFiles.push('images/avatars/girl1.jpeg');
imageFiles.push('images/avatars/girl2.png');
imageFiles.push('images/avatars/boy1.png');
imageFiles.push('images/avatars/boy2.png');
imageFiles.push('images/avatars/boy3.png');
imageFiles.push('images/avatars/boy4.png');
imageFiles.push('images/chips/chip1_stack.png');
imageFiles.push('images/chips/chip1_top.png');
imageFiles.push('images/chips/chip5_stack.png');
imageFiles.push('images/chips/chip5_top.png');
imageFiles.push('images/chips/chip25_stack.png');
imageFiles.push('images/chips/chip25_top.png');
imageFiles.push('images/chips/chip100_stack.png');
imageFiles.push('images/chips/chip100_top.png');
imageFiles.push('images/chips/chip500_stack.png');
imageFiles.push('images/chips/chip500_top.png');
imageFiles.push('images/chips/chip1000_stack.png');
imageFiles.push('images/chips/chip1000_top.png');
imageFiles.push('images/chips/chip5000_stack.png');
imageFiles.push('images/chips/chip5000_top.png');
imageFiles.push('images/chips/chip25000_stack.png');
imageFiles.push('images/chips/chip25000_top.png');




// TODO: remove, this for testing only
//var players=['ddd','eee','fff','iii-starter'];
//data='players=[ddd,false,10000,10000,0,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=302,ante=0,sb=0,bb=0'

//
// PIXI Aliases
let Application = PIXI.Application,
    Container = PIXI.Container,
    loader = PIXI.loader,
    resources = PIXI.loader.resources,
    TextureCache = PIXI.utils.TextureCache,
    Sprite = PIXI.Sprite;
    Graphics = PIXI.Graphics;

