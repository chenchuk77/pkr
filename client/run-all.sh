#!/bin/bash

ARGS="-fa 'Monospace' -fs 14"
terminator -e './run-ddd.sh' -T 'ddd' &
sleep 2s
terminator -e './run-eee.sh' -T 'eee' &
sleep 2s
terminator -e './run-fff.sh' -T 'fff' &
sleep 2s
terminator -e './run-iii.sh' -T 'iii' &
