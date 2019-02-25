#!/bin/bash

ARGS="-fa 'Monospace' -fs 14"

terminator -e './run-ddd.sh' -T 'ddd' &
#xterm ${ARGS} -hold -e './run-ddd.sh' &
sleep 2s
terminator -e './run-eee.sh' -T 'eee' &
#xterm ${ARGS} -hold -e './run-eee.sh' &
sleep 2s
terminator -e './run-fff.sh' -T 'fff' &
#xterm ${ARGS} -hold -e './run-fff.sh' &
sleep 2s
terminator -e './run-iii.sh' -T 'iii' &
#xterm ${ARGS} -hold -e './run-iii.sh' &

#xterm -fa 'Monospace' -fs 14
