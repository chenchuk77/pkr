#!/bin/bash

HOMEDIR=$(pwd)
cd ../../../../ranker/bin
./start-ranker-webserver.sh >> ${HOMEDIR}/ranker.log 2>&1 &
cd ${HOMEDIR}
