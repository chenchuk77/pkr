#!/bin/bash

ARGS="-fa 'Monospace' -fs 14"

#terminator -e '/home/lumos/dev/SpringWebSocket/client/run-ddd.sh' -T 'ddd' &
#sleep 2s
terminator -e '/home/lumos/dev/SpringWebSocket/client/run-eee.sh' -T 'eee' &
sleep 2s
terminator -e '/home/lumos/dev/SpringWebSocket/client/run-fff.sh' -T 'fff' &
sleep 2s
terminator -e '/home/lumos/dev/SpringWebSocket/client/run-iii.sh' -T 'iii' &

echo "sleep forever to keep clients up"
while true; do
  echo "sleeping for 10 min..."
  sleep 10m
done

