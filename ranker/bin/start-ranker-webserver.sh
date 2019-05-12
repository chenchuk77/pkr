#!/bin/bash

PROJECT_FOLDER="${HOME}/dev/pkr"

which node > /dev/null 2>&1 || { echo "missing node js";  exit 1; }
echo "to rank hand use:"
echo "$ curl -X GET localhost:3000/As2hAd5d4d2s2d"
echo ""
node ${PKR_ROOT}/ranker/bin/ranker-webserver.js
