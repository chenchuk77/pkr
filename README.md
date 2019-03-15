# pkr

iproject includes the following:
- server
- external poker evaluator
- java client (testing)
- javascript client

server:
- written in java with spring framework
- springboot to simplify deployment
- websocket based tcp connections
- src/main/java folder
- hosts many games (currently only 'testGame')
- starts 'testGame' automatically after short delay to allow users to register
- allow reconnection from different devices

ranker-webserver:
- nodejs service to rank and evaluates 7 cards poker hand
- server will use it as external service
- check license

java-client:
- should be simple as possible
- only visualize yable state
- console client that implements websockets
- auto login
- auto game registration
- shell scripts to run mutiple different players
- run-all.sh starts 4 players

javascript-client:
- this is the final client
- 800*600
- should support small phones
- using pixijs
- src/main/resources/static

TODO:
- b: check game logic, sometimes must check-check even if no chips behind
- f: instruct clients dynamically about options (bet/raise)
- f: allow showing 1/2 hole cards
- check gamevlogic with 9 players (currently 4, should be trivial)
- f: allow mtt torneys with table rebalancing
- f: design player profiles (pairPlayer, donkPlayer,neatPlater etc)
- f: allow bot play for testing and statisrics???
- b: raise,200 should be raise to 200 and not more (ie. if bb post 60, raise 200 should add 140
- f: allow console terminals to open in different position (os specific)
- b: must close clients before stop server. of not, the port is not closed silently and rerun the server will fail (recover in few minutes)
- f: javascript client, allow choose my relative seats

